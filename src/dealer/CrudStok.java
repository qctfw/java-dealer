/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dealer;

import com.sun.glass.events.KeyEvent;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author QCTFW
 */
public class CrudStok extends javax.swing.JFrame {

    public PreparedStatement ps;
    public Statement st;
    public ResultSet rs;
    public Connection cn = koneksi.getKoneksi();
    public DefaultTableModel tabModel;
    public String id_merk;
    /**
     * Creates new form crudParkir
     */
    public CrudStok() {
        initComponents();
        judulTabel();
        valueJenis();
        tampilData();
        setComboBoxMerk();
        tulisanID(false);
        jLabel9.setForeground(Color.GRAY);
    }
    
    public void valueJenis(){
        rad_mobil.setActionCommand("Mobil");
        rad_motor.setActionCommand("Motor");
        rad_truk.setActionCommand("Truk");
    }
    
    public void tulisanID(boolean bool){
        lblid.setVisible(bool);
        lbltulisanid.setVisible(bool);
    }
    
    public void judulTabel(){
        Object[] judul = {"Kode Unit", "Nama", "Jenis", "Merek", "Tahun Rilis", "Stok", "Harga per Unit"};
        tabModel = new DefaultTableModel(null, judul){
            @Override // Membuat Tabel tidak bisa diedit
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabel_stok.setModel(tabModel);
        tabel_stok.getColumnModel().getColumn(0).setMinWidth(WIDTH+50); // Kode Unit
        tabel_stok.getColumnModel().getColumn(1).setMinWidth(WIDTH+170); // Nama
        tabel_stok.getColumnModel().getColumn(2).setMinWidth(WIDTH-25); // Jenis
        tabel_stok.getColumnModel().getColumn(3).setMinWidth(WIDTH+55); // Merek
        tabel_stok.getColumnModel().getColumn(4).setMinWidth(WIDTH+70); // Tahun
        tabel_stok.getColumnModel().getColumn(5).setMinWidth(WIDTH-590); // Stok
        tabel_stok.getColumnModel().getColumn(6).setMinWidth(WIDTH+100); // Harga
    }
    
    
    
    public void getIdMerk(String merk){
        try {
            ps = cn.prepareStatement("SELECT id_merk FROM tb_merk WHERE merk = ?");
            ps.setString(1, merk);
            rs = ps.executeQuery();
            while (rs.next()) {
                id_merk = rs.getString("id_merk");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public Boolean cekPanjang(String txt, int length){
        return txt.length() < length;
    }
    
    public Boolean validasi(){
        return (!(txtnama.getText().equals(""))  &&
                radg_jenis.getSelection() != null &&
                txtmerek.getSelectedIndex() != 0 &&
                cekAngka(txttahun.getText()) &&
                cekAngka(txtstok.getText()) &&
                cekAngka(txtharga.getText())
                );
    }
    
    public static Boolean cekAngka(String string) {
        if (!string.equals("")) {
            try {
                long angka = Long.parseLong(string);
            }
            catch (NumberFormatException nfe) {
                return false;
            }
        }
        else {
            return false;
        }
        return true;
    }
    
    public Boolean cekMaxAngka(){
        try {
            boolean bool = Long.parseLong(txtharga.getText()) <= 4000000000L;
            return bool;
        }
        catch (NumberFormatException nfe) {
            return false;
        }
    }
    
    public void tampilData(){
        try {
            st = cn.createStatement();
            tabModel.getDataVector().removeAllElements();
            tabModel.fireTableDataChanged();
            rs = st.executeQuery("SELECT * FROM tampil_stok ORDER BY kd_barang ASC");
            while (rs.next()) {
                Object[] data = {
                    rs.getString("kd_barang"),
                    rs.getString("nama"),
                    rs.getString("jenis"),
                    rs.getString("merk"),
                    rs.getString("tahun"),
                    rs.getString("stok"),
                    rs.getString("harga")
                };
                tabModel.addRow(data);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void setComboBoxMerk(){
        try {
            st = cn.createStatement();
            rs = st.executeQuery("SELECT * FROM tb_merk");
            while (rs.next()) {
                txtmerek.addItem(rs.getString("merk"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
    
    public void reset(){
        txtnama.setText("");
        radg_jenis.clearSelection();
        txtmerek.setSelectedIndex(0);
        txttahun.setText("");
        txtstok.setText("");
        txtharga.setText("");
        lblid.setText("-");
        lblsubmit.setText("SUBMIT");
        jLabel9.setForeground(Color.GRAY);
        tulisanID(false);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        radg_jenis = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        lblid = new javax.swing.JLabel();
        lbltulisanid = new javax.swing.JLabel();
        lblnama = new javax.swing.JLabel();
        lbljenis = new javax.swing.JLabel();
        lblmerek = new javax.swing.JLabel();
        lbltahun = new javax.swing.JLabel();
        lblstok = new javax.swing.JLabel();
        lblunit = new javax.swing.JLabel();
        lblharga = new javax.swing.JLabel();
        lblrp = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        btnsubmit = new javax.swing.JPanel();
        lblsubmit = new javax.swing.JLabel();
        btndelete = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        btninputmerek = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        btnclear = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        rad_mobil = new javax.swing.JRadioButton();
        lblmobil = new javax.swing.JLabel();
        rad_motor = new javax.swing.JRadioButton();
        lblmotor = new javax.swing.JLabel();
        rad_truk = new javax.swing.JRadioButton();
        lbltruk = new javax.swing.JLabel();
        txtmerek = new javax.swing.JComboBox<>();
        txtnama = new javax.swing.JTextField();
        txtstok = new javax.swing.JTextField();
        txtharga = new javax.swing.JTextField();
        lblcari = new javax.swing.JLabel();
        txtcari = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        txttahun = new javax.swing.JTextField();
        jSeparator7 = new javax.swing.JSeparator();
        panel_judul = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtidmerek = new javax.swing.JLabel();
        lblidmerek = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabel_stok = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Kelola Stok");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jPanel1.setBackground(java.awt.Color.white);
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblid.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblid.setText("-");
        jPanel1.add(lblid, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 50, 70, -1));

        lbltulisanid.setText("ID:");
        jPanel1.add(lbltulisanid, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 50, -1, -1));

        lblnama.setFont(new java.awt.Font("Bahnschrift", 0, 16)); // NOI18N
        lblnama.setText("Nama");
        jPanel1.add(lblnama, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, -1));

        lbljenis.setFont(new java.awt.Font("Bahnschrift", 0, 16)); // NOI18N
        lbljenis.setText("Jenis");
        jPanel1.add(lbljenis, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, -1, -1));

        lblmerek.setFont(new java.awt.Font("Bahnschrift", 0, 16)); // NOI18N
        lblmerek.setText("Merek");
        jPanel1.add(lblmerek, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, -1, -1));

        lbltahun.setFont(new java.awt.Font("Bahnschrift", 0, 16)); // NOI18N
        lbltahun.setText("Tahun Rilis");
        jPanel1.add(lbltahun, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, -1, -1));

        lblstok.setFont(new java.awt.Font("Bahnschrift", 0, 16)); // NOI18N
        lblstok.setText("Jumlah Stok");
        jPanel1.add(lblstok, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, -1, -1));

        lblunit.setFont(new java.awt.Font("Bahnschrift", 0, 16)); // NOI18N
        lblunit.setText("unit");
        jPanel1.add(lblunit, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 230, -1, -1));

        lblharga.setFont(new java.awt.Font("Bahnschrift", 0, 16)); // NOI18N
        lblharga.setText("Harga per Unit");
        jPanel1.add(lblharga, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, -1, -1));

        lblrp.setFont(new java.awt.Font("Bahnschrift", 0, 16)); // NOI18N
        lblrp.setText("Rp.");
        jPanel1.add(lblrp, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 270, -1, -1));

        jSeparator1.setForeground(java.awt.Color.black);
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 290, 210, 10));

        jSeparator2.setForeground(java.awt.Color.black);
        jPanel1.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 90, 240, 10));

        jSeparator6.setForeground(java.awt.Color.black);
        jPanel1.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 250, 90, 10));

        btnsubmit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnsubmit.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblsubmit.setFont(new java.awt.Font("Bahnschrift", 0, 16)); // NOI18N
        lblsubmit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblsubmit.setText("SUBMIT");
        lblsubmit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblsubmitMouseClicked(evt);
            }
        });
        btnsubmit.add(lblsubmit, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 70, 30));

        jPanel1.add(btnsubmit, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, 70, 30));

        btndelete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btndelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btndeleteMouseClicked(evt);
            }
        });
        btndelete.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Bahnschrift", 0, 16)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("DELETE");
        btndelete.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 70, 30));

        jPanel1.add(btndelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 310, 70, 30));

        btninputmerek.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btninputmerek.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btninputmerekMouseClicked(evt);
            }
        });
        btninputmerek.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setFont(new java.awt.Font("Bahnschrift", 0, 12)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("KELOLA MEREK");
        btninputmerek.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 90, 20));

        jPanel1.add(btninputmerek, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 150, 90, 20));

        btnclear.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnclear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnclearMouseClicked(evt);
            }
        });
        btnclear.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Bahnschrift", 0, 16)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("CLEAR");
        btnclear.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 70, 30));

        jPanel1.add(btnclear, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 310, 70, 30));

        rad_mobil.setBackground(new java.awt.Color(255, 255, 255));
        radg_jenis.add(rad_mobil);
        rad_mobil.setPreferredSize(new java.awt.Dimension(20, 20));
        jPanel1.add(rad_mobil, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 110, -1, -1));

        lblmobil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dealer/ic_car.png"))); // NOI18N
        lblmobil.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblmobilMouseClicked(evt);
            }
        });
        jPanel1.add(lblmobil, new org.netbeans.lib.awtextra.AbsoluteConstraints(165, 110, 30, 20));

        rad_motor.setBackground(new java.awt.Color(255, 255, 255));
        radg_jenis.add(rad_motor);
        rad_motor.setPreferredSize(new java.awt.Dimension(20, 20));
        jPanel1.add(rad_motor, new org.netbeans.lib.awtextra.AbsoluteConstraints(225, 110, -1, -1));

        lblmotor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dealer/ic_motorcycle.png"))); // NOI18N
        lblmotor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblmotorMouseClicked(evt);
            }
        });
        jPanel1.add(lblmotor, new org.netbeans.lib.awtextra.AbsoluteConstraints(255, 110, 30, 20));

        rad_truk.setBackground(new java.awt.Color(255, 255, 255));
        radg_jenis.add(rad_truk);
        rad_truk.setPreferredSize(new java.awt.Dimension(20, 20));
        jPanel1.add(rad_truk, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 110, -1, -1));

        lbltruk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dealer/ic_truck.png"))); // NOI18N
        lbltruk.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        lbltruk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbltrukMouseClicked(evt);
            }
        });
        jPanel1.add(lbltruk, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 110, 30, 20));

        txtmerek.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Merek" }));
        txtmerek.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                txtmerekItemStateChanged(evt);
            }
        });
        jPanel1.add(txtmerek, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 150, 130, 20));

        txtnama.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        txtnama.setBorder(null);
        txtnama.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtnamaKeyTyped(evt);
            }
        });
        jPanel1.add(txtnama, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 60, 240, 30));

        txtstok.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        txtstok.setBorder(null);
        txtstok.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtstokKeyTyped(evt);
            }
        });
        jPanel1.add(txtstok, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 220, 90, 30));

        txtharga.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        txtharga.setBorder(null);
        txtharga.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txthargaFocusLost(evt);
            }
        });
        txtharga.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txthargaKeyTyped(evt);
            }
        });
        jPanel1.add(txtharga, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 260, 210, 30));

        lblcari.setFont(new java.awt.Font("Bahnschrift", 0, 16)); // NOI18N
        lblcari.setText("Cari");
        jPanel1.add(lblcari, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 20, -1, -1));

        txtcari.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        txtcari.setBorder(null);
        txtcari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtcariKeyTyped(evt);
            }
        });
        jPanel1.add(txtcari, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 10, 490, 30));

        jSeparator3.setForeground(java.awt.Color.black);
        jPanel1.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 40, 490, 10));

        txttahun.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        txttahun.setBorder(null);
        txttahun.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txttahunKeyTyped(evt);
            }
        });
        jPanel1.add(txttahun, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 180, 90, 30));

        jSeparator7.setForeground(java.awt.Color.black);
        jPanel1.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 210, 90, 10));

        panel_judul.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel15.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dealer/ic_garage_35px.png"))); // NOI18N
        panel_judul.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 60, 50));

        jLabel16.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        jLabel16.setText("KELOLA STOK KENDARAAN");
        panel_judul.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(57, -1, 310, 50));

        jPanel1.add(panel_judul, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 380, 50));

        txtidmerek.setFont(new java.awt.Font("Bahnschrift", 0, 12)); // NOI18N
        txtidmerek.setText("?");
        jPanel1.add(txtidmerek, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 170, 30, 10));

        lblidmerek.setFont(new java.awt.Font("Bahnschrift", 0, 12)); // NOI18N
        lblidmerek.setText("ID Merek:");
        jPanel1.add(lblidmerek, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 170, -1, 10));

        tabel_stok.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tabel_stok.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabel_stokMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabel_stok);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 50, 590, 290));

        jLabel1.setFont(new java.awt.Font("Bahnschrift", 0, 11)); // NOI18N
        jLabel1.setText("Max: 4.000.000.000");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 290, -1, 20));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1005, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txttahunKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txttahunKeyTyped
        if ((!cekPanjang(txttahun.getText(), 4) || evt.getKeyChar() == KeyEvent.VK_BACKSPACE) || !Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }//GEN-LAST:event_txttahunKeyTyped

    private void txtstokKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtstokKeyTyped
        if ((!cekPanjang(txtstok.getText(), 6) || evt.getKeyChar() == KeyEvent.VK_BACKSPACE) || !Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }//GEN-LAST:event_txtstokKeyTyped

    private void txtnamaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnamaKeyTyped
        if ((!cekPanjang(txtnama.getText(), 33) || evt.getKeyChar() == KeyEvent.VK_BACKSPACE)) {
            evt.consume();
        }
    }//GEN-LAST:event_txtnamaKeyTyped

    private void txthargaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txthargaKeyTyped
        if ((!cekPanjang(txtharga.getText(), 10) || evt.getKeyChar() == KeyEvent.VK_BACKSPACE) || !Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
        txtharga.setForeground(Color.BLACK);
    }//GEN-LAST:event_txthargaKeyTyped

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // KEMBALI KE MENU
        new menu().setVisible(true);
    }//GEN-LAST:event_formWindowClosed

    private void lblmobilMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblmobilMouseClicked
        rad_mobil.setSelected(true);
    }//GEN-LAST:event_lblmobilMouseClicked

    private void lblmotorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblmotorMouseClicked
        rad_motor.setSelected(true);
    }//GEN-LAST:event_lblmotorMouseClicked

    private void lbltrukMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbltrukMouseClicked
        rad_truk.setSelected(true);
    }//GEN-LAST:event_lbltrukMouseClicked

    private void lblsubmitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblsubmitMouseClicked
        if (validasi() && !(txtmerek.getSelectedIndex() == 0)) {
            if (Integer.parseInt(txttahun.getText()) < 2018 && Integer.parseInt(txttahun.getText()) >= 1800) {
                if (cekMaxAngka()) {
                    try {
                        String sql;
                        boolean isSubmit = lblsubmit.getText().equals("SUBMIT");
                        if (isSubmit) {
                            sql = "INSERT INTO tb_stok VALUES(null,?,?,?,?,?,?)";
                        }
                        else {
                            sql = "UPDATE tb_stok SET nama = ?, jenis = ?, id_merk = ?, tahun = ?, stok = ?, harga = ? WHERE kd_barang = ?";
                        }

                        ps = cn.prepareStatement(sql);
                        ps.setString(1, txtnama.getText());
                        ps.setString(2, radg_jenis.getSelection().getActionCommand());
                        ps.setString(3, txtidmerek.getText());
                        ps.setString(4, txttahun.getText());
                        ps.setString(5, txtstok.getText());
                        ps.setString(6, txtharga.getText());
                        if (!isSubmit) {
                            ps.setString(7, lblid.getText());
                        }
                        ps.executeUpdate();
                        if (isSubmit) {
                            JOptionPane.showMessageDialog(null, "Berhasil Diinput!");   
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "Berhasil Diupdate!");
                        }
                        tampilData();
                        reset();
                    }
                    catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "Gagal\n Error: " + e, "Error", JOptionPane.WARNING_MESSAGE);
                        e.printStackTrace();
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null, "Harga per Unit terlalu besar", "Validasi", JOptionPane.ERROR_MESSAGE);
                }
                
            }
            else {
                JOptionPane.showMessageDialog(null, "Tahun Rilis tidak valid (1800-2018)", "Validasi", JOptionPane.ERROR_MESSAGE);
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "Mohon isi semua data dengan benar", "Validasi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_lblsubmitMouseClicked

    private void txtmerekItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_txtmerekItemStateChanged
        if (txtmerek.getSelectedIndex() != 0) {
            getIdMerk(txtmerek.getSelectedItem().toString());
            txtidmerek.setText(id_merk);
        }
        else {
            txtidmerek.setText("?");
        }
    }//GEN-LAST:event_txtmerekItemStateChanged

    private void btnclearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnclearMouseClicked
        reset();
    }//GEN-LAST:event_btnclearMouseClicked

    private void tabel_stokMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabel_stokMouseClicked
        tulisanID(true);
        lblsubmit.setText("UPDATE");
        jLabel9.setForeground(Color.BLACK);
        lblid.setText(tabModel.getValueAt(tabel_stok.getSelectedRow(), 0).toString());
        txtnama.setText(tabModel.getValueAt(tabel_stok.getSelectedRow(), 1).toString());
        radg_jenis.clearSelection();
        switch (tabModel.getValueAt(tabel_stok.getSelectedRow(), 2).toString()) {
            case "Mobil" :
                rad_mobil.setSelected(true);
            break;
            case "Motor" :
                rad_motor.setSelected(true);
            break;
            case "Truk" :
                rad_truk.setSelected(true);
            break;
        }
        String namamerek = tabModel.getValueAt(tabel_stok.getSelectedRow(), 3).toString();
        txtmerek.setSelectedItem(namamerek);
        getIdMerk(namamerek);
        txtidmerek.setText(id_merk);
        txttahun.setText(tabModel.getValueAt(tabel_stok.getSelectedRow(), 4).toString());
        txtstok.setText(tabModel.getValueAt(tabel_stok.getSelectedRow(), 5).toString());
        txtharga.setText(tabModel.getValueAt(tabel_stok.getSelectedRow(), 6).toString());
    }//GEN-LAST:event_tabel_stokMouseClicked

    private void txtcariKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcariKeyTyped
        try {
            st = cn.createStatement();
            tabModel.getDataVector().removeAllElements();
            tabModel.fireTableDataChanged();
            String search = txtcari.getText();
            rs = st.executeQuery("SELECT * FROM tampil_stok WHERE nama LIKE '%" + search + "%' OR jenis LIKE '%" + search + "%' OR merk LIKE '%" + search + "%' OR tahun LIKE '%" + search + "%' OR stok LIKE '%" + search + "%' OR harga LIKE '%" + search + "%' ORDER BY kd_barang ASC");
            while (rs.next()) {
                Object[] data = {
                    rs.getString("kd_barang"),
                    rs.getString("nama"),
                    rs.getString("jenis"),
                    rs.getString("merk"),
                    rs.getString("tahun"),
                    rs.getString("stok"),
                    rs.getString("harga")
                };
                tabModel.addRow(data);
            }
        }
         catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtcariKeyTyped

    private void btndeleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btndeleteMouseClicked
        if (lblsubmit.getText().equals("UPDATE")) {
            try {
                int jawab = JOptionPane.showConfirmDialog(null, "Ingin Menghapus Field Ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                if (jawab == 0) {
                    st = cn.createStatement();
                    st.executeUpdate("DELETE FROM tb_stok WHERE kd_barang='" + lblid.getText() + "'");
                    tampilData();
                    reset();
                    JOptionPane.showMessageDialog(null, "Berhasil Dihapus!");
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
    }//GEN-LAST:event_btndeleteMouseClicked

    private void txthargaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txthargaFocusLost
        if (cekMaxAngka()) {
            txtharga.setForeground(Color.BLACK);
        } else {
            txtharga.setForeground(Color.RED);
        }
    }//GEN-LAST:event_txthargaFocusLost

    private void btninputmerekMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btninputmerekMouseClicked
        new CrudMerk().setVisible(true);
        this.hide();
    }//GEN-LAST:event_btninputmerekMouseClicked

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
            java.util.logging.Logger.getLogger(CrudStok.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CrudStok.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CrudStok.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CrudStok.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CrudStok().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel btnclear;
    private javax.swing.JPanel btndelete;
    private javax.swing.JPanel btninputmerek;
    private javax.swing.JPanel btnsubmit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JLabel lblcari;
    private javax.swing.JLabel lblharga;
    private javax.swing.JLabel lblid;
    private javax.swing.JLabel lblidmerek;
    private javax.swing.JLabel lbljenis;
    private javax.swing.JLabel lblmerek;
    private javax.swing.JLabel lblmobil;
    private javax.swing.JLabel lblmotor;
    private javax.swing.JLabel lblnama;
    private javax.swing.JLabel lblrp;
    private javax.swing.JLabel lblstok;
    private javax.swing.JLabel lblsubmit;
    private javax.swing.JLabel lbltahun;
    private javax.swing.JLabel lbltruk;
    private javax.swing.JLabel lbltulisanid;
    private javax.swing.JLabel lblunit;
    private javax.swing.JPanel panel_judul;
    private javax.swing.JRadioButton rad_mobil;
    private javax.swing.JRadioButton rad_motor;
    private javax.swing.JRadioButton rad_truk;
    private javax.swing.ButtonGroup radg_jenis;
    private javax.swing.JTable tabel_stok;
    private javax.swing.JTextField txtcari;
    private javax.swing.JTextField txtharga;
    private javax.swing.JLabel txtidmerek;
    private javax.swing.JComboBox<String> txtmerek;
    private javax.swing.JTextField txtnama;
    private javax.swing.JTextField txtstok;
    private javax.swing.JTextField txttahun;
    // End of variables declaration//GEN-END:variables
}
