package com.mimlitz.tumbleQ.sound;

import com.mimlitz.tumbleQ.gui.Divider;
import com.mimlitz.tumbleQ.util.io.SaveFile;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import net.miginfocom.swing.MigLayout;

import java.awt.Color;
import java.awt.EventQueue;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class CueListing extends JPanel {

    private List<Entry> cues;
    private int active = -1, selected = -1;
    private boolean locked = false;

    public CueListing(){
        setOpaque(false);
        setLayout(new MigLayout("", "0[fill,50]0[fill,grow]0", ""));
        cues = new ArrayList<>();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    select(Integer.parseInt(getComponentAt(e.getPoint()).getName()));
                }
                catch (NullPointerException | NumberFormatException ex){
                    select(-1);
                }
            }
        });
    }

    public void addCue(File file){
        cues.add(new Entry(file));
        if (cues.size() == 1){
            cues.get(0).active = true;
            active = 0;
        }
        updateView();
    }

    public SoundClip getCurrent(){
        return cues.get(active).clip;
    }

    public void advance(){
        cues.get(active).active = false;
        active++;
        cues.get(active).active = true;
        updateView();
    }

    public void rollback(){
        cues.get(active).active = false;
        active--;
        cues.get(active).active = true;
        updateView();
    }

    public void select(int i){
        if (selected != -1){
            cues.get(selected).selected = false;
        }
        selected = i;
        cues.get(selected).selected = true;
        updateView();
    }

    public void deleteSelected(){
        cues.remove(selected);
        if (active > selected){
            active--;
        }
        selected = -1;
        updateView();
    }

    public void moveUpSelected(){
        Entry select = cues.get(selected);
        Entry less = cues.get(selected-1);
        cues.set(selected, less);
        cues.set(selected-1, select);
        if (active == selected){
            active--;
        }
        else if (active == selected-1){
            active++;
        }
        select(selected-1);
    }

    public void moveDownSelected(){
        Entry select = cues.get(selected);
        Entry great = cues.get(selected+1);
        cues.set(selected, great);
        cues.set(selected+1, select);
        if (active == selected){
            active++;
        }
        else if (active == selected+1){
            active--;
        }
        select(selected+1);
    }

    private void updateView(){
        removeAll();
        for (int i = 0; i < cues.size(); i++){
            Entry cue = cues.get(i);
            JLabel act = new JLabel(cue.active ? "\u23F5" : " ");
            act.setForeground(cue.valid ? Color.WHITE : Color.RED);
            act.setHorizontalAlignment(SwingConstants.CENTER);
            act.setBackground(Color.BLUE);
            act.setOpaque(cue.selected);
            act.setName(i + "");
            add(act);
            JLabel name = new JLabel(cue.name);
            name.setForeground(cue.valid ? Color.WHITE : Color.RED);
            name.setBackground(Color.BLUE);
            name.setOpaque(cue.selected);
            name.setName(i + "");
            add(name, "wrap");

            add(new Divider(), "span");
        }
        remove(getComponent(getComponentCount()-1));
        EventQueue.invokeLater(() -> {
            revalidate();
            repaint(0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE);
        });
    }

    public SaveFile getSaveFile(){
        SaveFile.Builder builder = SaveFile.builder();
        for (Entry cue : cues){
            builder.addEntry(cue.file, cue.link);
        }
        return builder.build();
    }

    public void cloneFromListing(CueListing other){
        this.cues = other.cues;
        active = cues.size() > 0 ? 0 : -1;
        selected = -1;
        locked = false;
        updateView();
    }

    private class Entry {
        boolean active, selected, valid;
        final String name, link;
        final SoundClip clip;
        final File file;

        Entry(File file){
            this.file = file;
            active = false;
            selected  = false;
            link = "None";
            name = file.getName().substring(0, file.getName().lastIndexOf('.'));
            clip = new FXClipImpl();
            try {
                clip.load(file);
                valid = true;
            }
            catch (IOException e){
                valid = false;
            }
        }

    }

}
