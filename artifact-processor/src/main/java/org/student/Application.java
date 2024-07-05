package org.student;

import org.student.api.factories.ConsumerFactory;
import org.student.api.managers.ConsumersManager;
import org.student.api.managers.ConsumersManagerImpl;
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
import java.util.concurrent.atomic.AtomicReference;

import static org.yaml.snakeyaml.env.EnvScalarConstructor.ENV_FORMAT;
import static org.yaml.snakeyaml.env.EnvScalarConstructor.ENV_TAG;

public class Application {

	private static final AtomicReference<ConsumersManager> consumersManager = new AtomicReference<>(null);

	public static void main(String[] args) throws IOException {

		if (args.length != 1) {
			System.err.println("Config missing.");
			return;
		}
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("Shutdown hook is running!");

			if (consumersManager.get() != null){
				consumersManager.get().shutdown();
			}

		}));

		ApplicationConfig config = loadConfig(args[0]);

		new Thread(() -> {
			ArtifactsService artifactsService = new ArtifactsServiceImpl(config);

			var consumers = ConsumerFactory.createConsumers(config.getKafka(), artifactsService);

			if (consumersManager.get() == null) {
				consumersManager.set(new ConsumersManagerImpl(consumers));
				consumersManager.get().startListenMessages();
			}
		}).start();
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