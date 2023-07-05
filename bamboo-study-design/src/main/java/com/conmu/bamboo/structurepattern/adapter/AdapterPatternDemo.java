package com.conmu.bamboo.structurepattern.adapter;

import com.conmu.bamboo.structurepattern.adapter.impl.AudioPlayer;

/**
 * @author mucongcong
 * @date 2022/08/15 17:04
 * @since
 **/
public class AdapterPatternDemo {
    public static void main(String[] args) {
        AudioPlayer audioPlayer = new AudioPlayer();

        audioPlayer.play("mp3", "beyond the horizon.mp3");
        audioPlayer.play("mp4", "alone.mp4");
        audioPlayer.play("vlc", "far far away.vlc");
        audioPlayer.play("avi", "mind me.avi");
    }
}
