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

package com.mimlitz.tumbleQ.util.io;

import javax.swing.Timer;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Optional;

public abstract class DoubleClickListener implements MouseListener {

    private static final int DELAY = 200;

    private Optional<MouseEvent> click;
    private long click_time = 0;
    private Timer timer;

    public DoubleClickListener(){
        click = Optional.empty();
        click_time = System.currentTimeMillis();
        timer = new Timer(DELAY, this::timeOut);
    }

    @Override
    public final void mouseClicked(MouseEvent e) {
        if (click.isPresent() && (System.currentTimeMillis() - click_time) < DELAY){
            timer.stop();
            mouseDoubleClicked(e);
        }
        else {
            click = Optional.of(e);
            timer.start();
        }
        click_time = System.currentTimeMillis();
    }

    private void timeOut(ActionEvent e){
        timer.stop();
        mouseClickedOnce(click.get());
        click = Optional.empty();
    }

    public abstract void mouseClickedOnce(MouseEvent e);

    public abstract void mouseDoubleClicked(MouseEvent e);

    @Override
    public final void mousePressed(MouseEvent e) {}

    @Override
    public final void mouseReleased(MouseEvent e) {}

    @Override
    public final void mouseEntered(MouseEvent e) {}

    @Override
    public final void mouseExited(MouseEvent e) {}
}
