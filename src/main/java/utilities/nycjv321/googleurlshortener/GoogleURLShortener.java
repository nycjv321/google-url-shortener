package utilities.nycjv321.googleurlshortener;

import com.google.common.base.Strings;
import com.nycjv321.utilities.HttpUtilities;
import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;
import utilities.nycjv321.googleurlshortener.exceptions.ApiKeyNotDefinedException;
import utilities.nycjv321.googleurlshortener.exceptions.InvalidUrlException;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static com.nycjv321.utilities.HttpUtilities.post;

/**
 * Created by Javier L. Velasquez (nycjv321@gmail.com)
 */
public class GoogleURLShortener {
    private static final Header JSON_APPLICATION_HEADER = HttpUtilities.createBasicHeader("Content-Type", "application/json");
    private String apiKey;

    /**
     * Create a string entity representing the "longUrl" post parameter
     *
     * @param longURL a string that represents the uncompressed long url.
     * @return
     */
    private static StringEntity getLongURLStringEntity(String longURL) {
        return HttpUtilities.createStringEntity("{\"longUrl\": \"" + longURL + "/\"}");
    }

    private String getPostUrl() {
        String apiKey = getApiKey();
        if (Strings.isNullOrEmpty(apiKey)) {
            throw new ApiKeyNotDefinedException();
        }
        return String.format("%s%s", "https://www.googleapis.com/urlshortener/v1/url?key=", apiKey);
    }

    /**
     * Takes a URL and returns its goo.gl version.
     *
     * @param url a regular url
     * @return a goo.gl version of the given url
     */
    public URL getShortenedURL(String url) {
        if (url.startsWith("http://goo.gl")) {
            throw new InvalidUrlException(url);
        }
        String output;
        try {
            output = post(getPostUrl(),
                    getLongURLStringEntity(url),
                    JSON_APPLICATION_HEADER
            );

        JSONObject jsonobject = new JSONObject(output);
            return new URL(jsonobject.getString("id"));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Takes a shortened goo.gl link and returns its uncompressed url
     *
     * @param id a goo.gl url
     * @return an unshorted url
     */
    public URL getExpandedURL(String id) {
        if (!id.startsWith("http://goo.gl")) {
            throw new InvalidUrlException(id);
        }
        URI uri;
        try {
            uri = new URI(getPostUrl() + "&shortUrl=" + id);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        String output = HttpUtilities.get(uri, JSON_APPLICATION_HEADER);

        JSONObject jsonobject = new JSONObject(output);
        try {
            return new URL(jsonobject.getString("longUrl"));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

}
