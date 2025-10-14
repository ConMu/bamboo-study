package com.conmu.agent.javassist;

import com.conmu.agent.asm.Base;
import com.conmu.agent.instrument.TestTransformer;

import java.io.Serializable;
import java.lang.instrument.Instrumentation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 借助agent的能力讲instrument注入JVM中
 * Agent就是JVMTI的一种实现，Agent有两种启动方式，一是随Java进程启动而启动，经常见到的java -agentlib就是这种方式；
 * 二是运行时载入，通过attach API，将模块（jar包）动态地Attach到指定进程id的Java进程内。
 * 
 * @author mucongcong
 * @date 2025/02/14 17:56
 * @since
 **/
public class TestAgent {
    public static void agentmain(String args, Instrumentation inst) {
        //指定我们自己定义的Transformer，在其中利用Javassist做字节码替换
        inst.addTransformer(new TestTransformer(), true);
        try {
            //重定义类并载入新的字节码
            inst.retransformClasses(Base.class);
            System.out.println("Agent Load Done.");
        } catch (Exception e) {
            System.out.println("agent load failed!");
        }
    }

}