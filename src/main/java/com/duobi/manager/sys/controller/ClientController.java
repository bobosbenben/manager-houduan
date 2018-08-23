package com.duobi.manager.sys.controller;

import com.duobi.manager.sys.entity.User;
import com.duobi.manager.sys.service.UserService;
import com.duobi.manager.sys.utils.UserSexEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ClientController {

    @Autowired
    private UserService userService;

//    @RequestMapping(value = "/user",produces = "text/plain;charset=UTF-8")
//    String index(){
//        User user = new User("石伊波","123456", UserSexEnum.MAN);
//        User user1 = new User(); user1.setUserName("初始");
//
//        try {
//            userService.insert(user);
//            user1 = userService.getOne(1L);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//        return user1.getUserSex().toString();
//
//    }



}
