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
import java.util.Optional;
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

class PlayControl extends JPanel {

    private SoundClip clip;
    private ClipViewer view;
    private JLabel title;
    private ImageDisplay playPause;

    PlayControl(){
        setOpaque(false);
        setLayout(new BorderLayout());

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BorderLayout());
        add(header, BorderLayout.NORTH);

        title = new JLabel();
        header.add(title, BorderLayout.WEST);

        JPanel controls = new JPanel();
        controls.setOpaque(false);
        controls.setLayout(new FlowLayout(FlowLayout.TRAILING));
        controls.setPreferredSize(new Dimension(200, 40));
        header.add(controls, BorderLayout.CENTER);

        ImageDisplay rewind = new ImageDisplay();
        rewind.setIcon(getImage("/controls/Rewind.png"));
        rewind.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rewind.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (clip != null){
                    clip.setToTime(clip.getTime()-5);
                }
            }
        });
        controls.add(rewind);

        controls.add(new JLabel(" "));

        ImageDisplay stop = new ImageDisplay();
        stop.setIcon(getImage("/controls/Stop.png"));
        stop.setCursor(new Cursor(Cursor.HAND_CURSOR));
        stop.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (clip != null) {
                    clip.pause();
                    clip.setToTime(0);
                }
                updatePlayPause();
            }
        });
        controls.add(stop);

        controls.add(new JLabel(" "));

        playPause = new ImageDisplay();
        playPause.setIcon(getImage("/controls/Play.png"));
        playPause.setCursor(new Cursor(Cursor.HAND_CURSOR));
        playPause.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (clip != null) {
                    if (clip.isPlaying()) {
                        clip.pause();
                    } else {
                        clip.play();
                    }
                }
                updatePlayPause();
            }
        });
        controls.add(playPause);

        controls.add(new JLabel(" "));

        ImageDisplay forward = new ImageDisplay();
        forward.setIcon(getImage("/controls/Fast_Forward.png"));
        forward.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forward.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (clip != null){
                    clip.setToTime(clip.getTime()+5);
                }
            }
        });
        controls.add(forward);

        view = new ClipViewer();
        view.setOpaque(false);
        view.setBackground(Color.GRAY);
        view.setForeground(Color.WHITE);
        add(view, BorderLayout.CENTER);
    }

    public void updateClip(SoundClip clip){
        this.clip = clip;
        this.view.setClip(clip);
        this.title.setText("  " + clip.getName());
        updatePlayPause();
    }

    private void updatePlayPause(){
        if (clip == null || !clip.isPlaying()){
            playPause.setIcon(getImage("/controls/Play.png"));
        }
        else {
            playPause.setIcon(getImage("/controls/Pause.png"));
        }
    }

    private ImageIcon getImage(String name){
        return new ImageIcon(PlayControl.class.getResource(name));
    }

    @Override
    public void setFont(Font font){
        super.setFont(font);
        if (title != null) {
            title.setFont(font);
        }
    }

    @Override
    public void setForeground(Color fore){
        super.setForeground(fore);
        if (title != null){
            title.setForeground(fore);
        }
    }

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
