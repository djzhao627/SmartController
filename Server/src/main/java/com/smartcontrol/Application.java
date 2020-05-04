package com.smartcontrol;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import tk.mybatis.spring.annotation.MapperScan;

import java.net.InetAddress;

@Slf4j
@SpringBootApplication
@MapperScan("com.smartcontrol.dao")
public class Application implements ApplicationListener<WebServerInitializedEvent> {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @SneakyThrows
    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        WebServer server = event.getWebServer();
        WebServerApplicationContext context = event.getApplicationContext();
        Environment env = context.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        int port = server.getPort();
        String contextPath = env.getProperty("server.servlet.context-path");
        if (contextPath == null) {
            contextPath = "";
        }
        log.info("\n---------------------------------------------------------\n" +
                "\tApplication is running! Access address:\n" +
                "\tLocal:\t\thttp://localhost:{}" +
                "\n\tExternal:\thttp://{}:{}{}" +
                "\n---------------------------------------------------------\n", port, ip, port, contextPath);
    }

}
