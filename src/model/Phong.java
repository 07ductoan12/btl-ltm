/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author 07duc
 */
public class Phong implements Serializable {

    private int id;
    private ArrayList<NguoiChoi> dsng;
    private String password;
    private int userStatus1 = 0;
    private int userStatus2 = 0;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Phong(ArrayList<NguoiChoi> dsng, String password) {
        this.id = (int) (Math.random() * 10000);
        this.dsng = dsng;
        this.password = password;
    }

    public int getUserStatus1() {
        return userStatus1;
    }

    public void setUserStatus1(int userStatus1) {
        this.userStatus1 = userStatus1;
    }

    public int getUserStatus2() {
        return userStatus2;
    }

    public void setUserStatus2(int userStatus2) {
        this.userStatus2 = userStatus2;
    }

    public Phong(ArrayList<NguoiChoi> dsng) {
        this.id = (int) (Math.random() * 10000);
        this.dsng = dsng;
        this.password = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<NguoiChoi> getDsng() {
        return dsng;
    }

    public void setDsng(ArrayList<NguoiChoi> dsng) {
        this.dsng = dsng;
    }

}
