package com.demo.example.redis.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.example.redis.dto.LoginFormDTO;
import com.demo.example.redis.dto.Result;
import com.demo.example.redis.dto.UserDTO;
import com.demo.example.redis.entity.User;
import com.demo.example.redis.mapper.UserMapper;
import com.demo.example.redis.service.IUserService;
import com.demo.example.redis.utils.RegexUtils;
import com.demo.example.redis.utils.SystemConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result sendCode(String phone, HttpSession session) {
        //校验手机号
        if(RegexUtils.isPhoneInvalid(phone)){
            return Result.fail("手机号格式错误");
        }
        //生成校验码
        String code = RandomUtil.randomNumbers(6);
        //保存校验码到redis,有效期5分钟
        stringRedisTemplate.opsForValue().set("login:code:"+phone,code,5L, TimeUnit.MINUTES);
        //发送验证码
        log.info("发送验证码成功：{}",code);
        return Result.ok();
    }

    @Override
    public Result login(LoginFormDTO loginForm, HttpSession session) {
        //校验手机号
        if(RegexUtils.isPhoneInvalid(loginForm.getPhone())){
            return Result.fail("手机号格式错误");
        }
        //校验验证码
        String code = stringRedisTemplate.opsForValue().get("login:code:" + loginForm.getPhone());
        if(!StrUtil.equals(code,loginForm.getCode())){
            return Result.fail("验证码错误");
        }
        //根据手机号查询用户
        User user = query().eq("phone", loginForm.getPhone()).one();
        //用户不存在则新建用户
        if(user == null){
            user = createUserWithPhone(loginForm.getPhone());
        }
        //生成token，作为登录令牌
        String token = UUID.randomUUID().toString(true);
        //保存用户信息到redis，30分钟有效期
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        Map<String, Object> userMap = BeanUtil.beanToMap(userDTO,
                new HashMap<>(),
                CopyOptions.create()
                    .setIgnoreNullValue(true)
                    .setFieldValueEditor((fieldName,fieldValue)-> fieldValue.toString()));
        String tokenKey = "login:token:"+token;
        stringRedisTemplate.opsForHash().putAll(tokenKey,userMap);
        stringRedisTemplate.expire(tokenKey,30,TimeUnit.MINUTES);
        //返回token
        return Result.ok(token);
    }

    private User createUserWithPhone(String phone) {
        User user = new User();
        user.setPhone(phone);
        user.setNickName(SystemConstants.USER_NICK_NAME_PREFIX +RandomUtil.randomString(10));
        save(user);
        return user;
    }
}
