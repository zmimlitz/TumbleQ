package com.mimlitz.tumbleQ.sound;

import com.mimlitz.tumbleQ.gui.VolumeFader;
import java.awt.BorderLayout;
import javax.swing.JPanel;

public class ClipPlayer {

    private SoundClip clip;
    private VolumeControl volumeCtrl;

    public ClipPlayer(){
        volumeCtrl = new VolumeControl();
    }

    public void setCurrent(SoundClip clip){
        if (this.clip != null){
            this.clip.pause();
            this.clip.setToTime(0);
        }
        this.clip = clip;
        volumeCtrl.updateClip(this.clip);
    }

    public VolumeControl getVolumeControl(){
        return volumeCtrl;
    }

}

class PlayControl extends JPanel {



}

class VolumeControl extends JPanel {

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

}
