/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dealer;

import com.sun.glass.events.KeyEvent;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import java.io.File;
import java.util.HashMap;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
/**
 *
 * @author QCTFW
 */
public class CrudBayar extends javax.swing.JFrame {
    public Statement st;
    public ResultSet rs;
    public Connection cn = koneksi.getKoneksi();
    public PreparedStatement ps;
    JasperPrint jasperprint;
    JasperViewer jasperviewer;
    JasperDesign jasperdesign;
    JasperReport jasperreport;
    CrudTransaksi transaksi = new CrudTransaksi();
    File file = new File("src/dealer/strukpembayaran.jrxml");
    private long bayar, kembalian;
    ImageIcon pensil = new ImageIcon(getClass().getResource("/dealer/ic_pencil.png"));
    ImageIcon ceklis = new ImageIcon(getClass().getResource("/dealer/ic_checkmark.png"));
    /**
     * Creates new form CrudBayar
     */
    public CrudBayar() {
        initComponents();
        lblkodetransaksi.setText("Kode Transaksi: " + transaksi.kodetransaksi);
        txttotal.setText(String.valueOf(transaksi.numberToMoney(transaksi.total)));
    }
    
    public void input(){
        if (!txtpembeli.getText().equals("") && !txtbayar.getText().equals("")) {
            if (bayar >= transaksi.total) {
                int jawab = JOptionPane.showConfirmDialog(null, "Yakin ingin membayar?\nBayar: " + transaksi.numberToMoney(bayar) + "\nKembalian: " + transaksi.numberToMoney(kembalian), "Konfirmasi", JOptionPane.YES_NO_OPTION);
                System.out.println(String.valueOf(jawab));
                if (jawab == 0) {
                   try {
                       String sql = "INSERT INTO tb_transaksi VALUES(?,?,?,?,NULL)";
                       ps = cn.prepareStatement(sql);
                       ps.setString(1, transaksi.kodetransaksi);
                       ps.setString(2, txtpembeli.getText());
                       ps.setString(3, String.valueOf(transaksi.total));
                       ps.setString(4, String.valueOf(kembalian));
                       ps.executeUpdate();
                       try {
                            st = cn.createStatement();
                            rs = st.executeQuery("SELECT kd_barang, stok FROM tb_order WHERE kd_transaksi = '" + transaksi.kodetransaksi + "'");
                            while (rs.next()) {
                                sql = "UPDATE tb_stok SET stok = stok - " + rs.getString("stok") + " WHERE kd_barang = " + rs.getString("kd_barang");
                                System.out.println(sql);
                                st = cn.createStatement();
                                st.executeUpdate(sql);
                            }
                            if (chkstruk.isSelected()) {
                               cetakStruk();
                           }
                            JOptionPane.showMessageDialog(null, "Transaksi Berhasil!");
                            this.dispose();
                       }
                       catch (SQLException e) {
                           JOptionPane.showMessageDialog(null, "Proses Update Stok Gagal.\nError: " + e, "Gagal", JOptionPane.ERROR_MESSAGE);
                       }
                       
                   }
                   catch (SQLException e) {
                       JOptionPane.showMessageDialog(null, "Proses pembayaran gagal. Error: " + e, "Gagal", JOptionPane.INFORMATION_MESSAGE);
                   }
               }
            }
            else {
                JOptionPane.showMessageDialog(null, "Field bayar dan kembalian tidak valid", "Validasi", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "Harap isi semua field", "Validasi", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    
    
    public static Boolean cekAngka(String string) {
        if (!string.equals("")) {
            try {
                long angka = Long.parseUnsignedLong(string);
            }
            catch (NumberFormatException nfe) {
                nfe.printStackTrace();
                return false;
            }
        }
        else {
            return false;
        }
        return true;
    }
    
    public void updateKembalian(String string){
        bayar = Long.parseLong(string);
        kembalian = bayar - transaksi.total;
        txtkembalian.setText(transaksi.numberToMoney(kembalian));
        txtbayar.setText(transaksi.numberToMoney(bayar));
        txtbayar.setEditable(false);
        btnedit.setIcon(pensil);
    }
    
    protected void cekEdit(char c){
        boolean bool;
        System.out.println(c);
        bool = c == 'p'; // p = by entering, b = by button
        if (btnedit.getName().equals("edit")) {
            txtbayar.setEditable(true);
            txtbayar.setText(String.valueOf(bayar));
            txtbayar.requestFocus();
            btnedit.setName("done");
            btnedit.setIcon(ceklis);
        }
        else if (btnedit.getName().equals("done")) {
            String duit = "";
            if (bool) {
                duit = txtbayar.getText();
            }
            else {
                duit = txtbayar.getText().replace("Rp. ", "").replace(".", "");
            }
            if ( cekAngka(duit) ) {
                if (duit.length() <= 12) {
                    updateKembalian(duit);
                    txtpembeli.requestFocus();
                    btnedit.setName("edit");
                }
                else {
                    JOptionPane.showMessageDialog(null, "Panjang maksimum adalah 12 karakter", "Validasi", JOptionPane.INFORMATION_MESSAGE);
                    txtbayar.setText(String.valueOf(bayar));
                    txtbayar.requestFocus();
                }
            }
            else {
                JOptionPane.showMessageDialog(null, "Penulisan Uang Tidak Valid\nNote: Panjang maksimum adalah 12 karakter", "Validasi", JOptionPane.INFORMATION_MESSAGE);
                txtbayar.setText(String.valueOf(bayar));
                txtbayar.requestFocus();
            }
            
        }
    }
    
    public void cetakStruk(){
        try {
            jasperdesign = JRXmlLoader.load(file);
            HashMap isian = new HashMap();
            isian.put("kd_transaksi", transaksi.kodetransaksi);
            jasperreport = JasperCompileManager.compileReport(jasperdesign);
            jasperprint = JasperFillManager.fillReport(jasperreport, isian, koneksi.getKoneksi());
            jasperviewer = new JasperViewer(jasperprint, false);
            jasperviewer.setTitle("Struk Pembayaran");
            jasperviewer.setVisible(true);
        }
        catch (JRException je) {
            JOptionPane.showMessageDialog(null, "Report Error.\nError: " + je);
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
        jPanel2 = new javax.swing.JPanel();
        lblkodetransaksi = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        txtpembeli = new javax.swing.JTextField();
        btnedit = new javax.swing.JLabel();
        txtbayar = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtkembalian = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        btnbayar = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txttotal = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        chkstruk = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Halaman Pembayaran");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(java.awt.Color.white);
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblkodetransaksi.setFont(new java.awt.Font("Bahnschrift", 0, 22)); // NOI18N
        lblkodetransaksi.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblkodetransaksi.setText("Kode Transaksi: TR000");
        jPanel2.add(lblkodetransaksi, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 400, 30));

        jLabel3.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Pembayaran");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 400, 30));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 402, 60));

        jLabel2.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Pembeli");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, -1, 30));
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 180, 270, 10));

        txtpembeli.setFont(new java.awt.Font("Bahnschrift", 0, 16)); // NOI18N
        txtpembeli.setBorder(null);
        jPanel1.add(txtpembeli, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 150, 270, 30));

        btnedit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnedit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dealer/ic_pencil.png"))); // NOI18N
        btnedit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnedit.setName("edit"); // NOI18N
        btnedit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btneditMouseClicked(evt);
            }
        });
        jPanel1.add(btnedit, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 180, 40, 40));

        txtbayar.setEditable(false);
        txtbayar.setFont(new java.awt.Font("Bahnschrift", 0, 16)); // NOI18N
        txtbayar.setBorder(null);
        txtbayar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtbayarKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtbayarKeyReleased(evt);
            }
        });
        jPanel1.add(txtbayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 190, 270, 30));
        jPanel1.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 220, 270, 10));

        jLabel6.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Bayar");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 190, -1, 30));

        jLabel4.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Kembalian");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, -1, 30));

        txtkembalian.setEditable(false);
        txtkembalian.setFont(new java.awt.Font("Bahnschrift", 0, 16)); // NOI18N
        txtkembalian.setBorder(null);
        jPanel1.add(txtkembalian, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 230, 270, 30));
        jPanel1.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 260, 270, 10));

        btnbayar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnbayarMouseClicked(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("BAYAR");
        jLabel5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout btnbayarLayout = new javax.swing.GroupLayout(btnbayar);
        btnbayar.setLayout(btnbayarLayout);
        btnbayarLayout.setHorizontalGroup(
            btnbayarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
            .addGroup(btnbayarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(btnbayarLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        btnbayarLayout.setVerticalGroup(
            btnbayarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
            .addGroup(btnbayarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(btnbayarLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jPanel1.add(btnbayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 310, -1, 30));

        txttotal.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        txttotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txttotal.setText("Rp. 0.000.000.000");
        jPanel1.add(txttotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, 400, -1));

        jLabel7.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Total Biaya");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 400, -1));

        chkstruk.setBackground(java.awt.Color.white);
        chkstruk.setFont(new java.awt.Font("Bahnschrift", 0, 16)); // NOI18N
        chkstruk.setText("Cetak Struk");
        jPanel1.add(chkstruk, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 270, -1, 20));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 400, 360));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btneditMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btneditMouseClicked
        cekEdit('p');
    }//GEN-LAST:event_btneditMouseClicked

    private void txtbayarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtbayarKeyPressed
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            cekEdit('p');
        }
    }//GEN-LAST:event_txtbayarKeyPressed

    private void txtbayarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtbayarKeyReleased

    }//GEN-LAST:event_txtbayarKeyReleased

    private void btnbayarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnbayarMouseClicked
        btnedit.setName("done");
        cekEdit('b');
        input();
    }//GEN-LAST:event_btnbayarMouseClicked

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        new CrudTransaksi().setVisible(true);
    }//GEN-LAST:event_formWindowClosed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CrudBayar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CrudBayar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CrudBayar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CrudBayar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CrudBayar().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel btnbayar;
    private javax.swing.JLabel btnedit;
    private javax.swing.JCheckBox chkstruk;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel lblkodetransaksi;
    private javax.swing.JTextField txtbayar;
    private javax.swing.JTextField txtkembalian;
    private javax.swing.JTextField txtpembeli;
    private javax.swing.JLabel txttotal;
    // End of variables declaration//GEN-END:variables
}
