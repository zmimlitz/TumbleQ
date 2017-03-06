package com.mimlitz.tumbleQ.util.io;

import javax.swing.filechooser.FileFilter;

import java.io.File;

public class SoundFilter extends FileFilter {

    public static String getSuffix(){
        return "mp3";
    }

    @Override
    public boolean accept(File f){
        if (f.isDirectory()){
            return true;
        }
        if (f.getName().charAt(0) == '.'){
            return false;
        }
        if (f.getName().indexOf('.') == -1){
            return false;
        }
        String suffix = f.getName().substring(f.getName().lastIndexOf('.'));
        return suffix.equalsIgnoreCase(".mp3");
    }

    @Override
    public String getDescription() {
        return "Sound Files (mp3)";
    }

}
