//package com.recordcataloguer.recordcataloguer.database.mongodb;
//
//import com.google.api.client.util.Value;
//import com.mongodb.*;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.result.InsertOneResult;
//import com.recordcataloguer.recordcataloguer.database.mongodb.constants.MongoDbConstants;
//import com.recordcataloguer.recordcataloguer.dto.mongodb.UserDTO;
//import lombok.extern.log4j.Log4j2;
//import org.bson.BsonDocument;
//import org.bson.BsonInt64;
//import org.bson.BsonValue;
//import org.bson.Document;
//import org.bson.conversions.Bson;
//
//import com.mongodb.client.MongoClient;
//import com.mongodb.client.MongoClients;
//import com.mongodb.client.MongoDatabase;
//import org.bson.types.ObjectId;
//import org.springframework.stereotype.Component;
//
//
//@Log4j2
//public class MongoDbClient {
//
//    //@Value("${mongodb+srv}")
//    private static String mongoDbConnectionString = "mongodb+srv://vinyl_cataloger_api:2wLcARozF9yZEwN@cluster0.gq7y51s.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
//
//    public static void getMongoDbConnection(String[] args) {
//        // Replace the placeholder with your Atlas connection string
//        String uri = mongoDbConnectionString;
//
//        // Construct a ServerApi instance using the ServerApi.builder() method
//        ServerApi serverApi = ServerApi.builder()
//                .version(ServerApiVersion.V1)
//                .build();
//
//        MongoClientSettings settings = MongoClientSettings.builder()
//                .applyConnectionString(new ConnectionString(uri))
//                .serverApi(serverApi)
//                .build();
//
//        // Create a new client and connect to the server
//        try (MongoClient mongoClient = MongoClients.create(settings)) {
//            MongoDatabase database = mongoClient.getDatabase(MongoDbConstants.VINYL_CATALOGER_DB);
//            try {
//                // Send a ping to confirm a successful connection
//                Bson command = new BsonDocument("ping", new BsonInt64(1));
//                Document commandResult = database.runCommand(command);
//                System.out.println("Pinged your deployment. You successfully connected to MongoDB!");
//            } catch (MongoException me) {
//                System.err.println(me);
//            }
//        }
//    }
//
//    public static BsonValue insertUser(UserDTO user) {
//        // Replace the placeholder with your Atlas connection string
//        String uri = mongoDbConnectionString;
//        log.info("mongo connection string {}", uri);
//
//        if(user == null) {
//            log.info("Error inserting user into DB. User is null");
//
//            return null;
//        }
//
//        // Construct a ServerApi instance using the ServerApi.builder() method
//        ServerApi serverApi = ServerApi.builder()
//                .version(ServerApiVersion.V1)
//                .build();
//
//        MongoClientSettings settings = MongoClientSettings.builder()
//                .applyConnectionString(new ConnectionString(uri))
//                .serverApi(serverApi)
//                .build();
//
//        try (MongoClient mongoClient = MongoClients.create(uri)) {
//            MongoDatabase database = mongoClient.getDatabase(MongoDbConstants.VINYL_CATALOGER_DB);
//            MongoCollection<Document> collection = database.getCollection(MongoDbConstants.VINYL_CATALOGER_DB_USER_TABLE);
//
//            // Create a new client and connect to the server
//            try {
//                InsertOneResult result = collection.insertOne(buildUserEntity(user));
//
//                // Prints the ID of the inserted document
//                System.out.println("Success! Inserted document id: " + result.getInsertedId());
//                mongoClient.close();
//                return result.getInsertedId();
//
//                // Prints a message if any exceptions occur during the operation
//            } catch (MongoException me) {
//                mongoClient.close();
//                System.err.println("Unable to insert due to an error: " + me);
//            }
//
//        }
//        return null;
//    }
//
//    public static Document buildUserEntity(UserDTO user) {
//
//        Document entity = new Document()
//                .append("_id", new ObjectId())
//                .append("user_name", user.getUserName())
//                .append("verifier_token", user.getVerifierToken())
//                .append("oauth_token", user.getOAuthToken())
//                .append("oauth_token_secret", user.getOAuthTokenSecret());
//
//        return entity;
//    }
//
//    public static void testMongoDbConnection(String[] args) {
//        // Replace the placeholder with your Atlas connection string
//        String uri = mongoDbConnectionString;
//
//        // Construct a ServerApi instance using the ServerApi.builder() method
//        ServerApi serverApi = ServerApi.builder()
//                .version(ServerApiVersion.V1)
//                .build();
//
//        MongoClientSettings settings = MongoClientSettings.builder()
//                .applyConnectionString(new ConnectionString(uri))
//                .serverApi(serverApi)
//                .build();
//
//        // Create a new client and connect to the server
//        try (MongoClient mongoClient = MongoClients.create(settings)) {
//            MongoDatabase database = mongoClient.getDatabase(MongoDbConstants.VINYL_CATALOGER_DB);
//            try {
//                // Send a ping to confirm a successful connection
//                Bson command = new BsonDocument("ping", new BsonInt64(1));
//                Document commandResult = database.runCommand(command);
//                System.out.println("Pinged your deployment. You successfully connected to MongoDB!");
//            } catch (MongoException me) {
//                System.err.println(me);
//            }
//        }
//    }
//}
