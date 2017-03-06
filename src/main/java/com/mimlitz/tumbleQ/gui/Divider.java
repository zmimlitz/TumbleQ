package com.mimlitz.tumbleQ.gui;

import javax.swing.JComponent;

import java.awt.Color;
import java.awt.Dimension;

public class Divider extends JComponent {

    private int height = 1;

    public Divider(){
        setBackground(Color.GRAY);
        draw();
    }

    public void setThickness(int thick){
        if (thick <= 0){
            throw new IllegalArgumentException("Must be positive");
        }
        height = thick;
        draw();
    }

    public int getThickness(){
        return height;
    }


    private void draw(){
        setSize(Integer.MAX_VALUE, height);
        setPreferredSize(new Dimension(0, height));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
    }

}
