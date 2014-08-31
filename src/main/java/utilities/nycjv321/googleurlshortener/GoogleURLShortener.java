package utilities.nycjv321.googleurlshortener;

import com.nycjv321.utilities.HttpUtilities;
import org.apache.http.entity.StringEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.net.URI;

import static com.nycjv321.utilities.HttpUtilities.*;

/**
 * Created by Javier L. Velasquez (nycjv321@gmail.com)
 */
public class GoogleURLShortener {
    private static final URI GOOGLE_URL_SHORTENER = createURI("https://www.googleapis.com/urlshortener/v1/url");
    private static final Logger logger = LogManager.getLogger(GoogleURLShortener.class
            .getName());

    /**
     * This class is meant to not be instantiated so its constructor is marked as private
     */
    private GoogleURLShortener() {}

    /**
     * Takes a URL and returns its goo.gl version.
     * @param url a regular url
     * @return a goo.gl version of the given url
     */
    public static String getShortenedURL(String url) {
        if (url.startsWith("http://goo.gl")) {
            logger.warn("Invalid URL provided!");
            return "";
        }
        String output = post(GOOGLE_URL_SHORTENER,
                getLongURLStringEntity(url),
                HttpUtilities.createHeader("Content-Type", "application/json")
        );
        JSONObject jsonobject = new JSONObject(output);
        return jsonobject.getString("id");
    }

    /**
     * Takes a shortened goo.gl link and returns its uncompressed url
     * @param id a goo.gl url
     * @return an unshorted url
     */
    public static String getExpandedURL(String id) {
        if (!id.startsWith("http://goo.gl")) {
            logger.warn("Invalid URL provided!");
            return "";
        }
        URI uri = createURI(GOOGLE_URL_SHORTENER + "?shortUrl=" + id);
        String output = get(uri);

        JSONObject jsonobject = new JSONObject(output);
        return jsonobject.getString("longUrl");
    }

    /**
     * Create a string entity representing the "longUrl" post parameter
     * @param longURL a string that represents the uncompressed long url.
     * @return
     */
    private static StringEntity getLongURLStringEntity(String longURL) {
        return HttpUtilities.createStringEntity("{\"longUrl\": \"" + longURL + "/\"}");
    }

}
