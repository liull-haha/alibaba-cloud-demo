package com.example.demopattern.proxy.static_proxy;

import com.example.demopattern.proxy.SellTickets;
import com.example.demopattern.proxy.TrianStation;

/**
 * @Author liull
 * @Date 2023/7/8 15:43
 **/
public class SellProxy implements SellTickets {

    private final TrianStation trianStation = new TrianStation();

    @Override
    public void sellTickets(Integer ticketsNum) {
        System.out.println("代理商买票收取服务费");
        trianStation.sellTickets(ticketsNum);
    }

    @Override
    public Boolean pickUpTicket(Integer ticketsNum) {
        System.out.println("代理商取票不收取服务费");
        trianStation.pickUpTicket(ticketsNum);
        return true;
    }
}
