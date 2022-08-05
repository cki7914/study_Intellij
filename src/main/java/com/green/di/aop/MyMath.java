package com.green.di.aop;

import org.springframework.stereotype.Component;

@Component
public class MyMath {
    public int add(int a, int b){
        return a + b;
    }

    public int add(int a, int b, int c){
        return a + b + c;
    }

    public int subtract(int a, int b){
        return a - b;
    }

    public int multiple(int a, int b){
        return a * b;
    }
}
