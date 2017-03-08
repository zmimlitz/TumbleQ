package com.mimlitz.tumbleQ.gui;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JSlider;

public class VolumeFader extends JSlider {

    public VolumeFader(){
        setOrientation(JSlider.VERTICAL);
        setMinimum(-20*100);
        setMaximum(10*100);
        setValue(0);

        setOpaque(false);
        setSnapToTicks(false);
        setMajorTickSpacing(1000);
        setMinorTickSpacing(100);
    }

    public double getVolume(){
        double x = getValue()/100.;
        double V = 43.99*Math.atan(0.16667*x-0.1756) + 56.75;
        return V/100.;
    }

    private double inverseVolume(double vol){
        double V = vol*100;
        double x = (Math.tan((V - 56.75)/43.99) + 0.1756)/0.16667;
        return x*100;
    }

    @Override
    public void paintComponent(Graphics g){
        drawTick(0.1, g);
        drawTick(0.25, g);
        drawTick(0.4, g);
        drawTick(0.5, g);
        drawTick(0.6, g);
        drawTick(0.75, g);
        drawTick(0.9, g);
        super.paintComponent(g);
    }

    private void drawTick(double val, Graphics g){
        int range = getMaximum()-getMinimum();
        int height = (int)((inverseVolume(val)-getMinimum())*getHeight()/range);
        g.setColor(Color.LIGHT_GRAY);
        g.drawLine(getWidth()/2-50, getHeight()-height, getWidth()/2+50, getHeight()-height);
    }

}
