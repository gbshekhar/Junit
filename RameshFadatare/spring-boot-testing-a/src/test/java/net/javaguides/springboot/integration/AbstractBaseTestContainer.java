package net.javaguides.springboot.integration;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

public abstract class AbstractBaseTestContainer {

    //we are making this class as abstract and extending from other integration classes
    //By this we are achieving Singleton containers pattern

    static final MySQLContainer MY_SQL_CONTAINER;

    static {
        MY_SQL_CONTAINER = (MySQLContainer) new MySQLContainer("mysql:8.0")
                .withUsername("username")
                .withPassword("password")
                .withDatabaseName("ems");

        //Manually we are starting container; so we can remove @TestContainer annotation at integration class level
        MY_SQL_CONTAINER.start();
    }

    @DynamicPropertySource
    public static void dynamicePropertySource(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username",MY_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password",MY_SQL_CONTAINER::getPassword);
    }
}
