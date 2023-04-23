package com.fit.controllers;

import com.fit.common.filter.LoginFilter;
import com.fit.service.MailService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import ninja.FilterWith;
import ninja.Result;
import ninja.Results;

/**
 * @AUTO 发送邮件控制器
 * @FILE MailController.java
 * @DATE 2017-10-17 下午2:13:55
 * @Author AIM
 */
@Slf4j
@Singleton
public class MailController {

    @Inject
    MailService mailService;

    @FilterWith(LoginFilter.class)
    public Result sendMail() {
        log.debug("============== sendMail ==============");
        boolean sendMail = mailService.sendMail("ceshi massage", "imocence@qq.com", "imocence@foxmail.com", false, "this is test message");
        return Results.json().render(sendMail);
    }
}
