package com.conmu.bamboo.createpattern.abstractfactory;

import com.conmu.bamboo.createpattern.abstractfactory.inter.Color;
import com.conmu.bamboo.createpattern.abstractfactory.inter.Shape;

/**
 * @author mucongcong
 * @date 2022/08/05 17:27
 * @since
 **/
public abstract class AbstractFactory {
    public abstract Color getColor(String color);
    public abstract Shape getShape(String shape);
}
