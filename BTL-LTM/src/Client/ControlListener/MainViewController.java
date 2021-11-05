/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client.ControlListener;

import Client.View.GameView;
import Client.View.MainView;
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
    private boolean ingame;
    private GameView game;
    private TranDau tranDau;
    private Message response;
    private int turn;

    public MainViewController(MainView view, Connection con, NguoiChoi currentUser) {
        this.con = con;
        this.view = view;
        this.currentUser = currentUser;
        ingame = false;
        this.view.setVisible(true);
        view.challengeListener(new challenge(view, con));
        view.setUserNameView(currentUser.getFullName());
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

//        tranDau = (TranDau) response.getObject();
//        this.game = new GameView(tranDau, view);
//        GameViewController gameController = new GameViewController(con, game, tranDau, currentUser, this.view);
//        game.setVisible(true);
//        ingame = true;
//
//        view.setVisible(false);
//        turn = 1;
//        newTurn();
        this.tranDau = (TranDau) response.getObject();
//                        JOptionPane.showMessageDialog(dashboard, "Đối thủ đã chấp nhận yêu cầu thách đấu của bạn!!!");
        view.setVisible(false);
        game = new GameView(tranDau, this,view);
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

    public void setIngame(boolean ingame) {
        this.ingame = ingame;
    }

    private void FuncDecline() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        String message = (String) response.getObject();
        view.Log(message);
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

    public Message getMessage() {
        return this.response;
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
        game.dispose();
        ingame = false;
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
                        game.dispose();
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
