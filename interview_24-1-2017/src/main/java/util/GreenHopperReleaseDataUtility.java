package util;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import service.DatabaseUtility;

import java.util.Map;

import static util.Constant.MONGODB_ID;
import static util.Constant.NAME;

/**
 * Created by nnthat on 3/14/17.
 */
public class GreenHopperReleaseDataUtility extends DatabaseUtility {
    protected MongoCollection<Document> collection;
    protected MongoCollection<Document> columnConfigurationCollection;
    private static GreenHopperReleaseDataUtility INSTANCE = new GreenHopperReleaseDataUtility();
    private static final String RELEASE_COLLECTION = "GreenHopperReleaseWithUrl";
    private static final String COLUMN_CONFIGURATION_COLLECTION = "ColumnConfiguration";
    private static final String FIX_VERSION = "fixVersion";
    private static final String DATA = "DATA";

    private GreenHopperReleaseDataUtility() {
        super();
        collection = db.getCollection(RELEASE_COLLECTION);
        columnConfigurationCollection = db.getCollection(COLUMN_CONFIGURATION_COLLECTION);
    }

    public static GreenHopperReleaseDataUtility getInstance() {
        return INSTANCE;
    }

    public Document findReleaseByName(String name) {
        Document searchQuery = new Document(NAME, name);
        return collection.find(searchQuery).first();
    }

    public void insertReleaseWithUrl(Map<String, Object> data, String url) {
        if (data != null) {
            collection.insertOne(new Document(Constant.NAME, data.get(FIX_VERSION)).append(Constant.RELEASE_URL, url).append(DATA, data.get(DATA)));
        }
    }

    public String updateReleaseWithUrlById(String id, Map<String, Object> data, String url) {
        if (data != null) {
            Document doc = new Document(Constant.NAME, data.get(FIX_VERSION)).append(Constant.RELEASE_URL, url).append(DATA, data.get(DATA));
            collection.updateOne(new Document(MONGODB_ID, new ObjectId(id)), new Document(Constant.MONGODB_SET, doc));
            return id;
        }
        return null;
    }

    public String updateReleaseWithUrlByName(String name, Map<String, Object> data) {
        if (data != null) {
            Document doc = new Document(Constant.NAME, data.get(FIX_VERSION)).append(DATA, data.get(DATA));
            collection.updateOne(new Document(Constant.NAME, name), new Document(Constant.MONGODB_SET, doc));
            return name;
        }
        return null;
    }

    public void updateRelease(Document releaseDoc) {
        collection.updateOne(new Document(Constant.NAME, releaseDoc.getString(Constant.NAME)), new Document(Constant.MONGODB_SET, releaseDoc));
    }

    public FindIterable<Document> findAll() {
        return collection.find();
    }

    public void saveColumnConfiguration(Document document) {
        Document userColumnConfiguration = new Document(Constant.USER, document.getString(Constant.USER));
        Document exitedDocument = columnConfigurationCollection.find(userColumnConfiguration).first();
        if (exitedDocument == null) {
            columnConfigurationCollection.insertOne(document);
        } else {
            document.append(Constant.MONGODB_ID, exitedDocument.get(Constant.MONGODB_ID));
            columnConfigurationCollection.updateOne(exitedDocument, new Document(Constant.MONGODB_SET, document));
        }
    }

    public Document getColumnConfiguration(String userName) {
        Document newColumnConfiguration = new Document(Constant.USER, userName);
        FindIterable<Document> documents = columnConfigurationCollection.find(newColumnConfiguration);
        return documents.first();

    }
}
