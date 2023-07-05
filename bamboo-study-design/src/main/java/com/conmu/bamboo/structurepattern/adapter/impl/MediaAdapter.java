package com.conmu.bamboo.structurepattern.adapter.impl;

import com.conmu.bamboo.structurepattern.adapter.inter.AdvancedMediaPlayer;
import com.conmu.bamboo.structurepattern.adapter.inter.MediaPlayer;

/**
 * @author mucongcong
 * @date 2022/08/15 16:53
 * @since
 **/
public class MediaAdapter implements MediaPlayer {
    AdvancedMediaPlayer advancedMediaPlayer;

    public MediaAdapter(String audioType) {
        if (audioType.equalsIgnoreCase("vlc")) {
            advancedMediaPlayer = new VlcPlayer();
        } else if (audioType.equalsIgnoreCase("mp4")) {
            advancedMediaPlayer = new Mp4Player();
        }
    }
    @Override
    public void play(String audioType, String fileName) {
        if (audioType.equalsIgnoreCase("vlc")) {
            advancedMediaPlayer.playVlc(fileName);
        } else if (audioType.equalsIgnoreCase("mp4")) {
            advancedMediaPlayer.playMp4(fileName);
        }
    }
}
