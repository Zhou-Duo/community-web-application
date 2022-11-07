package com.nowcoder.community;

import com.nowcoder.community.dao.AlphaDao;
import com.nowcoder.community.service.AlphaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
// 声明配置类
@ContextConfiguration(classes = CommunityApplication.class)
// 类想要得到配置容器，则需要 implements ApplicationContextAware （实现该接口需要完成 setApplicationContext 方法）
class CommunityApplicationTests implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	// 自动传入配置容器
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	// 主动获取 Bean
	@Test
	public void testApplicationContext() {
		System.out.println(applicationContext);

		// 获取 Bean， 面向接口获取 Bean， 与实现类无关，因而降低了耦合度
		AlphaDao alphaDao = applicationContext.getBean(AlphaDao.class);
		System.out.println(alphaDao.select());

		// 通过 Bean 名字获取
		alphaDao = applicationContext.getBean("alphaHibernate", AlphaDao.class);
		System.out.println(alphaDao.select());
	}

	@Test
	public void testBeanManagement() {
		// 默认单例（被 Spring 容器管理的 Bean 默认是单例的，即只被实例化一次，也只被销毁一次
		// 若不希望单例，即每次从 Spring 容器获取都返回一个新的实例，则需在对应类前声明 @Scope("Prototype")
		AlphaService alphaService = applicationContext.getBean(AlphaService.class);
		alphaService = applicationContext.getBean(AlphaService.class);
	}

	// 通过配置类装配 java 中已有的类
	@Test
	public void testBeanConfig() {
		SimpleDateFormat simpleDateFormat = applicationContext.getBean(SimpleDateFormat.class);
		System.out.println(simpleDateFormat.format(new Date()));
	}

	// 依赖注入 IoC： 自动获取 Spring 容器中的该类实例
	@Autowired
	// 指定获取的类
	@Qualifier("alphaHibernate")
	private AlphaDao alphaDao;

	@Autowired
	private AlphaService alphaService;

	@Autowired SimpleDateFormat simpleDateFormat;

	@Test
	public void testDependencyInsert() {
		System.out.println(alphaDao);
	}
}
