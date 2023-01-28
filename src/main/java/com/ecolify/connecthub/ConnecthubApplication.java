package com.ecolify.connecthub;

import com.ecolify.connecthub.persistency.MongoClientConnection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConnecthubApplication {

	public static void main(String[] args) {

		System.out.println("Starting...");

		System.out.println("Initiating Database Config...");
		MongoClientConnection mongoClientConnection = new MongoClientConnection();
		if (mongoClientConnection.testConfig()){
			System.out.println("ALL GOOD");
		} else{
			System.err.println("MONGODB MIGHT HAVE A BAD CONFIG");
		}

		SpringApplication.run(ConnecthubApplication.class, args);
	}

}
