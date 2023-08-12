package ir.mohaymen.iris;

import com.redis.om.spring.annotations.EnableRedisDocumentRepositories;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRedisDocumentRepositories(basePackages = "ir.mohaymen.iris.*")//,keyspaceConfiguration = MyKeyspaceConfiguration.class)
public class IrisApplication {

	public static void main(String[] args) {
		SpringApplication.run(IrisApplication.class, args);
	}

}
