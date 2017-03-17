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
