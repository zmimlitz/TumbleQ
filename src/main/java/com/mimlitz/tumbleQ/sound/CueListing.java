package com.mimlitz.tumbleQ.sound;

import com.mimlitz.tumbleQ.gui.ComboBox;
import com.mimlitz.tumbleQ.gui.Divider;
import com.mimlitz.tumbleQ.util.io.SaveFile;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import net.miginfocom.swing.MigLayout;

import java.awt.Color;
import java.awt.EventQueue;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CueListing extends JPanel {

    private List<Entry> cues;
    private int active = -1, selected = -1;
    private boolean locked = false;

    public CueListing(){
        setOpaque(false);
        setLayout(new MigLayout("", "0[fill,50]0[fill,grow]0[fill,200]0", "fill"));
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
        addCue(file, "None");
    }

    public void addCue(File file, String link) {
        addCue(file, link, Collections.emptyList());
    }

    public void addCue(File file, String link, List<Integer> bookmarks){
        Entry cue = new Entry(file);
        cue.link = link;
        for (Integer mark : bookmarks){
            cue.clip.addBookmark(mark);
        }
        cues.add(cue);
        if (cues.size() == 1){
            cues.get(0).active = true;
            active = 0;
        }
        updateView();
    }

    public SoundClip getCurrent(){
        try {
            return cues.get(active).clip;
        }
        catch (ArrayIndexOutOfBoundsException e){
            return null;
        }
    }

    public void advance(){
        cues.get(active).active = false;
        active = (active+1) % cues.size();
        cues.get(active).active = true;
        updateView();
    }

    public void rollback(){
        cues.get(active).active = false;
        active = (active-1+cues.size()) % cues.size();
        cues.get(active).active = true;
        updateView();
    }

    public boolean currentIsLinked(){
        return !cues.get(active).link.equals("None");
    }

    public void select(int i){
        if (selected != -1){
            cues.get(selected).selected = false;
        }
        selected = i;
        if (i != -1) {
            cues.get(selected).selected = true;
        }
        updateView();
    }

    public void deleteSelected(){
        if (selected == -1){
            return;
        }
        cues.remove(selected);
        if (active > selected){
            active--;
        }
        selected = -1;
        updateView();
    }

    public void moveUpSelected(){
        if (selected == -1){
            return;
        }
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
        if (selected == -1){
            return;
        }
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

    public void swapMedia(File file){
        if (selected == -1){
            return;
        }
        Entry newEntry = new Entry(file);
        Entry oldEntry = cues.get(selected);
        newEntry.link = oldEntry.link;
        newEntry.active = oldEntry.active;
        newEntry.selected = oldEntry.selected;
        cues.set(selected, newEntry);
        updateView();
    }

    public boolean hasSelected(){
        return selected != -1;
    }

    public void updateView(){
        removeAll();
        for (int i = 0; i < cues.size(); i++){
            Entry cue = cues.get(i);
            JLabel act = new JLabel(cue.active ? "\u23F5" : " ");
            act.setForeground(cue.valid ? cue.clip.isPlaying() ? Color.GREEN : Color.WHITE : Color.RED);
            act.setHorizontalAlignment(SwingConstants.CENTER);
            act.setBackground(Color.BLUE);
            act.setOpaque(cue.selected);
            act.setName(i + "");
            add(act);
            JLabel name = new JLabel(cue.name);
            name.setForeground(cue.valid ? cue.clip.isPlaying() ? Color.GREEN : Color.WHITE : Color.RED);
            name.setBackground(Color.BLUE);
            name.setOpaque(cue.selected);
            name.setName(i + "");
            add(name);
            ComboBox link = new ComboBox("None", "After Last");
            link.setForeground(cue.valid ? Color.WHITE : Color.RED);
            link.setSelectedItem(cue.link);
            link.setBackground(Color.BLUE);
            link.setOpaque(cue.selected);
            for (Component comp : link.getComponents()){
                comp.setBackground(Color.BLACK);
            }
            link.setName(i+"");
            final int index = i;
            link.addActionListener((a) -> {
                cues.get(index).link = link.getSelectedItem();
            });
            add(link, "wrap");

            add(new Divider(), "span");
        }
        if (getComponentCount() > 1) {
            remove(getComponent(getComponentCount() - 1));
        }
        EventQueue.invokeLater(() -> {
            revalidate();
            repaint(0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE);
        });
    }

    public SaveFile getSaveFile(){
        SaveFile.Builder builder = SaveFile.builder();
        for (Entry cue : cues){
            builder.addEntry(cue.file, cue.link, cue.clip.getBookmarks());
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
        final String name;
        final SoundClip clip;
        final File file;
        String link;

        Entry(File file){
            this.file = file;
            active = false;
            selected  = false;
            link = "None";
            name = file.getName().substring(0, file.getName().lastIndexOf('.'));
            clip = new FXClipImpl();
            clip.setName(name);
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
