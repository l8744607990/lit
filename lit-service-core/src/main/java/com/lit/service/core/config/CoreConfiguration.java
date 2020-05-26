package com.lit.service.core.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfinal.template.ext.spring.JFinalViewResolver;
import com.jfinal.template.source.ClassPathSourceFactory;
import com.lit.service.core.model.Route;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.util.Map;
import java.util.Optional;

/**
 * @author liulu
 * @version v1.0
 * date 2019-05-13
 */
@Configuration
@Import({ApiExceptionHandler.class, ApiResponseBodyAdvice.class})
@EnableConfigurationProperties(CoreProperties.class)
public class CoreConfiguration {

    @Resource
    private CoreProperties coreProperties;
    @Resource
    private ObjectMapper objectMapper;

    @Bean
    public Route index() {
        return new Route("home", "/", "/views/home.js");
    }

    @Bean
    public JFinalViewResolver getJFinalViewResolver() {
        JFinalViewResolver jfr = new JFinalViewResolver();
        // setDevMode 配置放在最前面
        jfr.setDevMode(true);

        // 使用 ClassPathSourceFactory 从 class path 与 jar 包中加载模板文件
        jfr.setSourceFactory(new ClassPathSourceFactory());

        // 在使用 ClassPathSourceFactory 时要使用 setBaseTemplatePath
        // 代替 jfr.setPrefix("/view/")
        JFinalViewResolver.engine.setBaseTemplatePath("/templates");

        jfr.setSuffix(".html");
        jfr.setSessionInView(true);
        return jfr;
    }

    @Bean
    public WebMvcConfigurer litAdminWebMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController(coreProperties.getIndexPath()).setViewName("index");
            }
        };
    }

    @EventListener
    public void appStartListener(ContextRefreshedEvent contextRefreshedEvent) throws JsonProcessingException {

        ApplicationContext context = contextRefreshedEvent.getApplicationContext();
        JFinalViewResolver viewResolver = context.getBean(JFinalViewResolver.class);
        viewResolver.addSharedObject("singlePage", coreProperties.getSinglePage());
        viewResolver.addSharedObject("theme", coreProperties.getTheme());
        if (coreProperties.getSinglePage()) {
            // 单页应用路由配置
            Map<String, Route> routeMap = context.getBeansOfType(Route.class);
            viewResolver. addSharedObject("routes", objectMapper.writeValueAsString(routeMap.values()));
        }
        if (context instanceof WebApplicationContext) {
            ServletContext servletContext = ((WebApplicationContext) context).getServletContext();
            String contextPath = Optional.ofNullable(servletContext)
                    .map(ServletContext::getContextPath).orElse("");
            viewResolver.addSharedObject("contextPath", contextPath);
        }
    }

}