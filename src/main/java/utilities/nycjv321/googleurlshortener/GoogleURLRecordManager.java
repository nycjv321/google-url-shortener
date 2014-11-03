package utilities.nycjv321.googleurlshortener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * This class represents a Google URL
 * Created by Javier on 11/2/2014.
 */
public class GoogleURLRecordManager {
    private Connection connection;
    private static final Logger logger = LogManager.getLogger(GoogleURLRecordManager.class);

    public GoogleURLRecordManager() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/records.db");
        } catch (ClassNotFoundException | SQLException e) {
            logger.error(e);
        }
        create();
    }

    public Set<GoogleURLShortenerRecord> getRecords() {
        Set<GoogleURLShortenerRecord> records = new HashSet<>();
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM GOOGLE_URLS;");
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String url = resultSet.getString("URL");
                String shortenedUrl = resultSet.getString("SHORTENED_URL");
                Date timestamp = new java.util.Date(resultSet.getLong("TIMESTAMP") * 1000);

                GoogleURLShortenerRecord record;
                try {
                    record = new GoogleURLShortenerRecord(id, new URL(url), new URL(shortenedUrl), timestamp);
                } catch (MalformedURLException e) {
                    continue;
                }

                records.add(record);
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch(SQLException e) {
                logger.error(e);
            }
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return records;
    }

    public GoogleURLShortenerRecord insert(URL url, URL shortenedURL) {
        long time = Calendar.getInstance().getTime().getTime();
        long unixTime = time / 1000;
        Statement statement = null;
        try {
            statement = connection.createStatement();
            String sql = "INSERT INTO GOOGLE_URLS (ID, URL, SHORTENED_URL, TIMESTAMP) " +
                    String.format("VALUES (NULL, '%s', '%s', '%s' );", url.toString(), shortenedURL.toString(), unixTime);
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }
        }
        return new GoogleURLShortenerRecord(url, shortenedURL, Calendar.getInstance().getTime());
    }

    private void create() {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS GOOGLE_URLS" +
                    "(" +
                    "ID             INT PRIMARY KEY," +
                    " URL            TEXT    NOT NULL, " +
                    " SHORTENED_URL  TEXT    NOT NULL," +
                    " TIMESTAMP      REAL    NOT NULL, " +
                    " UNIQUE(URL, SHORTENED_URL)" +
                    " ON CONFLICT REPLACE)";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }
        }

    }

}
