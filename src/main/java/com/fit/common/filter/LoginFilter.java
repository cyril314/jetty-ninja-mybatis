package com.fit.common.filter;

import com.fit.common.base.BaseController;
import com.fit.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import ninja.*;
import ninja.session.Session;

/**
 * @AUTO 后台用户登录拦截器
 * @FILE LoginFilter.java
 * @DATE 2017-10-17 下午10:29:08
 * @Author AIM
 */
@Slf4j
public class LoginFilter extends BaseController implements Filter {

    private static final String NO_INTERCEPTOR_PATH = ".*/((login*)|(logout)|(main)|(assets)|(websocket)).*";

    @Override
    public Result filter(FilterChain chain, Context context) {
        log.debug("============== LoginFilter ==============");
        String path = context.getRequestPath();
        if (!path.matches(NO_INTERCEPTOR_PATH)) {
            Session session = context.getSession();
            String str = session.get("adminUser");
            log.debug(str + " ===== " + session.getData().toString());
            if (StringUtil.isBlank(str)) {
                return Results.redirect("/admin");
            }
        }
        return chain.next(context).render("webPath", getWebPath(context));
    }
}
