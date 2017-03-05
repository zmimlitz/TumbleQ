package com.mimlitz.tumbleQ.util.io;

import java.io.File;

public class MyFileFilter extends javax.swing.filechooser.FileFilter {

    public static String getSuffix(){
        return "tmblQ";
    }

    @Override
    public boolean accept(File f){
        if (f.isDirectory()){
            return true;
        }
        String suffix = f.getName().substring(f.getName().lastIndexOf('.'));
        return suffix.equalsIgnoreCase(".tmblQ");
    }

    @Override
    public String getDescription(){
        return "TumbleQ Files";
    }

}
