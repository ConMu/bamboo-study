package com.conmu.agent;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.NamingStrategy;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;

/**
 * @author mucongcong
 * @date 2024/11/22 16:55
 * @since
 **/
public class ToStringAgent {

    public static void main(String[] args) {
        // 1. 创建子类。创建一个继承至 Object 类型的类,默认配置命名
        DynamicType.Unloaded<?> make = new ByteBuddy()
                .subclass(Object.class)
                .name("example.Type")
                .make();

        // 2. 创建子类，命名规则自定义。匿名类被简单实现为连接 i.love.ByteBuddy 和基类的简要名称。当扩展 Object 类型时，动态类将被命名为 i.love.ByteBuddy.Object
        DynamicType.Unloaded<?> make1 = new ByteBuddy().with(new NamingStrategy.AbstractBase() {
                    @Override
                    protected String name(TypeDescription superClass) {
                        return "i.love.ByteBuddy." + superClass.getSimpleName();
                    }
                })
                .subclass(Object.class)
                .make();



    }

    class Foo{
        String bar(){return "bar";}
    }
}