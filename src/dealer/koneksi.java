/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dealer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
/**
 *
 * @author QCTFW
 */
public class koneksi {
    private static Connection conn;
    public static Connection getKoneksi(){
        try {
                DriverManager.registerDriver(new com.mysql.jdbc.Driver());
                conn = DriverManager.getConnection("jdbc:mysql://localhost/db_belajar_javaparkir", "root", "");
            }
            catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Gagal Koneksi\nError: " + e);
                e.printStackTrace();
            }
        return conn;
    }
}
