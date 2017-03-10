package com.mimlitz.tumbleQ.sound;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface SoundClip {

    void setName(String name);

    String getName();

    void load(File fin) throws IOException;

    void play();

    void pause();

    void setToTime(int sec);

    int getTime();

    int getLength();

    void setAfterAction(Runnable after);

    void addBookmark(int sec);

    List<Integer> getBookmarks();

    void setVolume(double vol);

    boolean isPlaying();

}
