/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dealer;

import java.awt.Color;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import javax.swing.JOptionPane;
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
public class CrudLaporan extends javax.swing.JFrame {
    Color clrmati = new Color(153, 153, 153);
    Connection cn = koneksi.getKoneksi();
    Statement st;
    ResultSet rs;
    JasperPrint jasperprint;
    JasperViewer jasperviewer;
    JasperDesign jasperdesign;
    JasperReport jasperreport;
    String file_struk = "/dealer/strukpembayaran.jrxml";
    String file_stok = "/dealer/reportstok.jrxml";
    String file_periode = "/dealer/reportperiode.jrxml";
    /**
     * Creates new form CrudLaporan
     */
    public CrudLaporan() {
        initComponents();
        setPilihanTransaksi(false);
        isianStruk();
        kodeAction();
        gantimethodkode();
    }
    
    void kodeAction(){
        pilihantransaksi_1.setActionCommand("Pilihan");
        pilihantransaksi_2.setActionCommand("Input Manual");
    }
    void setPilihanTransaksi(boolean bool){
        pilihantransaksi_1.setSelected(!bool);
        pilihantransaksi_1.setVisible(bool);
        pilihantransaksi_2.setVisible(bool);
        combokode.setEnabled(bool);
        txtkode.setEnabled(bool);
        if (bool) {
            lblkode.setForeground(Color.BLACK);
        }
        else {
            lblkode.setForeground(clrmati);
        }
    }
    void isianStruk(){
        try {
            st = cn.createStatement();
            rs = st.executeQuery("SELECT kd_transaksi FROM tb_transaksi");
            combokode.removeAllItems();
            combokode.addItem("Pilih Kode Transaksi");
            lblkode.setText("Kode Transaksi");
            while (rs.next()) {
                combokode.addItem(rs.getString("kd_transaksi"));
            }
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error\n" + e, "Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }
    void isianPeriode(){
        int minPeriode = 0, maxPeriode = 0;
        combokode.setVisible(true);
        txtkode.setVisible(false);
        borderkode.setVisible(false);
        pilihantransaksi_1.setVisible(false);
        pilihantransaksi_2.setVisible(false);
        combokode.removeAllItems();
        combokode.addItem("Pilih Periode");
        lblkode.setText("Tahun Periode");
        try {
            st = cn.createStatement();
            rs = st.executeQuery("SELECT MAX(YEAR(tgl_order)) AS maks FROM tb_order");
            if (rs.next()) {
                maxPeriode = Integer.parseInt(rs.getString("maks"));
            }
            rs = st.executeQuery("SELECT MIN(YEAR(tgl_order)) AS min FROM tb_order");
            if (rs.next()) {
                minPeriode = Integer.parseInt(rs.getString("min"));
            }
            for (int i = minPeriode; i <= maxPeriode; i++) {
                combokode.addItem(String.valueOf(i));
            }
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
    }
    void gantimethodkode(){
        if (pilihantransaksi.getSelection().getActionCommand().equals("Pilihan")) {
            txtkode.setVisible(false);
            borderkode.setVisible(false);
            combokode.setVisible(true);
        }
        else {
            txtkode.setVisible(true);
            borderkode.setVisible(true);
            combokode.setVisible(false);
        }
    }

    /**
     * Menampilkan report tanpa parameter
     * @param file Lokasi File
    */
    void cetakReport(String fileName) {
        try {
            InputStream reportFile = CrudLaporan.class.getResourceAsStream(fileName);
            jasperdesign = JRXmlLoader.load(reportFile);
            jasperreport = JasperCompileManager.compileReport(jasperdesign);
            jasperprint = JasperFillManager.fillReport(jasperreport, null, cn);
            jasperviewer = new JasperViewer(jasperprint, false);
            jasperviewer.setTitle("Laporan");
            jasperviewer.setVisible(true);
        }
        catch (JRException je) {
            JOptionPane.showMessageDialog(null, "Report Error.\nError: " + je);
        }        
    }

    /**
     * Menampilkan report dengan satu parameter
     * @param file Lokasi File
     * @param parameter Nama Patokan
     * @param isiparameter Isi Patokan
    */
    void cetakReport(String fileName, String parameter, String isiparameter){
        try {
            InputStream reportFile = CrudLaporan.class.getResourceAsStream(fileName);
            jasperdesign = JRXmlLoader.load(reportFile);
            HashMap isian = new HashMap();
            isian.put(parameter, isiparameter);
            jasperreport = JasperCompileManager.compileReport(jasperdesign);
            jasperprint = JasperFillManager.fillReport(jasperreport, isian, cn);
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

        pilihantransaksi = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jenis_laporan = new javax.swing.JComboBox<>();
        btncetak = new javax.swing.JButton();
        lblkode = new javax.swing.JLabel();
        combokode = new javax.swing.JComboBox<>();
        txtkode = new javax.swing.JTextField();
        borderkode = new javax.swing.JSeparator();
        pilihantransaksi_1 = new javax.swing.JRadioButton();
        pilihantransaksi_2 = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Menu Laporan");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(java.awt.Color.white);
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("MENU LAPORAN");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 400, 50));

        jLabel2.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Jenis Laporan");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, 100, 30));

        jenis_laporan.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        jenis_laporan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Laporan", "Struk Pembayaran", "Order Transaksi", "Stok Unit" }));
        jenis_laporan.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jenis_laporanItemStateChanged(evt);
            }
        });
        jPanel1.add(jenis_laporan, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 80, 210, 30));

        btncetak.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        btncetak.setText("Cetak Laporan");
        btncetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncetakActionPerformed(evt);
            }
        });
        jPanel1.add(btncetak, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 200, 130, -1));

        lblkode.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        lblkode.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblkode.setText("Kode Transaksi");
        jPanel1.add(lblkode, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 150, 100, 30));

        combokode.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        combokode.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Kode Transaksi" }));
        jPanel1.add(combokode, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 150, 210, 30));

        txtkode.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        txtkode.setBorder(null);
        jPanel1.add(txtkode, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 150, 210, 30));
        jPanel1.add(borderkode, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 180, 210, 10));

        pilihantransaksi_1.setBackground(new java.awt.Color(255, 255, 255));
        pilihantransaksi.add(pilihantransaksi_1);
        pilihantransaksi_1.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        pilihantransaksi_1.setSelected(true);
        pilihantransaksi_1.setText("Pilihan");
        pilihantransaksi_1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                pilihantransaksi_1ItemStateChanged(evt);
            }
        });
        jPanel1.add(pilihantransaksi_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 115, -1, 30));

        pilihantransaksi_2.setBackground(new java.awt.Color(255, 255, 255));
        pilihantransaksi.add(pilihantransaksi_2);
        pilihantransaksi_2.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        pilihantransaksi_2.setText("Input Manual");
        pilihantransaksi_2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                pilihantransaksi_2ItemStateChanged(evt);
            }
        });
        jPanel1.add(pilihantransaksi_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 115, 110, 30));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 400, 240));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jenis_laporanItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jenis_laporanItemStateChanged
        switch (jenis_laporan.getSelectedItem().toString()) {
            case "Struk Pembayaran" :
                pilihantransaksi_1.setSelected(true);
                setPilihanTransaksi(true);
                isianStruk();
            break;
            case "Order Transaksi" :
                setPilihanTransaksi(true);
                isianPeriode();
            break;
            case "Stok Unit" :
                setPilihanTransaksi(false);
            break;
            default:
                setPilihanTransaksi(false);
            break;
        }
    }//GEN-LAST:event_jenis_laporanItemStateChanged

    private void pilihantransaksi_1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_pilihantransaksi_1ItemStateChanged
        gantimethodkode();
    }//GEN-LAST:event_pilihantransaksi_1ItemStateChanged

    private void pilihantransaksi_2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_pilihantransaksi_2ItemStateChanged
        gantimethodkode();
    }//GEN-LAST:event_pilihantransaksi_2ItemStateChanged

    private void btncetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncetakActionPerformed
        if (jenis_laporan.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Harap pilih jenis laporan", "Validasi", JOptionPane.INFORMATION_MESSAGE);
        }
        else {
            switch (jenis_laporan.getSelectedItem().toString()) {
                case "Struk Pembayaran" :
                    if (pilihantransaksi.getSelection().getActionCommand().equals("Pilihan")) {
                        if (combokode.getSelectedIndex() == 0) {
                            JOptionPane.showMessageDialog(null, "Harap pilih Kode Transaksi", "Validasi", JOptionPane.INFORMATION_MESSAGE);
                        }
                        else {
                            cetakReport(file_struk, "kd_transaksi", combokode.getSelectedItem().toString());
                        }
                    }
                    else {
                        if (!txtkode.getText().equals("")) {
                            cetakReport(file_struk, "kd_transaksi", txtkode.getText());
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "Harap isi Kode Transaksi", "Validasi", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                break;
                case "Order Transaksi" :
                    if (combokode.getSelectedIndex() > 0) {
                            cetakReport(file_periode, "tahun", combokode.getSelectedItem().toString());
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Harap pilih Periode Tahun", "Validasi", JOptionPane.INFORMATION_MESSAGE);
                    }
                    
                break;
                case "Stok Unit" :
                    cetakReport(file_stok);
                break;
                default:
                    JOptionPane.showMessageDialog(null, "what");
                break;
            }
        }
    }//GEN-LAST:event_btncetakActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        new menu().setVisible(true);
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
            java.util.logging.Logger.getLogger(CrudLaporan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CrudLaporan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CrudLaporan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CrudLaporan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CrudLaporan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSeparator borderkode;
    private javax.swing.JButton btncetak;
    private javax.swing.JComboBox<String> combokode;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JComboBox<String> jenis_laporan;
    private javax.swing.JLabel lblkode;
    private javax.swing.ButtonGroup pilihantransaksi;
    private javax.swing.JRadioButton pilihantransaksi_1;
    private javax.swing.JRadioButton pilihantransaksi_2;
    private javax.swing.JTextField txtkode;
    // End of variables declaration//GEN-END:variables
}
