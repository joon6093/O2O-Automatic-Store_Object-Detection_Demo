package com.IIA.o2o_automatic_store_springmvc_java.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@RequiredArgsConstructor
@Component
public class MariaDBConfig implements ApplicationRunner {
    private final DataSource dataSource;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try(Connection connection = dataSource.getConnection()){
            System.out.println("MariaDB Connection : " + connection);
            String URL = connection.getMetaData().getURL();
            System.out.println("MariaDB URL : " + URL);
            String User = connection.getMetaData().getUserName();
            System.out.println("MariaDB User : " + User);
        }
    }
}
