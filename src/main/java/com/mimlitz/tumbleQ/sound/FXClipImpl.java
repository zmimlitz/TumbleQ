package com.mimlitz.tumbleQ.sound;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class FXClipImpl implements SoundClip {

    private String name = "?";
    private MediaPlayer clip;
    private PriorityQueue<Integer> bookmarks;
    private boolean playing = false;

    public FXClipImpl(){
        bookmarks = new PriorityQueue<>();
    }

    @Override
    public void setName(String name){
        this.name = name;
    }

    @Override
    public void load(File file) throws IOException {
        try {
            Media media = new Media(file.toURI().toString());
            clip = new MediaPlayer(media);
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
        clip.seek(new Duration(sec*1000));
    }

    @Override
    public int getTime() {
        return 0;
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
    public List<Integer> getBookmarks() {
        List<Integer> out = new ArrayList<>();
        out.addAll(bookmarks);
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
