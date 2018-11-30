package config;

import logger.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AWSConfig {

    public static final String DEFAULT_FILE_PROPERTY_PATH = "src/main/resources/";
    public static final String DEFAULT_FILE_PROPERTY_NAME = "fileconfig.properties";
    private static Properties prop;

    public static void loadProperties() {
        InputStream input = null;
        prop = new Properties();
        String fileName = String.format("%s%s", DEFAULT_FILE_PROPERTY_PATH, DEFAULT_FILE_PROPERTY_NAME);

        try {
            input = new FileInputStream(fileName);
            if (input == null) {
                Logger.error(String.format("Config file '%s' not found", fileName));
                System.exit(-1);
            }
            prop.load(input);
            input.close();
        } catch (IOException e) {
            Logger.error(String.format("Exception during loading config file '%s'", fileName), e);
            System.exit(-1);
        }
    }

    public static String getBucketName() {
        return getProperty("bucketName");
    }

    public static String getBucketFolderName() {
        return getProperty("bucketFolderName");
    }

    public static String getDynamoDBTableName() {
        return getProperty("dynamoDBTableName");
    }

    public static String getLambdaFunctionName() {
        return getProperty("lambdaFunctionName");
    }

    public static String getFileExtension() {
        return getProperty("fileExtension");
    }

    private static String getProperty(String propertyName) {
        if (prop == null) {
            loadProperties();
        }
        return prop.getProperty(propertyName);
    }
}
