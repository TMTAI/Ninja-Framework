package controllers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import filter.AdminSecureFilter;
import filter.SecureFilter;
import handle.GreenHopperReleaseHandler;
import handle.scheduler.SimpleThread;
import ninja.Context;
import ninja.FilterWith;
import ninja.Result;
import ninja.Results;
import ninja.params.Param;
import ninja.session.Session;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import util.Constant;
import util.PropertiesUtil;

import java.io.BufferedReader;
import java.util.*;
import java.util.function.Consumer;

import static util.Constant.*;
import static util.MyUtill.getHttpURLConnection;
import static util.MyUtill.isCacheExpired;

@Singleton
public class EpgDashboardConfigurationController {

    final static Logger logger = Logger.getLogger(EpgDashboardConfigurationController.class);
    private static final String RELEASE_COLLECTION = "GreenHopperReleaseWithUrl";
    @Inject
    GreenHopperReleaseHandler handler;

    public static JSONObject getPeriod(Session session) throws Exception {
        JSONObject res = new JSONObject();
        //todo
        JSONArray PeriodArray = new JSONArray();
        MongoClient mongoClient = new MongoClient();
        MongoCollection<Document> collection = mongoClient.getDatabase(PropertiesUtil.getString(Constant.DATABASE_SCHEMA)).getCollection(METRIC_TABLE);
        Document document = collection.find(new Document("code", "new_coverage")).first();

        if (isCacheExpired(document, 24)) {

            BufferedReader br = getHttpURLConnection(LINK_GET_JIRA_PERIODS, session);

            String rs = "";
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                rs = rs + inputLine;
            }

            br.close();


            JSONArray jsonArray = new JSONArray(rs);
            JSONObject jsonObject;
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("key").contains("sonar.timemachine.period")) {
                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put("value", jsonObject.getString("value"));
                    jsonObject1.put("key", jsonObject.getString("key").replace("sonar.timemachine.", ""));
                    PeriodArray.put(jsonObject1);
                }
            }


            collection.updateMany(new Document(new Document("code", "new_coverage")), new Document(Constant.MONGODB_SET, new Document("cache", PeriodArray.toString()).append(Constant.UPDATE_DATE, new GregorianCalendar(Locale.getDefault()).getTimeInMillis())));


        } else {
            PeriodArray = new JSONArray(document.getString("cache"));
        }

        res.put("PeriodArray", PeriodArray);
        res.put("CurrentPeriod", document.getString("period"));

        mongoClient.close();

        return res;

    }

    @FilterWith(SecureFilter.class)
    public Result configuration() {
        return Results.ok();
    }

    @FilterWith(SecureFilter.class)
    public Result getReleaseWithUrl(Context context) {
        MongoClient mongoClient = new MongoClient();
        MongoDatabase db = mongoClient.getDatabase(PropertiesUtil.getString(Constant.DATABASE_SCHEMA));
        MongoCollection<Document> collection = db.getCollection(RELEASE_COLLECTION);
        FindIterable<Document> documents = collection.find();
        JSONArray releases = new JSONArray();
        documents.forEach(new Consumer<Document>() {
            @Override
            public void accept(Document doc) {
                String name = (String) doc.get(NAME);
                String url = (String) doc.get("url");
                Boolean display = (Boolean) doc.get("display");
                String id = doc.getObjectId(Constant.MONGODB_ID).toHexString();
                JSONObject release = new JSONObject();
                release.put("id", id);
                release.put("name", name);
                release.put("url", url);
                release.put("display", display);
                releases.put(release);
            }
        });
        return Results.text().render(releases);
    }

    @FilterWith(AdminSecureFilter.class)
    public Result addNewGreenHopperRelease(@Param(NAME) String name, @Param("url") String url, @Param("display") Boolean display, Context context) {
        MongoClient mongoClient = new MongoClient();
        MongoDatabase db = mongoClient.getDatabase(PropertiesUtil.getString(Constant.DATABASE_SCHEMA));
        MongoCollection<Document> collection = db.getCollection(RELEASE_COLLECTION);
        Document doc = new Document(NAME, name).append(Constant.RELEASE_URL, url).append(Constant.RELEASE_DISPLAY, display)
                .append("cacheData", "updating");
        collection.insertOne(doc);
        mongoClient.close();

        SimpleThread updateThread = new SimpleThread(handler, name);
        updateThread.run();
        return Results.text().render("success");
    }

    @FilterWith(AdminSecureFilter.class)
    public Result updateGreenHopperRelease(@Param("id") String id, @Param("name") String name, @Param("url") String url, @Param("display") Boolean display, Context context) {
        MongoClient mongoClient = new MongoClient();
        MongoDatabase db = mongoClient.getDatabase(PropertiesUtil.getString(Constant.DATABASE_SCHEMA));
        MongoCollection<Document> collection = db.getCollection(RELEASE_COLLECTION);
        Document doc = handler.getGreenHopperReleaseByName(name);
        doc.append(Constant.RELEASE_URL, url);
        doc.append(Constant.RELEASE_DISPLAY, display);
        collection.updateOne(new Document(MONGODB_ID, new ObjectId(id)), new Document(Constant.MONGODB_SET, doc));
//        collection.updateOne(new Document(MONGODB_ID, new ObjectId(id)), new Document(Constant.MONGODB_SET, new Document("name", name).append(Constant.RELEASE_URL, url).append("cache", 0).append(Constant.UPDATE_DATE, 0)));
        mongoClient.close();
        SimpleThread updateThread = new SimpleThread(handler, name);
        updateThread.run();
        return Results.json().status(200);
    }

    @FilterWith(AdminSecureFilter.class)
    public Result deleteGreenHopperRelease(@Param("url") String url) {
        MongoClient mongoClient = new MongoClient();
        MongoDatabase db = mongoClient.getDatabase(PropertiesUtil.getString(Constant.DATABASE_SCHEMA));
        MongoCollection<Document> collection = db.getCollection(RELEASE_COLLECTION);
        collection.deleteOne(new Document(Constant.RELEASE_URL, url));
        mongoClient.close();
        return Results.ok();
    }

    @FilterWith(AdminSecureFilter.class)
    public Result getPeriodList(Session session) {
        try {
            return Results.text().render(getPeriod(session));
        } catch (Exception e) {
            logger.error(e);
            return Results.internalServerError();
        }
    }

    @FilterWith(AdminSecureFilter.class)
    public Result setPeriod(@Param("period") String period) {
        try {
            MongoClient mongoClient = new MongoClient();
            MongoCollection<Document> collection = mongoClient.getDatabase(PropertiesUtil.getString(Constant.DATABASE_SCHEMA)).getCollection(Constant.METRIC_TABLE);
            collection.updateMany(new Document(new Document("code", "new_coverage")), new Document(Constant.MONGODB_SET, new Document("period", period)));
            mongoClient.close();
            return Results.ok();
        } catch (Exception e) {
            logger.error(e);
            return Results.internalServerError();
        }
    }


}
