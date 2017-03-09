package com.mimlitz.tumbleQ.gui;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import net.miginfocom.swing.MigLayout;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ComboBox extends JPanel {

    private JLabel display;
    private ImageDisplay button;
    private JComboBox<String> driver;

    private Set<ActionListener> listeners;

    public ComboBox(String... items){
        listeners = new HashSet<>();

        setOpaque(true);
        setLayout(null);

        display = new JLabel();
        add(display);

        button = new ImageDisplay();
        button.setIcon(new ImageIcon(ComboBox.class.getResource("/controls/Combo Select.png")));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                driver.showPopup();
            }
        });
        add(button);

        driver = new JComboBox<>(items);
        driver.addActionListener((ActionEvent e) -> setSelectedItem((String)driver.getSelectedItem()));
        add(driver);

        setForeground(getForeground());
        redraw();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                redraw();
            }
        });
    }

    private void redraw(){
        button.setBounds(getWidth()-getHeight()-2, 0, getHeight()+2, getHeight());
        display.setBounds(0, 0, getWidth()-getHeight()-2, getHeight());
        driver.setBounds(0, getHeight(), getWidth(), 0);
    }

    @Override
    public void setForeground(Color fore){
        super.setForeground(fore);
        if (display != null) {
            display.setForeground(fore);
            display.setBorder(new LineBorder(fore.darker(), 1));
        }
        if (button != null){
            button.setBorder(new LineBorder(fore.darker(), 1));
        }
        repaint();
    }

    @Override
    public void setFont(Font font){
        super.setFont(font);
        if (display != null){
            display.setFont(font);
        }
        repaint();
    }

    public void setSelectedItem(String item){
        if (driver != null) {
            if (!driver.getSelectedItem().equals(item)) {
                driver.setSelectedItem(item);
            }
            if (display != null){
                display.setText("  " + driver.getSelectedItem());
            }
        }
        fireActionEvent();
    }

    public String getSelectedItem(){
        return display.getText().substring(2);
    }

    public void addActionListener(ActionListener listener){
        listeners.add(listener);
    }

    private void fireActionEvent(){
        for (ActionListener listener : listeners){
            listener.actionPerformed(new ActionEvent(this, 0, "changed"));
        }
    }

}
