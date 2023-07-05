package com.conmu.bamboo.createpattern.abstractfactory.impl;

import com.conmu.bamboo.createpattern.abstractfactory.inter.Color;

/**
 * @author mucongcong
 * @date 2022/08/05 17:25
 * @since
 **/
public class Red implements Color {

    @Override
    public void fill() {
        System.out.println("Inside Red::fill() method.");
    }
}
