/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.DAO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author toan
 */
public class DAOServer {
    Connection con;
    private final String dbServer = "localhost";
    private final String dbName = "oantuti";
    private final String dbUserName = "root";
    private final String dbPasswd = "123456";
    
    public DAOServer(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            this.con = DriverManager.getConnection("jdbc:mysql://" + this.dbServer +":3306/" + dbName +"?useSSL=false"
                    ,this.dbUserName,this.dbPasswd);
//            ServerView view = new ServerView();
//            view.log("ket noi database thanh cong");
        } catch (ClassNotFoundException ex){
            ex.printStackTrace();
        } catch(SQLException ex){
            ex.printStackTrace();
        }
    }
}
