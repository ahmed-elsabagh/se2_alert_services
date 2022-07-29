package com.se2.alert.config;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class PrintConf {

	@EventListener
	public void handleContextRefreshed(ContextRefreshedEvent event) {
		printActiveProperties((ConfigurableEnvironment) event.getApplicationContext().getEnvironment());
	}

	/**
	 * This method is used only for debugging
	 * 
	 * @param env
	 *            Environment variables
	 */
	private void printActiveProperties(ConfigurableEnvironment env) {

		System.out.println("************************* ACTIVE APP PROPERTIES ******************************");
		List<MapPropertySource> propertySources = new ArrayList<>();

		env.getPropertySources().forEach(it -> {
			if (it instanceof MapPropertySource && it.getName().contains("applicationConfig")) {
				propertySources.add((MapPropertySource) it);
			}
		});

		propertySources.stream().map(propertySource -> propertySource.getSource().keySet()).flatMap(Collection::stream).distinct().sorted()
				.forEach(key -> {
					try {
						System.out.println(key + "=" + env.getProperty(key));
					} catch (Exception e) {
						System.out.println(String.format("{%s} -> {%s}", key, e.getMessage()));
					}
				});
		System.out.println("******************************************************************************");
	}
}