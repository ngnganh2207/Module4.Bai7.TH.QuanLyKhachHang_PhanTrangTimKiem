package com.codegym.config;


import com.codegym.formatter.ProvinceFomatter;
import com.codegym.service.impl.CustomerService;
import com.codegym.service.impl.ICustomerService;
import com.codegym.service.impl.IProvinceService;
import com.codegym.service.impl.ProvinceService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.format.FormatterRegistry;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@Configuration
@EnableWebMvc
//C??c h??nh ?????ng nh?? th??m s???a x??a info v??o Databse g???i l?? 1 transaction, mu???n d??ng transaction th?? th??m
//Anotation n??y: @EnableTransactionManagement
@EnableTransactionManagement
//Nh?? n??y s??? k ph???i ti??m t???ng repo n???a
@EnableJpaRepositories("com.codegym.repository")
@ComponentScan("com.codegym.controller")
//Th???ng n??y ????? tham chi???u ?????n file "resources/upload_file.properties"
@PropertySource("classpath:upload_file.properties")
//C??i n??y h??? tr??? fomater
//????? vi???c h??? tr??? mapping t??? ?????ng n??y x???y ra, c???p nh???t l???p AppConfig s??? d???ng @EnableSpringDataWebSupport.
@EnableSpringDataWebSupport
public class AppConfig implements WebMvcConfigurer, ApplicationContextAware {
    //l???y gi?? tr???
    @Value("${file-upload}")
    private String fileUpload;

    private ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext= applicationContext;
    }
//   Ph???i th??m th?? vi???n thymleaf m???i ?????c dc
//        doi tuong
//            thuoc tinh 1 = gia tri 1
//            thuoc tinh 2 = gia tri 2
//    ????y th?????ng ch??? d??ng cho JSP
//    @Bean
//    public InternalResourceViewResolver internalResourceViewResolver(){
//        InternalResourceViewResolver viewResolver= new InternalResourceViewResolver();
//        viewResolver.setPrefix("/WEB-INF/views/");
//        viewResolver.setSuffix(".html");
//        return viewResolver;
//    }

    @Bean
    public SpringResourceTemplateResolver templateResolver(){
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        //(??o???n c???a a Ch??nh): th??m ti???ng vi???t
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }
    @Bean
    public SpringTemplateEngine templateEngine(){
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        return templateEngine;
    }

    @Bean
    public ThymeleafViewResolver viewResolver(){
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        //(??o???n c???a A Ch??nh) 2 d??ng d?????i li??n quan ?????n ti???ng vi???t
        viewResolver.setContentType("UTF-8");
        viewResolver.setCharacterEncoding("UTF-8");
        return viewResolver;
    }

    //C???u h??nh upload file
    //C???u h??nh ra ???????c n??i ????? l??u tr??? file, c??i c???u h??nh uploadfile
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/image/**")
                .addResourceLocations("file:" + fileUpload);

    }
    //B??? name=multipartResolver v???n dc
    @Bean(name = "multipartResolver")
    //CommonsMultipartResolver-> t???c l?? ch????ng tr??nh s??? h??? tr??? upload file
    public CommonsMultipartResolver getResolver() throws IOException {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setMaxUploadSizePerFile(52428800);//set max file
        return resolver;
    }
    //H???t c???u h??nh upload file

    //C???u h??nh CSDL- ORM
    //Ch??? ra cho th???ng hibernate(tri???n khai orm) r???ng t??i d??ng MySQL5, c??c fields cho hibernate
    Properties additionalProperties(){
        Properties properties= new Properties();
        //Update th?? ngh??a l?? th??m d??? li???u ??? model -> s??? th??m v??o CSDL
        //Create th?? s??? t???o m???i ho??n to??n, x??c nh???ng c??i c?? ??? CSDL m???i l???n run l???i tomcat
        properties.setProperty("hibernate.hbm2ddl.auto","update");
        properties.setProperty("hibernate.dialect","org.hibernate.dialect.MySQL5Dialect");
        //H???i l???i ??o???n n??y l?? g?? m?? minh l???i note(//) l???i
        properties.setProperty("show_sql", "true");
        return properties;
    }

    //C???p quy???n cho app g???i ???????c database n??y
    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource dataSource= new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/customer_27_9_21");
        dataSource.setUsername("root");
        dataSource.setPassword("dtk1051030073");
        return dataSource;
    }

    //C???u h??nh c??c Entity ????? qu???n l?? c??c Entity
    @Bean
    @Qualifier(value = "entityManager")
    public EntityManager entityManager(EntityManagerFactory entityManagerFactory){
        return entityManagerFactory.createEntityManager();
    }

    //N??i cho bi???t l?? d??? ??n tham chi???u ????n object model n??o
    //C??i n??y d??ng cho hibernate
    //Qu???n l?? c??c entity trong package com.codegym.model
//    @Bean
//    public LocalSessionFactoryBean sessionFactoryBean(){
//        LocalSessionFactoryBean sessionFactoryBean=new LocalSessionFactoryBean();
//        //C??i LocalSessionFactoryBean n??y qu???n l?? ?????i t?????ng trong database, c???n c???p datasoure cho n??
//        sessionFactoryBean.setDataSource(dataSource());
//        sessionFactoryBean.setPackagesToScan("com.codegym.model");
//        //c???u h??nh hibernate
//        sessionFactoryBean.setHibernateProperties(additionalProperties());
//        return sessionFactoryBean;
//    }


    //H???t c???u h??nh CSDL b??i 5

    //C???u h??nh cho JPA b??i 6
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(){
        LocalContainerEntityManagerFactoryBean em= new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("com.codegym.model");
        JpaVendorAdapter jpaVendorAdapter= new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(jpaVendorAdapter);
        em.setJpaProperties(additionalProperties());
        return em;
    }
    //H???i l???i c??i n??y d??ng l??m g??, c??c b??i trc l???i k c???-> Hibernate n?? h??? tr??? transaction r???i,
    // c??n JPA th?? n?? ko ?????y ?????, ph???i c???u h??nh th??m transaction
    //Qu???n l?? c??c transaction, th??m platform ????? h??? tr??? transaction
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf){
        JpaTransactionManager transactionManager=new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    //H???t b??i 6

    //Formatter
//    @Override
//    public void addFormatters(FormatterRegistry registry) {
//        registry.addFormatter(new ClasseFormatter(applicationContext.getBean(IClassesService.class)));
//    }
    @Bean
    public ICustomerService customerService(){
        return new CustomerService();
    }
    @Bean
    public IProvinceService provinceService(){
        return new ProvinceService();
    }
    //Fomatter
    @Override
    public void addFormatters(FormatterRegistry registry){
        registry.addFormatter(new ProvinceFomatter(applicationContext.getBean(IProvinceService.class)));
    }



}
