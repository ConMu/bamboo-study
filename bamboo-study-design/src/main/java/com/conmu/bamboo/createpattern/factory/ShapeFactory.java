package com.conmu.bamboo.createpattern.factory;

import com.conmu.bamboo.createpattern.abstractfactory.impl.Circle;
import com.conmu.bamboo.createpattern.abstractfactory.impl.Rectangle;
import com.conmu.bamboo.createpattern.abstractfactory.impl.Square;
import com.conmu.bamboo.createpattern.abstractfactory.inter.Shape;

/**
 * @author mucongcong
 * @date 2022/08/05 16:59
 * @since
 **/
public class ShapeFactory {

    //使用 getShape 方法获取形状类型的对象
    public Shape getShape(String shapeType){
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
