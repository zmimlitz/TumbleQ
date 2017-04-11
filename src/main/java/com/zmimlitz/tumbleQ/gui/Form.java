/*
 * TumbleQ: Theatrical Cuing Software
 * Copyright (C) 2017  Zac Mimlitz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.zmimlitz.tumbleQ.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zmimlitz.tumbleQ.sound.ClipPlayer;
import com.zmimlitz.tumbleQ.sound.PlayControl;
import com.zmimlitz.tumbleQ.sound.SoundClip;
import com.zmimlitz.tumbleQ.sound.VolumeControl;
import com.zmimlitz.tumbleQ.util.DoubleClickListener;
import com.zmimlitz.tumbleQ.util.io.MyFileFilter;
import com.zmimlitz.tumbleQ.util.io.SaveFile;
import com.zmimlitz.tumbleQ.util.io.SoundFilter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CancellationException;

import com.zmimlitz.tumbleQ.sound.CueListing;
import javafx.application.Platform;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import javax.swing.border.LineBorder;
import net.miginfocom.swing.MigLayout;

public class Form extends JFrame {

    private static Optional<Form> instance = Optional.empty();

    private JFileChooser chooser;

    private ClipPlayer player;
    private GlobalKeyListener globalKey;

    private JPopupMenu optionMenu;

    public static Form getInstance(){
        if (!instance.isPresent()){
            instance = Optional.of(new Form());
        }
        return instance.get();
    }

    private CueListing listing;

    private Form(){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e) {}
        player = new ClipPlayer();
        listing = new CueListing();
        globalKey = new GlobalKeyListener();
        KeyboardFocusManager manager = KeyboardFocusManager
                .getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(globalKey);

        setTitle("TumbleQ");
        setIconImage(new ImageIcon(Form.class.getResource("/TumbleQ.png")).getImage());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });
        initialize();
    }

    private void initialize(){
        setBounds(100, 100, 600, 300);
        JPanel content = new JPanel();
        content.setBackground(Color.DARK_GRAY);
        content.setLayout(new MigLayout("", "[fill,67%]5[fill,13%]5[fill,20%]", "[fill,66%]5[fill,34%]"));
        setContentPane(content);

        optionMenu = new JPopupMenu();
        listing.addMouseListener(new DoubleClickListener() {
            @Override
            public void mouseClickedOnce(MouseEvent e) {
                EventQueue.invokeLater(() -> {
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        optionMenu.show(listing, e.getX() - 2, e.getY() - 2);
                    }
                });
            }

            @Override
            public void mouseDoubleClicked(MouseEvent e) {}
        });

        JMenuItem addMenu = new JMenuItem("Add", getImage("/controls/Menu-Add.png"));
        addMenu.addActionListener((a) -> add());
        optionMenu.add(addMenu);

        JMenuItem removeMenu = new JMenuItem("Remove", getImage("/controls/Menu-Remove.png"));
        removeMenu.addActionListener((a) -> listing.deleteSelected());
        optionMenu.add(removeMenu);

        JMenuItem swapMenu = new JMenuItem("Swap Media", getImage("/controls/Menu-Refresh.png"));
        swapMenu.addActionListener((a) -> swap());
        optionMenu.add(swapMenu);

        JMenuItem levelMenu = new JMenuItem("Set Level", getImage("/controls/Menu-Levels.png"));
        levelMenu.addActionListener((a) -> setTrackLevel());
        optionMenu.add(levelMenu);

        JMenuItem upMenu = new JMenuItem("Move Up", getImage("/controls/Menu-Up.png"));
        upMenu.addActionListener((a) -> listing.moveUpSelected());
        optionMenu.add(upMenu);

        JMenuItem downMenu = new JMenuItem("Move Down", getImage("/controls/Menu-Down.png"));
        downMenu.addActionListener((a) -> listing.moveDownSelected());
        optionMenu.add(downMenu);

        JPanel qListPnl = new JPanel();
        qListPnl.setBackground(Color.BLACK);
        qListPnl.setLayout(new BorderLayout());
        content.add(qListPnl, "cell 0 0 2 1");

        CueControls controls = new CueControls();
        controls.setAddAction(this::add);
        controls.setRemoveAction(listing::deleteSelected);
        controls.setMoveUpAction(listing::moveUpSelected);
        controls.setMoveDownAction(listing::moveDownSelected);
        controls.setSwapAction(this::swap);
        controls.setSaveAction(this::save);
        controls.setLevelAction(this::setTrackLevel);
        controls.setForeground(Color.BLUE);
        controls.setFont(new Font(controls.getFont().getName(), Font.BOLD, 15));
        controls.setBorder(new LineBorder(Color.DARK_GRAY, 2));
        qListPnl.add(controls, BorderLayout.NORTH);

        JScrollPane listingScroll = new JScrollPane(listing);
        listingScroll.setOpaque(false);
        listingScroll.setBorder(null);
        listingScroll.getViewport().setOpaque(false);
        listingScroll.getVerticalScrollBar().setUnitIncrement(20);
        MyScrollUI vScrollUI = new MyScrollUI();
        vScrollUI.setForeground(Color.GRAY);
        listingScroll.getVerticalScrollBar().setUI(vScrollUI);
        MyScrollUI hScrollUI = new MyScrollUI();
        hScrollUI.setForeground(Color.GRAY);
        listingScroll.getHorizontalScrollBar().setUI(hScrollUI);
        qListPnl.add(listingScroll, BorderLayout.CENTER);

        JPanel goPnl = new JPanel();
        goPnl.setBackground(Color.BLACK);
        goPnl.setLayout(new BorderLayout());
        content.add(goPnl, "cell 1 1 2 1");

        GoControls goControls = new GoControls();
        goControls.setBackAction(listing::rollback);
        goControls.setNextAction(listing::advance);
        goControls.setGoAction(this::go);
        goControls.setFadeAction(player.getPlayControl()::fireFadeAction);
        goControls.setForeground(Color.BLUE);
        goControls.setFont(new Font(goControls.getName(), Font.BOLD, 15));
        goPnl.add(goControls, BorderLayout.CENTER);
        globalKey.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()){
                    case KeyEvent.VK_SPACE:
                        go();
                        break;
                }
            }
        });

        JPanel controlsPnl = new JPanel();
        controlsPnl.setBackground(Color.BLACK);
        controlsPnl.setLayout(new BorderLayout());
        content.add(controlsPnl, "cell 2 0");

        controlsPnl.add(player.getVolumeControl(), BorderLayout.CENTER);
        globalKey.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                VolumeControl control = player.getVolumeControl();
                switch (e.getKeyCode()){
                    case KeyEvent.VK_UP:
                        control.increaseVolume();;
                        break;
                    case KeyEvent.VK_DOWN:
                        control.decreaseVolume();
                        break;
                }
            }
        });

        JPanel viewPnl = new JPanel();
        viewPnl.setBackground(Color.BLACK);
        viewPnl.setLayout(new BorderLayout());
        content.add(viewPnl, "cell 0 1");

        viewPnl.add(player.getPlayControl(), BorderLayout.CENTER);
        globalKey.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                PlayControl control = player.getPlayControl();
                switch (e.getKeyCode()){
                    case KeyEvent.VK_ENTER:
                        control.firePlayPauseAction();
                        break;
                    case KeyEvent.VK_PERIOD:
                        control.fireNextBookmarkAction();
                        break;
                    case KeyEvent.VK_COMMA:
                        control.fireLastBookmarkAction();
                        break;
                    case KeyEvent.VK_BACK_SPACE:
                        control.fireFadeAction();
                        break;
                }
            }
        });

        chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    }

    private void go(){
        SoundClip clip = listing.getCurrent();
        if (clip == null){
            return;
        }
        if (listing.currentIsFloat()){
            if (clip.hasInitialLevel()){
                clip.setVolume(clip.getInitialLevel());
            }
            clip.play();
            clip.setAfterAction(() -> {
                clip.setToTime(0);
                clip.pause();
                listing.updateView();
            });
            listing.advance();
        }
        else {
            listing.advance();
            if (listing.currentIsLinked()) {
                player.setCurrent(clip, this::go);
            }
            else {
                player.setCurrent(clip);
            }
            clip.play();
            listing.updateView();
        }
    }

    private void add(){
        try {
            File[] files = getCueFile();
            for (File file : files) {
                listing.addCue(file);
            }
        } catch (CancellationException e) {}
    }

    private void swap(){
        if (listing.hasSelected()) {
            try {
                File[] newFile = getCueFile();
                listing.swapMedia(newFile[0]);
            } catch (CancellationException e) {}
        }
    }

    private void setTrackLevel(){
        listing.setSelectedLevel(player.getVolumeControl().getVolume());
    }

    private void save(){
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileFilter(new MyFileFilter());
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION){
            ObjectMapper mapper = new ObjectMapper();
            File fout = chooser.getSelectedFile();
            if (!fout.getName().endsWith(MyFileFilter.getSuffix())){
                fout = new File(fout.getAbsolutePath() + MyFileFilter.getSuffix());
            }
            try {
                if (fout.exists()){
                    int overwrite = JOptionPane.showConfirmDialog(this, "The file already exists do you want to replace it?", "Confirm Save",
                            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (overwrite == JOptionPane.YES_OPTION){
                        mapper.writeValue(fout, listing.getSaveFile());
                    }
                    else if (overwrite == JOptionPane.NO_OPTION) {
                        save();
                    }
                    else {
                        //cancel: do not save
                    }
                }
                else {
                    mapper.writeValue(fout, listing.getSaveFile());
                }
            }
            catch (IOException e) {
                JOptionPane.showConfirmDialog(this, e.getMessage(), "Save Failed", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    public void load(File fin){
        try {
            ObjectMapper mapper = new ObjectMapper();
            chooser.setCurrentDirectory(fin.getParentFile());
            SaveFile save = mapper.readValue(fin, SaveFile.class);
            listing.cloneFromListing(save.load());
            repaint();
        }
        catch (IOException e){
            JOptionPane.showConfirmDialog(
                    this,
                    e.getMessage(),
                    "Load Failed",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void showForm(){
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void exit(){
        if (JOptionPane.showConfirmDialog(this,
                "Are you sure you want to exit?",
                "Closing TumbleQ",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE) == JOptionPane.YES_OPTION) {
            Platform.exit();
            System.exit(0);
        }
    }

    private File[] getCueFile() throws CancellationException {
        chooser.setFileFilter(new SoundFilter());
        chooser.setMultiSelectionEnabled(true);
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
            return chooser.getSelectedFiles();
        }
        else {
            throw new CancellationException("Chooser Canceled");
        }
    }

    private ImageIcon getImage(String name){
        return new ImageIcon(CueControls.class.getResource(name));
    }

}

class GlobalKeyListener implements KeyEventDispatcher {

    private Set<KeyListener> listeners;

    GlobalKeyListener(){
        listeners = new HashSet<>();
    }

    public boolean dispatchKeyEvent(KeyEvent e) {
        for (KeyListener listener : listeners){
            switch (e.getID()){
                case KeyEvent.KEY_PRESSED:
                    listener.keyPressed(e);
                    break;
                case KeyEvent.KEY_RELEASED:
                    listener.keyReleased(e);
                    break;
                case KeyEvent.KEY_TYPED:
                    listener.keyTyped(e);
                    break;
            }
        }
        return false;
    }

    public void addKeyListener(KeyListener listener){
        listeners.add(listener);
    }

}

