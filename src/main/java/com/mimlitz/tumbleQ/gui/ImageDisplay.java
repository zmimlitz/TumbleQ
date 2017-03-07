package com.mimlitz.tumbleQ.gui;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.util.Optional;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

class ImageDisplay extends JLabel{

    private Optional<Icon> image;
    private boolean square = false;

    ImageDisplay(){
        image = Optional.empty();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateImage();
            }
        });
    }

    Icon getImage(){
        if (image.isPresent()){
            return image.get();
        }
        return null;
    }

    protected void updateImage(){
        if (image.isPresent() && getWidth()> 5 && getHeight() > 5){
            int width = this.getWidth(), height = this.getHeight();
            if (square){
                if (width < height){
                    height = width;
                }
                if (height < width){
                    width = height;
                }
            }
            super.setIcon(resize(image.get(), width, height));
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

    public void setSquare(boolean square){
        this.square = square;
        repaint();
    }

}
