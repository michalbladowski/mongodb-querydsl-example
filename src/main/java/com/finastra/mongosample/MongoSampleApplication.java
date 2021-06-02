package com.finastra.mongosample;

import com.finastra.mongosample.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = UserRepository.class)
public class MongoSampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(MongoSampleApplication.class, args);
	}

}
