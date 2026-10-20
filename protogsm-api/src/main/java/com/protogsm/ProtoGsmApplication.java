package com.protogsm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.protogsm"
})
public class ProtoGsmApplication {

	void main(String[] args) {
		SpringApplication.run(ProtoGsmApplication.class, args);
	}
}
