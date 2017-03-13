package com.mimlitz.tumbleQ.sound;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;

import javafx.collections.transformation.SortedList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class FXClipImpl implements SoundClip {

    private String name = "?";
    private MediaPlayer clip;
    private Set<Integer> bookmarks;
    private boolean playing = false;

    public FXClipImpl(){
        bookmarks = new TreeSet<>();
    }

    @Override
    public void setName(String name){
        this.name = name;
    }

    @Override
    public String getName(){
        return name;
    }

    @Override
    public void load(File file) throws IOException {
        try {
            Media media = new Media(file.toURI().toString());
            clip = new MediaPlayer(media);
            bookmarks = new TreeSet<>();
            bookmarks.add(0);
            bookmarks.add(getLength());
            setVolume(0.5);
        }
        catch (Exception e){
            throw new IOException("Could not read in sound file", e);
        }
    }

    @Override
    public void play() {
        clip.play();
        playing = true;
    }

    @Override
    public void pause() {
        clip.pause();
        playing = false;
    }

    @Override
    public void setToTime(int sec) {
        if (sec < 0){
            sec = 0;
        }
        if (sec > getLength()){
            clip.seek(clip.getTotalDuration());
        }
        else {
            clip.seek(new Duration(sec * 1000));
        }
    }

    @Override
    public int getTime() {
        return (int)Math.round(clip.getCurrentTime().toSeconds());
    }

    @Override
    public int getLength(){
        return (int)Math.ceil(clip.getMedia().getDuration().toSeconds());
    }

    @Override
    public void setAfterAction(Runnable after){
        clip.setOnEndOfMedia(after);
    }

    @Override
    public void addBookmark(int sec) {
        bookmarks.add(sec);
    }

    @Override
    public void removeBookmark(int sec){
        bookmarks.remove(sec);
    }

    @Override
    public void nextBookmark(){
        int time = getTime();
        List<Integer> bookmarks = getBookmarks();
        for (Integer mark : bookmarks){
            if (mark > time){
                setToTime(mark);
                return;
            }
        }
    }

    @Override
    public void lastBookmark(){
        int time = getTime();
        List<Integer> bookmarks = getBookmarks();
        for (int i = bookmarks.size()-1; i >= 0; i--){
            if (bookmarks.get(i) < time){
                setToTime(bookmarks.get(i));
                return;
            }
        }
    }

    @Override
    public List<Integer> getBookmarks() {
        List<Integer> out = new ArrayList<>();
        out.addAll(bookmarks);
        Collections.sort(out);
        return Collections.unmodifiableList(out);
    }

    @Override
    public void setVolume(double vol) {
        clip.setVolume(vol);
    }

    @Override
    public boolean isPlaying() {
        return playing;
    }

}
