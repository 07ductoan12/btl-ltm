/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client.ControlListener;

import Client.View.GameView;
import Client.View.MainView;
import Client.View.PhongView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.text.View;
import model.Message;
import model.NguoiChoi;
import model.Phong;
import model.TranDau;

/**
 *
 * @author toan
 */
public class MainViewController extends Thread {

    private Timer timerTurn = new Timer("Timer");
    private Timer timerStart = new Timer("Timer");
    private int totalTime;
    private int timeStart;
    private MainView view;
    private Connection con;
    private ArrayList<NguoiChoi> listUser;
    private NguoiChoi currentUser;
    private GameView game;
    private TranDau tranDau;
    private Message response;
    private int turn;
    private ArrayList<Phong> listPhong;
    private PhongView phongView;
    private Phong phong;

    public MainViewController(MainView view, Connection con, NguoiChoi currentUser) {
        this.con = con;
        this.view = view;
        this.currentUser = currentUser;
        this.view.setVisible(true);
        view.setInZoom(false);
        listPhong = new ArrayList<>();
        view.challengeListener(new challenge(view, con));
        view.setUserNameView(currentUser.getFullName());
        view.btnTaoPhong(new taoPhong(view, con, this.currentUser));
        view.btnVaoPhong(new VaoPhong(view, con, this.currentUser));
        view.setScoreView(currentUser.getWin() + "/" + currentUser.getLose() + "/" + currentUser.getDraw());
    }

    private void FuncChallengeFrom() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        NguoiChoi competitor = (NguoiChoi) response.getObject();
        int choice = JOptionPane.showConfirmDialog(view, competitor.getUsername() + " muốn thách đấu với bạn!!", "Lời mời", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            con.SendRequest(new Message("Accept", competitor));
        } else {
            con.SendRequest(new Message("Decline", competitor));
        }
    }

    private void FuncStartGame() throws InterruptedException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        this.tranDau = (TranDau) response.getObject();
//                        JOptionPane.showMessageDialog(dashboard, "Đối thủ đã chấp nhận yêu cầu thách đấu của bạn!!!");
        view.setVisible(false);
        if (phongView != null && phongView.isVisible()) {
            phongView.dispose();
            phongView.setVisible(false);
        }
        game = new GameView(tranDau, this, view);
        game.addKeoButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tranDau.getListVanChoi().get(turn - 1).getNcvc1().getNguoiChoi().equals(currentUser)) {
                    tranDau.getListVanChoi().get(turn - 1).getNcvc1().setNuocChoi(1);
                } else {
                    tranDau.getListVanChoi().get(turn - 1).getNcvc2().setNuocChoi(1);
                }
                if (tranDau.getListVanChoi().get(turn - 1).getNcvc1().getNguoiChoi().equals(currentUser)) {
                    System.out.println(tranDau.getListVanChoi().get(turn - 1).getNcvc1().getNuocChoi());
                    Message request = new Message("Send turn result", tranDau.getListVanChoi().get(turn - 1).getNcvc1());
                    con.SendRequest(request);
                } else {
                    System.out.println(tranDau.getListVanChoi().get(turn - 1).getNcvc2().getNuocChoi());
                    Message request = new Message("Send turn result", tranDau.getListVanChoi().get(turn - 1).getNcvc2());
                    con.SendRequest(request);
                }
            }
        });
        game.addBuaButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tranDau.getListVanChoi().get(turn - 1).getNcvc1().getNguoiChoi().equals(currentUser)) {
                    tranDau.getListVanChoi().get(turn - 1).getNcvc1().setNuocChoi(2);
                } else {
                    tranDau.getListVanChoi().get(turn - 1).getNcvc2().setNuocChoi(2);
                }
                if (tranDau.getListVanChoi().get(turn - 1).getNcvc1().getNguoiChoi().equals(currentUser)) {
                    System.out.println(tranDau.getListVanChoi().get(turn - 1).getNcvc1().getNuocChoi());
                    Message request = new Message("Send turn result", tranDau.getListVanChoi().get(turn - 1).getNcvc1());
                    con.SendRequest(request);
                } else {
                    System.out.println(tranDau.getListVanChoi().get(turn - 1).getNcvc2().getNuocChoi());
                    Message request = new Message("Send turn result", tranDau.getListVanChoi().get(turn - 1).getNcvc2());
                    con.SendRequest(request);
                }
            }
        });
        game.addBaoButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tranDau.getListVanChoi().get(turn - 1).getNcvc1().getNguoiChoi().equals(currentUser)) {
                    tranDau.getListVanChoi().get(turn - 1).getNcvc1().setNuocChoi(3);
                } else {
                    tranDau.getListVanChoi().get(turn - 1).getNcvc2().setNuocChoi(3);
                }
                if (tranDau.getListVanChoi().get(turn - 1).getNcvc1().getNguoiChoi().equals(currentUser)) {
                    System.out.println(tranDau.getListVanChoi().get(turn - 1).getNcvc1().getNuocChoi());
                    Message request = new Message("Send turn result", tranDau.getListVanChoi().get(turn - 1).getNcvc1());
                    con.SendRequest(request);
                } else {
                    System.out.println(tranDau.getListVanChoi().get(turn - 1).getNcvc2().getNuocChoi());
                    Message request = new Message("Send turn result", tranDau.getListVanChoi().get(turn - 1).getNcvc2());
                    con.SendRequest(request);
                }
            }
        });
        game.addExitButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println("Out");
                sendCloseGame();
            }
        });
        game.setVisible(true);
        game.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        turn = 1;
        newTurn();
    }

    public void thoatPhong() {
        Message message = new Message("Thoat Phong", phong.getId());
        phong = null;
        System.out.println("send thoat phong");
        con.SendRequest(message);
    }

    private static class VaoPhong implements ActionListener {

        MainView view;
        Connection con;
        NguoiChoi currentUser;

        public VaoPhong(MainView view, Connection con, NguoiChoi nc) {
            this.view = view;
            this.con = con;
            this.currentUser = nc;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            Phong phong = view.getSelectPhong();
            if (phong != null) {
                System.out.println("send request vao phong");
                Message message = new Message("vao phong", currentUser, phong.getId());
                con.SendRequest(message);
            } else {
                System.out.println("phong null");
            }
        }
    }

    private void FuncDecline() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        String message = (String) response.getObject();
        view.Log(message);
    }

    private void FuncTaoPhong() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        System.out.println("Tao Phong func");
        this.phong = (Phong) response.getObject();
        System.out.println(phong.getId());
        view.setInZoom(true);
        this.phongView = new PhongView(phong, this);
        phongView.setVisible(true);
        int i = 0;
        phongView.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        System.out.println(phong.getDsng().size());
        phongView.addSanSangListener(new ActionListener() {
            int i = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                System.out.println("chon ss" + i++);
                phongView.setTextBtnSS();
                con.SendRequest(new Message("san sang", currentUser, phong.getId()));
            }
        });
        phongView.addStartGameListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                if (currentUser.getId() == phong.getDsng().get(0).getId()) {
                    if (phong.getUserStatus1() == 1 && phong.getUserStatus2() == 1) {
                        Message message = new Message("start game");
                        message.setMessageInt(phong.getId());
                        con.SendRequest(message);
                        phongView.dispose();
                        System.out.println("go game");
                    } else {
                        String str = "";
                        if (phong.getUserStatus1() != 1) {
                            str += "nguoi choi 1 chua ss";
                        }
                        if (phong.getUserStatus1() != 2) {
                            str += "nguoi choi 1 chua ss";
                        }
                        phongView.log(str);
                    }
                }
            }
        });
        phongView.addExitListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                if (phongView.xacNhan()) {
                    thoatPhong();
                    view.setInZoom(false);
                    phongView.dispose();
                }
            }
        });
    }

    private static class challenge implements ActionListener {

        MainView view;
        Connection con;

        public challenge(MainView view, Connection con) {
            this.view = view;
            this.con = con;
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            NguoiChoi nc = view.getSelectUser();
            System.out.println(nc.getId());
            if (nc != null) {
                Message message = new Message("Challenge to", nc);
                con.SendRequest(message);
            } else {
                System.out.println("nc is null");
            }
        }
    }

    private static class taoPhong implements ActionListener {

        MainView view;
        Connection con;
        NguoiChoi currentUser;

        public taoPhong(MainView view, Connection con, NguoiChoi nc) {
            this.view = view;
            this.con = con;
            this.currentUser = nc;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            if (!view.getInZomm()) {
                System.out.println("Tao Phong btn");
                Message message = new Message("Tao phong");
                con.SendRequest(message);
                System.out.println("Tao Phong send");
            } else {
                view.Log("ban dang o trong phong");
            }
        }
    }

    public void newTurn() {
        try {
            timeStart = 3;
            game.setTimeLabelStatus(false);
            game.setTurnLabel(turn);
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (timeStart > 0) {
                        game.setTimeStartLabelStatus(true);
                        game.setTimeStartLabel(timeStart);
                        timeStart = timeStart - 1;
                    } else if (timeStart == 0) {
                        timeStart = timeStart - 1;
                        game.setTimeStartLabelStatus(false);
                        setTimeCounter(15);
                        timerStart.cancel();
                    }
                }
            };
            long delay = 1000L;
            timerTurn = new Timer(turn + "");
            timerStart.schedule(timerTask, 0, delay);
        } catch (Exception e) {

        }
    }

    public void sendCloseGame() {
        Message mess = new Message("Close game", this.tranDau.getId());
        con.SendRequest(mess);
    }

    public void setTimeCounter(int total) {
        try {
            game.setResultLabelStatus(false);
            this.totalTime = total;
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (totalTime > 0) {
                        game.setTimeLabelStatus(true);
                        game.setTimeLabel(totalTime);
                        totalTime = totalTime - 1;
                    } else if (totalTime == 0) {
                        totalTime = totalTime - 1;
                        game.setTimeLabelStatus(false);
                        game.setResultLabelStatus(true);
                        if (tranDau.getListVanChoi().get(turn - 1).getNcvc1().getNguoiChoi().equals(currentUser)) {
                            System.out.println(tranDau.getListVanChoi().get(turn - 1).getNcvc1().getNuocChoi());
                            Message request = new Message("Get result turn", tranDau.getListVanChoi().get(turn - 1).getNcvc1());
                            con.SendRequest(request);
                        } else {
                            System.out.println(tranDau.getListVanChoi().get(turn - 1).getNcvc2().getNuocChoi());
                            Message request = new Message("Get result turn", tranDau.getListVanChoi().get(turn - 1).getNcvc2());
                            con.SendRequest(request);
                        }

                        turn = turn + 1;
                        if (turn < 4) {
                            newTurn();
                        } else {
                            turn = turn + 1;
                            con.SendRequest(new Message("Get result game", tranDau));
                        }
                        timerTurn.cancel();
                    }
                }
            };
            long delay = 1000L;
            timerStart = new Timer(turn + "");
            timerTurn.schedule(timerTask, 0, delay);
        } catch (Exception e) {

        }
    }

    public void closeGame() {
        view.setVisible(true);
        if (phongView != null) {
            phongView.setVisible(true);
        }
        game.dispose();
    }

    @Override
    public void run() {
        while (true) {
            this.response = (Message) con.receiverResponse();
            try {
                switch (response.getMessage()) {
                    case "List user online": {
                        this.listUser = (ArrayList<NguoiChoi>) response.getObject();
                        view.updateListUser(listUser, currentUser.getId());
                        break;
                    }
                    case "Phong duoc tao": {
                        FuncTaoPhong();
                        break;
                    }
                    case "Challenge from": {
                        FuncChallengeFrom();
                        break;
                    }
                    case "Start game": {
                        FuncStartGame();
                        break;
                    }
                    case "Decline": {
                        FuncDecline();
                        break;
                    }
                    case "Result turn": {
                        String rs = (String) response.getObject();
                        System.out.println(rs);
                        game.setResultLabel(rs);
                        break;
                    }
                    case "Result game": {
                        String rs = (String) response.getObject();
                        System.out.println(rs);
                        closeGame();
                        break;
                    }
                    case "Close game": {
                        System.out.println("close game");
                        closeGame();
                        break;
                    }
                    case "List phong": {
                        ArrayList<Phong> p = (ArrayList<Phong>) response.getObject();
                        view.updateListPhong(p);
                        break;
                    }
                    case "update status": {
                        int status = (Integer) response.getMessageInt();
                        int id = (Integer) response.getObject();
                        if (phong.getDsng().get(0).getId() == id) {
                            this.phong.setUserStatus1(status);
                        } else {
                            this.phong.setUserStatus2(status);
                        }
                        System.out.println("set tatus");
                        phongView.updateListUserStatus(this.phong);
                        break;
                    }
                    case "update phong": {
                        ArrayList<NguoiChoi> list = (ArrayList<NguoiChoi>) response.getObject();
                        phong.setDsng(list);
                        System.out.println("update phong " + list.size());
                        phongView.updateListUser(list);
                        break;
                    }
                    case "add phong": {
                        Phong p = (Phong) response.getObject();
                        System.out.println("add phong " + p.getId());
                        this.listPhong.add(p);
                        view.updateListPhong(listPhong);
                        break;
                    }
                    case "remove phong": {
                        System.out.println("in remove phong");
                        int idPhong = (Integer) response.getMessageInt();
                        System.out.println("xoa phong " + idPhong);
                        for (Phong p : listPhong) {
                            if (p.getId() == idPhong) {
                                listPhong.remove(p);
                                view.updateListPhong(listPhong);
                                break;
                            } else {
                                System.out.println("ko co phong");
                            }
                        }
                        break;
                    }
                    case "remove user":{
                        int idUser = (Integer) response.getMessageInt();
                        ArrayList<NguoiChoi> listUser = new ArrayList<>();
                        listUser = phong.getDsng();
                        int i = 0;
                        for(;i<listUser.size();i++){
                            if(listUser.get(i).getId() == idUser) break;
                        }
                        listUser.remove(i);
                        phong.setDsng(listUser);
                        phongView.updateListUser(listUser);
                        break;
                    }
                    case "Fix phong":
                    {
                        Phong p = (Phong) response.getObject();
                        int i =0;
                        for(;i<listPhong.size();i++){
                            if(p.getId() == listPhong.get(i).getId()){
                                break;
                            }
                        }
                        listPhong.set(i, p);
                        view.updateListPhong(listPhong);
                        break;
                    }
                    default: {
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
