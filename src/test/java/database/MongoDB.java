package database;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import io.qameta.allure.Step;
import org.bson.Document;
import setup.ConfVars;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;

public enum MongoDB {
    INSTANCE;

    ConfVars confVars = ConfVars.getInstance();
    private MongoClient mongoClient;
    String serverIP, dbName, dbUsername, dbPassword;
    int port;

    MongoDB() {
        if (mongoClient == null) {
            try {
                mongoClient = getClient();
            } catch (UnknownHostException e) {
                ConfVars.logger.error(e);
            }

        }
    }

    private MongoClient getClient() throws UnknownHostException {
        serverIP = confVars.MONGO_IP;
        port = Integer.parseInt(confVars.MONGO_PORT);
        dbName = confVars.MONGO_NAME;
        dbUsername = confVars.MONGO_USERNAME;
        dbPassword = confVars.MONGO_PASSWORD;
        MongoCredential credential = MongoCredential.createScramSha1Credential(dbUsername, dbName, dbPassword.toCharArray());
        return new MongoClient(new ServerAddress(serverIP,port), Arrays.asList(credential));
    }

    @Step("Удалить заявки начиная с даты '{date}'")
    public void deleteClaimsOlderDate(String ldap, String date) throws ParseException {
        MongoDatabase database = mongoClient.getDatabase(dbName);
        MongoCollection<Document> collection = database.getCollection("operation");

        collection.deleteMany(
            and(
                eq("ld", ldap.toUpperCase()),
                gt("dm", new SimpleDateFormat("yyyy-MM-dd").parse(date))
            )
        );
    }

    @Step("Добавить заявку '{claimName}' с референсом {ref} в БД")
    public void addUnfinishedClaims(long ref, String claimName, String date, String ldap, String claimUrl)
        throws ParseException {
        MongoDatabase database = mongoClient.getDatabase(dbName);
        MongoCollection<Document> collection = database.getCollection("operation");

        Document doc = new Document("_id", new Document("ref", Long.toString(ref))
                                                .append("idp", claimName)
                                                .append("ids", "WAVE"))
                    .append("dc", new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date))
                    .append("dm", new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date))
                    .append("cl", new Document("id", "17589602")
                                        .append("fio", "ХЛЕБНИКОВ ДМИТРИЙ РАДОМИРОВИЧ")
                                        .append("inn", "2181618276"))
                    .append("st", "NEW")
                    .append("ld", ldap.toUpperCase())
                    .append("br", "DNH0")
                    .append("url", new Document("v", claimUrl))
                    .append("dt", "<b>уникальное слово - нотрдам</b><br>")
                    .append("ext", new Document("dt", "LINUX")
                                        .append("de", "CLTMNG53121102511066625jIKKff"));

        collection.insertOne(doc);
    }

    public List<String> getClaimInfoByRef(String ref) throws ParseException {
        List<String> lastClaimsInfo = new ArrayList<>();

        MongoDatabase database = mongoClient.getDatabase(dbName);
        MongoCollection<Document> collection = database.getCollection("operation");

        MongoCursor <Document> cursor = collection.find(eq("_id.ref", ref))
                                        .sort(Sorts.descending("dm")).limit(1).iterator();

        try {
            while(cursor.hasNext()) {
                lastClaimsInfo.add(cursor.next().toJson().toString());
            }
        } finally {
            cursor.close();
        }

        return lastClaimsInfo;
    }

    public List<String> getClaimInfo(String subsystemName, String productId) throws ParseException {
        List<String> claimsInfo = new ArrayList<>();

        MongoDatabase database = mongoClient.getDatabase(dbName);
        MongoCollection<Document> collection = database.getCollection("system");

        MongoCursor <Document> cursor = collection.aggregate(Arrays.asList(
            Aggregates.match(Filters.eq("_id", subsystemName)),
            Aggregates.unwind("$products"),
            Aggregates.match(Filters.eq("products._id", productId)),
            Aggregates.project(Projections.fields(
                Projections.include("products.name")
            ))
        )).iterator();

        try {
            while(cursor.hasNext()) {
                claimsInfo.add(cursor.next().toJson().toString());
            }
        } finally {
            cursor.close();
        }

        return claimsInfo;
    }

    public List<String> getClaimStatusInfo(String status) throws ParseException {
        List<String> claimsInfo = new ArrayList<>();

        MongoDatabase database = mongoClient.getDatabase(dbName);
        MongoCollection<Document> collection = database.getCollection("status");

        MongoCursor <Document> cursor = collection.find(eq("_id", status)).iterator();

        try {
            while(cursor.hasNext()) {
                claimsInfo.add(cursor.next().toJson().toString());
            }
        } finally {
            cursor.close();
        }

        return claimsInfo;
    }
}
