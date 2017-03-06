package com.mimlitz.tumbleQ.gui;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
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

    public CueControls(){
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setOpaque(false);
        actions = new TreeMap<>();

        ActionLabel add = new ActionLabel(new ImageIcon(CueControls.class.getResource("/cues/Add.png")), "Add");
        add.setForeground(Color.WHITE);
        add.addActionListener((ActionEvent e) -> {
                if (actions.containsKey("add")){
                    actions.get("add").run();
                }
            }
        );
        add(add);
    }

    public void setAddAction(Runnable action){
        actions.put("add", action);
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
            listener.actionPerformed(new ActionEvent(this, 0, "action"));
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
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
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
