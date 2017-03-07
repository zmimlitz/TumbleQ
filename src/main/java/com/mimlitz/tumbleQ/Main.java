package com.mimlitz.tumbleQ;

import com.mimlitz.tumbleQ.gui.Form;
import com.mimlitz.tumbleQ.util.io.MyFileFilter;

import javafx.application.Application;
import javafx.stage.Stage;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import java.io.File;
import java.util.concurrent.CancellationException;
import javax.swing.JFrame;

public class Main extends Application {

    public static void main(String[] args){
        launch(args);
    }

    private final Form GUI;

    public Main(){
        this.GUI = Form.getInstance();
        try {
            File open = openFile();
            if (!open.exists()){
                throw new CancellationException("Not a real file");
            }
        }
        catch (CancellationException e){}
    }

    private File openFile() throws CancellationException {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new MyFileFilter());
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (chooser.showOpenDialog(GUI) == JFileChooser.APPROVE_OPTION){
            return chooser.getSelectedFile();
        }
        else {
            throw new CancellationException("Chooser Canceled");
        }
    }

    void load(File file){

    }

    @Override
    public void init(){

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GUI.showForm();
    }

}
