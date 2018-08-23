package com.duobi.manager;

public class FatherClass {

    public void functionA(){
        System.out.println("father: A");
    }

    public void functionB(){
        functionA();
    }


}
