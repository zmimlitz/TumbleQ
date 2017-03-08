package com.mimlitz.tumbleQ.util.io;

import java.io.File;

public class MyFileFilter extends javax.swing.filechooser.FileFilter {

    public static String getSuffix(){
        return ".tmblQ";
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
        return suffix.equalsIgnoreCase(".tmblQ");
    }

    @Override
    public String getDescription(){
        return "TumbleQ Files";
    }

}
