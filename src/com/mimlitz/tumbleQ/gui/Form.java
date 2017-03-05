package com.mimlitz.tumbleQ.gui;

import java.util.Optional;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Form extends JFrame {

    private static Optional<Form> instance = Optional.empty();

    public static Form getInstance(){
        if (!instance.isPresent()){
            instance = Optional.of(new Form());
        }
        return instance.get();
    }

    private Form(){
        initialize();
    }

    private void initialize(){
        JPanel content = new JPanel();
        content.setLayout(null);
    }

    public void showForm(){
        setVisible(true);
    }

}
