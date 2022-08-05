package com.green.di.aop;

import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AopMain {
    public static void main(String[] args) throws Exception {
        MyAdvice myAdvice = new MyAdvice();

        Class myClass = Class.forName("com.green.di.aop.MyClass");

        Object obj = myClass.newInstance();
        for(Method m : myClass.getDeclaredMethods()){
            myAdvice.invoke(m, obj, null);
        }
    }
}

class MyAdvice {
    Pattern p = Pattern.compile("a.*");

    boolean matches(Method m){
        Matcher matcher = p.matcher(m.getName());
        return matcher.matches();
    }

    void invoke(Method m, Object obj, Object... args) throws Exception{
        if(m.getAnnotation(Transactional.class) != null)
            System.out.println("[before]-----");
        m.invoke(obj, args);
        if(m.getAnnotation(Transactional.class) != null)
            System.out.println("-----[after]");
    }
}

class MyClass{
    @Transactional
    void aaa1(){
        System.out.println("aaa1 호출");
    }

    void aaa2(){
        System.out.println("aaa2 호출");
    }

    void bbb(){
        System.out.println("bbb 호출");
    }
}
