package lambda;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.event.S3EventNotification.S3EventNotificationRecord;
import config.AWSConfig;
import org.apache.commons.io.FilenameUtils;
import service.dynamodb.DynamoDBService;

public class UploadItemLambdaFunction implements RequestHandler<S3Event, String> {

    private AmazonS3 s3 = AmazonS3ClientBuilder.standard().build();
    S3EventNotificationRecord record;
    private String tableName = AWSConfig.getDynamoDBTableName();


    public UploadItemLambdaFunction() {
    }

    // Test purpose only.
    UploadItemLambdaFunction(AmazonS3 s3) {
        this.s3 = s3;
    }

    @Override
    public String handleRequest(S3Event event, Context context) {
//        context.getLogger().log("Received event: " + event);
//        context.getLogger().log("LAMBDA FileName:" + event.getRecords().get(0).getS3().getObject().getKey());

//        System.out.println("Debug. Step 1.");

        record = event.getRecords().get(0);
        String fileName = record.getS3().getObject().getKey();
        String bucketId = record.getS3().getBucket().getArn();
        String bucketName = record.getS3().getBucket().getName();
        String fileType = FilenameUtils.getExtension(fileName);

//        System.out.println(String.format("Debug. Step2. FileName = %s, bucketId=%s, bucketName=%s, fileType=%s",
//                fileName, bucketId, bucketName, fileType));

        DynamoDBService dbClient = new DynamoDBService();
        Item item = dbClient.configureFilePutItem(bucketId, bucketName + "/" + fileName, fileType);

//        System.out.println("Debug. Success!");
        return dbClient.putItem(tableName, item);
    }
}