package com.example.demopattern.proxy.dynamic_cglib_proxy;


/**
 * @Author liull
 * @Date 2023/7/8 15:42
 **/
public class TrianStation{

    public void sellTickets(Integer ticketsNum) {
        System.out.println("火车站卖了"+ticketsNum+"张票");
    }

    public Boolean pickUpTicket(Integer ticketsNum) {
        System.out.println("成功取了"+ticketsNum+"张票");
        return true;
    }
}
