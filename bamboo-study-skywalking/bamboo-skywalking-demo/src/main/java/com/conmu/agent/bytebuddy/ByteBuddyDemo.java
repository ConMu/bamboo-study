package com.conmu.agent.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.NamingStrategy;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;

import static net.bytebuddy.matcher.ElementMatchers.is;

/**
 * demo of ByteBuddy
 * https://notes.diguage.com/byte-buddy-tutorial/creating-a-class.html
 * 1. subclass 生成一个子类
 * 2. redefine 类型重定义，会覆盖原有类方法
 * 3. rebase 重定义基底，保存基底类所有方法实现
 * @author mucongcong
 * @date 2024/11/22 16:55
 * @since
 **/
public class ByteBuddyDemo {

    public static void main(String[] args) {
        // 1. 创建子类。创建一个继承至 Object 类型的类,默认配置命名
        DynamicType.Unloaded<?> make = new ByteBuddy()
                .subclass(Object.class)
                .name("example.Type")
                .make();
        Class<?> loaded = make.load(Object.class.getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER).getLoaded();

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

}