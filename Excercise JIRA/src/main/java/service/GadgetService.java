package service;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import models.gadget.Gadget;
import models.gadget.GadgetAPI;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import util.Constant;
import util.PropertiesUtil;
import util.gadget.GadgetUtility;

import java.util.Iterator;
import java.util.List;

public class GadgetService {

    public static JSONArray getGadgetListfromDB() throws Exception {
        JSONArray gadgets = new JSONArray();
        Iterator<GadgetAPI> iterator = GadgetAPI.getIterator();
        while (iterator.hasNext()) {
            GadgetAPI gadgetObj = iterator.next();
            JSONObject gadget = new JSONObject();
            gadget.put("name", gadgetObj.getName());
            gadget.put("des", gadgetObj.getDescription());
            gadget.put("author", gadgetObj.getAuthor());
            gadget.put("img", gadgetObj.getPictureUrl());
            gadget.put("addnewUIurl", gadgetObj.getAddnewUIurl());
            gadget.put("type", gadgetObj.getType().toString());
            gadgets.put(gadget);
        }
        return gadgets;
    }

    public static void insertDashboardGadgettoDB(String dashboardId, String name, String data) {
        MongoClient mongoClient = new MongoClient();
        MongoCollection<Document> collection = mongoClient.getDatabase(PropertiesUtil.getString(Constant.DATABASE_SCHEMA)).getCollection("DashboardGadget");
        org.bson.Document doc = new org.bson.Document("dashboardId", dashboardId).append("type", name).append("data", data);
        collection.insertOne(doc);
        mongoClient.close();
    }

    public static void updateDashboardGadgettoDB(String GadgetId, String data) {
        MongoClient mongoClient = new MongoClient();
        MongoCollection<Document> collection = mongoClient.getDatabase(PropertiesUtil.getString(Constant.DATABASE_SCHEMA)).getCollection("DashboardGadget");
        org.bson.Document doc = new org.bson.Document("data", data).append("cache", "").append(Constant.UPDATE_DATE, 0);
        collection.updateOne(new org.bson.Document(Constant.MONGODB_ID, new ObjectId(GadgetId)), new org.bson.Document(Constant.MONGODB_SET, doc));
        mongoClient.close();
    }

    public static void deleteDashboardGadgetfromDB(String gadgetId) {
        ////System.out.println(gadgetId);
        MongoClient mongoClient = new MongoClient();
        MongoCollection<org.bson.Document> gadgetCollection = mongoClient.getDatabase(PropertiesUtil.getString(Constant.DATABASE_SCHEMA)).getCollection("DashboardGadget");
        FindIterable<Document> gadgetIterable = gadgetCollection.find(new org.bson.Document(Constant.MONGODB_ID, new ObjectId(gadgetId)));
        Document gadgetDoc = gadgetIterable.first();

        //START - Added to track the gadget delete issue - Can be reverted once RC found
        String data = gadgetDoc.getString("data");
        JSONObject dataObject = null;
        if(data != null) {
            dataObject = new JSONObject(data);
        }
//        logger.info("Deleting gadget type: " + gadgetDoc.getString("type") + " with release " + dataObject==null?"": dataObject.getString("Release"));
        //END - Added to track the gadget delete issue - Can be reverted once RC found

        gadgetCollection.deleteOne(gadgetDoc);

        mongoClient.close();
    }

    public static void clearCacheGadgetfromDB(String gadgetId) {
        MongoClient mongoClient = new MongoClient();
        MongoDatabase dataBase = mongoClient.getDatabase(PropertiesUtil.getString(Constant.DATABASE_SCHEMA));
        MongoCollection<org.bson.Document> collection = dataBase.getCollection(Constant.DASHBOARD_GADGET_COLECCTION);
        org.bson.Document document = collection.find(new org.bson.Document(Constant.MONGODB_ID, new ObjectId(gadgetId))).first();

        org.bson.Document doc = new org.bson.Document("cache", "").append(Constant.UPDATE_DATE, 0);
        collection.updateMany(new org.bson.Document("data", document.get("data")), new org.bson.Document(Constant.MONGODB_SET, doc));

        clearReleaseCache(dataBase, document.getString("data"));

        mongoClient.close();
    }


    private static void clearReleaseCache(MongoDatabase dataBase, String data) {
//        logger.debug("Clearing release cache with data " + data);
        if (data != null) {
            MongoCollection<org.bson.Document> collection = dataBase
                    .getCollection(Constant.RELEASE_TABLE);
            JSONObject dataObject = new JSONObject(data);
            if (dataObject.getString("Release") != null) {
//                logger.debug("Clearing cache for release "
//                        + dataObject.getString("Release"));
                collection.updateOne(
                        new Document("name", dataObject.getString("Release")),
                        new Document(Constant.MONGODB_SET, new Document(
                                "cache", "").append(Constant.UPDATE_DATE, 0)));
            }
        }
    }

    public static List<Gadget> getDashboardGadgetbyDashboardId(String dashboardId) throws Exception {
        List<Gadget> gadgets = GadgetUtility.getInstance().findByDashboardId(dashboardId);
        return gadgets;
    }
}
