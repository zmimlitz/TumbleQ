package com.mimlitz.tumbleQ.gui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import net.miginfocom.swing.MigLayout;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class CueListing extends JPanel {

    private List<Entry> cues;

    public CueListing(){
        setOpaque(false);
        setLayout(new MigLayout("", "[grow,fill][fill,grow]", "grow,fill"));
        cues = new ArrayList<>();
    }

    public void addCue(File file){
        cues.add(new Entry(file));
        updateView();
    }

    private void updateView(){
        this.removeAll();
        for (int i = 0; i < cues.size(); i++){
            Entry cue = cues.get(i);
            List<JLabel> row = new ArrayList<>();
            row.add(new JLabel(cue.active ? "\u23F5" : ""));
            row.get(0).setHorizontalAlignment(SwingConstants.CENTER);
            row.add(new JLabel(cue.name));
            for (int j = 0; j < row.size(); j++){
                if (!cue.valid){
                    row.get(j).setBackground(Color.RED);
                }
                else {
                    row.get(j).setBackground(Color.WHITE);
                }
                add(row.get(j), String.format("cell %d %d", j*2, i));
            }
            add(new Divider(), String.format("cell %d %d %d %d", 0, i*2+1, 4, 1));
        }
        repaint();
    }

    private class Entry {
        boolean active, link, valid;
        final String name;
        final MediaPlayer clip;

        Entry(File file){
            active = false;
            link  = false;
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
