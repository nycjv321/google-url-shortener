package utilities.nycjv321.googleurlshortener;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by fedora on 7/3/15.
 */
class TestPropertiesReader {
    private static Properties properties;

    static {
        properties = new Properties();
        try {
            properties.load(GoogleURLShortenerTests.class.getResourceAsStream("/test.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Could not find test.properties");
        }
    }

    public static String getApiKey() {
        return properties.getProperty("api.key");
    }
}
