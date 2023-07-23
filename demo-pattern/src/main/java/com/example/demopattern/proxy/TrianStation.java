package com.example.demopattern.proxy;

/**
 * @Author liull
 * @Date 2023/7/8 15:42
 **/
public class TrianStation implements SellTickets{
    @Override
    public void sellTickets(Integer ticketsNum) {
        System.out.println("火车站卖了"+ticketsNum+"张票");
    }

    @Override
    public Boolean pickUpTicket(Integer ticketsNum) {
        System.out.println("成功取了"+ticketsNum+"张票");
        return true;
    }
}
