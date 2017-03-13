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

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.util.Optional;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ImageDisplay extends JLabel{

    private Optional<Icon> image;

    public ImageDisplay(){
        image = Optional.empty();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateImage();
            }
        });
    }

    public Icon getImage(){
        if (image.isPresent()){
            return image.get();
        }
        return null;
    }

    protected void updateImage(){
        if (image.isPresent() && getWidth()> 5 && getHeight() > 5){
            Dimension imageSize = getImageSize();
            super.setIcon(resize(image.get(), imageSize.width, imageSize.height));
        }
    }

    private ImageIcon resize(Icon image, int width, int height) {
        try {
            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
            Graphics2D g = bi.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Composite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1);
            g.setComposite(comp);
            g.drawImage(((ImageIcon) (image)).getImage(), 0, 0, width, height, null);
            g.dispose();
            return new ImageIcon(bi);
        }
        catch (Exception i) {
            throw new RuntimeException(i);
        }
    }

    @Override
    public void setIcon(Icon img){
        if (img == null || img.getIconWidth() == 0 || img.getIconHeight() == 0){
            image = Optional.empty();
        }
        else {
            image = Optional.of(img);
        }
        updateImage();
    }

   private Dimension getImageSize(){
        if (!image.isPresent()){
            return new Dimension(0, 0);
        }
        else {
            if (getHeight() < getWidth()) {
                return new Dimension(getHeight() * image.get().getIconWidth() / image.get().getIconHeight() -1,
                        getHeight()-1);
            } else {
                return new Dimension(getWidth() - 1,
                        getWidth() * image.get().getIconHeight() / image.get().getIconWidth() - 1);
            }
        }
    }

    @Override
    public Dimension getPreferredSize(){
        if (!image.isPresent() || getParent() == null || getParent().getWidth() == 0 || getParent().getHeight() == 0){
            return new Dimension(0, 0);
        }
        else {
            if (getParent().getHeight() < getParent().getWidth()) {
                return new Dimension(getParent().getHeight() * image.get().getIconWidth() / image.get().getIconHeight() -1,
                        getParent().getHeight()-1);
            } else {
                return new Dimension(getParent().getWidth() - 1,
                        getParent().getWidth() * image.get().getIconHeight() / image.get().getIconWidth() - 1);
            }
        }
    }

}

