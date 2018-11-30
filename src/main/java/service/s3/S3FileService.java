package service.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import logger.Logger;

import java.io.File;

public class S3FileService {

//    TODO add separate method to init TransferManager

    public void uploadFile(String bucketName, File file) {
        String fileName = file.getName();
        TransferManager transferManager = TransferManagerBuilder.standard().build();
        try {
            Upload uploader = transferManager.upload(bucketName, fileName, file);
            while (uploader.isDone() == false) {
                Logger.info(uploader.getProgress().getPercentTransferred() + "%");
            }
            Logger.info(String.format("File '%s' was loaded to '%s'", fileName, bucketName));
        } catch (AmazonServiceException e) {
            Logger.error(e.getErrorMessage(), e);
            System.exit(1);
        }
    }

    public boolean isFileInBucket(String bucketName, String fileName) {
        Logger.info(String.format("Search '%s' file in the '%s' bucket.", fileName, bucketName));
        boolean isPresent = false;
        try {
            AmazonS3 amazonS3Client = AmazonS3ClientBuilder.defaultClient();
            ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucketName).withMaxKeys(2);
            ListObjectsV2Result result;
            do {
                result = amazonS3Client.listObjectsV2(req);
                for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
//                    System.out.printf(" - %s (size: %d)\n", objectSummary.getKey(), objectSummary.getSize());
                    if (objectSummary.getKey().equals(fileName)) {
                        isPresent = true;
                    }
                }
                String token = result.getNextContinuationToken();
                req.setContinuationToken(token);
            } while (result.isTruncated());
        } catch (AmazonServiceException e) {
            Logger.error(e.getMessage(), e);
        }
        return isPresent;
    }
}
