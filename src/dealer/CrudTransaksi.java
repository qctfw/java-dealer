/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dealer;

import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author QCTFW
 */
public class CrudTransaksi extends javax.swing.JFrame {

    public Connection cn = koneksi.getKoneksi();
    public PreparedStatement ps;
    public Statement st;
    public ResultSet rs;
    public DefaultTableModel tabModel;
    public long hargaperunit;
    public long subtotal;
    public int stoktersedia;
    public long total;
    public String kodebarang;
    public String kodetransaksi;
    public int order_maks;
    public int transaksi_maks;
    public boolean aftercrudpembayaran;
    DecimalFormat kursID = (DecimalFormat) DecimalFormat.getCurrencyInstance();
    DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
    
    /**
     * Creates new form CrudBeli
     */
    public CrudTransaksi() {
        initComponents();
        judulTabel();
        unitItems();
        setCodeFirst();
        tampilData();
        setKurs();
        txtstok.setVisible(false);
        lblstoktersedia.setText("0");
        lblhargaperunit.setText("Rp. 0");
        txtsubtotal.setText("Rp. 0");
    }

    public void setKurs(){
        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');
        kursID.setDecimalFormatSymbols(formatRp);
    }
    
    public void unitItems(){    
        try {
            st = cn.createStatement();
            rs = st.executeQuery("SELECT nama FROM tampil_stok");
            while (rs.next()) {
                selectunit.addItem(rs.getString("nama"));
            }
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Tidak dapat menampilkan unit. Error: " + e);
        }
    }
    
    public void setCodeFirst(){
        try {
            st = cn.createStatement();
            rs = st.executeQuery("SELECT * FROM banding_kodetransaksi");
            if (rs.next()) {
                if (rs.getString("maks_tborder").equals(rs.getString("maks_tbtransaksi"))) {
                    setTransaksiCode(true);
                }
                else {
                    setTransaksiCode(false);
                    updateTotal();
                }
            }
            setOrderCode();
            kodetransaksi = lbl_kodetransaksi.getText();
            
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal Mendapat Kode. Error: " + e);
            e.printStackTrace();
        }
    }
    
    public String numberToMoney(long money) {
        return kursID.format(money).replace(",00", "");
    }
    
    public void judulTabel(){
        Object[] judul = {"Kode Order", "Kode Transaksi", "Nama Barang", "Stok", "Sub Total", "Tanggal Order"};
        tabModel = new DefaultTableModel(null, judul){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        tabel_order.setModel(tabModel);
        tabel_order.getColumnModel().getColumn(0).setMinWidth(WIDTH+70); // Kode Order
        tabel_order.getColumnModel().getColumn(1).setMinWidth(WIDTH+100); // Kode Transaksi
        tabel_order.getColumnModel().getColumn(2).setMinWidth(WIDTH+150); // Nama Barang
        tabel_order.getColumnModel().getColumn(3).setMinWidth(WIDTH-450); // Stok
        tabel_order.getColumnModel().getColumn(4).setMinWidth(WIDTH+90); // Sub Total
        tabel_order.getColumnModel().getColumn(5).setMinWidth(WIDTH+140); // Tanggal Order
    }
    
    public void setOrderCode(){
        try {
            st = cn.createStatement();
            rs = st.executeQuery("SELECT COUNT(kd_order) AS jumlah FROM tb_order");
            // Cek apakah kd_order ada di database
            if (rs.next()) {
                int jumlah = Integer.parseInt(rs.getString("jumlah"));
                if (jumlah > 0) {
                    // Mengambil urutan terakhir dalam tb_order
                    try {
                        st = cn.createStatement();
                        rs = st.executeQuery("SELECT MAX(kd_order) AS order_maks FROM tb_order");
                        if (rs.next()) {
                            order_maks = Integer.parseInt(rs.getString("order_maks").substring(2)) + 1;
                            
                            switch (String.valueOf(order_maks).length()) {
                                case 1:
                                    lbl_kodeorder.setText("OR00" + String.valueOf(order_maks));
                                    break;
                                case 2:
                                    lbl_kodeorder.setText("OR0" + String.valueOf(order_maks));
                                    break;
                                default:
                                    lbl_kodeorder.setText("OR" + String.valueOf(order_maks));
                                    break;
                            }
                        }
                        
                    }
                    catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "Gagal Mendapat Kode Order. Error : " + e);
                    }
                    
                }
                else {
                    lbl_kodeorder.setText("OR001");
                }
            }
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal Mendapat Kode Order. Error : " + e);
        }
    }
    public void setTransaksiCode(boolean bln){
        try {
            st = cn.createStatement();
            rs = st.executeQuery("SELECT COUNT(kd_order) AS jumlah FROM tb_order");
            // Cek apakah kd_order ada di database
            if (rs.next()) {
                int jumlah = Integer.parseInt(rs.getString("jumlah"));
                if (jumlah > 0) {
                    // Mengambil urutan terakhir dalam tb_order
                    try {
                        st = cn.createStatement();
                        rs = st.executeQuery("SELECT MAX(kd_transaksi) AS transaksi_maks FROM tb_order");
                        if (rs.next()) {
                            transaksi_maks = Integer.parseInt(rs.getString("transaksi_maks").substring(2));
                            if (bln) {
                                transaksi_maks = transaksi_maks + 1;
                            }
                            switch (String.valueOf(transaksi_maks).length()) {
                                case 1:
                                    lbl_kodetransaksi.setText("TR00" + String.valueOf(transaksi_maks));
                                    break;
                                case 2:
                                    lbl_kodetransaksi.setText("TR0" + String.valueOf(transaksi_maks));
                                    break;
                                default:
                                    lbl_kodetransaksi.setText("TR" + String.valueOf(transaksi_maks));
                                    break;
                            }
                        }
                    }
                    catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "Gagal Mendapat Kode. Error : " + e);
                    }
                    
                }
                else {
                    lbl_kodetransaksi.setText("TR001");
                }
            }
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error : " + e);
        }
    }
    
    public void tampilData(){
        try {
            st = cn.createStatement();
            tabModel.getDataVector().removeAllElements();
            tabModel.fireTableDataChanged();
            rs = st.executeQuery("SELECT * FROM tampil_order WHERE kd_transaksi = '" + lbl_kodetransaksi.getText() + "'");
            while (rs.next()) {
                Object[] data = {
                    rs.getString("kd_order"),
                    rs.getString("kd_transaksi"),
                    rs.getString("nama"),
                    rs.getString("stok"),
                    rs.getString("sub_total"),
                    rs.getString("tgl_order")
                };
                tabModel.addRow(data);
            }
        }
        catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e);
        }
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
    public void updateSubTotal(){
        try {
        if (Integer.parseInt(txtstok.getText()) > 0) {
            if (Integer.parseInt(txtstok.getText()) <= stoktersedia && cekAngka(txtstok.getText())) {
                subtotal = Integer.parseInt(txtstok.getText()) * hargaperunit;
                txtsubtotal.setText(numberToMoney(subtotal));
            }
            else {
                JOptionPane.showMessageDialog(null, "Stok yang dimasukkan melebihi stok yang tersedia", "Validasi", JOptionPane.ERROR_MESSAGE);
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "Stok yang dimasukkan tidak valid", "Validasi", JOptionPane.ERROR_MESSAGE);
            txtstok.setText("");
        }
        }
        catch (NumberFormatException e) {
            
        }
    }
    
    private void reset(){
        txtstok.setVisible(false);
        txtstok.setText("0");
        lblstoktersedia.setText("0");
        lblhargaperunit.setText("Rp. 0");
        txtsubtotal.setText("Rp. 0");
        selectunit.setSelectedIndex(0);
    }
    
    public void updateTotal(){
        try {
            st = cn.createStatement();
            rs = st.executeQuery("SELECT SUM(sub_total) AS total FROM tampil_order WHERE kd_transaksi = '" + lbl_kodetransaksi.getText() + "'");
            if (rs.next()) {
                total = Long.parseLong(rs.getString("total"));
                txttotal.setText(numberToMoney(total));
            }
        }
        catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e);
        }
    }
    
    public void afterBayar(){
        JOptionPane.showMessageDialog(null, "Transaksi Berhasil!");
        SwingUtilities.updateComponentTreeUI(this);
        this.invalidate();
        this.validate();
        this.repaint();
        setCodeFirst();
        tampilData();
        reset();
        txttotal.setText("Rp. 0");
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel_tr = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabel_order = new javax.swing.JTable();
        lbl_kodetransaksi = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lbl_kodeorder = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        selectunit = new javax.swing.JComboBox<>();
        lblstoktersedia = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel7 = new javax.swing.JLabel();
        txttotal = new javax.swing.JLabel();
        btnorder = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        btnbatal = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        panel_judul = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        lblhargaperunit = new javax.swing.JLabel();
        btnbayar = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtsubtotal = new javax.swing.JLabel();
        btnhapusall = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        btnhapus = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        txtstok = new javax.swing.JFormattedTextField();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Transaksi");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        panel_tr.setBackground(java.awt.Color.white);
        panel_tr.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tabel_order.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tabel_order);

        panel_tr.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 70, 620, 270));

        lbl_kodetransaksi.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        lbl_kodetransaksi.setText("TR000");
        panel_tr.add(lbl_kodetransaksi, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, -1, 20));

        jLabel2.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        jLabel2.setText("Unit");
        panel_tr.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, -1, 20));

        lbl_kodeorder.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        lbl_kodeorder.setText("OR000");
        panel_tr.add(lbl_kodeorder, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 100, -1, 20));

        jLabel4.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        jLabel4.setText("Kode Order");
        panel_tr.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 70, -1, 20));

        jLabel5.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        jLabel5.setText("Kode Transaksi");
        panel_tr.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, 20));

        selectunit.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        selectunit.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Unit" }));
        selectunit.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                selectunitItemStateChanged(evt);
            }
        });
        panel_tr.add(selectunit, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 360, 30));

        lblstoktersedia.setFont(new java.awt.Font("Bahnschrift", 0, 16)); // NOI18N
        lblstoktersedia.setText("000");
        panel_tr.add(lblstoktersedia, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 200, 190, -1));

        jLabel9.setFont(new java.awt.Font("Bahnschrift", 0, 16)); // NOI18N
        jLabel9.setText("Stok tersedia");
        panel_tr.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, -1, -1));

        jLabel12.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        jLabel12.setText("Stok");
        panel_tr.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, -1, 20));
        panel_tr.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, 110, 10));

        jLabel7.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        jLabel7.setText("Total :");
        panel_tr.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 350, -1, 30));

        txttotal.setFont(new java.awt.Font("Bahnschrift", 0, 16)); // NOI18N
        txttotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txttotal.setText("Rp. 0.000.000.000");
        panel_tr.add(txttotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 350, 150, 30));

        btnorder.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnorder.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnorderMouseClicked(evt);
            }
        });
        btnorder.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("ORDER");
        btnorder.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 80, 30));

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dealer/ic_order_28px.png"))); // NOI18N
        btnorder.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 39, 30));

        panel_tr.add(btnorder, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 350, 110, 30));

        btnbatal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnbatal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnbatalMouseClicked(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("RESET");

        javax.swing.GroupLayout btnbatalLayout = new javax.swing.GroupLayout(btnbatal);
        btnbatal.setLayout(btnbatalLayout);
        btnbatalLayout.setHorizontalGroup(
            btnbatalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 110, Short.MAX_VALUE)
            .addGroup(btnbatalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(btnbatalLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        btnbatalLayout.setVerticalGroup(
            btnbatalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
            .addGroup(btnbatalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(btnbatalLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        panel_tr.add(btnbatal, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 350, 110, 30));

        panel_judul.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel15.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dealer/ic_transaction_32px.png"))); // NOI18N
        panel_judul.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 60, 50));

        jLabel16.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("KELOLA TRANSAKSI");
        panel_judul.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(57, -1, 320, 50));

        panel_tr.add(panel_judul, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 380, 50));

        jLabel19.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("--- TABEL ORDER ---");
        panel_tr.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 10, 620, 50));

        jLabel13.setFont(new java.awt.Font("Bahnschrift", 0, 16)); // NOI18N
        jLabel13.setText("Harga per Unit");
        panel_tr.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, -1, 20));

        lblhargaperunit.setFont(new java.awt.Font("Bahnschrift", 0, 16)); // NOI18N
        lblhargaperunit.setText("Rp. 0.000.000.000");
        panel_tr.add(lblhargaperunit, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 220, 190, 20));

        btnbayar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnbayar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnbayarMouseClicked(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("BAYAR");

        javax.swing.GroupLayout btnbayarLayout = new javax.swing.GroupLayout(btnbayar);
        btnbayar.setLayout(btnbayarLayout);
        btnbayarLayout.setHorizontalGroup(
            btnbayarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnbayarLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        btnbayarLayout.setVerticalGroup(
            btnbayarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnbayarLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panel_tr.add(btnbayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 350, 70, -1));

        jLabel18.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        jLabel18.setText("Subtotal");
        panel_tr.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 250, -1, 20));

        txtsubtotal.setFont(new java.awt.Font("Bahnschrift", 0, 16)); // NOI18N
        txtsubtotal.setText("Rp. 0.000.000.000");
        panel_tr.add(txtsubtotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 280, 160, 20));

        btnhapusall.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnhapusall.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("BATAL TRANSAKSI");
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });
        btnhapusall.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 140, 30));

        panel_tr.add(btnhapusall, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 350, 140, 30));

        btnhapus.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnhapus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnhapusMouseClicked(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("HAPUS");

        javax.swing.GroupLayout btnhapusLayout = new javax.swing.GroupLayout(btnhapus);
        btnhapus.setLayout(btnhapusLayout);
        btnhapusLayout.setHorizontalGroup(
            btnhapusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 70, Short.MAX_VALUE)
            .addGroup(btnhapusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(btnhapusLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        btnhapusLayout.setVerticalGroup(
            btnhapusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
            .addGroup(btnhapusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(btnhapusLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        panel_tr.add(btnhapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 350, 70, -1));

        txtstok.setBorder(null);
        txtstok.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txtstok.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        txtstok.setDoubleBuffered(true);
        txtstok.setFont(new java.awt.Font("Bahnschrift", 0, 16)); // NOI18N
        txtstok.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtstokKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtstokKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtstokKeyTyped(evt);
            }
        });
        panel_tr.add(txtstok, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, 110, 30));

        jLabel1.setFont(new java.awt.Font("Bahnschrift", 0, 12)); // NOI18N
        jLabel1.setText("Tekan Enter untuk menghitung");
        panel_tr.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, -1, 20));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panel_tr, javax.swing.GroupLayout.PREFERRED_SIZE, 1045, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_tr, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void selectunitItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_selectunitItemStateChanged
        if (selectunit.getSelectedIndex() != 0) {
            txtstok.setVisible(true);
            try {
                ps = cn.prepareStatement("SELECT harga, stok, kd_barang FROM tampil_stok WHERE nama = ?");
                ps.setString(1, selectunit.getSelectedItem().toString());
                rs = ps.executeQuery();
                if (rs.next()) {
                    hargaperunit = Long.parseLong(rs.getString("harga"));
                    lblhargaperunit.setText(numberToMoney(hargaperunit));
                    stoktersedia = Integer.parseInt(rs.getString("stok"));
                    lblstoktersedia.setText(String.valueOf(stoktersedia));
                    kodebarang = rs.getString("kd_barang");
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else {
            txtstok.setVisible(false);
            hargaperunit = 0;
            stoktersedia = 0;
            kodebarang = "";
            lblhargaperunit.setText("Rp. 0");
            lblstoktersedia.setText("0");
        }
        txtstok.setText("");
        txtsubtotal.setText("Rp. 0");
    }//GEN-LAST:event_selectunitItemStateChanged

    private void txtstokKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtstokKeyPressed

    }//GEN-LAST:event_txtstokKeyPressed

    private void txtstokKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtstokKeyTyped
        if (txtstok.getText().length() >= 2 || evt.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
            evt.consume();
        }
    }//GEN-LAST:event_txtstokKeyTyped

    private void btnorderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnorderMouseClicked
        updateSubTotal();
        if (selectunit.getSelectedIndex() != 0 && !txtstok.getText().equals("")) {
            if (JOptionPane.showConfirmDialog(null, "Apakah anda yakin ingin order unit ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION) == 0) {
                try {
                    String sql = "INSERT INTO tb_order VALUES(?,?,?,?,?,NULL)";
                    ps = cn.prepareStatement(sql);
                    ps.setString(1, lbl_kodeorder.getText());
                    ps.setString(2, lbl_kodetransaksi.getText());
                    ps.setString(3, kodebarang);
                    ps.setString(4, txtstok.getText());
                    ps.setString(5, String.valueOf(subtotal));
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Berhasil diinput!");
                    setCodeFirst();
                    tampilData();
                    updateTotal();
                }
                catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Gagal Input Data. Error: " + e);
                }
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "Harap isi semua field", "Validasi", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnorderMouseClicked

    private void btnbayarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnbayarMouseClicked
        if (tabel_order.getRowCount() > 0) {
            new CrudBayar().setVisible(true);
            this.setVisible(false);
        }
        else {
            JOptionPane.showMessageDialog(null, "Harap pesan terlebih dahulu", "Validasi", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnbayarMouseClicked

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        if (!aftercrudpembayaran) {
            new menu().setVisible(true);
        }
    }//GEN-LAST:event_formWindowClosed

    private void btnbatalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnbatalMouseClicked
        reset();
    }//GEN-LAST:event_btnbatalMouseClicked

    private void btnhapusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnhapusMouseClicked
        if (!tabel_order.isRowSelected(0)) {
            JOptionPane.showMessageDialog(null, "Harap pilih order untuk dihapus", "Validasi", JOptionPane.INFORMATION_MESSAGE);
        }
        else {
            if (JOptionPane.showConfirmDialog(null, "Apakah anda yakin ingin menghapusnya?", "Konfirmasi", JOptionPane.YES_NO_OPTION) == 0) {
                try {
                    String id_terancam = tabModel.getValueAt(tabel_order.getSelectedRow(), 0).toString();
                    ps = cn.prepareStatement("DELETE FROM tb_order WHERE kd_order = ?");
                    ps.setString(1, id_terancam);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Berhasil Menghapus Data!");
                    tampilData();
                    setCodeFirst();
                }
                catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Gagal Menghapus Data. Error: " + e, "Gagal", JOptionPane.ERROR_MESSAGE);
                } 
            }
        }
    }//GEN-LAST:event_btnhapusMouseClicked

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        if (JOptionPane.showConfirmDialog(null, "Apakah anda ingin membatalkan transaksi ini? Kode: " + kodetransaksi, "Konfirmasi", JOptionPane.YES_NO_OPTION) == 0) {
            try {
                st = cn.createStatement();
                st.executeUpdate("DELETE FROM tb_order WHERE kd_transaksi = '" + kodetransaksi + "'");
                JOptionPane.showMessageDialog(null, "Transaksi dibatalkan");
                tampilData();
                setCodeFirst();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Gagal Membatalkan Transaksi.\nError: " + ex, "Gagal", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jLabel3MouseClicked

    private void txtstokKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtstokKeyReleased
        if (!txtstok.getText().equals("")) {
            if (Integer.parseInt(txtstok.getText()) <= stoktersedia && txtstok.getText().length() <= 2) {
                updateSubTotal();
            }
            else {
                JOptionPane.showMessageDialog(null, "Stok yang dimasukkan melebihi stok yang tersedia", "Validasi", JOptionPane.ERROR_MESSAGE);
                txtstok.setText("");
            }
        }
        else {
            txtsubtotal.setText("Rp. 0");
        }
    }//GEN-LAST:event_txtstokKeyReleased

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
            java.util.logging.Logger.getLogger(CrudTransaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CrudTransaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CrudTransaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CrudTransaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CrudTransaksi().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel btnbatal;
    private javax.swing.JPanel btnbayar;
    private javax.swing.JPanel btnhapus;
    private javax.swing.JPanel btnhapusall;
    private javax.swing.JPanel btnorder;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lbl_kodeorder;
    private javax.swing.JLabel lbl_kodetransaksi;
    private javax.swing.JLabel lblhargaperunit;
    private javax.swing.JLabel lblstoktersedia;
    private javax.swing.JPanel panel_judul;
    private javax.swing.JPanel panel_tr;
    private javax.swing.JComboBox<String> selectunit;
    private javax.swing.JTable tabel_order;
    private javax.swing.JFormattedTextField txtstok;
    private javax.swing.JLabel txtsubtotal;
    private javax.swing.JLabel txttotal;
    // End of variables declaration//GEN-END:variables
}
