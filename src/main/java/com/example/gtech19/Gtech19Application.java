package com.example.gtech19;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.gtech19.mapper")
public class Gtech19Application {

	public static void main(String[] args) {
		SpringApplication.run(Gtech19Application.class, args);
	}

}
