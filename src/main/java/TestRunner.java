import com.amazonaws.services.dynamodbv2.document.Item;
import config.AWSConfig;
import service.FileService;
import service.dynamodb.DynamoDBService;

import java.io.File;

public class TestRunner {

    public static void main(String [] args) {




//        boolean res = s3FileService.isFileInBucket(AWSConfig.getBucketName(), "qT5yuz.txt");
//        System.out.println("Res = " + res);

        File file = FileService.createFileWithRandomName(AWSConfig.getFileExtension());
        DynamoDBService dbClient = new DynamoDBService();
        Item item = dbClient.configureFilePutItem(AWSConfig.getBucketFolderName(), AWSConfig.getBucketName() + "/" + file.getName(), AWSConfig.getFileExtension());
        String res = dbClient.putItem(AWSConfig.getDynamoDBTableName(), item);
        System.out.println("RES: " + res);

    }




}
