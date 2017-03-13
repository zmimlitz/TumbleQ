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

package com.mimlitz.tumbleQ.sound;

import com.mimlitz.tumbleQ.gui.VolumeFader;
import javax.swing.JPanel;

import java.awt.BorderLayout;

public class VolumeControl extends JPanel {

    private VolumeFader fader;
    private SoundClip clip;

    VolumeControl(){
        setOpaque(false);
        setLayout(new BorderLayout());

        fader = new VolumeFader();
        fader.addChangeListener((a) -> {
            if (clip != null){
                clip.setVolume(fader.getVolume());
            }
        });
        add(fader, BorderLayout.CENTER);
    }

    void updateClip(SoundClip clip){
        this.clip = clip;
        clip.setVolume(fader.getVolume());
    }

    public void increaseVolume(){
        fader.pushUp();
    }

    public void decreaseVolume(){
        fader.pushDown();
    }

}
