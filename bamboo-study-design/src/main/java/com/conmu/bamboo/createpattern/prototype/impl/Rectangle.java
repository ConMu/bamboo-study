package com.conmu.bamboo.createpattern.prototype.impl;

import com.conmu.bamboo.createpattern.prototype.abs.Shape;

/**
 * @author mucongcong
 * @date 2022/08/15 15:45
 * @since
 **/
public class Rectangle extends Shape {

    public Rectangle(){
        type = "Rectangle";
    }

    @Override
    public void draw() {
        System.out.println("Inside Rectangle::draw() method.");
    }
}
