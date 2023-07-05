package com.conmu.bamboo.createpattern.abstractfactory.factory;

import com.conmu.bamboo.createpattern.abstractfactory.AbstractFactory;
import com.conmu.bamboo.createpattern.abstractfactory.impl.Circle;
import com.conmu.bamboo.createpattern.abstractfactory.impl.Rectangle;
import com.conmu.bamboo.createpattern.abstractfactory.impl.Square;
import com.conmu.bamboo.createpattern.abstractfactory.inter.Color;
import com.conmu.bamboo.createpattern.abstractfactory.inter.Shape;

/**
 * @author mucongcong
 * @date 2022/08/05 17:28
 * @since
 **/
public class ShapeFactory extends AbstractFactory {
    @Override
    public Color getColor(String color) {
        return null;
    }

    @Override
    public Shape getShape(String shapeType) {
        if(shapeType == null){
            return null;
        }
        if(shapeType.equalsIgnoreCase("CIRCLE")){
            return new Circle();
        } else if(shapeType.equalsIgnoreCase("RECTANGLE")){
            return new Rectangle();
        } else if(shapeType.equalsIgnoreCase("SQUARE")){
            return new Square();
        }
        return null;
    }
}
