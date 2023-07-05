package com.conmu.bamboo.createpattern.prototype.impl;

import com.conmu.bamboo.createpattern.prototype.abs.Shape;

/**
 * @author mucongcong
 * @date 2022/08/15 15:46
 * @since
 **/
public class Circle extends Shape {

    public Circle() {
        type = "Circle";
    }

    @Override
    public void draw() {
        System.out.println("Inside Circle::draw() method.");
    }
}
