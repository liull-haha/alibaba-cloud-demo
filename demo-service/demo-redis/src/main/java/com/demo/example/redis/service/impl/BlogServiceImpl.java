package com.demo.example.redis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.example.redis.dto.Result;
import com.demo.example.redis.entity.Blog;
import com.demo.example.redis.entity.User;
import com.demo.example.redis.mapper.BlogMapper;
import com.demo.example.redis.service.IBlogService;
import com.demo.example.redis.service.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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

    @Resource
    private IUserService userService;

    @Override
    public Result queryBolg(Long id) {
        Blog blog = this.getById(id);
        Long userId = blog.getUserId();
        User user = userService.getById(userId);
        blog.setName(user.getNickName());
        blog.setIcon(user.getIcon());
        return Result.ok(blog);
    }
}
