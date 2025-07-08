package helper;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class DBConnection {
    private static final String URI = "mongodb://localhost:27017";
    private static final String DB_NAME = "penjualan_tanah";
    private static MongoClient mongoClient;
    private static MongoDatabase database;

    static {
        try {
            mongoClient = MongoClients.create(URI);
            database = mongoClient.getDatabase(DB_NAME);
        } catch (Exception e) {
            e.printStackTrace();  // bisa juga disimpan ke log
        }
    }

    public static MongoDatabase getDatabase() {
        return database;
    }
}
