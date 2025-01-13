package com.ainkai.config;

import io.micrometer.tracing.Tracer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TracerConfig {

    @Bean
    public Tracer tracer() {
        return Tracer.NOOP; // No-op Tracer for bypassing the issue
    }
}
