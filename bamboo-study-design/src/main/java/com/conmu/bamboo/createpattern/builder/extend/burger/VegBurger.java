package com.conmu.bamboo.createpattern.builder.extend.burger;

import com.conmu.bamboo.createpattern.builder.impl.Burger;

/**
 * @author mucongcong
 * @date 2022/08/11 20:49
 * @since
 **/
public class VegBurger extends Burger {
    @Override
    public String name() {
        return "Veg Burger";
    }

    @Override
    public float price() {
        return 25.0f;
    }
}
