package com.demo.example.redis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.example.redis.dto.Result;
import com.demo.example.redis.entity.Voucher;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
public interface IVoucherService extends IService<Voucher> {

    Result queryVoucherOfShop(Long shopId);

    void addSeckillVoucher(Voucher voucher);
}
