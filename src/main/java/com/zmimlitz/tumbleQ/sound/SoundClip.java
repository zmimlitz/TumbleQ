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

    void removeBookmark(int sec);

    void nextBookmark();

    void lastBookmark();

    List<Integer> getBookmarks();

    void setVolume(double vol);

    double getVolume();

    boolean isPlaying();

    void end();

}
