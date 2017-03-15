package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import com.example.DatabaseService;

/**
 * Entry point to the application.
 * @author Pritpal Chahal
 */

@SpringBootApplication
public class Application implements CommandLineRunner{

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    @Autowired
    JdbcTemplate jdbcTemplate;
//    
    @Autowired
    DatabaseService databaseService;
    
	@Override
    public void run(String... strings) throws Exception {
    	// run database setup script.
		databaseService.setupTables();
    }
}
