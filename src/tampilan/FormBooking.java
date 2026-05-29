/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package tampilan;

/**
 *
 * @author Administrator
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import koneksi.koneksi;

public class FormBooking extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FormBooking.class.getName());
    private Map<String, Double> hargaLapangan = new HashMap<>();

    /**
     * Creates new form FormBooking
     */
    public FormBooking() {
        initComponents();
        setLocationRelativeTo(null);
        loadMember();
        loadLapangan();
        loadJadwal();

        datatable();
        kosong();
        aktif();
    }
    
    private void aktif() {
    txtKodeBooking.setEditable(false);
    txtTotalHarga.setEditable(false);
    dcTanggalMain.requestFocus();
    }
    
    private void kosong() {
    txtKodeBooking.setText("");
    dcTanggalMain.setDate(new Date());
    cmbJumlahJam.setSelectedItem("1");
    txtTotalHarga.setText("");
    txtCatatan.setText("");
    txtCari.setText("");

    if (cmbMember.getItemCount() > 0) {
        cmbMember.setSelectedIndex(0);
    }

    if (cmbLapangan.getItemCount() > 0) {
        cmbLapangan.setSelectedIndex(0);
    }

    if (cmbJadwal.getItemCount() > 0) {
        cmbJadwal.setSelectedIndex(0);
    }

    cmbStatusBooking.setSelectedItem("pending");

    autoKodeBooking();
    hitungTotal();
    }
    
    private void autoKodeBooking() {
    try {
        Connection conn = koneksi.configDB();

        String tanggal = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String prefix = "BK-" + tanggal + "-";

        String sql = "SELECT MAX(RIGHT(kode_booking, 3)) AS nomor "
                   + "FROM booking WHERE kode_booking LIKE ?";

        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, prefix + "%");

        ResultSet rs = pst.executeQuery();

        if (rs.next() && rs.getString("nomor") != null) {
            int nomor = Integer.parseInt(rs.getString("nomor")) + 1;
            txtKodeBooking.setText(prefix + String.format("%03d", nomor));
        } else {
            txtKodeBooking.setText(prefix + "001");
        }

        rs.close();
        pst.close();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal membuat kode booking: " + e.getMessage());
    }
    }

    private int ambilIdCombo(String text) {
    try {
        return Integer.parseInt(text.split(" - ")[0]);
    } catch (Exception e) {
        return 0;
    }
    }
    
    private void pilihComboById(javax.swing.JComboBox combo, int id) {
    for (int i = 0; i < combo.getItemCount(); i++) {
        String item = combo.getItemAt(i).toString();

        if (ambilIdCombo(item) == id) {
            combo.setSelectedIndex(i);
            break;
        }
    }
    }
    
    private boolean validTanggal() {
    if (dcTanggalMain.getDate() == null) {
        JOptionPane.showMessageDialog(this, "Tanggal main harus dipilih!");
        return false;
    }

    return true;
    }
    
    private String getTanggalMain() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    return sdf.format(dcTanggalMain.getDate());
    }

    private void loadMember() {
    cmbMember.removeAllItems();

    try {
        Connection conn = koneksi.configDB();

        String sql = "SELECT id_member, kode_member, nama_lengkap "
                   + "FROM members WHERE is_active = 1 ORDER BY nama_lengkap ASC";

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            cmbMember.addItem(
                    rs.getInt("id_member") + " - "
                    + rs.getString("kode_member") + " - "
                    + rs.getString("nama_lengkap")
            );
        }

        rs.close();
        st.close();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal load member: " + e.getMessage());
    }
    }
    
    private void loadLapangan() {
    cmbLapangan.removeAllItems();
    hargaLapangan.clear();

    try {
        Connection conn = koneksi.configDB();

        String sql = "SELECT id_lapangan, kode_lapangan, nama_lapangan, harga_per_jam "
                   + "FROM lapangan WHERE status = 'tersedia' ORDER BY kode_lapangan ASC";

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            String item = rs.getInt("id_lapangan") + " - "
                    + rs.getString("kode_lapangan") + " - "
                    + rs.getString("nama_lapangan");

            cmbLapangan.addItem(item);
            hargaLapangan.put(item, rs.getDouble("harga_per_jam"));
        }

        rs.close();
        st.close();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal load lapangan: " + e.getMessage());
    }
    }
    
    private void loadJadwal() {
    cmbJadwal.removeAllItems();

    try {
        Connection conn = koneksi.configDB();

        String sql = "SELECT id_jadwal, label FROM jadwal "
                   + "WHERE is_active = 1 ORDER BY jam_mulai ASC";

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            cmbJadwal.addItem(rs.getInt("id_jadwal") + " - " + rs.getString("label"));
        }

        rs.close();
        st.close();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal load jadwal: " + e.getMessage());
    }
    }
    
    private void hitungTotal() {
    try {
        if (cmbLapangan.getSelectedItem() == null || cmbJumlahJam.getSelectedItem() == null) {
            txtTotalHarga.setText("0");
            return;
        }

        String lapangan = cmbLapangan.getSelectedItem().toString();
        double harga = hargaLapangan.getOrDefault(lapangan, 0.0);
        int jumlahJam = Integer.parseInt(cmbJumlahJam.getSelectedItem().toString());

        double total = harga * jumlahJam;
        txtTotalHarga.setText(String.valueOf(total));

    } catch (Exception e) {
        txtTotalHarga.setText("0");
    }
    }
    
    private void datatable() {
    DefaultTableModel tbl = new DefaultTableModel();
    tbl.addColumn("Kode Booking");
    tbl.addColumn("Tanggal Main");
    tbl.addColumn("Member");
    tbl.addColumn("Lapangan");
    tbl.addColumn("Jadwal");
    tbl.addColumn("Jumlah Jam");
    tbl.addColumn("Total Harga");
    tbl.addColumn("Status");

    tblBooking.setModel(tbl);

    try {
        Connection conn = koneksi.configDB();

        String sql = "SELECT b.kode_booking, b.tanggal_main, m.nama_lengkap AS member, "
                   + "l.nama_lapangan AS lapangan, j.label AS jadwal, "
                   + "b.jumlah_jam, b.total_harga, b.status_booking "
                   + "FROM booking b "
                   + "JOIN members m ON b.id_member = m.id_member "
                   + "JOIN lapangan l ON b.id_lapangan = l.id_lapangan "
                   + "JOIN jadwal j ON b.id_jadwal = j.id_jadwal "
                   + "ORDER BY b.id_booking DESC";

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            tbl.addRow(new Object[]{
                rs.getString("kode_booking"),
                rs.getString("tanggal_main"),
                rs.getString("member"),
                rs.getString("lapangan"),
                rs.getString("jadwal"),
                rs.getString("jumlah_jam"),
                rs.getString("total_harga"),
                rs.getString("status_booking")
            });
        }

        rs.close();
        st.close();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal menampilkan data booking: " + e.getMessage());
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

        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtKodeBooking = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtCatatan = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        cmbMember = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtCari = new javax.swing.JTextField();
        btnCari = new javax.swing.JButton();
        btnSimpan = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnKeluar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblBooking = new javax.swing.JTable();
        jLabel14 = new javax.swing.JLabel();
        cmbStatusBooking = new javax.swing.JComboBox<>();
        cmbLapangan = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        cmbJumlahJam = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        cmbJadwal = new javax.swing.JComboBox<>();
        dcTanggalMain = new com.toedter.calendar.JDateChooser();
        txtTotalHarga = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel3.setPreferredSize(new java.awt.Dimension(800, 800));

        jLabel5.setFont(new java.awt.Font("Arial Black", 1, 24)); // NOI18N
        jLabel5.setText("DATA BOOKING");

        jLabel6.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel6.setText("Kode Booking:");

        txtKodeBooking.addActionListener(this::txtKodeBookingActionPerformed);

        jLabel7.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel7.setText("Status Booking:");

        jLabel8.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel8.setText("Lapangan:");

        jLabel9.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel9.setText("Jadwal:");

        jLabel10.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel10.setText("Deskripsi:");

        txtCatatan.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtCatatan.addActionListener(this::txtCatatanActionPerformed);

        jLabel11.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel11.setText("Member:");

        cmbMember.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "item 1", "item 2", "item 3", "item 4" }));
        cmbMember.addActionListener(this::cmbMemberActionPerformed);

        jLabel12.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel12.setText("Input Data");

        jLabel13.setText("Pencarian");

        btnCari.setText("Cari");
        btnCari.addActionListener(this::btnCariActionPerformed);

        btnSimpan.setText("Simpan");
        btnSimpan.addActionListener(this::btnSimpanActionPerformed);

        btnEdit.setText("Edit");
        btnEdit.addActionListener(this::btnEditActionPerformed);

        btnClear.setText("Clear");
        btnClear.addActionListener(this::btnClearActionPerformed);

        btnHapus.setText("Hapus");
        btnHapus.addActionListener(this::btnHapusActionPerformed);

        btnKeluar.setText("Keluar");
        btnKeluar.addActionListener(this::btnKeluarActionPerformed);

        tblBooking.setModel(new javax.swing.table.DefaultTableModel(
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
        tblBooking.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBookingMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblBooking);

        jLabel14.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel14.setText("Tanggal Main:");

        cmbStatusBooking.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "pending", "konfirmasi", "selesai", "batal" }));

        cmbLapangan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbLapangan.addActionListener(this::cmbLapanganActionPerformed);

        jLabel15.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel15.setText("Jumlah Jam:");

        cmbJumlahJam.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", " " }));
        cmbJumlahJam.addActionListener(this::cmbJumlahJamActionPerformed);

        jLabel16.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel16.setText("Total Harga:");

        cmbJadwal.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCatatan)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(txtCari)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtKodeBooking, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(cmbMember, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel8)
                            .addComponent(cmbStatusBooking, 0, 200, Short.MAX_VALUE)
                            .addComponent(cmbLapangan, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(207, 207, 207)
                                .addComponent(jLabel14))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(168, 168, 168)
                                .addComponent(jLabel7))
                            .addComponent(jLabel11)
                            .addComponent(jLabel10)
                            .addComponent(jLabel13)
                            .addComponent(jLabel12)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 2, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbJumlahJam, javax.swing.GroupLayout.Alignment.LEADING, 0, 200, Short.MAX_VALUE)
                            .addComponent(cmbJadwal, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(dcTanggalMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addGap(131, 131, 131))
                            .addComponent(txtTotalHarga))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addGap(27, 27, 27)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCari))
                .addGap(53, 53, 53)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addGap(5, 5, 5)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtKodeBooking, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbStatusBooking, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel11))
                        .addGap(3, 3, 3)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbMember, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbLapangan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jLabel14))
                        .addGap(8, 8, 8)
                        .addComponent(cmbJadwal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(dcTanggalMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbJumlahJam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTotalHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtCatatan, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSimpan)
                    .addComponent(btnEdit)
                    .addComponent(btnHapus)
                    .addComponent(btnClear)
                    .addComponent(btnKeluar))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 968, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtKodeBookingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKodeBookingActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKodeBookingActionPerformed

    private void txtCatatanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCatatanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCatatanActionPerformed

    private void cmbMemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMemberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbMemberActionPerformed

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        // TODO add your handling code here:
        DefaultTableModel tbl = new DefaultTableModel();
    tbl.addColumn("Kode Booking");
    tbl.addColumn("Tanggal Main");
    tbl.addColumn("Member");
    tbl.addColumn("Lapangan");
    tbl.addColumn("Jadwal");
    tbl.addColumn("Jumlah Jam");
    tbl.addColumn("Total Harga");
    tbl.addColumn("Status");

    tblBooking.setModel(tbl);

    try {
        Connection conn = koneksi.configDB();

        String sql = "SELECT b.kode_booking, b.tanggal_main, m.nama_lengkap AS member, "
                   + "l.nama_lapangan AS lapangan, j.label AS jadwal, "
                   + "b.jumlah_jam, b.total_harga, b.status_booking "
                   + "FROM booking b "
                   + "JOIN members m ON b.id_member = m.id_member "
                   + "JOIN lapangan l ON b.id_lapangan = l.id_lapangan "
                   + "JOIN jadwal j ON b.id_jadwal = j.id_jadwal "
                   + "WHERE b.kode_booking LIKE ? "
                   + "OR m.nama_lengkap LIKE ? "
                   + "OR l.nama_lapangan LIKE ? "
                   + "OR b.status_booking LIKE ? "
                   + "ORDER BY b.id_booking DESC";

        PreparedStatement pst = conn.prepareStatement(sql);
        String cari = "%" + txtCari.getText().trim() + "%";

        pst.setString(1, cari);
        pst.setString(2, cari);
        pst.setString(3, cari);
        pst.setString(4, cari);

        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            tbl.addRow(new Object[]{
                rs.getString("kode_booking"),
                rs.getString("tanggal_main"),
                rs.getString("member"),
                rs.getString("lapangan"),
                rs.getString("jadwal"),
                rs.getString("jumlah_jam"),
                rs.getString("total_harga"),
                rs.getString("status_booking")
            });
        }

        rs.close();
        pst.close();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal mencari booking: " + e.getMessage());
    }
    }//GEN-LAST:event_btnCariActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        // TODO add your handling code here:
        if (cmbMember.getSelectedItem() == null
            || cmbLapangan.getSelectedItem() == null
            || cmbJadwal.getSelectedItem() == null) {

        JOptionPane.showMessageDialog(this, "Data booking belum lengkap!");
        return;
    }

    if (!validTanggal()) {
        return;
    }

    try {
        hitungTotal();

        int idMember = ambilIdCombo(cmbMember.getSelectedItem().toString());
        int idLapangan = ambilIdCombo(cmbLapangan.getSelectedItem().toString());
        int idJadwal = ambilIdCombo(cmbJadwal.getSelectedItem().toString());

        int idUser = Session.idUser == 0 ? 1 : Session.idUser;

        int jumlahJam = Integer.parseInt(cmbJumlahJam.getSelectedItem().toString());
        double totalHarga = Double.parseDouble(txtTotalHarga.getText().trim());

        Connection conn = koneksi.configDB();

        String sql = "INSERT INTO booking "
                   + "(kode_booking, id_member, id_lapangan, id_jadwal, id_user, tanggal_main, "
                   + "jumlah_jam, total_harga, catatan, status_booking) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, txtKodeBooking.getText());
        pst.setInt(2, idMember);
        pst.setInt(3, idLapangan);
        pst.setInt(4, idJadwal);
        pst.setInt(5, idUser);
        pst.setString(6, getTanggalMain());
        pst.setInt(7, jumlahJam);
        pst.setDouble(8, totalHarga);
        pst.setString(9, txtCatatan.getText().trim());
        pst.setString(10, cmbStatusBooking.getSelectedItem().toString());

        pst.executeUpdate();
        pst.close();

        JOptionPane.showMessageDialog(this, "Data booking berhasil disimpan.");
        datatable();
        kosong();
        aktif();

    } catch (java.sql.SQLIntegrityConstraintViolationException e) {
        JOptionPane.showMessageDialog(this, "Slot ini sudah dibooking. Pilih lapangan/jadwal/tanggal lain!");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal menyimpan booking: " + e.getMessage());
    }
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        int baris = tblBooking.getSelectedRow();

    if (baris == -1) {
        JOptionPane.showMessageDialog(this, "Pilih data booking yang ingin diedit!");
        return;
    }

    if (!validTanggal()) {
        return;
    }

    try {
        hitungTotal();

        int idMember = ambilIdCombo(cmbMember.getSelectedItem().toString());
        int idLapangan = ambilIdCombo(cmbLapangan.getSelectedItem().toString());
        int idJadwal = ambilIdCombo(cmbJadwal.getSelectedItem().toString());
        int jumlahJam = Integer.parseInt(cmbJumlahJam.getSelectedItem().toString());
        double totalHarga = Double.parseDouble(txtTotalHarga.getText().trim());

        Connection conn = koneksi.configDB();

        String sql = "UPDATE booking SET "
                   + "id_member = ?, "
                   + "id_lapangan = ?, "
                   + "id_jadwal = ?, "
                   + "tanggal_main = ?, "
                   + "jumlah_jam = ?, "
                   + "total_harga = ?, "
                   + "catatan = ?, "
                   + "status_booking = ? "
                   + "WHERE kode_booking = ?";

        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, idMember);
        pst.setInt(2, idLapangan);
        pst.setInt(3, idJadwal);
        pst.setString(4, getTanggalMain());
        pst.setInt(5, jumlahJam);
        pst.setDouble(6, totalHarga);
        pst.setString(7, txtCatatan.getText().trim());
        pst.setString(8, cmbStatusBooking.getSelectedItem().toString());
        pst.setString(9, txtKodeBooking.getText());

        pst.executeUpdate();
        pst.close();

        JOptionPane.showMessageDialog(this, "Data booking berhasil diedit.");
        datatable();
        kosong();
        aktif();

    } catch (java.sql.SQLIntegrityConstraintViolationException e) {
        JOptionPane.showMessageDialog(this, "Slot ini sudah dibooking. Pilih lapangan/jadwal/tanggal lain!");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal mengedit booking: " + e.getMessage());
    }
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        kosong();
        aktif();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        // TODO add your handling code here:
        int baris = tblBooking.getSelectedRow();

    if (baris == -1) {
        JOptionPane.showMessageDialog(this, "Pilih data booking yang ingin dibatalkan!");
        return;
    }

    int jawab = JOptionPane.showConfirmDialog(
            this,
            "Yakin ingin membatalkan booking ini?",
            "Konfirmasi",
            JOptionPane.YES_NO_OPTION
    );

    if (jawab != JOptionPane.YES_OPTION) {
        return;
    }

    try {
        Connection conn = koneksi.configDB();

        String sql = "UPDATE booking SET status_booking = 'batal' WHERE kode_booking = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, txtKodeBooking.getText());

        pst.executeUpdate();
        pst.close();

        JOptionPane.showMessageDialog(this, "Booking berhasil dibatalkan.");
        datatable();
        kosong();
        aktif();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal membatalkan booking: " + e.getMessage());
    }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKeluarActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnKeluarActionPerformed

    private void tblBookingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBookingMouseClicked
        // TODO add your handling code here:
        int baris = tblBooking.rowAtPoint(evt.getPoint());

    if (baris == -1) {
        return;
    }

    String kodeBooking = tblBooking.getValueAt(baris, 0).toString();

    try {
        Connection conn = koneksi.configDB();

        String sql = "SELECT * FROM booking WHERE kode_booking = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, kodeBooking);

        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            txtKodeBooking.setText(rs.getString("kode_booking"));
            pilihComboById(cmbMember, rs.getInt("id_member"));
            pilihComboById(cmbLapangan, rs.getInt("id_lapangan"));
            pilihComboById(cmbJadwal, rs.getInt("id_jadwal"));
            dcTanggalMain.setDate(rs.getDate("tanggal_main"));
            cmbJumlahJam.setSelectedItem(rs.getString("jumlah_jam"));
            txtTotalHarga.setText(rs.getString("total_harga"));
            txtCatatan.setText(rs.getString("catatan"));
            cmbStatusBooking.setSelectedItem(rs.getString("status_booking"));
        }

        rs.close();
        pst.close();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal mengambil detail booking: " + e.getMessage());
    }
    }//GEN-LAST:event_tblBookingMouseClicked

    private void cmbLapanganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbLapanganActionPerformed
        // TODO add your handling code here:
        hitungTotal();
    }//GEN-LAST:event_cmbLapanganActionPerformed

    private void cmbJumlahJamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbJumlahJamActionPerformed
        // TODO add your handling code here:
        hitungTotal();
    }//GEN-LAST:event_cmbJumlahJamActionPerformed

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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new FormBooking().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JComboBox<String> cmbJadwal;
    private javax.swing.JComboBox<String> cmbJumlahJam;
    private javax.swing.JComboBox<String> cmbLapangan;
    private javax.swing.JComboBox<String> cmbMember;
    private javax.swing.JComboBox<String> cmbStatusBooking;
    private com.toedter.calendar.JDateChooser dcTanggalMain;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblBooking;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtCatatan;
    private javax.swing.JTextField txtKodeBooking;
    private javax.swing.JTextField txtKodeLapangan;
    private javax.swing.JTextField txtKodeLapangan1;
    private javax.swing.JTextField txtTotalHarga;
    // End of variables declaration//GEN-END:variables
}
