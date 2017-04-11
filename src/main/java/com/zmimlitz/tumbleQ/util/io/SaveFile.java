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

package com.zmimlitz.tumbleQ.util.io;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zmimlitz.tumbleQ.sound.CueListing;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SaveFile {

    @JsonProperty("entries")
    public final List<SaveEntry> entries;

    @JsonCreator
    public SaveFile(@JsonProperty("entries") List<SaveEntry> entries){
        this.entries = Collections.unmodifiableList(entries);
    }

    public static Builder builder(){
        return new Builder();
    }

    @JsonIgnore
    public CueListing load(){
        CueListing listing = new CueListing();
        for (SaveEntry entry : entries){
            listing.addCue(new File(entry.file), entry.link, entry.volume, entry.bookmarks);

        }
        return listing;
    }

    public static class Builder {

        private List<SaveEntry> entries;

        private Builder(){
            entries = new ArrayList<>();
        }

        public Builder addEntry(File f, String link, double volume, List<Integer> bookmarks){
            entries.add(new SaveEntry(f.getAbsolutePath(), link, volume, bookmarks));
            return this;
        }

        public SaveFile build(){
            return new SaveFile(entries);
        }

    }

}

class SaveEntry {

    public final String file, link;
    public final List<Integer> bookmarks;
    public final double volume;

    @JsonCreator
    public SaveEntry(@JsonProperty("file") String file,
                     @JsonProperty("link") String link,
                     @JsonProperty("volume") double volume,
                     @JsonProperty("bookmarks") List<Integer> bookmarks){
        this.file = file;
        this.link = link;
        this.volume = volume;
        this.bookmarks = Collections.unmodifiableList(bookmarks);
    }

}
