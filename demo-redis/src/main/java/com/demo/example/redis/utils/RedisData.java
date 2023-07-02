package com.demo.example.redis.utils;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RedisData<T> {
    private LocalDateTime expireTime;
    private T data;
}
