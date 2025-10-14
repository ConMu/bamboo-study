package com.conmu.agent.javassist;

import com.conmu.agent.asm.Base;
import javassist.*;

import java.io.IOException;

/**
 * @author mucongcong
 * @date 2025/02/14 17:42
 * @since
 **/
public class JavassistTest {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException, IOException, NotFoundException, CannotCompileException {
        ClassPool cp = ClassPool.getDefault();
        CtClass cc = cp.get("com.conmu.agent.asm.Base");
        CtMethod m = cc.getDeclaredMethod("process");
        m.insertBefore("{ System.out.println(\"start1\"); }");
        m.insertAfter("{ System.out.println(\"end1\"); }");
        Class c = cc.toClass();
        cc.writeFile("/Users/mucongcong/Projects/gitprojects/bamboo-study/bamboo-study-java/target/classes/com/conmu/agent/asm");
        Base h = (Base)c.newInstance();
        h.process();
    }
}