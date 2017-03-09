package com.mimlitz.tumbleQ.gui;

import java.awt.BorderLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class CueControls extends JPanel {

    private Map<String, Runnable> actions;

    public CueControls() {
        setLayout(new BorderLayout());
        setOpaque(false);
        actions = new TreeMap<>();
        initialize();
    }

    private void initialize(){
        JPanel leftHold = new JPanel();
        leftHold.setLayout(new FlowLayout(FlowLayout.LEFT));
        leftHold.setOpaque(false);
        add(leftHold, BorderLayout.WEST);

        ActionLabel add = new ActionLabel(getImage("/controls/Add.png"), "Add");
        add.addActionListener(this::runAction);
        leftHold.add(add);

        ActionLabel remove = new ActionLabel(getImage("/controls/Remove.png"), "Remove");
        remove.addActionListener(this::runAction);
        leftHold.add(remove);

        leftHold.add(new JLabel("   "));

        ActionLabel up = new ActionLabel(getImage("/controls/Up.png"), "Move Up");
        up.addActionListener(this::runAction);
        leftHold.add(up);

        ActionLabel down = new ActionLabel(getImage("/controls/Down.png"), "Move Down");
        down.addActionListener(this::runAction);
        leftHold.add(down);

        JPanel rightHold = new JPanel();
        rightHold.setLayout(new FlowLayout(FlowLayout.RIGHT));
        rightHold.setOpaque(false);
        add(rightHold, BorderLayout.EAST);

        ActionLabel save = new ActionLabel(getImage("/controls/Save.png"), "Save");
        save.addActionListener(this::runAction);
        rightHold.add(save);
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

    public void setAddAction(Runnable action){
        actions.put("Add", action);
    }

    public void setRemoveAction(Runnable action){
        actions.put("Remove", action);
    }

    public void setMoveUpAction(Runnable action){
        actions.put("Move Up", action);
    }

    public void setMoveDownAction(Runnable action){
        actions.put("Move Down", action);
    }

    public void setSaveAction(Runnable action){
        actions.put("Save", action);
    }

    private ImageIcon getImage(String name){
        return new ImageIcon(CueControls.class.getResource(name));
    }

    private void runAction(ActionEvent e){
        if (actions.containsKey(e.getActionCommand())){
            actions.get(e.getActionCommand()).run();
        }
    }

}

class ActionLabel extends JComponent {

    private Icon icon;
    private String text;
    private Set<ActionListener> listeners;

    public ActionLabel(Icon icon, String title){
        listeners = new HashSet<>();
        setIcon(icon);
        setText(title);

        setCursor(new Cursor(Cursor.HAND_CURSOR));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fireAction();
            }
        });
    }

    public void setIcon(Icon icon){
        this.icon = icon;
    }

    public Icon getIcon(){
        return icon;
    }

    public void setText(String text){
        this.text = text;
    }

    public String getText(){
        return text;
    }

    public void addActionListener(ActionListener listener){
        listeners.add(listener);
    }

    private void fireAction(){
        for (ActionListener listener : listeners){
            listener.actionPerformed(new ActionEvent(this, 0, text));
        }
    }

    @Override
    public Dimension getPreferredSize(){
        Dimension text = getTextSize(getGraphics());
        return new Dimension(text.height+5+text.width+4, text.height+4);
    }

    @Override
    public void paintComponent(Graphics g){
        Dimension bounds = getTextSize(g);
        g.drawImage(resize(icon, bounds.height, bounds.height).getImage(), 2, (getHeight()-bounds.height)/2, null);
        g.setColor(getForeground());
        g.setFont(getFont());
        g.drawString(text, 7+bounds.height, (getHeight()-bounds.height)/2+bounds.height);
    }

    private Dimension getTextSize(Graphics context){
        FontMetrics metric = getFontMetrics(getFont());
        Rectangle2D bounds = metric.getStringBounds(text, context);
        return new Dimension((int)bounds.getWidth(), (int)bounds.getHeight());
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

}
