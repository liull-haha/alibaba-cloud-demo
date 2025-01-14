-- 
-- Generated by Luanalysis
-- User: liull
-- Date: 2022/7/16    
--
-- 参数列表：优惠券ID，用户ID
local voucherId = ARGV[1]
local userId = ARGV[2]

-- 数据key：库存key，订单key
local stockKey = 'seckill:stock:' .. voucherId
local orderKey = 'seckill:order:' .. voucherId

-- 脚本业务
-- 1.判断库存是否充足
if(tonumber(redis.call('get',stockKey)) <= 0) then
    return 1
end
-- 2.判断用户是否下单
if (redis.call('sismember',orderKey,userId) == 1) then
    return 2
end

-- 校验通过，开始扣减库存，并记录用户下单
redis.call('incrby',stockKey,-1);
redis.call('sadd',orderKey,userId);
return 0



