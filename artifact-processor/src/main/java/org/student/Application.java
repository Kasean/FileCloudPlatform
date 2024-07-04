package org.student;

import org.student.api.factories.ConsumerFactory;
import org.student.configs.ApplicationConfig;
import org.student.api.consumers.MessageConsumer;
import org.student.services.ArtifactsService;
import org.student.services.ArtifactsServiceImpl;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.env.EnvScalarConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.yaml.snakeyaml.env.EnvScalarConstructor.ENV_FORMAT;
import static org.yaml.snakeyaml.env.EnvScalarConstructor.ENV_TAG;

public class Application {
	public static void main(String[] args) throws IOException {

		if (args.length != 1) {
			System.err.println("Config missing.");
			return;
		}

		ApplicationConfig config = loadConfig(args[0]);

		ArtifactsService artifactsService = new ArtifactsServiceImpl(config);

		var consumers = ConsumerFactory.createConsumers(config.getKafka(), artifactsService);

		consumers.forEach(c -> c.consume());
	}

	private static ApplicationConfig loadConfig(String configFile) throws IOException {
		Yaml yaml = new Yaml(new EnvScalarConstructor());
		yaml.addImplicitResolver(ENV_TAG, ENV_FORMAT, "$");

		ApplicationConfig config;
		try (InputStream in = Files.newInputStream(Paths.get(configFile))) {
			config = yaml.loadAs(in, ApplicationConfig.class);
		}

		return config;
	}
}