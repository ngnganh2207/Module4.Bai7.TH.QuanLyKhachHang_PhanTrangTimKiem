package com.codegym.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class AppInit extends AbstractAnnotationConfigDispatcherServletInitializer {
    //Chỉ đến lớp cấu hình là AppConfig.class
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{AppConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{};
    }

    //Chỉ định đường dẫn mặc định là "/"
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
