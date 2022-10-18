package com.demo.example.redis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.example.redis.dto.Result;
import com.demo.example.redis.entity.Shop;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
public interface IShopService extends IService<Shop> {

    Result queryId(Long id);

    Result updateShop(Shop shop);
}
