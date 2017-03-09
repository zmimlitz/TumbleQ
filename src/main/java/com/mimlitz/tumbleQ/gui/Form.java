package com.mimlitz.tumbleQ.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mimlitz.tumbleQ.sound.ClipPlayer;
import com.mimlitz.tumbleQ.sound.SoundClip;
import com.mimlitz.tumbleQ.util.io.MyFileFilter;
import com.mimlitz.tumbleQ.util.io.SaveFile;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CancellationException;

import com.mimlitz.tumbleQ.sound.CueListing;
import com.mimlitz.tumbleQ.util.io.SoundFilter;
import javafx.application.Platform;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import javax.swing.border.LineBorder;
import javax.swing.plaf.ColorUIResource;
import net.miginfocom.swing.MigLayout;

public class Form extends JFrame {

    private static Optional<Form> instance = Optional.empty();

    private JFileChooser chooser;

    private ClipPlayer player;

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
        UIManager.put("ComboBox.background", new ColorUIResource(Color.DARK_GRAY));
        UIManager.put("ComboBox.foreground", new ColorUIResource(Color.WHITE));
        UIManager.put("JTextField.background", new ColorUIResource(Color.BLACK));
        UIManager.put("ComboBox.selectionBackground", new ColorUIResource(Color.BLUE));
        UIManager.put("ComboBox.selectionForeground", new ColorUIResource(Color.WHITE));
        player = new ClipPlayer();
        listing = new CueListing();

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
        content.setLayout(new MigLayout("", "[fill,grow][fill,grow]5[fill,grow]", "[fill,grow][fill,grow]5[fill,grow]"));
        setContentPane(content);

        JPanel qListPnl = new JPanel();
        qListPnl.setBackground(Color.BLACK);
        qListPnl.setLayout(new BorderLayout());
        content.add(qListPnl, "cell 0 0 2 2");

        CueControls controls = new CueControls();
        controls.setAddAction(() -> {
            try {
                File[] files = getCueFile();
                for (File file : files) {
                    listing.addCue(file);
                }
            } catch (CancellationException e) {}
        });
        controls.setRemoveAction(listing::deleteSelected);
        controls.setMoveUpAction(listing::moveUpSelected);
        controls.setMoveDownAction(listing::moveDownSelected);
        controls.setSaveAction(this::save);
        controls.setStopAction(() -> {
            listing.getCurrent().pause();
            listing.getCurrent().setToTime(0);
        });
        controls.setForeground(Color.BLUE);
        controls.setFont(new Font(controls.getFont().getName(), Font.BOLD, 15));
        controls.setBorder(new LineBorder(Color.DARK_GRAY, 2));
        qListPnl.add(controls, BorderLayout.NORTH);

        JScrollPane listingScroll = new JScrollPane(listing);
        listingScroll.setOpaque(false);
        listingScroll.setBorder(null);
        listingScroll.getViewport().setOpaque(false);
        qListPnl.add(listingScroll, BorderLayout.CENTER);

        JPanel goPnl = new JPanel();
        goPnl.setBackground(Color.BLACK);
        goPnl.setLayout(new BorderLayout());
        content.add(goPnl, "cell 2 2");

        GoControls goControls = new GoControls();
        goControls.setBackAction(listing::rollback);
        goControls.setNextAction(listing::advance);
        goControls.setGoAction(this::go);
        goControls.setForeground(Color.BLUE);
        goControls.setFont(new Font(goControls.getName(), Font.BOLD, 15));
        goPnl.add(goControls, BorderLayout.CENTER);

        JPanel controlsPnl = new JPanel();
        controlsPnl.setBackground(Color.BLACK);
        controlsPnl.setLayout(new BorderLayout());
        content.add(controlsPnl, "cell 2 0 1 2");

        controlsPnl.add(player.getVolumeControl(), BorderLayout.CENTER);

        JPanel viewPnl = new JPanel();
        viewPnl.setBackground(Color.BLACK);
        content.add(viewPnl, "cell 0 2 2 1");

        chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    }

    private void go(){
        SoundClip clip = listing.getCurrent();
        clip.play();
        listing.advance();
        if (listing.currentIsLinked()){
            player.setCurrent(clip, this::go);
        }
        else {
            player.setCurrent(clip);
        }
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
            JOptionPane.showConfirmDialog(this, e.getMessage(), "Load Failed", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
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

}
