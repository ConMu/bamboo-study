package com.conmu.bamboo.createpattern.builder.impl;

import com.conmu.bamboo.createpattern.builder.inter.Packing;

/**
 * @author mucongcong
 * @date 2022/08/11 20:45
 * @since
 **/
public class Bottle implements Packing {
    @Override
    public String pack() {
        return "Bottle";
    }
}
