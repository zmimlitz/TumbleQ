package com.mimlitz.tumbleQ.sound;

import java.io.File;
import java.io.IOException;
import java.util.IllegalFormatException;
import java.util.List;

public interface SoundClip {

    void load(File fin) throws IOException;

    void play();

    void pause();

    void setToTime(int sec);

    int getTime();

    void addBookmark(int sec);

    List<Integer> getBookmarks();

    void setVolume(int vol);

}
