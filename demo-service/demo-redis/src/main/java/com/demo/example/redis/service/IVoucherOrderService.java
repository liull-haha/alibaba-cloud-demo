package com.demo.example.redis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.example.redis.dto.Result;
import com.demo.example.redis.entity.VoucherOrder;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
public interface IVoucherOrderService extends IService<VoucherOrder> {

    Result seckillVoucher(Long voucherId);

    Result seckillVoucherBySync(Long voucherId);

    Result createVoucherOrder(Long voucherId);
}
