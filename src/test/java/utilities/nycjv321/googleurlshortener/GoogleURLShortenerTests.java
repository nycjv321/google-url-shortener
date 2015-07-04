package utilities.nycjv321.googleurlshortener;

import com.google.common.base.Strings;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.nycjv321.googleurlshortener.exceptions.InvalidUrlException;

import java.net.MalformedURLException;
import java.net.URL;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

/**
 * Created by Javier on 8/31/2014.
 */
public class GoogleURLShortenerTests {

    private GoogleURLShortener googleUrlShortener;

    @BeforeClass
    public void beforeClass() {
        googleUrlShortener = new GoogleURLShortener();
        String apiKey = TestPropertiesReader.getApiKey();
        assertFalse(Strings.isNullOrEmpty(apiKey));
        googleUrlShortener.setApiKey(apiKey);
    }

    @Test
    public void testShortenURL() throws MalformedURLException {
        assertEquals(googleUrlShortener.getShortenedURL("http://www.google.com/"), new URL("http://goo.gl/CRwF"));
    }

    @Test(expectedExceptions = InvalidUrlException.class)
    public void testInvalidShortenURL() {
        assertEquals(googleUrlShortener.getShortenedURL("http://goo.gl/CRwF"), null);
    }

    @Test
    public void testExpandURL() throws MalformedURLException {
        assertEquals(googleUrlShortener.getExpandedURL("http://goo.gl/fbsS"), new URL("http://www.google.com/"));
    }

    @Test(expectedExceptions = InvalidUrlException.class)
    public void tesInvalidExpandURL() {
        assertEquals(googleUrlShortener.getExpandedURL("http://www.google.com"), null);
    }
}
