package com.conmu.bamboo.createpattern.builder.extend.burger;

import com.conmu.bamboo.createpattern.builder.impl.Burger;

/**
 * @author mucongcong
 * @date 2022/08/11 20:50
 * @since
 **/
public class ChickenBurger extends Burger {

    @Override
    public float price() {
        return 50.5f;
    }

    @Override
    public String name() {
        return "Chicken Burger";
    }
}