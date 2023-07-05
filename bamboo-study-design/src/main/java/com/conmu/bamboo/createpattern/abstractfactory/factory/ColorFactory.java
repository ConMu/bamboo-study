package com.conmu.bamboo.createpattern.abstractfactory.factory;

import com.conmu.bamboo.createpattern.abstractfactory.AbstractFactory;
import com.conmu.bamboo.createpattern.abstractfactory.impl.Blue;
import com.conmu.bamboo.createpattern.abstractfactory.impl.Green;
import com.conmu.bamboo.createpattern.abstractfactory.impl.Red;
import com.conmu.bamboo.createpattern.abstractfactory.inter.Color;
import com.conmu.bamboo.createpattern.abstractfactory.inter.Shape;

/**
 * @author mucongcong
 * @date 2022/08/05 17:30
 * @since
 **/
public class ColorFactory extends AbstractFactory {
    @Override
    public Shape getShape(String shapeType){
        return null;
    }

    @Override
    public Color getColor(String color) {
        if(color == null){
            return null;
        }
        if(color.equalsIgnoreCase("RED")){
            return new Red();
        } else if(color.equalsIgnoreCase("GREEN")){
            return new Green();
        } else if(color.equalsIgnoreCase("BLUE")){
            return new Blue();
        }
        return null;
    }
}
