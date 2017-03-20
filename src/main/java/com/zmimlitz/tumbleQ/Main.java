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

import javafx.application.Application;
import javafx.stage.Stage;
import javax.swing.JFileChooser;
import java.io.File;
import java.util.concurrent.CancellationException;

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
            load(open);
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
        GUI.load(file);
    }

    @Override
    public void init(){

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GUI.showForm();
    }

}
