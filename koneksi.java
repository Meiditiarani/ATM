package database;

/**
 *
 * @author dupi
 */
import java.sql.*;
import javax.swing.*;

public class Koneksi {
    private String dbuser = "root";
    private String dbpasswd = "";
    private Statement stmt = null;
    private Connection con = null;
    private ResultSet rs = null;

    public Koneksi(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            try {
                con = DriverManager.getConnection("jdbc:mysql://localhost/atm",dbuser, dbpasswd);
                stmt = con.createStatement();
                System.out.println("INFO - Koneksi Berhasil...");
            }catch (Exception e) {
                System.out.println("INFO - Koneksi Gagal...");
                JOptionPane.showMessageDialog(null,""+e.getMessage(),"Connection Error",JOptionPane.WARNING_MESSAGE);
            }
        }catch(Exception e) {
            System.out.println("INFO - JDBC Driver Error...");
            JOptionPane.showMessageDialog(null,""+e.getMessage(),"JDBC Driver Error",JOptionPane.WARNING_MESSAGE);
        }
    }

    public ResultSet getData(String SQLString){
        try {
            rs = stmt.executeQuery(SQLString);
            System.out.println("INFO - Query:["+ SQLString +"] Berhasil dijalankan...");
        }catch (SQLException e) {
            System.out.println("ERROR - Query:["+ SQLString +"] Gagal dijalankan...");
            JOptionPane.showMessageDialog(null,"Error :"+e.getMessage(),"Communication Error",JOptionPane.WARNING_MESSAGE);
        }
        return rs;
    }
        
    public void runQuery(String SQLString) {
        try {
            stmt.executeUpdate(SQLString);
            System.out.println("INFO - Query:["+ SQLString +"] Berhasil dijalankan...");
        }catch (SQLException e) {
            System.out.println("ERROR - Query:["+ SQLString +"] Gagal dijalankan...");
            JOptionPane.showMessageDialog(null,"error:"+e.getMessage(),"Communication Error",JOptionPane.WARNING_MESSAGE);
        }
    }
}
