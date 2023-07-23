package com.example.demopattern.proxy;

/**
 * @Author liull
 * @Date 2023/7/8 15:41
 **/
public interface SellTickets {

    void sellTickets(Integer ticketsNum);

    Boolean pickUpTicket(Integer ticketsNum);
}
