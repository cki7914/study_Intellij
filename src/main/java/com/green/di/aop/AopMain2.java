package com.green.di.aop;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.stereotype.Component;

public class AopMain2 {
    public static void main(String[] args) {
        ApplicationContext ac = new GenericXmlApplicationContext("file:src/main/webapp/WEB-INF/spring/**/root-context_aop.xml");

        MyMath mm = (MyMath) ac.getBean("myMath");

//        System.out.println("mm.add(3, 4) = " + mm.add(3, 4));
//        System.out.println("mm.multiple(3, 4) = " + mm.multiple(3, 4));

        mm.add(3, 4);
        mm.add(3, 4, 5);
        mm.multiple(3, 4);
    }
}
