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
    private javax.swing.JComboBox<String> cmbSumberBooking;
    private javax.swing.JLabel lblSumberBooking;
    
    /**
     * Creates new form FormBooking
     */
    public FormBooking() {
        initComponents();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setMinimumSize(new java.awt.Dimension(1200, 720));
    setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
    setResizable(true);
    setupSumberBooking();
    rebuildLayoutBookingLikeMember();
    styleFormBookingModernPurple();
    loadMember();
    loadLapangan();
    loadJadwal();
    datatable();
    setupTableBooking();
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
    cmbSumberBooking.setSelectedItem("Manual");

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
    tbl.addColumn("Sumber");

    tblBooking.setModel(tbl);

    try {
        Connection conn = koneksi.configDB();

        String sql = "SELECT b.kode_booking, b.tanggal_main, m.nama_lengkap AS member, "
                   + "l.nama_lapangan AS lapangan, j.label AS jadwal, "
                   + "b.jumlah_jam, b.total_harga, b.status_booking, b.sumber_booking "
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
                rs.getString("status_booking"),
                rs.getString("sumber_booking")
            });
        }

        rs.close();
        st.close();

        setupTableBooking();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal menampilkan data booking: " + e.getMessage());
    }
    }
    
    private void setupSumberBooking() {
    cmbSumberBooking = new javax.swing.JComboBox<>();
    cmbSumberBooking.addItem("Online");
    cmbSumberBooking.addItem("Manual");
    cmbSumberBooking.addItem("WhatsApp");

    lblSumberBooking = new javax.swing.JLabel("Sumber Booking");
    }
    
    private void rebuildLayoutBookingLikeMember() {
    getContentPane().removeAll();
    getContentPane().setLayout(new java.awt.BorderLayout());

    jPanel3.removeAll();
    jPanel3.setLayout(new java.awt.BorderLayout(0, 18));
    jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(28, 28, 28, 8));

    javax.swing.JPanel headerPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
    headerPanel.setOpaque(false);
    headerPanel.add(jLabel5, java.awt.BorderLayout.WEST);

    javax.swing.JPanel contentPanel = new javax.swing.JPanel(new java.awt.BorderLayout(8, 0));
    contentPanel.setOpaque(false);

    javax.swing.JPanel leftPanel = new javax.swing.JPanel(new java.awt.BorderLayout(0, 22));
    leftPanel.setOpaque(false);

    javax.swing.JPanel searchPanel = new javax.swing.JPanel(new java.awt.BorderLayout(8, 8));
    searchPanel.setOpaque(false);

    javax.swing.JPanel searchInputPanel = new javax.swing.JPanel(new java.awt.BorderLayout(8, 0));
    searchInputPanel.setOpaque(false);
    searchInputPanel.add(txtCari, java.awt.BorderLayout.CENTER);
    searchInputPanel.add(btnCari, java.awt.BorderLayout.EAST);

    jLabel13.setText("PENCARIAN");

    searchPanel.add(jLabel13, java.awt.BorderLayout.NORTH);
    searchPanel.add(searchInputPanel, java.awt.BorderLayout.CENTER);

    javax.swing.JPanel formPanel = new javax.swing.JPanel(new java.awt.GridBagLayout());
    formPanel.setOpaque(false);

    java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
    gbc.insets = new java.awt.Insets(6, 0, 6, 14);
    gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1.0;

    jLabel12.setText("INPUT DATA");

    addTitle(formPanel, gbc, 0, jLabel12);

    jLabel6.setText("Kode Booking");
    jLabel7.setText("Status Booking");
    addPair(formPanel, gbc, 1, jLabel6, txtKodeBooking, jLabel7, cmbStatusBooking);

    jLabel11.setText("Member");
    lblSumberBooking.setText("Sumber Booking");
    addPair(formPanel, gbc, 3, jLabel11, cmbMember, lblSumberBooking, cmbSumberBooking);

    jLabel8.setText("Lapangan");
    jLabel9.setText("Jadwal");
    addPair(formPanel, gbc, 5, jLabel8, cmbLapangan, jLabel9, cmbJadwal);

    jLabel14.setText("Tanggal Main");
    jLabel15.setText("Jumlah Jam");
    addPair(formPanel, gbc, 7, jLabel14, dcTanggalMain, jLabel15, cmbJumlahJam);

    jLabel16.setText("Total Harga");
    jLabel10.setText("Catatan");
    addPair(formPanel, gbc, 9, jLabel16, txtTotalHarga, jLabel10, txtCatatan);

    javax.swing.JPanel buttonPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 14, 0));
    buttonPanel.setOpaque(false);
    buttonPanel.add(btnSimpan);
    buttonPanel.add(btnEdit);
    buttonPanel.add(btnHapus);
    buttonPanel.add(btnClear);
    buttonPanel.add(btnKeluar);

    gbc.gridx = 0;
    gbc.gridy = 11;
    gbc.gridwidth = 4;
    gbc.insets = new java.awt.Insets(18, 0, 0, 14);
    formPanel.add(buttonPanel, gbc);

    leftPanel.add(searchPanel, java.awt.BorderLayout.NORTH);
    leftPanel.add(formPanel, java.awt.BorderLayout.CENTER);

    javax.swing.JPanel tablePanel = new javax.swing.JPanel(new java.awt.BorderLayout());
    tablePanel.setOpaque(false);
    tablePanel.setPreferredSize(new java.awt.Dimension(520, 10));
    tablePanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);

    contentPanel.add(leftPanel, java.awt.BorderLayout.CENTER);
    contentPanel.add(tablePanel, java.awt.BorderLayout.EAST);

    jPanel3.add(headerPanel, java.awt.BorderLayout.NORTH);
    jPanel3.add(contentPanel, java.awt.BorderLayout.CENTER);

    getContentPane().add(jPanel3, java.awt.BorderLayout.CENTER);

    getContentPane().revalidate();
    getContentPane().repaint();
    }
    
    private void addTitle(javax.swing.JPanel panel, java.awt.GridBagConstraints gbc, int row, java.awt.Component component) {
    gbc.gridx = 0;
    gbc.gridy = row;
    gbc.gridwidth = 4;
    gbc.weightx = 1.0;
    gbc.insets = new java.awt.Insets(0, 0, 10, 14);
    panel.add(component, gbc);
    }
    
    private void addPair(javax.swing.JPanel panel, java.awt.GridBagConstraints gbc, int row,
        javax.swing.JLabel label1, java.awt.Component component1,
        javax.swing.JLabel label2, java.awt.Component component2) {

    gbc.gridwidth = 1;
    gbc.insets = new java.awt.Insets(6, 0, 4, 14);

    gbc.gridx = 0;
    gbc.gridy = row;
    gbc.weightx = 1.0;
    panel.add(label1, gbc);

    gbc.gridx = 2;
    gbc.gridy = row;
    gbc.weightx = 1.0;
    panel.add(label2, gbc);

    gbc.insets = new java.awt.Insets(0, 0, 12, 14);

    gbc.gridx = 0;
    gbc.gridy = row + 1;
    gbc.weightx = 1.0;
    panel.add(component1, gbc);

    gbc.gridx = 2;
    gbc.gridy = row + 1;
    gbc.weightx = 1.0;
    panel.add(component2, gbc);
    }
    
    private void styleTextField(javax.swing.JTextField field, java.awt.Color bg,
        java.awt.Color fg, java.awt.Color border, java.awt.Color caret) {

    field.setBackground(bg);
    field.setForeground(fg);
    field.setCaretColor(caret);
    field.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
    field.setOpaque(true);
    field.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(border, 1),
            javax.swing.BorderFactory.createEmptyBorder(6, 10, 6, 10)
    ));
    field.setPreferredSize(new java.awt.Dimension(field.getPreferredSize().width, 38));
    }
    
    private void styleComboBox(javax.swing.JComboBox<String> combo, java.awt.Color bg,
        java.awt.Color fg, java.awt.Color border) {

    combo.setBackground(bg);
    combo.setForeground(fg);
    combo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
    combo.setBorder(javax.swing.BorderFactory.createLineBorder(border, 1));
    combo.setPreferredSize(new java.awt.Dimension(combo.getPreferredSize().width, 38));
    }
    
    private void styleDateChooser(com.toedter.calendar.JDateChooser chooser,
        java.awt.Color bg, java.awt.Color fg, java.awt.Color border, java.awt.Color caret) {

    chooser.setBackground(bg);
    chooser.setForeground(fg);
    chooser.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
    chooser.setBorder(javax.swing.BorderFactory.createLineBorder(border, 1));
    chooser.setPreferredSize(new java.awt.Dimension(chooser.getPreferredSize().width, 38));

    java.awt.Component editor = chooser.getDateEditor().getUiComponent();

    if (editor instanceof javax.swing.JTextField) {
        javax.swing.JTextField textField = (javax.swing.JTextField) editor;
        textField.setBackground(bg);
        textField.setForeground(fg);
        textField.setCaretColor(caret);
        textField.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        textField.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 10, 6, 10));
    }
    }
    
    private void styleButton(javax.swing.JButton button, String text, java.awt.Color bg,
        java.awt.Color fg, java.awt.Color border) {

    button.setText(text);
    button.setBackground(bg);
    button.setForeground(fg);
    button.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
    button.setFocusPainted(false);
    button.setOpaque(true);
    button.setContentAreaFilled(true);
    button.setBorderPainted(false);
    button.putClientProperty("JButton.buttonType", "square");
    button.setPreferredSize(new java.awt.Dimension(104, 42));
    button.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(border, 1),
            javax.swing.BorderFactory.createEmptyBorder(8, 14, 8, 14)
    ));
    }
    
    private void setupTableBooking() {
    tblBooking.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
    tblBooking.setFillsViewportHeight(true);
    tblBooking.setRowHeight(34);
    tblBooking.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

    tblBooking.setBackground(AppTheme.ABYSS);
    tblBooking.setForeground(AppTheme.SLATE);
    tblBooking.setGridColor(AppTheme.RIM);
    tblBooking.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
    tblBooking.setSelectionBackground(AppTheme.INDIGO_GHOST);
    tblBooking.setSelectionForeground(AppTheme.SNOW);
    tblBooking.setShowVerticalLines(false);
    tblBooking.setShowHorizontalLines(true);

    tblBooking.getTableHeader().setBackground(AppTheme.COURT);
    tblBooking.getTableHeader().setForeground(AppTheme.INDIGO_LIGHT);
    tblBooking.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));

    jScrollPane1.getViewport().setBackground(AppTheme.ABYSS);
    jScrollPane1.setBackground(AppTheme.ABYSS);
    jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(AppTheme.RIM, 1));

    jScrollPane1.setVerticalScrollBarPolicy(
            javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
    );

    jScrollPane1.setHorizontalScrollBarPolicy(
            javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
    );
    }
    
    private void styleFormBookingModernPurple() {
    getContentPane().setBackground(AppTheme.ABYSS);
    jPanel3.setBackground(AppTheme.ABYSS);
    jPanel3.setOpaque(true);

    jLabel5.setText("DATA BOOKING");
    jLabel5.setForeground(AppTheme.SNOW);
    jLabel5.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 34));

    jLabel12.setForeground(AppTheme.INDIGO_LIGHT);
    jLabel12.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 24));

    jLabel13.setForeground(AppTheme.INDIGO_LIGHT);
    jLabel13.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));

    javax.swing.JLabel[] labels = {
        jLabel6, jLabel7, jLabel8, jLabel9, jLabel10, jLabel11,
        jLabel14, jLabel15, jLabel16, lblSumberBooking
    };

    for (javax.swing.JLabel label : labels) {
        label.setForeground(AppTheme.SLATE);
        label.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
    }

    styleTextField(txtCari, AppTheme.MIDNIGHT, AppTheme.SNOW, AppTheme.RIM, AppTheme.INDIGO_LIGHT);
    styleTextField(txtKodeBooking, AppTheme.MIDNIGHT, AppTheme.SNOW, AppTheme.RIM, AppTheme.INDIGO_LIGHT);
    styleTextField(txtCatatan, AppTheme.MIDNIGHT, AppTheme.SNOW, AppTheme.RIM, AppTheme.INDIGO_LIGHT);
    styleTextField(txtTotalHarga, AppTheme.MIDNIGHT, AppTheme.SNOW, AppTheme.RIM, AppTheme.INDIGO_LIGHT);

    styleComboBox(cmbMember, AppTheme.MIDNIGHT, AppTheme.SNOW, AppTheme.RIM);
    styleComboBox(cmbLapangan, AppTheme.MIDNIGHT, AppTheme.SNOW, AppTheme.RIM);
    styleComboBox(cmbJadwal, AppTheme.MIDNIGHT, AppTheme.SNOW, AppTheme.RIM);
    styleComboBox(cmbJumlahJam, AppTheme.MIDNIGHT, AppTheme.SNOW, AppTheme.RIM);
    styleComboBox(cmbStatusBooking, AppTheme.MIDNIGHT, AppTheme.SNOW, AppTheme.RIM);
    styleComboBox(cmbSumberBooking, AppTheme.MIDNIGHT, AppTheme.SNOW, AppTheme.RIM);

    styleDateChooser(dcTanggalMain, AppTheme.MIDNIGHT, AppTheme.SNOW, AppTheme.RIM, AppTheme.INDIGO_LIGHT);

    styleButton(btnCari, "Cari", AppTheme.INDIGO, AppTheme.SNOW, AppTheme.RIM);
    styleButton(btnSimpan, "Simpan", AppTheme.EMERALD_TINT, AppTheme.SNOW, AppTheme.RIM);
    styleButton(btnEdit, "Edit", AppTheme.INDIGO_DEEP, AppTheme.SNOW, AppTheme.RIM);
    styleButton(btnHapus, "Batalkan", AppTheme.CORAL_TINT, AppTheme.SNOW, AppTheme.RIM);
    styleButton(btnClear, "Bersihkan", AppTheme.ELEVATED, AppTheme.SNOW, AppTheme.RIM);
    styleButton(btnKeluar, "Keluar", AppTheme.ELEVATED, AppTheme.SLATE, AppTheme.RIM);

    setupTableBooking();
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
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(txtCari)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel12)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(168, 168, 168)
                                .addComponent(jLabel7))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(250, 250, 250)
                                .addComponent(cmbStatusBooking, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtKodeBooking, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(cmbMember, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(cmbJumlahJam, javax.swing.GroupLayout.Alignment.LEADING, 0, 200, Short.MAX_VALUE)
                                        .addComponent(cmbJadwal, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jLabel15))
                                .addGap(50, 50, 50)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel16)
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(cmbLapangan, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel14)
                                        .addComponent(jLabel8)
                                        .addComponent(dcTanggalMain, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                                        .addComponent(txtTotalHarga))))
                            .addComponent(jLabel10)
                            .addComponent(txtCatatan, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addGap(27, 27, 27)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCari))
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addGap(5, 5, 5)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jLabel14))
                        .addGap(8, 8, 8)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbJadwal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dcTanggalMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(jLabel16))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbJumlahJam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTotalHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCatatan, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnSimpan)
                            .addComponent(btnEdit)
                            .addComponent(btnHapus)
                            .addComponent(btnClear)
                            .addComponent(btnKeluar)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(742, 742, 742))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 691, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 545, javax.swing.GroupLayout.PREFERRED_SIZE)
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
    tbl.addColumn("Sumber");

    tblBooking.setModel(tbl);

    try {
        Connection conn = koneksi.configDB();

        String sql = "SELECT b.kode_booking, b.tanggal_main, m.nama_lengkap AS member, "
                   + "l.nama_lapangan AS lapangan, j.label AS jadwal, "
                   + "b.jumlah_jam, b.total_harga, b.status_booking, b.sumber_booking "
                   + "FROM booking b "
                   + "JOIN members m ON b.id_member = m.id_member "
                   + "JOIN lapangan l ON b.id_lapangan = l.id_lapangan "
                   + "JOIN jadwal j ON b.id_jadwal = j.id_jadwal "
                   + "WHERE b.kode_booking LIKE ? "
                   + "OR m.nama_lengkap LIKE ? "
                   + "OR l.nama_lapangan LIKE ? "
                   + "OR b.status_booking LIKE ? "
                   + "OR b.sumber_booking LIKE ? "
                   + "ORDER BY b.id_booking DESC";

        PreparedStatement pst = conn.prepareStatement(sql);
        String cari = "%" + txtCari.getText().trim() + "%";

        pst.setString(1, cari);
        pst.setString(2, cari);
        pst.setString(3, cari);
        pst.setString(4, cari);
        pst.setString(5, cari);

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
                rs.getString("status_booking"),
                rs.getString("sumber_booking")
            });
        }

        rs.close();
        pst.close();

        setupTableBooking();

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
                + "jumlah_jam, total_harga, catatan, status_booking, sumber_booking) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
        pst.setString(11, cmbSumberBooking.getSelectedItem().toString());

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
           + "status_booking = ?, "
           + "sumber_booking = ? "
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
        pst.setString(9, cmbSumberBooking.getSelectedItem().toString());
        pst.setString(10, txtKodeBooking.getText());

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
            cmbSumberBooking.setSelectedItem(rs.getString("sumber_booking"));
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
        System.setProperty("flatlaf.useNativeLibrary", "false");

        javax.swing.UIManager.put("Button.arc", 10);
        javax.swing.UIManager.put("Component.arc", 10);
        javax.swing.UIManager.put("TextComponent.arc", 8);
        javax.swing.UIManager.put("Table.rowHeight", 28);

        com.formdev.flatlaf.FlatDarkLaf.setup();
    } catch (Exception e) {
        System.out.println("FlatLaf gagal dimuat: " + e.getMessage());
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
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblBooking;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtCatatan;
    private javax.swing.JTextField txtKodeBooking;
    private javax.swing.JTextField txtTotalHarga;
    // End of variables declaration//GEN-END:variables
}
