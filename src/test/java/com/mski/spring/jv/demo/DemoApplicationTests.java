package com.mski.spring.jv.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class DemoApplicationTests {

	@Autowired
	ConfigurableEnvironment environment;

	@Test
	void contextLoads() {
		assertThat(environment.getActiveProfiles()).contains("test");
	}

}
