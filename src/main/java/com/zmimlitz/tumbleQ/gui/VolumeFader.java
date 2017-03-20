/*
 * TumbleQ: Theatrical Cuing Software
 * Copyright (C) 2017  Zac Mimlitz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.zmimlitz.tumbleQ.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;

public class VolumeFader extends JSlider {

    private int push = 25;
    private FaderUI ui;

    public VolumeFader(){
        setOrientation(JSlider.VERTICAL);
        setMinimum(-20*100);
        setMaximum(10*100);
        setValue(0);
        setBorder(null);
        setOpaque(false);
        setSnapToTicks(false);
        for (KeyListener listener : super.getKeyListeners()){
            super.removeKeyListener(listener);
        }

        ui = new FaderUI(this, new ImageIcon(VolumeFader.class.getResource("/Fader Knob.png")));
        setUI(ui);

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
        g.setColor(Color.LIGHT_GRAY);
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
        int height = ui.yPositionForValue((int)inverseVolume(val));
        g.drawLine(getWidth()/2-35, height, getWidth()/2+35, height);
    }

    @Override
    public void setValue(int value){
        super.setValue(value);
    }

    @Override
    public Dimension getPreferredSize(){
        return new Dimension(100, super.getPreferredSize().height);
    }
    
    private class FaderUI extends BasicSliderUI {
        
        private ImageIcon knob;
        
        public FaderUI(JSlider slider, ImageIcon knob){
            super(slider);
            this.knob = knob;
        }
        
        @Override
        public void paintThumb(Graphics g){
            int y = yPositionForValue(getValue());
            g.drawImage(knob.getImage(), (getWidth()-knob.getIconWidth())/2, y-knob.getIconHeight()/2, null);
        }

        @Override
        public int yPositionForValue(int value){
            return super.yPositionForValue(value);
        }

        @Override
        public Dimension getThumbSize(){
            return new Dimension(knob.getIconWidth(), knob.getIconHeight());
        }
        
    }

    public void pushUp(){
        setValue(getValue()+push);
    }

    public void pushDown(){
        setValue(getValue()-push);
    }

}
