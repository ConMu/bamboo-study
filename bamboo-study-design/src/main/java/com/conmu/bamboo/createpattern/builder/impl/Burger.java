package com.conmu.bamboo.createpattern.builder.impl;

import com.conmu.bamboo.createpattern.builder.inter.Item;
import com.conmu.bamboo.createpattern.builder.inter.Packing;

/**
 * @author mucongcong
 * @date 2022/08/11 20:46
 * @since
 **/
public abstract class Burger implements Item {

    @Override
    public Packing packing(){
        return new Wrapper();
    }

    @Override
    public abstract float price();
}
