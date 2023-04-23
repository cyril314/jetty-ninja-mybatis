package com.fit.controllers;

import com.fit.common.base.BaseController;
import com.fit.common.base.Pager;
import com.fit.common.base.Query;
import com.fit.common.filter.LoginFilter;
import com.fit.common.utils.BeanUtil;
import com.fit.common.utils.ConverterUtils;
import com.fit.common.utils.StringUtil;
import com.fit.entity.GoodsEntity;
import com.fit.service.GoodsService;
import com.fit.utils.SystemUtil;
import com.fit.utils.WebUtil;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import ninja.Context;
import ninja.FilterWith;
import ninja.Result;
import ninja.Results;
import ninja.params.Param;
import ninja.session.Session;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @AUTO 后台管理
 * @FILE AdminController.java
 * @DATE 2017-10-1 下午5:56:00
 * @Author AIM
 */
@Slf4j
@Singleton
public class AdminController extends BaseController {

    @Inject
    GoodsService goodsService;

    /**
     * 登录
     */
    public Result login(Context context, @Param("rememberMe") Boolean rememberMe, Session session) {
        log.debug("============== index ============== " + session.getData());
        // if (rememberMe != null && rememberMe) {
        // // Set the expiry time 30 days (in milliseconds) in the future
        // session.setExpiryTime(30 * 24 * 60 * 60 * 1000L);
        // } else {
        // // Set the expiry time 1 hour in the future
        // session.setExpiryTime(60 * 1000L);
        // }
        return Results.html().template("views/AdminController/login.ftl.html");
    }

    /**
     * 退出
     */
    public Result logout(Context context) {
        log.debug("============== logout ==============");
        Session session = context.getSession();
        String string = session.get("adminUser");
        log.debug(string);
        session.remove("adminUser");
        return Results.redirect("/admin");
    }

    /**
     * 后台入口
     */
    @FilterWith(LoginFilter.class)
    public Result loginVerify(Context context) {
        log.debug("============== mian ==============");
        Map<String, String> map = WebUtil.getPraramsAsMap(context.getParameters());
        if (map != null) {
            log.debug(map.toString());
            String username = map.get("UserName");
            String password = map.get("password");
            if (username.equals("admin") && password.equals("admin")) {
                Session session = context.getSession();
                session.put("adminUser", username);
                session.save(context);
                return Results.html().template("views/AdminController/main.ftl.html");
            }
        }
        return Results.redirect("/admin/logout");
    }

    /**
     * 后台首页
     */
    @FilterWith(LoginFilter.class)
    public Result index(Context context) {
        log.debug("============== admin ==============");
        return Results.html().template("views/AdminController/index.ftl.html")
                .render("os", SystemUtil.getOsInfo()).render("port", getServerPort(context));
    }

    /**
     * 商品管理页面
     */
    @FilterWith(LoginFilter.class)
    public Result goodsManage(Context context, @Param("name") String goodsName, @Param("currentPage") Integer currentPage) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtil.isNotBlank(goodsName)) {
            map.put("goodsName", goodsName);
        }
        Pager<GoodsEntity> pager = new Pager<GoodsEntity>();
        if (WebUtil.isNotEmpty(currentPage)) {
            pager.setPage(currentPage);
        }
        map.put("page", pager.getPage());
        map.put("limit", pager.getRows());
        Query query = new Query(map);
        List<GoodsEntity> list = goodsService.findList(query);
        pager.setList(list);
        pager.setRecords(goodsService.findCount());
        map.put("pager", pager);
        map.put("webPath", getWebPath(context));
        return Results.html().template("views/AdminController/goodsManage.ftl.html").render("map", map);
    }

    /**
     * 根据ID查询信息
     */
    @FilterWith(LoginFilter.class)
    public Result editGoods(@Param("id") Integer id, Context context) {
        GoodsEntity goodsEntity = new GoodsEntity();
        if (id != null) {
            goodsEntity = goodsService.get(id);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("webPath", getWebPath(context));
        map.put("entity", goodsEntity);
        return Results.html().render("map", map);
    }

    /**
     * 保存
     */
    @FilterWith(LoginFilter.class)
    public Result saveGoods(Context context) {
        Map<String, String[]> parameters = getParameters(context);
        GoodsEntity entity = ConverterUtils.converter2Map(parameters, GoodsEntity.class);
        if (WebUtil.isEmpty(entity.getId())) {
            goodsService.save(entity);
        } else {
            GoodsEntity goodsEntity = goodsService.get(ConverterUtils.toInt(entity.getId()));
            try {
                BeanUtil.copyProperties(goodsEntity, entity);
                goodsService.update(goodsEntity);
            } catch (InvocationTargetException e) {
                log.error("信息保存失败", e);
            }
        }

        return Results.redirect("/goodsManage");
    }

    /**
     * 删除商品
     */
    @FilterWith(LoginFilter.class)
    public Result delGoods(@Param("id") Integer id, Context context) {
        if (id != null) {
            int delete = goodsService.delete(id);
            log.debug(id + " ====== 删除状态 ====== " + delete);
        }
        return Results.redirect("/goodsManage");
    }
}
