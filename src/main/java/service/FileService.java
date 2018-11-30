package service;

import logger.Logger;
import utils.RandomUtils;

import java.io.File;
import java.io.IOException;

public class FileService {

    private static final int DEFAULT_FILE_NAME_LENGHT = 6;

    public static File createFile(String fileName, String extension) {
        String fullFileName = String.format("%s.%s", fileName, extension);
        File newFile = null;
        try {
            newFile = new File(fullFileName);
            if(newFile.createNewFile()) {
                Logger.info(String.format("New file with name '%s' was successfully created.", fullFileName));
            }
        } catch (IOException e) {
            Logger.error(String.format("File with name '%s' wasn't created", fullFileName), e);
        }
        return newFile;
    }

    public static File createFileWithRandomName(String extension) {
        String fullFileName = String.format("%s.%s", RandomUtils.randomAlphanumeric(DEFAULT_FILE_NAME_LENGHT), extension);
        return createFile(fullFileName, extension);
    }

    public static void deleteFile(String fileName) {
        File file = new File(fileName);
        if(file.delete()){
            Logger.info(String.format("'%s' file was deleted", fileName));
        }else {
            Logger.error(String.format("'%s' file wasn't deleted", fileName));
        }
    }
}
