package com.mimlitz.tumbleQ.gui;

import com.mimlitz.tumbleQ.sound.SoundClip;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.Timer;

public class ClipViewer extends JComponent {

    private SoundClip clip;
    private final int track_height = 15;
    private final int slider_width = 20;

    public ClipViewer(){
        new Timer(200, (a) -> repaint()).start();
    }

    public void setClip(SoundClip clip){
        this.clip = clip;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if (clip != null) {
            g.setColor(getBackground());
            g.fillRoundRect(5, (getHeight()-track_height)/2, getWidth()-10, track_height,
                    track_height/2, track_height/2);
            g.setColor(getForeground());
            int pos = (getWidth()-slider_width-10)*clip.getTime()/(clip.getLength()-1);
            g.fillRoundRect(pos+5, (getHeight()-track_height)/2+1, slider_width, track_height-2,
                    track_height/2, track_height/2);
            g.setColor(Color.GREEN);
            for (int mark : clip.getBookmarks()){
                if (mark == 0){
                    continue;
                }
                g.drawRoundRect((getWidth()-slider_width-10)*mark/(clip.getLength()-1)+5,
                        (getHeight()-track_height)/2+1, slider_width,
                        track_height-2,
                        track_height/2, track_height/2);
            }
        }
    }

}
