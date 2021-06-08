package com.demo.example.provider.controller;

import com.demo.example.provider.dao.SysRoleMapper;
import com.demo.example.provider.model.SysRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liull
 * @description
 * @date 2021/3/20
 */
@RestController
@RequestMapping("/provider")
public class EchoController {

    @Value("${server.port}")
    private String serverPort;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @RequestMapping(value = "/echo/{string}", method = RequestMethod.GET)
    public String echo(@PathVariable String string){
        return string+"："+serverPort;
    }

    @RequestMapping(value = "/queryRole", method = RequestMethod.POST)
    public SysRole queryRole(){
        return sysRoleMapper.selectByPrimaryKey(Long.valueOf("1"));
    }

}
