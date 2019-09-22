/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wgugzp1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author jakes
 */
public class Schedule extends Application {
    
    private static volatile Connection conn;
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
        
        Connection db = getDbInstance();
        Statement s = db.createStatement();
        //s.executeUpdate("delete from country;");
        ResultSet r = s.executeQuery("select * from user");
        ResultSetMetaData rm = r.getMetaData();
        int columnsNumber = rm.getColumnCount();
        while (r.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) System.out.print(", ");
                String val = r.getString(i);
                System.out.print(val + " " + rm.getColumnName(i));
            }
            System.out.println();
        }
    }
    
    @Override
    public void stop() throws SQLException{
        System.out.println("Stopping");
        Connection db = getDbInstance();
        db.close();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public static Connection getDbInstance() throws SQLException {
        if (conn == null) {
            synchronized(Connection.class) {
                if (conn == null) {
                    conn = DriverManager.getConnection(
                    "jdbc:mysql://52.206.157.109/U05Zr6",
                    "U05Zr6",
                    "53688650566"
                    );
                }
            }
        }
        return conn;
    }
    
}
