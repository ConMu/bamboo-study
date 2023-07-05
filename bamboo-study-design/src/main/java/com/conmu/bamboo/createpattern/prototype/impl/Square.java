package com.conmu.bamboo.createpattern.prototype.impl;

import com.conmu.bamboo.createpattern.prototype.abs.Shape;

/**
 * @author mucongcong
 * @date 2022/08/15 15:46
 * @since
 **/
public class Square extends Shape {

    public Square(){
        type = "Square";
    }
    @Override
    public void draw() {
        System.out.println("Inside Square::draw() method.");
    }
}
