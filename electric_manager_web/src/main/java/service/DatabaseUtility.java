package service;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

/**
 * Created by tmtai on 7/10/2017.
 */
public class DatabaseUtility {
    protected MongoClient mongoClient = new MongoClient("localhost",27017);
    protected MongoDatabase db = mongoClient.getDatabase("test");
}
