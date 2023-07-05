package com.conmu.bamboo.structurepattern.adapter.impl;

import com.conmu.bamboo.structurepattern.adapter.inter.AdvancedMediaPlayer;

/**
 * @author mucongcong
 * @date 2022/08/15 16:52
 * @since
 **/
public class Mp4Player implements AdvancedMediaPlayer {
    @Override
    public void playVlc(String fileName) {
        //什么也不做
    }

    @Override
    public void playMp4(String fileName) {
        System.out.println("Playing mp4 file. Name: "+ fileName);
    }
}
