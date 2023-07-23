package com.example.demopattern.proxy.dynamic_cglib_proxy;

/**
 * @Author liull
 * @Date 2023/7/8 23:31
 **/
public class Client {
    public static void main(String[] args) {
        ProxyFactory proxyFactory = new ProxyFactory();
        TrianStation proxyObject = proxyFactory.getProxyObject();
        proxyObject.sellTickets(2);
        proxyObject.pickUpTicket(1);
    }
}
