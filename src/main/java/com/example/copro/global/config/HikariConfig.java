package com.example.copro.global.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.sql.DataSource;

@Component
public class HikariConfig {

    private final DataSource dataSource;

    public HikariConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PreDestroy
    public void close() {
        if (dataSource instanceof HikariDataSource) {
            ((HikariDataSource) dataSource).close();
        }
    }
}
