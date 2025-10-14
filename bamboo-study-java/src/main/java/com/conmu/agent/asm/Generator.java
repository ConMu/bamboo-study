package com.conmu.agent.asm;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @author mucongcong
 * @date 2025/02/14 17:15
 * @since
 **/
public class Generator {
    public static void main(String[] args) throws Exception {
        //读取
        ClassReader classReader = new ClassReader("com/conmu/agent/asm/Base");
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        //处理
        ClassVisitor classVisitor = new MyClassVisitor(classWriter);
        classReader.accept(classVisitor, ClassReader.SKIP_DEBUG);
        byte[] data = classWriter.toByteArray();
        //输出
        File f = new File("/Users/mucongcong/Projects/gitprojects/bamboo-study/bamboo-study-java/target/classes/com/conmu/agent/asm/Base.class");
        FileOutputStream fout = new FileOutputStream(f);
        fout.write(data);
        fout.close();
        System.out.println("now generator cc success!!!!!");
    }
}