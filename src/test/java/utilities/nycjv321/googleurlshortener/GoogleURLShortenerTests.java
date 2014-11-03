package utilities.nycjv321.googleurlshortener;

import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.testng.Assert.assertEquals;

/**
 * Created by Javier on 8/31/2014.
 */
public class GoogleURLShortenerTests {

    @Test
    public void testShortenURL() throws MalformedURLException {
        assertEquals(GoogleURLShortener.getShortenedURL("http://www.google.com/"), new URL("http://goo.gl/CRwF"));
    }

    @Test
    public void testInvalidShortenURL() {
        assertEquals(GoogleURLShortener.getShortenedURL("http://goo.gl/CRwF"), null);
    }

    @Test
    public void testExpandURL() throws MalformedURLException {
        assertEquals(GoogleURLShortener.getExpandedURL("http://goo.gl/fbsS"), new URL("http://www.google.com/"));
    }

    @Test
    public void tesInvalidExpandURL() {
        assertEquals(GoogleURLShortener.getExpandedURL("http://www.google.com"), null);
    }
}
