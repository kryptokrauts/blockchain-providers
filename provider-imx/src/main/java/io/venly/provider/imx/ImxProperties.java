package io.venly.provider.imx;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "io.venly.provider.imx-gateway")
record ImxProperties(
        String endpoint,
        String user,
        String password
) {}