package com.example.demopattern.proxy.static_proxy;

/**
 * @Author liull
 * @Date 2023/7/8 15:46
 * 静态代理
 **/
public class Client {
    public static void main(String[] args) {
        //使用代理对象进行访问目标对象
        SellProxy sellProxy = new SellProxy();
        sellProxy.sellTickets(2);
        sellProxy.pickUpTicket(1);
    }
}
