package utilities.nycjv321.googleurlshortener;

import org.json.JSONObject;

import java.net.URL;
import java.util.Date;

/**
 * Created by Javier on 11/2/2014.
 */
public class GoogleURLShortenerRecord {

    private final Integer id;
    private final URL url;
    private final URL shortenedURL;
    private final Date date;

    public GoogleURLShortenerRecord(URL url, URL shortenedURL, Date date) {
        this(null, url, shortenedURL, date);
    }

    public GoogleURLShortenerRecord(Integer id, URL url, URL shortenedURL, Date date) {
        this.id = id;
        this.url = url;
        this.shortenedURL = shortenedURL;
        this.date = date;
    }

    @Override
    public String toString() {
        if (id == null) {
            return String.format("URL: %s, Shortened URL: %s, Date: %s", getUrl(), getShortenedURL(), getDate());
        } else {
            return String.format("ID: %s, URL: %s, Shortened URL: %s, Date: %s", getId(), getUrl(), getShortenedURL(), getDate());
        }
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("url", url);
        json.put("shortUrl", shortenedURL);
        json.put("date", date);
        return json;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof GoogleURLShortenerRecord) {
            GoogleURLShortenerRecord casted = (GoogleURLShortenerRecord) object;
            if (casted.getShortenedURL().equals(this.getShortenedURL()) && this.getUrl().equals(casted.getUrl())) {
                return true;
            }
        }
        return false;
    }

    public int getId() {
        return id;
    }

    public URL getUrl() {
        return url;
    }

    public URL getShortenedURL() {
        return shortenedURL;
    }

    public Date getDate() {
        return date;
    }
}
