package com.mimlitz.tumbleQ.gui;

import com.mimlitz.tumbleQ.sound.SoundClip;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.Timer;

public class ClipViewer extends JComponent {

    private SoundClip clip;
    private int track_height = 15;
    private int slider_width = 30;

    public ClipViewer(){
        Timer update = new Timer(200, (a) -> repaint());
        update.start();
    }

    public void setClip(SoundClip clip){
        this.clip = clip;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if (clip != null) {
//            int[] data = clip.getData();
//            int max = 0;
//            for (int i : data) {
//                if (i > max) {
//                    max = i;
//                }
//            }
//            double bar_width = getWidth() / (double) data.length;
//            double start = 0;
//            g.setColor(getForeground());
//            for (int dat : data) {
//                int height = getHeight() / 2 * dat / max;
//                g.fillRect((int) start, getHeight() / 2 - height, (int) (start + bar_width), 2 * height);
//            }

            g.setColor(getBackground());
            g.fillRoundRect(5, (getHeight()-track_height)/2, getWidth()-10, track_height, track_height/2, track_height/2);
            g.setColor(getForeground());
            int pos = (getWidth()-slider_width-10)*clip.getTime()/(clip.getLength()-1);
            g.fillRoundRect(pos+5, (getHeight()-track_height)/2+1, slider_width, track_height-2, track_height/2, track_height/2);
        }
    }

}
