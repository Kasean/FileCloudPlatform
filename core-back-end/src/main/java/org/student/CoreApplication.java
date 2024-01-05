package org.student;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;

@SpringBootApplication
@Controller
public class CoreApplication {

	private static final Logger logger = LogManager.getLogger(CoreApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CoreApplication.class, args);
		logger.info("[CORE MODULE] started.");
	}
}