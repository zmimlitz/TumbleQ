package com.mimlitz.tumbleQ.util.io;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mimlitz.tumbleQ.sound.CueListing;
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
            listing.addCue(new File(entry.file), entry.link);

        }
        return listing;
    }

    public static class Builder {

        private List<SaveEntry> entries;

        private Builder(){
            entries = new ArrayList<>();
        }

        public Builder addEntry(File f, String link){
            entries.add(new SaveEntry(f.getAbsolutePath(), link));
            return this;
        }

        public SaveFile build(){
            return new SaveFile(entries);
        }

    }

}

class SaveEntry {

    public final String file, link;

    @JsonCreator
    public SaveEntry(@JsonProperty("file") String file, @JsonProperty("link") String link){
        this.file = file;
        this.link = link;
    }

}
