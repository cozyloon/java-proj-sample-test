package lk.ijse.dep.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static DBConnection dbConnection;
    private Connection connection;

    private DBConnection()  {
        try {
            Properties properties = new Properties();
            InputStream resourceAsStream = this.getClass().getResourceAsStream("/database.properties");
            properties.load(resourceAsStream);

            String user = properties.getProperty("db.user");
            String password = properties.getProperty("db.password");
            String ip = properties.getProperty("db.ip");
            String port = properties.getProperty("db.port");
            String database = properties.getProperty("db.database");

            String url = "jdbc:mysql://" + ip + ":" + port + "/" + database + "";

             connection = DriverManager.getConnection(url, user, password);

        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
    }

    public static DBConnection getInstance() throws IOException {
        return (dbConnection == null) ? (dbConnection = new DBConnection()) : dbConnection;
    }

    public Connection getConnection() {
        return connection;
    }

}
