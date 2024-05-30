package com.example.demopattern.generics;

import java.util.List;

/**
 * @Author liull
 * @Date 2024/5/27 16:30
 * @Description: 固定子类泛型（有界限的泛型）
 *  泛型也可以定义在方法上
 **/
public class SubClassPrinter{
//public class SubClassPrinter<T extends Vehicle>{
//    T item;
//
//    public SubClassPrinter() {
//    }
//
//    public SubClassPrinter(T item)
//    {
//        this.item = item;
//    }

    public <T extends Vehicle> void print(T item)
    {
        System.out.println(item.getClass().getName());
    }

    /**
     * 可以使用?通配符，适配所有类型
     */
    public void printList(List<?> list){
        System.out.println(list);
    }

    /**
     *  上界限通配符 可以是Vehicle的本身或者子类
     */
    public void printVehicleList(List<? extends Vehicle> list){
        System.out.println(list);
    }

    /**
     *  下界限通配符 可以是Bus的本身或者父类
     */
    public void printBusList(List<? super Bus> list){
        System.out.println(list);
    }
}

