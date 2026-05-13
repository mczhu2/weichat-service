package com.weichat.common.startup;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartupLogger {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationStartupLogger.class);

    private final Environment environment;

    public ApplicationStartupLogger(Environment environment) {
        this.environment = environment;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void logApplicationStartup() {
        String applicationName = environment.getProperty("spring.application.name", "application");
        String port = environment.getProperty("local.server.port",
                environment.getProperty("server.port", "unknown"));
        String[] activeProfiles = environment.getActiveProfiles();
        String profiles = activeProfiles.length == 0 ? "default" : Arrays.toString(activeProfiles);

        LOGGER.info("==================================================");
        LOGGER.info("Application started: {}", applicationName);
        LOGGER.info("Active profiles: {}", profiles);
        LOGGER.info("Local access: http://localhost:{}", port);
        LOGGER.info("Network access: http://{}:{}", resolveHostAddress(), port);
        LOGGER.info("==================================================");
    }

    private String resolveHostAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            LOGGER.warn("Failed to resolve host address for startup log", ex);
            return "unknown-host";
        }
    }
}
