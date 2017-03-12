package com.mimlitz.tumbleQ.gui;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.metal.MetalScrollBarUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

public class MyScrollUI extends MetalScrollBarUI {

    private Color fore = Color.BLACK;
    private JButton fakeBtn;

    MyScrollUI(){
        fakeBtn = new JButton() {
            @Override
            public Dimension getPreferredSize(){
                return new Dimension(0, 0);
            }
        };
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds){
        g.setColor(fore.brighter());
        g.fillRoundRect(thumbBounds.x+4, thumbBounds.y+3, thumbBounds.width-7, thumbBounds.height-7,
                thumbBounds.width/2, thumbBounds.width/2);
        if (c.isOpaque()){
            c.setOpaque(false);
            c.repaint();
        }
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds){
        g.setColor(fore);
        g.drawRoundRect(trackBounds.x+1, trackBounds.y+1, trackBounds.width-2, trackBounds.height-2,
                trackBounds.width/2, trackBounds.width/2);
        g.drawRoundRect(trackBounds.x+2, trackBounds.y+2, trackBounds.width-4, trackBounds.height-4,
                trackBounds.width/2, trackBounds.width/2);
        if (c.isOpaque()){
            c.setOpaque(false);
            c.repaint();
        }
    }

    public void setForeground(Color fore){
        this.fore = fore;
    }

    public Color getForeground(){
        return fore;
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return fakeBtn;
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return fakeBtn;
    }

}
