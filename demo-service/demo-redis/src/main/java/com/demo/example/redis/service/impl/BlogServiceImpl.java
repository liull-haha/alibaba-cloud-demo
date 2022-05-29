package com.demo.example.redis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.example.redis.entity.Blog;
import com.demo.example.redis.mapper.BlogMapper;
import com.demo.example.redis.service.IBlogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {

}
