package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author jlombardo
 */
public class MySqlDBStrategy implements DBStrategy {
    private Connection conn;
    
    @Override
    public void openConnection(String driverClass, String url, 
            String userName, String password) throws ClassNotFoundException, SQLException {
        
        Class.forName (driverClass);
        conn = DriverManager.getConnection(url,userName, password);
        
    }
    
    public void closeConnection() throws SQLException {
        conn.close();
    }
}
