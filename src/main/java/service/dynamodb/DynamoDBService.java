package service.dynamodb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;
import logger.Logger;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class DynamoDBService {
    private AmazonDynamoDB dynamoDBClient;
    private DynamoDB dynamoDB;
    private Table table;

    public DynamoDBService() {
        dynamoDBClient = AmazonDynamoDBClientBuilder.defaultClient();
        dynamoDB = new DynamoDB(dynamoDBClient);
    }

    public String putItem(String tableName, Item item) {
        try {
            table = dynamoDB.getTable(tableName);
            table.putItem(item);
            return String.format("Success. The Item was successfully added to '%s' table.", tableName);
        } catch (AmazonDynamoDBException e) {
            Logger.error(e.getMessage(), e);
            return String.format("Fail. The Item has not been added to '%s' table", tableName);
        }
    }

    public Item configureFilePutItem(String packageId, String filePath, String fileType) {
        Item item = new Item()
                .withPrimaryKey("packageId", packageId, "originTimeStamp", new Timestamp(System.currentTimeMillis()).getTime())
                .withString("filePath", filePath)
                .withString("fileType", fileType);
        Logger.info(String.format("Item was configured: \n %s", item.toString()));
        return item;
    }

    public Item getItemFromTable(String tableName, String filePath) {
        Item item;
        table = dynamoDB.getTable(tableName);
        try {
            table.waitForActive();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Condition scanFilterCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.EQ.toString())
                .withAttributeValueList(new AttributeValue().withS(filePath));
        Map<String, Condition> conditions = new HashMap();
        conditions.put("filePath", scanFilterCondition);
        ScanRequest scanRequest = new ScanRequest()
                .withTableName(tableName)
                .withScanFilter(conditions);
        ScanResult result = dynamoDBClient.scan(scanRequest);
        for (Map<String, AttributeValue> i : result.getItems()) {
            String packageId = i.get("packageId").getS();
            double timeStamp = Double.parseDouble(i.get("originTimeStamp").getN());
            item = table.getItem("packageId", packageId,
                    "originTimeStamp", timeStamp);
            Logger.info("The Item was found.");
            Logger.info(item.toString());
            return item;
        }
        return null;
    }

}
