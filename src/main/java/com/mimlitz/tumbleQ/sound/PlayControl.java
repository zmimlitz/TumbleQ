package com.mimlitz.tumbleQ.sound;

import com.mimlitz.tumbleQ.gui.ClipViewer;
import com.mimlitz.tumbleQ.gui.ImageDisplay;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.TreeMap;

public class PlayControl extends JPanel {

    private SoundClip clip;
    private ClipViewer view;
    private JLabel title;
    private ImageDisplay playPause;
    private ImageDisplay bookmark;
    private Map<String, Runnable> actions;

    PlayControl(){
        actions = new TreeMap<>();
        setOpaque(false);
        setLayout(new BorderLayout());

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BorderLayout());
        add(header, BorderLayout.NORTH);

        title = new JLabel();
        header.add(title, BorderLayout.WEST);

        JPanel controls = new JPanel();
        controls.setOpaque(false);
        controls.setLayout(new FlowLayout(FlowLayout.TRAILING));
        controls.setPreferredSize(new Dimension(200, 40));
        header.add(controls, BorderLayout.CENTER);

        ImageDisplay rewind = new ImageDisplay();
        rewind.setIcon(getImage("/controls/Rewind.png"));
        rewind.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rewind.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fireAction("Rewind");
            }
        });
        controls.add(rewind);

        controls.add(new JLabel(" "));

        ImageDisplay stop = new ImageDisplay();
        stop.setIcon(getImage("/controls/Stop.png"));
        stop.setCursor(new Cursor(Cursor.HAND_CURSOR));
        stop.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fireAction("Stop");
            }
        });
        controls.add(stop);

        controls.add(new JLabel(" "));

        playPause = new ImageDisplay();
        playPause.setIcon(getImage("/controls/Play.png"));
        playPause.setCursor(new Cursor(Cursor.HAND_CURSOR));
        playPause.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fireAction("Play");
            }
        });
        controls.add(playPause);

        controls.add(new JLabel(" "));

        ImageDisplay forward = new ImageDisplay();
        forward.setIcon(getImage("/controls/Fast_Forward.png"));
        forward.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forward.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fireAction("Fast Forward");
            }
        });
        controls.add(forward);

        view = new ClipViewer();
        view.setOpaque(false);
        view.setBackground(Color.GRAY);
        view.setForeground(Color.WHITE);
        add(view, BorderLayout.CENTER);

        JPanel controls2 = new JPanel();
        controls2.setLayout(new FlowLayout(FlowLayout.LEFT));
        controls2.setOpaque(false);
        controls2.setPreferredSize(new Dimension(200, 40));
        add(controls2, BorderLayout.SOUTH);

        ImageDisplay lastMark = new ImageDisplay();
        lastMark.setIcon(getImage("/controls/Last.png"));
        lastMark.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lastMark.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fireAction("Last");
            }
        });
        controls2.add(lastMark);

        bookmark = new ImageDisplay();
        bookmark.setIcon(getImage("/controls/Bookmark.png"));
        bookmark.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bookmark.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fireAction("Bookmark");
            }
        });
        controls2.add(bookmark);

        ImageDisplay nextMark = new ImageDisplay();
        nextMark.setIcon(getImage("/controls/Next.png"));
        nextMark.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nextMark.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fireAction("Next");
            }
        });
        controls2.add(nextMark);

        actions.put("Play", () -> {
            if (clip != null) {
                if (clip.isPlaying()) {
                    clip.pause();
                } else {
                    clip.play();
                }
            }
        });
        actions.put("Stop", () -> {
            if (clip != null) {
                clip.pause();
                clip.setToTime(0);
            }
        });
        actions.put("Rewind", () -> {
            if (clip != null){
                clip.setToTime(clip.getTime()-5);
            }
        });
        actions.put("Fast Forward", () -> {
            if (clip != null){
                clip.setToTime(clip.getTime()+5);
            }
        });
        actions.put("Last", () -> {
            if (clip != null) {
                clip.lastBookmark();
            }
        });
        actions.put("Bookmark", () -> {
            if (clip != null) {
                int time = clip.getTime();
                for (int mark : clip.getBookmarks()){
                    if (Math.abs(mark-time) < 2){
                        clip.removeBookmark(mark);
                        repaint();
                        return;
                    }
                }
                clip.addBookmark(clip.getTime());
                repaint();
            }
        });
        actions.put("Next", () -> {
            if (clip != null){
                clip.nextBookmark();
            }
        });

        new Timer(200, (a) -> {
            updatePlayPause();
            updateBookmarks();
        }).start();
    }

    public void updateClip(SoundClip clip){
        this.clip = clip;
        this.view.setClip(clip);
        this.title.setText("  " + clip.getName());
    }

    public void firePlayPauseAction(){
        fireAction("Play");
    }

    public void fireStopAction(){
        fireAction("Stop");
    }

    public void fireRewindAction(){
        fireAction("Rewind");
    }

    public void fireFastForwardAction(){
        fireAction("Fast Forward");
    }

    public void fireLastBookmarkAction(){
        fireAction("Last");
    }

    public void fireNextBookmarkAction(){
        fireAction("Next");
    }

    public void fireBookmarkAction(){
        fireAction("Bookmark");
    }

    private void fireAction(String cmd){
        if (actions.containsKey(cmd)){
            actions.get(cmd).run();
        }
    }

    private void updatePlayPause(){
        if (clip == null || !clip.isPlaying()){
            playPause.setIcon(getImage("/controls/Play.png"));
        }
        else {
            playPause.setIcon(getImage("/controls/Pause.png"));
        }
    }

    private void updateBookmarks(){
        if (clip == null){
            return;
        }
        bookmark.setIcon(getImage("/controls/Bookmark.png"));
        int time = clip.getTime();
        for (Integer mark : clip.getBookmarks()){
            if (mark == 0){
                continue;
            }
            if (Math.abs(time-mark) < 2){
                bookmark.setIcon(getImage("/controls/Cancel.png"));
            }
        }
    }

    private ImageIcon getImage(String name){
        return new ImageIcon(PlayControl.class.getResource(name));
    }

    @Override
    public void setFont(Font font){
        super.setFont(font);
        if (title != null) {
            title.setFont(font);
        }
    }

    @Override
    public void setForeground(Color fore){
        super.setForeground(fore);
        if (title != null){
            title.setForeground(fore);
        }
    }

}