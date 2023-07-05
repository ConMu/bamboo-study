package com.conmu.bamboo.structurepattern.adapter.impl;

import com.conmu.bamboo.structurepattern.adapter.inter.AdvancedMediaPlayer;

/**
 * @author mucongcong
 * @date 2022/08/15 16:51
 * @since
 **/
public class VlcPlayer implements AdvancedMediaPlayer {
    @Override
    public void playVlc(String fileName) {
        System.out.println("Playing vlc file. Name: "+ fileName);
    }

    @Override
    public void playMp4(String fileName) {
        //什么也不做
    }
}
