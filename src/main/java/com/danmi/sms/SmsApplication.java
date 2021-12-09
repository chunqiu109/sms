package com.danmi.sms;

//import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
//import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.danmi.sms.mapper")
@EnableScheduling
//@NacosPropertySource(dataId = "chun", autoRefreshed = true)
@Slf4j
public class SmsApplication {

	public static void main(String[] args) {
		String osName = System.getProperty("os.name");
		log.info("OS >>> {}", osName);
		ConfigurableApplicationContext context = SpringApplication.run(SmsApplication.class, args);
//		for (String name : context.getBeanDefinitionNames()) {
//			System.out.println(name);
//		}
	}

}
