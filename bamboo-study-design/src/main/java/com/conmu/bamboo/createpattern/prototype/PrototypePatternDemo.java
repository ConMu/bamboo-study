package com.conmu.bamboo.createpattern.prototype;

import com.conmu.bamboo.createpattern.prototype.abs.Shape;

/**
 * 原型模式
 *
 * @author mucongcong
 * @date 2022/08/15 15:52
 * @since
 **/
public class PrototypePatternDemo {
    public static void main(String[] args) {
        ShapeCache.loadCache();

        Shape clonedShape = (Shape) ShapeCache.getShape("1");
        System.out.println("Shape : " + clonedShape.getType());

        Shape clonedShape2 = (Shape) ShapeCache.getShape("2");
        System.out.println("Shape : " + clonedShape2.getType());

        Shape clonedShape3 = (Shape) ShapeCache.getShape("3");
        System.out.println("Shape : " + clonedShape3.getType());
    }
}
