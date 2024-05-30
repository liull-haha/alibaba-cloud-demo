package com.example.demopattern.generics;

/**
 * @Author liull
 * @Date 2024/5/27 16:18
 * @Description: 可以指定一个或者多个泛型
 **/
public class Printer<T, K> {

    T value;

    K content;

    public Printer() {
    }

    public Printer(T value) {
        this.value = value;
    }

    public Printer(T value, K content) {
        this.value = value;
        this.content = content;
    }

    public void print()
    {
        if (value != null)
        System.out.println(value);

        if (content != null)
        System.out.println(content);
    }
}
