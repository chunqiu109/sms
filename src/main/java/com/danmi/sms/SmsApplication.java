package com.danmi.sms;

//import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
//import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.danmi.sms.mapper")
//@NacosPropertySource(dataId = "chun", autoRefreshed = true)
public class SmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmsApplication.class, args);
	}

}
