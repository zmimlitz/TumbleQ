package com.mimlitz.tumbleQ.gui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import net.miginfocom.swing.MigLayout;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class CueListing extends JPanel {

    private List<Entry> cues;

    public CueListing(){
        setOpaque(false);
        setLayout(new MigLayout("", "0[fill,50]0[fill,grow]0", ""));
        cues = new ArrayList<>();
    }

    public void addCue(File file){
        cues.add(new Entry(file));
        updateView();
    }

    private void updateView(){
        removeAll();
        for (Entry cue : cues){
            JLabel act = new JLabel(cue.active ? "\u23F5" : "");
            act.setForeground(cue.valid ? Color.WHITE : Color.RED);
            act.setHorizontalAlignment(SwingConstants.CENTER);
            act.setBackground(Color.BLUE);
            act.setOpaque(cue.selected);
            add(act);
            JLabel name = new JLabel(cue.name);
            name.setForeground(cue.valid ? Color.WHITE : Color.RED);
            name.setBackground(Color.BLUE);
            name.setOpaque(cue.selected);
            add(name, "wrap");

            add(new Divider(), "span");
        }
        remove(getComponent(getComponentCount()-1));
        EventQueue.invokeLater(() -> {
            revalidate();
            repaint(0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE);
        });
    }

    private class Entry {
        boolean active, selected, valid;
        final String name;
        final MediaPlayer clip;

        Entry(File file){
            active = false;
            selected  = false;
            name = file.getName().substring(0, file.getName().lastIndexOf('.'));
            Media media = null;
            try {
                media = new Media(file.toURI().toString());
                valid = true;
            }
            catch (Exception e){
                valid = false;
            }
            if (valid){
                clip = new MediaPlayer(media);
            }
            else {
                clip = null;
            }
        }

    }

}
