package com.conmu.bamboo.createpattern.builder.impl;

import com.conmu.bamboo.createpattern.builder.inter.Item;

/**
 * @author mucongcong
 * @date 2022/08/11 20:47
 * @since
 **/
public abstract class ColdDrink implements Item {

    @Override
    public Packing packing() {
        return new Bottle();
    }

    @Override
    public abstract float price();
}