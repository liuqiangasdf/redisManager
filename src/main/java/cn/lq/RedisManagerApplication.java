package cn.lq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import cn.lq.config.ApplicationClose;
import cn.lq.config.ApplicationStartup;
import cn.lq.config.ApplicationStop;

@SpringBootApplication
@EnableTransactionManagement
public class RedisManagerApplication implements CommandLineRunner {
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisManagerApplication.class);

	@Value("${spring.profiles.active:DEV}")
	private String profiles;

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(RedisManagerApplication.class);
		springApplication.addListeners(new ApplicationStartup());
		springApplication.addListeners(new ApplicationStop());
		springApplication.addListeners(new ApplicationClose());
		springApplication.run(args);
		
	}

	@Override
	public void run(String... args) throws Exception {
		LOGGER.info("Application [ {} ] finished start...", profiles);
	}

}
