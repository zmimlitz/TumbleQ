package com.mimlitz.tumbleQ.sound;

import com.mimlitz.tumbleQ.gui.ClipViewer;
import com.mimlitz.tumbleQ.gui.ImageDisplay;
import com.mimlitz.tumbleQ.gui.VolumeFader;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ClipPlayer {

    private SoundClip clip;
    private VolumeControl volumeCtrl;
    private PlayControl playControl;

    public ClipPlayer(){
        volumeCtrl = new VolumeControl();
        playControl = new PlayControl();
        playControl.setForeground(Color.WHITE);
        playControl.setFont(new Font(
                playControl.getFont().getName(),
                Font.BOLD,
                16
        ));
    }

    public void setCurrent(SoundClip clip){
        if (this.clip != null){
            this.clip.setToTime(0);
            this.clip.pause();
        }
        this.clip = clip;
        volumeCtrl.updateClip(this.clip);
        playControl.updateClip(this.clip);
        doSetAfter(Optional.empty());
    }

    public void setCurrent(SoundClip clip, Runnable after){
        setCurrent(clip);
        doSetAfter(Optional.of(after));
    }

    private void doSetAfter(Optional<Runnable> after){
        clip.setAfterAction(() -> {
            clip.setToTime(0);
            clip.pause();
            if (after.isPresent()){
                after.get().run();
            }
            clip.setAfterAction(null);
        });
    }

    public VolumeControl getVolumeControl(){
        return volumeCtrl;
    }

    public PlayControl getPlayControl(){
        return playControl;
    }

}
