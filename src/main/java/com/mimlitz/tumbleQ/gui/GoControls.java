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

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import net.miginfocom.swing.MigLayout;

public class GoControls extends JPanel {

    private Map<String, Runnable> actions;

    public GoControls(){
        actions = new TreeMap<>();
        setOpaque(false);
        setLayout(new MigLayout("", "10[grow,fill][grow,fill][grow,fill][grow,fill]10", "10[grow,fill]10[grow,fill]10"));
        initialize();
    }

    private void initialize(){
        BigButton go = new BigButton("GO", getImage("/controls/Play.png"));
        go.setIconLoc(BigButton.ICON_RIGHT);
        go.addActionListener(this::runAction);
        add(go, "cell 1 0 2 1");

        BigButton back = new BigButton("LAST", getImage("/controls/Skip Back.png"));
        back.setIconLoc(BigButton.ICON_LEFT);
        back.addActionListener(this::runAction);
        add(back, "cell 0 1 2 1");

        BigButton next = new BigButton("NEXT", getImage("/controls/Skip Forward.png"));
        next.setIconLoc(BigButton.ICON_RIGHT);
        next.addActionListener(this::runAction);
        add(next, "cell 2 1 2 1");
    }

    public void setGoAction(Runnable action){
        actions.put("GO", action);
    }

    public void setBackAction(Runnable action){
        actions.put("LAST", action);
    }

    public void setNextAction(Runnable action){
        actions.put("NEXT", action);
    }

    private ImageIcon getImage(String name){
        return new ImageIcon(CueControls.class.getResource(name));
    }

    private void runAction(ActionEvent e){
        if (actions.containsKey(e.getActionCommand())){
            actions.get(e.getActionCommand()).run();
        }
    }

    @Override
    public void setForeground(Color fore){
        super.setForeground(fore);
        for (Component comp : getComponents()){
            comp.setForeground(fore);
        }
    }

    @Override
    public void setFont(Font font){
        super.setFont(font);
        for (Component comp : getComponents()){
            comp.setFont(font);
        }
    }

}

class BigButton extends JPanel {

    public static final int ICON_LEFT = 0, ICON_RIGHT = 1;

    private final JLabel text;
    private final ImageDisplay icon;
    private Set<ActionListener> listeners;

    public BigButton(String text, Icon image){
        listeners = new HashSet<>();
        this.text = new JLabel();
        this.icon = new ImageDisplay();
        setLayout(new MigLayout("", "[fill,grow,right,50%][fill,grow,left,50%]", "[fill,grow,align 50%]"));
        setIconLoc(ICON_RIGHT);
        setText(text);
        setIcon(image);
        this.icon.setMaximumSize(new Dimension(this.text.getPreferredSize().height*2, this.text.getPreferredSize().height*2));
        setOpaque(false);
        setBorder(new LineBorder(getForeground(), 2));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fireAction();
            }
        });
    }

    @Override
    public void setForeground(Color fore){
        super.setForeground(fore);
        setBorder(new LineBorder(getForeground(), 2));
        try {
            text.setForeground(fore);
        } catch (NullPointerException e) {}
    }

    @Override
    public void setFont(Font font){
        super.setFont(font);
        try {
            text.setFont(font);
        } catch (NullPointerException e) {}
    }

    public void setText(String text){
        this.text.setText(text);
        repaint();
    }

    public String getText(){
        return text.getText();
    }

    public void setIcon(Icon icon){
        this.icon.setIcon(icon);
        repaint();
    }

    public Icon getIcon(){
        return icon.getImage();
    }

    public void setIconLoc(int loc){
        removeAll();
        add(loc == ICON_LEFT ? icon : text, "cell 0 0");
        add(loc == ICON_LEFT ? text : icon, "cell 1 0");
        text.setHorizontalAlignment(loc == ICON_LEFT ? SwingConstants.LEFT : SwingConstants.RIGHT);
        revalidate();
        repaint();
    }

    public void addActionListener(ActionListener listener){
        listeners.add(listener);
    }

    private void fireAction(){
        for (ActionListener listener : listeners){
            listener.actionPerformed(new ActionEvent(this, 0, text.getText()));
        }
    }

    @Override
    public Dimension getMinimumSize(){
        Dimension bounds = text.getPreferredSize();
        return new Dimension(2*bounds.width+10, 4*bounds.height);
    }

    @Override
    public Dimension getPreferredSize(){
        Dimension bounds = text.getPreferredSize();
        return new Dimension(2*bounds.width+30, 4*bounds.height);
    }

}
