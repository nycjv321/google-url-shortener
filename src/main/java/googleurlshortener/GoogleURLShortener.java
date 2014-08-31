/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package googleurlshortener;

import com.nycjv321.utilities.HttpUtilities;
import org.apache.http.entity.StringEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URI;

import static com.nycjv321.utilities.HttpUtilities.*;

/**
 *
 * @author javier
 */
public class GoogleURLShortener {
    private static final URI GOOGLE_URL_SHORTENER = createURI("https://www.googleapis.com/urlshortener/v1/url");
    private static final Logger logger = LogManager.getLogger(GoogleURLShortener.class
            .getName());

    private GoogleURLShortener() {}

    public static String getShortenedURL(String url) {
        String output = post(GOOGLE_URL_SHORTENER, getLongURLStringEntity(url), HttpUtilities.createHeader("Content-Type", "application/json"));
        JSONObject jsonobject = new JSONObject(output);
        return jsonobject.getString("id");
    }

    public static String getExpandedURL(String id) {
        URI uri = createURI(GOOGLE_URL_SHORTENER + "?shortUrl=" + id);
        String output = get(uri);

        JSONObject jsonobject = new JSONObject(output);
        return jsonobject.getString("longUrl");
    }

    private static StringEntity getLongURLStringEntity(String longURL) {
        try {
            return new StringEntity("{\"longUrl\": \""+longURL+"/\"}");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
