package com.mimlitz.tumbleQ.gui;

import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.TreeMap;

public class CueControls extends JPanel {

    private Map<String, Runnable> actions;

    public CueControls(){
        setLayout(new BorderLayout());
        setOpaque(false);
        actions = new TreeMap<>();

        JPanel hold = new JPanel();
        hold.setLayout(new FlowLayout());
        hold.setOpaque(false);
        add(hold, BorderLayout.WEST);

        JLabel add = new JLabel("+ Add");
        add.setForeground(Color.WHITE);
        add.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (actions.containsKey("add")){
                    actions.get("add").run();
                }
            }
        });
        hold.add(add);
    }

    public void setAddAction(Runnable action){
        actions.put("add", action);
    }

}
