package com.ecolify.connecthub.Hub;

import com.ecolify.connecthub.Hub.model.HubConfigRecord;
import com.ecolify.connecthub.Node.NodeConfigRecord;
import com.ecolify.connecthub.persistency.MongoClientConnection;

import java.util.Scanner;

public class HubManager {
    private final String HUB_CONFIG_COLLECTION = "hubConfig";

    private HubConfigRecord hubConfigRecord;

    public HubManager(){
        this.retrieveHubConfig();
    }


    public void retrieveHubConfig(){
        MongoClientConnection mongoClientConnection = new MongoClientConnection();
        this.hubConfigRecord = mongoClientConnection.findHubConfig(HUB_CONFIG_COLLECTION);
        if (this.hubConfigRecord == null){
            System.err.println("HubConfigRecord is null, setup assistant will be launched...");
            this.setupHubConfig();
            mongoClientConnection.insertHubConfig(HUB_CONFIG_COLLECTION, this.hubConfigRecord);
        }

    }

    private void setupHubConfig(){
        System.out.println("Setup assistant launched...");
        System.out.println("Please enter the following information:");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the name of the hub:");
        String hubName = scanner.nextLine();

        System.out.println("Enter the location of the hub:");
        String hubLocation = scanner.nextLine();

        System.out.println("Enter the owner of the hub:");
        String hubOwner = scanner.nextLine();

        System.out.println("Enter the number of nodes connected to the hub:");
        int numberOfNodes = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < numberOfNodes; i++) {
            System.out.println("Enter the mac address of node " + i + ":");
            String macAddress = scanner.nextLine();

            System.out.println("Enter the room of node " + i + ":");
            String room = scanner.nextLine();

            NodeConfigRecord nodeConfigRecord = new NodeConfigRecord(macAddress, room);
        }


        this.hubConfigRecord = new HubConfigRecord(hubName, hubLocation, hubOwner, null);
    }


}
