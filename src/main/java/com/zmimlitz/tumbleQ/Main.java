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

package com.zmimlitz.tumbleQ;

import com.zmimlitz.tumbleQ.gui.Form;
import com.zmimlitz.tumbleQ.util.io.MyFileFilter;

import java.io.IOException;
import java.util.TreeSet;
import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import java.io.File;
import java.util.concurrent.CancellationException;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class Main extends Application {

    private static String[] arguments;

    public static void main(String[] args){
        arguments = args;
        launch(args);
    }

    private final Form GUI;

    public Main(){
        this.GUI = Form.getInstance();
        if (arguments.length > 0){
            String filepath = arguments[0];
            File file = new File(filepath);
            if (file.exists() && new MyFileFilter().accept(file)){
                load(file);
            }
            else {
                JOptionPane.showConfirmDialog(GUI, "Cannot open the file " + file.getAbsolutePath(),
                        "Failed to Load", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            }
        }
        else {
            try {
                File open = openFile();
                if (!open.exists()) {
                    throw new CancellationException("Not a real file");
                }
                load(open);
            } catch (CancellationException e) {}
        }
        startSpeakerSaver();
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
        GUI.load(file);
    }

    @Override
    public void init(){

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GUI.showForm();
    }

    private void startSpeakerSaver(){
        try {
            Media media = new Media(Main.class.getResource("/speaker_saver.mp3").toString());
            MediaPlayer clip = new MediaPlayer(media);
            clip.setVolume(0.1);
            new Timer((int)(1.9*60*1000), (a) -> clip.play()).start();
        }
        catch (Exception e){
            System.err.println("Could not load empty track");
            e.printStackTrace();
        }
    }

}
