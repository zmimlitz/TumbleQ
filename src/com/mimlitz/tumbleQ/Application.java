package com.mimlitz.tumbleQ;

import com.mimlitz.tumbleQ.gui.Form;
import com.mimlitz.tumbleQ.util.io.MyFileFilter;

import javax.swing.JFileChooser;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CancellationException;

public class Application {

    public static void main(String[] args){
        Form gui = Form.getInstance();
        Application app = new Application(gui);
        try {
            File open = openFile();
            if (!open.exists()){
                throw new CancellationException("Not a real file");
            }
            app.load(open);
        }
        catch (CancellationException e){
            app.init();
        }
    }

    private static File openFile() throws CancellationException {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new MyFileFilter());
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
            return chooser.getSelectedFile();
        }
        else {
            throw new CancellationException("Chooser Canceled");
        }
    }

    private final Form GUI;

    private Application(Form gui){
        this.GUI = gui;
    }

    void load(File file){

        init();
    }

    void init(){

    }

}
