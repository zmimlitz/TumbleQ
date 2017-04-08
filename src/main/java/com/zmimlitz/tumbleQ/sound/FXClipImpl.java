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

package com.zmimlitz.tumbleQ.sound;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class FXClipImpl implements SoundClip {

    private String name = "?";
    private MediaPlayer clip;
    private Set<Integer> bookmarks;
    private boolean playing = false;
    private Optional<Double> init_level;

    public FXClipImpl(){
        bookmarks = new TreeSet<>();
        init_level = Optional.empty();
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
        if (vol > 1){
            vol = 1;
        }
        if (vol < 0){
            vol = 0;
        }
        clip.setVolume(vol);
    }

    @Override
    public double getVolume(){
        return clip.getVolume();
    }

    @Override
    public boolean hasInitialLevel(){
        return init_level.isPresent();
    }

    @Override
    public void setInitialLevel(double volume){
        init_level = Optional.of(volume);
    }

    @Override
    public double getInitialLevel(){
        return init_level.isPresent() ? init_level.get() : getVolume();
    }

    @Override
    public boolean isPlaying() {
        return playing;
    }

    @Override
    public void end(){
        clip.seek(clip.getTotalDuration());
    }

}
