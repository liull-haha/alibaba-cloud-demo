package com.example.demopattern.builder;

import lombok.Builder;
import lombok.Data;

/**
 * @Author liull
 * @Date 2023/7/5 22:59
 **/
@Data
@Builder
public class Bike {

    private String seat;

    private String frame;

}
