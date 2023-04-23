package com.fit;

import lombok.extern.slf4j.Slf4j;
import ninja.servlet.NinjaServletListener;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import javax.servlet.DispatcherType;
import java.net.InetAddress;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @AUTO Jetty启动web入口
 * @Author Cyril
 * @DATE 2023/4/23
 */
@Slf4j
public class Main {

    private final static String[] WELCOMES = new String[]{"/index.ftl.html", "/index.ftl", "/index.html"};
    private final static String RESOURCE_PATH = Main.class.getResource("").getPath();
    private final static String GUICE_FILTER = "com.google.inject.servlet.GuiceFilter";
    private final static String CONTEXT_PATH = "/";
    private final static String WEB_APP = "fit.jar";
    private final static String PORT_ENV = "PORT";
    private final static int DEFAULT_PORT = 8080;

    public static void main(String[] args) throws Exception {
        int port = getPort();
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            WebAppContext web = new WebAppContext(WEB_APP, CONTEXT_PATH);
            web.addLocaleEncoding("zh", "utf-8");
            web.addFilter(GUICE_FILTER, "/*", EnumSet.of(DispatcherType.REQUEST));
            web.setInitParameter("TemplatePath", RESOURCE_PATH + "/views");
            web.setParentLoaderPriority(true);
            web.addEventListener(new NinjaServletListener());
            // 设置Web内容上下文路径
            web.setClassLoader(Thread.currentThread().getContextClassLoader());
            web.setResourceBase(RESOURCE_PATH);
            web.setResourceAliases(getResources());
            web.setParentLoaderPriority(true);
            web.setWelcomeFiles(WELCOMES);
            web.setDisplayName("Fly");

            Server server = new Server(port);
            // WEB服务
            server.setHandler(web);
            if (!web.isAvailable()) {
                System.exit(1);
            }
            server.start();
            log.info("\n---------------------------------------------------------\n" +
                    "Application Fly-Jetty is running! Access URLs:\n\t" +
                    "Local: \t\thttp://localhost:" + port + "/\n\t" +
                    "External:\thttp://" + ip + ":" + port + "/" +
                    "\n-----------------页面请部署 ---- web ----------------------");
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1); // Without an explicit exit non-daemon threads will keep JVM alive.
        }
    }

    /**
     * 静态资源跳转
     */
    private static Map<String, String> getResources() {
        Map<String, String> map = new HashMap<>();
        map.put("assets", RESOURCE_PATH + "assets");
        map.put("views", RESOURCE_PATH + "views");
        return map;
    }

    /**
     * Returns the port specified by the environment variable PORT or DEFAULT_PORT if not present.
     */
    private static int getPort() {
        Optional<String> envPort = Optional.ofNullable(System.getenv(PORT_ENV));
        return envPort.map(Integer::parseInt).orElse(DEFAULT_PORT);
    }
}
