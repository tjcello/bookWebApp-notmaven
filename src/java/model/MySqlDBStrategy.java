package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 *
 * @author jlombardo
 */
public class MySqlDBStrategy implements DBStrategy{
    
    private Connection conn;
    
    
    public MySqlDBStrategy(String driverClass, String url, String username,String password) throws ClassNotFoundException, SQLException{
        Class.forName(driverClass);
        conn = DriverManager.getConnection(url,username,password);
        
    }

    private MySqlDBStrategy() {
       
    }
    @Override
    public void openConnection(String driverClass, String url, String username,String password) throws ClassNotFoundException, SQLException{
        Class.forName(driverClass);
        conn = DriverManager.getConnection(url,username,password);
        
    }
    @Override
    public void closeConnection() throws SQLException{
      conn.close();  
    
    }
    /**Make sure you open and close connection when using this method
     * Future optimization may include change the return type an array
     * @param tableName
     * @return 
     */
    @Override
    public List<Map<String,Object>> findAllRecords(String tableName, int maxRecords) throws SQLException{
        String sql;
        if(maxRecords  < 1){
            sql = "select * from " + tableName;
        }else{
            sql = ("select * from " + tableName + " limit " + maxRecords);
        }
    
        
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        List<Map<String, Object>> records = new ArrayList<>();
        
        while(rs.next()){
            Map<String, Object> record = new HashMap<>();
            for(int colNo = 1;colNo <= columnCount;colNo++){
                Object colData = rs.getObject(colNo);
                String colName = rsmd.getColumnName(colNo);
                record.put(colName, colData);
            }
            records.add(record);
        }
        return records;
    }
     @Override
    public int deleteRecordbyPrimaryKey(String tableName, String primaryKey, Object primaryKeyValue) throws SQLException {
        int recordsDeleted = 0;
        PreparedStatement prepStmt = null;
        String sqlStatement = null;

        if (primaryKeyValue instanceof String) {
            sqlStatement = "= '" + primaryKeyValue + "'";
        } else {
            sqlStatement = "=" + primaryKeyValue;
        }
        final String sqlDeleteStmt = "DELETE FROM " + tableName + " WHERE " + primaryKey + sqlStatement;

        prepStmt = conn.prepareStatement(sqlDeleteStmt);
        recordsDeleted = prepStmt.executeUpdate(sqlDeleteStmt);

        return recordsDeleted;
    }
    
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        DBStrategy db = new MySqlDBStrategy();
        db.openConnection("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/book", "root", "admin");
         List<Map<String,Object>> rawData = db.findAllRecords("author",0);
//         db.deleteRecordbyPrimaryKey("author","author_id",3);
//         List<Map<String,Object>> rawData2 = db.findAllRecordsForTable("author",0);
         db.closeConnection();
         
         System.out.println(rawData);
    }
    
    
    
}