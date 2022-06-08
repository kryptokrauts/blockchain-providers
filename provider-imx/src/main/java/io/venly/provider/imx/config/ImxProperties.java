package io.venly.provider.imx.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "io.venly.provider.imx-gateway")
public record ImxProperties(
        String endpoint,
        String user,
        String password
) {}