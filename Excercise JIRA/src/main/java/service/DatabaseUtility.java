package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import util.Constant;
import util.PropertiesUtil;

/**
 * Created by tmtai on 7/10/2017.
 */
public class DatabaseUtility {
    protected MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://"+PropertiesUtil.getString(Constant.DATABASE_HOST) +":"+PropertiesUtil.getString(Constant.DATABASE_PORT)));
    protected MongoDatabase db = mongoClient.getDatabase(PropertiesUtil.getString(Constant.DATABASE_SCHEMA));
    protected ObjectMapper mapper = new ObjectMapper();

    public String getObjectId(Document dbObj) {
        ObjectId id = dbObj.getObjectId(Constant.MONGODB_ID);
        return id.toHexString();
    }
}
