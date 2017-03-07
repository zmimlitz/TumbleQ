package com.mimlitz.tumbleQ.gui;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Optional;
import java.util.concurrent.CancellationException;

import com.mimlitz.tumbleQ.sound.CueListing;
import com.mimlitz.tumbleQ.util.io.SoundFilter;
import javafx.application.Platform;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import javax.swing.border.LineBorder;
import net.miginfocom.swing.MigLayout;

public class Form extends JFrame {

    private static Optional<Form> instance = Optional.empty();

    private JFileChooser chooser;

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
                File file = getCueFile();
                listing.addCue(file);
            } catch (CancellationException e) {}
        });
        controls.setRemoveAction(listing::deleteSelected);
        controls.setMoveUpAction(listing::moveUpSelected);
        controls.setMoveDownAction(listing::moveDownSelected);
        controls.setForeground(Color.BLUE);
        controls.setFont(new Font(controls.getFont().getName(), Font.BOLD, 15));
        controls.setBorder(new LineBorder(Color.DARK_GRAY, 2));
        qListPnl.add(controls, BorderLayout.NORTH);

        qListPnl.add(listing, BorderLayout.CENTER);

        JPanel goPnl = new JPanel();
        goPnl.setBackground(Color.BLACK);
        goPnl.setLayout(new BorderLayout());
        content.add(goPnl, "cell 2 2");

        GoControls goControls = new GoControls();
        goControls.setBackAction(listing::rollback);
        goControls.setNextAction(listing::advance);
        goControls.setGoAction(() -> {
            listing.getCurrent().play();
            listing.advance();
        });
        goControls.setForeground(Color.BLUE);
        goControls.setFont(new Font(goControls.getName(), Font.BOLD, 15));
        goPnl.add(goControls, BorderLayout.CENTER);

        JPanel controlsPnl = new JPanel();
        controlsPnl.setBackground(Color.BLACK);
        content.add(controlsPnl, "cell 2 0 1 2");

        JPanel viewPnl = new JPanel();
        viewPnl.setBackground(Color.BLACK);
        content.add(viewPnl, "cell 0 2 2 1");

        chooser = new JFileChooser();
        chooser.setFileFilter(new SoundFilter());
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    }

    public void showForm(){
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void exit(){
        Platform.exit();
        System.exit(0);
    }

    private File getCueFile() throws CancellationException {
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
            return chooser.getSelectedFile();
        }
        else {
            throw new CancellationException("Chooser Canceled");
        }
    }

}
