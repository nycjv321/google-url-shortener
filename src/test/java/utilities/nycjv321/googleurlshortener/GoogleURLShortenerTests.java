package utilities.nycjv321.googleurlshortener;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created by Javier on 8/31/2014.
 */
public class GoogleURLShortenerTests {

    @Test
    public void testShortenURL() {
        assertEquals(GoogleURLShortener.getShortenedURL("http://www.google.com/"), "http://goo.gl/CRwF");
    }

    @Test
    public void testExpandURL() {
        assertEquals(GoogleURLShortener.getExpandedURL("http://goo.gl/fbsS"), "http://www.google.com/");
    }

}
