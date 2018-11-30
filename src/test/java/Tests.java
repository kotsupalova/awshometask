import config.AWSConfig;
import org.junit.jupiter.api.Test;
import service.FileService;
import service.s3.S3FileService;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Tests  extends Hooks{

    @Test
//    @ParameterizedTest
//    void uploadFileAndCheckLambdaTrigger(FileObject fileObject){
    void uploadFileAndCheckLambdaTrigger(){

        File file = FileService.createFileWithRandomName(AWSConfig.getFileExtension());
        S3FileService s3FileService = new S3FileService();
        s3FileService.uploadFile(AWSConfig.getBucketName(), file);
        assertTrue(s3FileService.isFileInBucket(AWSConfig.getBucketName(), file.getName()));
    }

}
