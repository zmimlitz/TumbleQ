package com.mimlitz.tumbleQ.sound;

import com.mimlitz.tumbleQ.gui.VolumeFader;
import javax.swing.JPanel;

import java.awt.BorderLayout;

public class VolumeControl extends JPanel {

    private VolumeFader fader;
    private SoundClip clip;

    VolumeControl(){
        setOpaque(false);
        setLayout(new BorderLayout());

        fader = new VolumeFader();
        fader.addChangeListener((a) -> {
            if (clip != null){
                clip.setVolume(fader.getVolume());
            }
        });
        add(fader, BorderLayout.CENTER);
    }

    void updateClip(SoundClip clip){
        this.clip = clip;
        clip.setVolume(fader.getVolume());
    }

    public void increaseVolume(){
        fader.pushUp();
    }

    public void decreaseVolume(){
        fader.pushDown();
    }

}
