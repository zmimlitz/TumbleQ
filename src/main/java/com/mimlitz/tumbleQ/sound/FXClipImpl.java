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

    private MediaPlayer clip;
    private PriorityQueue<Integer> bookmarks;

    public FXClipImpl(){
        bookmarks = new PriorityQueue<>();
    }

    @Override
    public void load(File file) throws IOException {
        try {
            Media media = new Media(file.toURI().toString());
            clip = new MediaPlayer(media);
            setVolume(70);
        }
        catch (Exception e){
            throw new IOException("Could not read in sound file", e);
        }
    }

    @Override
    public void play() {
        clip.play();
    }

    @Override
    public void pause() {
        clip.pause();
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
    public void setVolume(int vol) {
        clip.setVolume(vol/100.);
    }

}
