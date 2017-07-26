package util.gadget;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import models.gadget.*;
import org.bson.Document;
import service.DatabaseUtility;
import util.Constant;
import util.JSONUtil;

import java.util.ArrayList;
import java.util.List;

public class GadgetUtility extends DatabaseUtility {
    private static final String TYPE = "type";
    private static GadgetUtility INSTANCE = new GadgetUtility();

    protected MongoCollection<Document> collection;

    private GadgetUtility() {
        super();
        collection = db.getCollection(Constant.DASHBOARD_GADGET_COLECCTION);
    }

    public static GadgetUtility getInstance() {
        return INSTANCE;
    }

    public List<Gadget> findByDashboardId(String id) {
        org.bson.Document searchQuery = new org.bson.Document(Constant.DASHBOARD_ID, id);
        FindIterable<Document> Iterable = collection.find(searchQuery);
        return parse(Iterable);
    }

    private List<Gadget> parse(FindIterable<org.bson.Document> Iterable) {
        //MongoCursor<Document> dbCursor = documents.iterator();
        List<Gadget> gadgets = new ArrayList<Gadget>();

        for (org.bson.Document document : Iterable) {
            Gadget.Type type = Gadget.Type.fromString((String) document.get(TYPE));

            if (type.equals(Gadget.Type.AMS_SONAR_STATISTICS_GADGET)) {
                SonarStatisticsGadget sonarStatisticsGadget = JSONUtil.getInstance().convertJSONtoObject(document.toJson(), SonarStatisticsGadget.class);
                sonarStatisticsGadget.setId(getObjectId(document));
                gadgets.add(sonarStatisticsGadget);

            } else if (type.equals(Gadget.Type.AMS_OVERDUE_REVIEWS)) {
                OverdueReviewsGadget overGadget = JSONUtil.getInstance()
                        .convertJSONtoObject(document.toJson(), OverdueReviewsGadget.class);
                overGadget.setId(getObjectId(document));
                gadgets.add(overGadget);
            } else {
//                logger.fastDebug("type %s is not available", document.get(TYPE));
            }

        }
        return gadgets;
    }
}
