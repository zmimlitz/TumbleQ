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

import java.awt.Color;
import java.awt.Font;
import java.util.Optional;

public class ClipPlayer {

    private SoundClip clip;
    private VolumeControl volumeCtrl;
    private PlayControl playControl;

    public ClipPlayer(){
        volumeCtrl = new VolumeControl();
        playControl = new PlayControl();
        playControl.setForeground(Color.WHITE);
        playControl.setFont(new Font(
                playControl.getFont().getName(),
                Font.BOLD,
                16
        ));
    }

    public void setCurrent(SoundClip clip){
        if (this.clip != null){
            this.clip.setToTime(0);
            if (clip != this.clip) {
                this.clip.pause();
            }
        }
        this.clip = clip;
        volumeCtrl.updateClip(this.clip);
        playControl.updateClip(this.clip);
        doSetAfter(Optional.empty());
    }

    public void setCurrent(SoundClip clip, Runnable after){
        setCurrent(clip);
        doSetAfter(Optional.of(after));
    }

    private void doSetAfter(Optional<Runnable> after){
        clip.setAfterAction(() -> {
            clip.setToTime(0);
            clip.pause();
            if (after.isPresent()){
                after.get().run();
            }
            clip.setAfterAction(null);
        });
    }

    public VolumeControl getVolumeControl(){
        return volumeCtrl;
    }

    public PlayControl getPlayControl(){
        return playControl;
    }

}
