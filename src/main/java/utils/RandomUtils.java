package utils;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * Methods for getting random values
 */

public class RandomUtils {

    public static String randomAlphanumeric(int count) {
        return RandomStringUtils.randomAlphanumeric(count);
    }

}
