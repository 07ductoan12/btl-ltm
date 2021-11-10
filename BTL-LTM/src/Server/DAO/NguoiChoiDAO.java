/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.NguoiChoi;

public class NguoiChoiDAO extends DAOServer {

    private final String checkLoginSQL = "SELECT * FROM nguoichoi WHERE username = ? AND password = ?";
    private final String getUserSQL = "SELECT * FROM nguoichoi WHERE id = ?";
    private final String updateStatusSQL = "UPDATE nguoichoi SET userStatus = ?, win = ?, draw = ?, lose = ? WHERE id = ?";
    private final String updateStatusUser = "UPDATE nguoichoi SET userStatus = ? WHERE id = ?";
    private final String getListUserSQL = "SELECT * FROM nguoichoi WHERE userStatus >= ?";
    private final String registerSQL = "INSERT INTO nguoichoi VALUES (?,?,?,?,?,?,?,?)";
    public NguoiChoiDAO() {
        super();
    }

//    public NguoiChoi userReg(NguoiChoi user){
//        try{
//            PreparedStatement preparedStatement = con.prepareStatement(registerSQL);
//            preparedStatement.setString(1,"8");
//            preparedStatement.setString(2, user.getUsername());
//            preparedStatement.setString(3, user.getPassword());
//            preparedStatement.setString(4, null);
//            preparedStatement.setString(5, "0");
//            preparedStatement.setString(6, "0");
//            preparedStatement.setString(7, "0");
//            preparedStatement.setString(8, "0");
//            int res = preparedStatement.executeUpdate();
//        } catch(SQLException ex){
//            
//        }
//        return null;
//    }
    
    public NguoiChoi checkLogin(NguoiChoi user) {
        try {
            PreparedStatement preparedStatement = con.prepareStatement(checkLoginSQL);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            ResultSet res = preparedStatement.executeQuery();
            while (res.next()) {
                NguoiChoi rs = new NguoiChoi(res.getInt("id"), res.getString("username"), res.getString("password"), res.getString("fullName"));
                return rs;
            }
        } catch (SQLException ex) {
            Logger.getLogger(NguoiChoiDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public NguoiChoi getUserById(int id) {
        try {
            PreparedStatement preparedStatement = con.prepareStatement(getUserSQL);
            preparedStatement.setInt(1, id);
            ResultSet res = preparedStatement.executeQuery();
            while (res.next()) {
                NguoiChoi rs = new NguoiChoi(res.getInt("id"), res.getString("username"), res.getString("password"), res.getString("fullName"));
                return rs;
            }
        } catch (SQLException ex) {
            Logger.getLogger(NguoiChoiDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public int getUserStatus(int id) {
        try {
            PreparedStatement preparedStatement = con.prepareStatement(getUserSQL);
            preparedStatement.setInt(1, id);
            ResultSet res = preparedStatement.executeQuery();
            return res.getInt("userStatus");
        } catch (SQLException ex) {
            Logger.getLogger(NguoiChoiDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return -1;
    }

    public boolean updateUserStatus(int id,int status) {
        try {
            PreparedStatement preparedStatement = con.prepareStatement(updateStatusUser);
            preparedStatement.setInt(1, status);
            preparedStatement.setInt(2, id);
            boolean res = preparedStatement.execute();
            return res;
        } catch (SQLException ex) {
            Logger.getLogger(NguoiChoiDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    public boolean updateStatus(NguoiChoi user) {
        try {
            PreparedStatement preparedStatement = con.prepareStatement(updateStatusSQL);
            preparedStatement.setInt(1, user.getUserStatus());
            preparedStatement.setInt(2, user.getWin());
            preparedStatement.setInt(3, user.getDraw());
            preparedStatement.setInt(4, user.getLose());
            preparedStatement.setInt(5, user.getId());
            boolean res = preparedStatement.execute();
            return res;
        } catch (SQLException ex) {
            Logger.getLogger(NguoiChoiDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public ArrayList<NguoiChoi> getListUser(int status) {
        ArrayList<NguoiChoi> listUser = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = con.prepareStatement(getListUserSQL);
            preparedStatement.setInt(1, status);
            ResultSet res = preparedStatement.executeQuery();
            while (res.next()) {
                NguoiChoi rs = new NguoiChoi(res.getInt("id"), res.getString("username"), res.getString("fullname"), res.getInt("userStatus"), res.getInt("win"), res.getInt("draw"), res.getInt("lose"));
                listUser.add(rs);
            }
            return listUser;
        } catch (SQLException ex) {
            Logger.getLogger(NguoiChoiDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
