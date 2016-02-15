package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

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
    
    @Override
    public void closeConnection() throws SQLException {
        conn.close();
    }
    
    /**
     * Make sure you open and close connection when using this method
     * Future optimization may include change the return type as an array
     * @param tableName
     * @param maxRecords - limits records found to first maxRecords or if maxRecords is
     * zero (0) then no limit
     * @return 
     */
    
    @Override
    public List<Map<String,Object>> findAllRecords(String tableName,
            int maxRecords) throws SQLException{
        
        String sql;
        if(maxRecords == 1) {
            sql = "select * from " + tableName;
        }else { 
            sql = "select * from " + tableName + " limit " + maxRecords;
        }
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        ResultSetMetaData rsmd= rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        List<Map<String,Object>> records = new ArrayList<>();
        
        while(rs.next()) {
            Map<String, Object> record = new HashMap<>();
            for(int colNo = 1; colNo <= columnCount+1; colNo++){
                Object colData = rs.getObject(colNo);
                String colName = rsmd.getColumnName(colNo);
                record.put(colName, colData);
            }
            records.add(record);
        }
        return records;
        
    }
    
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        DBStrategy db = new MySqlDBStrategy();
        db.openConnection("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/book", "root", "admin");
        List<Map<String,Object>> rawData = db.findAllRecords("author", 0);
        db.closeConnection();
        
        System.out.println(rawData);
    }

    
}
