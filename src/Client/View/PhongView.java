/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.View;

import Client.ControlListener.MainViewController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.NguoiChoi;
import model.Phong;

/**
 *
 * @author 07duc
 */
public class PhongView extends javax.swing.JFrame implements ActionListener {

    /**
     * Creates new form PhongView
     */
    private Phong phong;
    DefaultTableModel model1;
    MainViewController controller;
    private boolean ss;

    public PhongView(Phong phong, MainViewController controller) {
        initComponents();
        this.phong = phong;
        ss = false;
        model1 = (DefaultTableModel) tblUser.getModel();
        this.controller = controller;
        updateListUser(phong.getDsng());
        setIdPhong();
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                //super.windowClosing(e); //To change body of generated methods, choose Tools | Templates.
                xacNhan();
            }

        });

    }

    public void addSanSangListener(ActionListener ss) {
        BtnReady.addActionListener(ss);
    }

    public void addStartGameListener(ActionListener st) {
        btnStartGame.addActionListener(st);
    }
    public void addExitListener(ActionListener ex){
        btnExit.addActionListener(ex);
    }

    public void setTextBtnSS() {
        if (!ss) {
            BtnReady.setText("chưa sẵn sàng");

        } else {
            BtnReady.setText("sẵn sàng");
        }
        ss = !ss;
    }

    public boolean xacNhan() {
        int choice = JOptionPane.showConfirmDialog(this, "thoat phong", "Xac nhan", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            return true;
        }
        return false;
    }

    public void setIdPhong() {
        textIDPhong.setText(String.valueOf(phong.getId()));
    }

    public void updateListUser(ArrayList<NguoiChoi> list) {
        model1.setRowCount(0);
        System.out.println("update size phong " +list.size());
        this.phong.setDsng(list);
        for (int i = 0; i < list.size(); i++) {
            String str = "";
            if (i == 0) {
                if (phong.getUserStatus1() == 1) {
                    str = "san sang";
                } else {
                    str = "chua san sang";
                }
            } else {
                if (phong.getUserStatus2() == 1) {
                    str = "san sang";
                } else {
                    str = "chua san sang";
                }
            }
            model1.addRow(new Object[]{
                phong.getDsng().get(i).getFullName(), str
            });
        }
    }

    public void log(String mess){
        JOptionPane.showMessageDialog(rootPane, mess);
    }
    
    public void updateListUserStatus(Phong phong) {
        model1.setRowCount(0);
        this.phong = phong;
        for (int i = 0; i < this.phong.getDsng().size(); i++) {
            String str = "";
            if (i == 0) {
                if (phong.getUserStatus1() == 1) {
                    str = "san sang";
                } else {
                    str = "chua san sang";
                }
            } else {
                if (phong.getUserStatus2() == 1) {
                    str = "san sang";
                } else {
                    str = "chua san sang";
                }
            }
            model1.addRow(new Object[]{
                phong.getDsng().get(i).getFullName(), str
            });
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblUser = new javax.swing.JTable();
        BtnReady = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        textIDPhong = new javax.swing.JLabel();
        btnStartGame = new javax.swing.JButton();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tblUser.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Ten nguoi choi", "San Sang"
            }
        ));
        jScrollPane1.setViewportView(tblUser);

        BtnReady.setText("Sẵn sàng");

        btnExit.setText("Thoát");

        jLabel1.setText("Phong:");

        textIDPhong.setText("jLabel2");

        btnStartGame.setText("Bắt đầu");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(textIDPhong)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnExit)
                .addGap(35, 35, 35))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(BtnReady)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnStartGame)
                .addGap(63, 63, 63))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnExit)
                    .addComponent(jLabel1)
                    .addComponent(textIDPhong))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BtnReady)
                    .addComponent(btnStartGame))
                .addContainerGap(51, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(PhongView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(PhongView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(PhongView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(PhongView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new PhongView().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnReady;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnStartGame;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblUser;
    private javax.swing.JLabel textIDPhong;
    // End of variables declaration//GEN-END:variables

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
