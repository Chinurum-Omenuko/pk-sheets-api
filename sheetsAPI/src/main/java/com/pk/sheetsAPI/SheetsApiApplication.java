package com.pk.sheetsAPI;

import com.google.cloud.spring.autoconfigure.core.GcpContextAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {GcpContextAutoConfiguration.class})
public class SheetsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SheetsApiApplication.class, args);
	}

}
