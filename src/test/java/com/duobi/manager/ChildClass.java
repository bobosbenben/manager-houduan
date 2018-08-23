package com.duobi.manager;

public class ChildClass extends FatherClass {

    @Override
    public void functionA(){
        System.out.println("child: A");
    }

    @Override
    public void functionB(){
        functionA();
    }

}
