package com.mimlitz.tumbleQ.gui;

import javax.swing.JComponent;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

public class Divider extends JComponent {

    private int thick;

    public Divider(){
        setThickness(2);
        setForeground(Color.GRAY);
    }

    public void setThickness(int thick){
        if (thick <= 0){
            throw new IllegalArgumentException("Must be positive");
        }
        this.thick = thick;
    }

    public int getThickness(){
        return thick;
    }

    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(getForeground());
        g2.setStroke(new BasicStroke(thick));
        g2.draw(new Line2D.Float(0, getHeight()/2, getWidth(), getHeight()/2));
    }

    @Override
    public Dimension getPreferredSize(){
        return new Dimension(100, thick+2);
    }

}
