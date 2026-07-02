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
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import koneksi.koneksi;
public class FormPembayaran extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FormPembayaran.class.getName());

    /**
     * Creates new form FormPembayaran
     */
    public FormPembayaran() {
         initComponents();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1200, 720));
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        rebuildLayoutPembayaranLikeBooking();
        styleFormPembayaran();
        loadBooking();
        datatable();
        setupTablePembayaran();
        kosong();
        aktif();
    }
    
    private void aktif() {
    txtKodePembayaran.setEditable(false);
    txtNamaMember.setEditable(false);
    txtNamaLapangan.setEditable(false);
    txtTotalTagihan.setEditable(false);

    cmbBooking.requestFocus();
    }
    
    private void kosong() {
    txtKodePembayaran.setText("");
    txtNamaMember.setText("");
    txtNamaLapangan.setText("");
    txtTotalTagihan.setText("");
    txtJumlahBayar.setText("");
    txtKeterangan.setText("");
    txtCari.setText("");

    if (cmbBooking.getItemCount() > 0) {
        cmbBooking.setSelectedIndex(0);
    }

    cmbMetodeBayar.setSelectedItem("tunai");
    cmbStatusBayar.setSelectedItem("lunas");

    autoKodePembayaran();
    detailBooking();
    }
    
    private void autoKodePembayaran() {
    try {
        Connection conn = koneksi.configDB();

        String tanggal = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String prefix = "PAY-" + tanggal + "-";

        String sql = "SELECT MAX(RIGHT(kode_pembayaran, 3)) AS nomor "
                   + "FROM pembayaran WHERE kode_pembayaran LIKE ?";

        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, prefix + "%");

        ResultSet rs = pst.executeQuery();

        if (rs.next() && rs.getString("nomor") != null) {
            int nomor = Integer.parseInt(rs.getString("nomor")) + 1;
            txtKodePembayaran.setText(prefix + String.format("%03d", nomor));
        } else {
            txtKodePembayaran.setText(prefix + "001");
        }

        rs.close();
        pst.close();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal membuat kode pembayaran: " + e.getMessage());
    }
    }
    
    private int ambilIdCombo(String text) {
    try {
        return Integer.parseInt(text.split(" - ")[0]);
    } catch (Exception e) {
        return 0;
    }
    }
    
    private void pilihBookingById(int idBooking) {
    for (int i = 0; i < cmbBooking.getItemCount(); i++) {
        String item = cmbBooking.getItemAt(i).toString();

        if (ambilIdCombo(item) == idBooking) {
            cmbBooking.setSelectedIndex(i);
            break;
        }
    }
    }
    
    private void loadBooking() {
    cmbBooking.removeAllItems();

    try {
        Connection conn = koneksi.configDB();

        String sql = "SELECT id_booking, kode_booking "
                   + "FROM booking "
                   + "WHERE status_booking <> 'batal' "
                   + "ORDER BY id_booking DESC";

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            cmbBooking.addItem(
                    rs.getInt("id_booking") + " - " + rs.getString("kode_booking")
            );
        }

        rs.close();
        st.close();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal load booking: " + e.getMessage());
    }
    }
    
    private void detailBooking() {
    if (cmbBooking.getSelectedItem() == null) {
        return;
    }

    try {
        int idBooking = ambilIdCombo(cmbBooking.getSelectedItem().toString());

        Connection conn = koneksi.configDB();

        String sql = "SELECT b.total_harga, m.nama_lengkap, l.nama_lapangan "
                   + "FROM booking b "
                   + "JOIN members m ON b.id_member = m.id_member "
                   + "JOIN lapangan l ON b.id_lapangan = l.id_lapangan "
                   + "WHERE b.id_booking = ?";

        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, idBooking);

        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            txtNamaMember.setText(rs.getString("nama_lengkap"));
            txtNamaLapangan.setText(rs.getString("nama_lapangan"));
            txtTotalTagihan.setText(rs.getString("total_harga"));

            if (txtJumlahBayar.getText().trim().isEmpty()) {
                txtJumlahBayar.setText(rs.getString("total_harga"));
            }
        }

        rs.close();
        pst.close();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal mengambil detail booking: " + e.getMessage());
    }
    }
    
    private void datatable() {
    DefaultTableModel tbl = new DefaultTableModel();
    tbl.addColumn("Kode Pembayaran");
    tbl.addColumn("Tanggal Bayar");
    tbl.addColumn("Kode Booking");
    tbl.addColumn("Nama Member");
    tbl.addColumn("Nama Lapangan");
    tbl.addColumn("Total Tagihan");
    tbl.addColumn("Jumlah Bayar");
    tbl.addColumn("Metode");
    tbl.addColumn("Status");

    tblPembayaran.setModel(tbl);

    try {
        Connection conn = koneksi.configDB();

        String sql = "SELECT p.kode_pembayaran, p.tanggal_bayar, b.kode_booking, "
                   + "m.nama_lengkap AS nama_member, l.nama_lapangan, "
                   + "b.total_harga, p.jumlah_bayar, p.metode_bayar, p.status_bayar "
                   + "FROM pembayaran p "
                   + "JOIN booking b ON p.id_booking = b.id_booking "
                   + "JOIN members m ON b.id_member = m.id_member "
                   + "JOIN lapangan l ON b.id_lapangan = l.id_lapangan "
                   + "ORDER BY p.id_pembayaran DESC";

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            tbl.addRow(new Object[]{
                rs.getString("kode_pembayaran"),
                rs.getString("tanggal_bayar"),
                rs.getString("kode_booking"),
                rs.getString("nama_member"),
                rs.getString("nama_lapangan"),
                rs.getString("total_harga"),
                rs.getString("jumlah_bayar"),
                rs.getString("metode_bayar"),
                rs.getString("status_bayar")
            });
        }

        rs.close();
        st.close();
        
        setupTablePembayaran();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal menampilkan data pembayaran: " + e.getMessage());
    }
    }
    
    private void rebuildLayoutPembayaranLikeBooking() {
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
    gbc.insets = new java.awt.Insets(6, 0, 6, 18);
    gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1.0;

    jLabel12.setText("INPUT DATA");

    addTitlePembayaran(formPanel, gbc, 0, jLabel12);

    jLabel6.setText("Kode Pembayaran");
    jLabel7.setText("Booking");
    addPairPembayaran(formPanel, gbc, 1, jLabel6, txtKodePembayaran, jLabel7, cmbBooking);

    jLabel11.setText("Nama Member");
    jLabel8.setText("Nama Lapangan");
    addPairPembayaran(formPanel, gbc, 3, jLabel11, txtNamaMember, jLabel8, txtNamaLapangan);

    jLabel9.setText("Total Tagihan");
    jLabel14.setText("Jumlah Bayar");
    addPairPembayaran(formPanel, gbc, 5, jLabel9, txtTotalTagihan, jLabel14, txtJumlahBayar);

    jLabel15.setText("Metode Bayar");
    jLabel16.setText("Status Bayar");
    addPairPembayaran(formPanel, gbc, 7, jLabel15, cmbMetodeBayar, jLabel16, cmbStatusBayar);

    jLabel10.setText("Keterangan");
    addFullFieldPembayaran(formPanel, gbc, 9, jLabel10, txtKeterangan);

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
    gbc.insets = new java.awt.Insets(18, 0, 0, 18);
    formPanel.add(buttonPanel, gbc);

    leftPanel.add(searchPanel, java.awt.BorderLayout.NORTH);
    leftPanel.add(formPanel, java.awt.BorderLayout.CENTER);

    javax.swing.JPanel tablePanel = new javax.swing.JPanel(new java.awt.BorderLayout());
    tablePanel.setOpaque(false);
    tablePanel.setPreferredSize(new java.awt.Dimension(620, 10));
    tablePanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);

    contentPanel.add(leftPanel, java.awt.BorderLayout.CENTER);
    contentPanel.add(tablePanel, java.awt.BorderLayout.EAST);

    jPanel3.add(headerPanel, java.awt.BorderLayout.NORTH);
    jPanel3.add(contentPanel, java.awt.BorderLayout.CENTER);

    getContentPane().add(jPanel3, java.awt.BorderLayout.CENTER);

    getContentPane().revalidate();
    getContentPane().repaint();
    }
    
    private void addTitlePembayaran(javax.swing.JPanel panel, java.awt.GridBagConstraints gbc, int row, java.awt.Component component) {
    gbc.gridx = 0;
    gbc.gridy = row;
    gbc.gridwidth = 4;
    gbc.weightx = 1.0;
    gbc.insets = new java.awt.Insets(0, 0, 10, 18);
    panel.add(component, gbc);
    }
    
    private void addPairPembayaran(javax.swing.JPanel panel, java.awt.GridBagConstraints gbc, int row,
        javax.swing.JLabel label1, java.awt.Component component1,
        javax.swing.JLabel label2, java.awt.Component component2) {

    gbc.gridwidth = 1;
    gbc.insets = new java.awt.Insets(6, 0, 4, 18);

    gbc.gridx = 0;
    gbc.gridy = row;
    gbc.weightx = 1.0;
    panel.add(label1, gbc);

    gbc.gridx = 2;
    gbc.gridy = row;
    gbc.weightx = 1.0;
    panel.add(label2, gbc);

    gbc.insets = new java.awt.Insets(0, 0, 12, 18);

    gbc.gridx = 0;
    gbc.gridy = row + 1;
    gbc.weightx = 1.0;
    panel.add(component1, gbc);

    gbc.gridx = 2;
    gbc.gridy = row + 1;
    gbc.weightx = 1.0;
    panel.add(component2, gbc);
    }
    
    private void addFullFieldPembayaran(javax.swing.JPanel panel, java.awt.GridBagConstraints gbc, int row,
        javax.swing.JLabel label, java.awt.Component component) {

    gbc.gridwidth = 4;
    gbc.insets = new java.awt.Insets(6, 0, 4, 18);
    gbc.gridx = 0;
    gbc.gridy = row;
    gbc.weightx = 1.0;
    panel.add(label, gbc);

    gbc.insets = new java.awt.Insets(0, 0, 12, 18);
    gbc.gridx = 0;
    gbc.gridy = row + 1;
    gbc.weightx = 1.0;
    panel.add(component, gbc);
    }
    
    private void styleFormPembayaran() {
    getContentPane().setBackground(AppTheme.ABYSS);
    jPanel3.setBackground(AppTheme.ABYSS);
    jPanel3.setOpaque(true);

    jLabel5.setText("DATA PEMBAYARAN");
    jLabel5.setForeground(AppTheme.SNOW);
    jLabel5.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 34));

    jLabel13.setForeground(AppTheme.INDIGO_LIGHT);
    jLabel13.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));

    jLabel12.setForeground(AppTheme.INDIGO_LIGHT);
    jLabel12.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 24));

    javax.swing.JLabel[] labels = {
        jLabel6, jLabel7, jLabel8, jLabel9, jLabel10, jLabel11,
        jLabel14, jLabel15, jLabel16
    };

    for (javax.swing.JLabel label : labels) {
        label.setForeground(AppTheme.SLATE);
        label.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
    }

    styleTextField(txtCari, AppTheme.MIDNIGHT, AppTheme.SNOW, AppTheme.RIM, AppTheme.INDIGO_LIGHT);
    styleTextField(txtKodePembayaran, AppTheme.MIDNIGHT, AppTheme.SNOW, AppTheme.RIM, AppTheme.INDIGO_LIGHT);
    styleTextField(txtNamaMember, AppTheme.MIDNIGHT, AppTheme.SNOW, AppTheme.RIM, AppTheme.INDIGO_LIGHT);
    styleTextField(txtNamaLapangan, AppTheme.MIDNIGHT, AppTheme.SNOW, AppTheme.RIM, AppTheme.INDIGO_LIGHT);
    styleTextField(txtTotalTagihan, AppTheme.MIDNIGHT, AppTheme.SNOW, AppTheme.RIM, AppTheme.INDIGO_LIGHT);
    styleTextField(txtJumlahBayar, AppTheme.MIDNIGHT, AppTheme.SNOW, AppTheme.RIM, AppTheme.INDIGO_LIGHT);
    styleTextField(txtKeterangan, AppTheme.MIDNIGHT, AppTheme.SNOW, AppTheme.RIM, AppTheme.INDIGO_LIGHT);

    styleComboBox(cmbBooking, AppTheme.MIDNIGHT, AppTheme.SNOW, AppTheme.RIM);
    styleComboBox(cmbMetodeBayar, AppTheme.MIDNIGHT, AppTheme.SNOW, AppTheme.RIM);
    styleComboBox(cmbStatusBayar, AppTheme.MIDNIGHT, AppTheme.SNOW, AppTheme.RIM);

    styleButton(btnCari, "Cari", AppTheme.INDIGO, AppTheme.SNOW, AppTheme.RIM);
    styleButton(btnSimpan, "Simpan", AppTheme.EMERALD_TINT, AppTheme.SNOW, AppTheme.RIM);
    styleButton(btnEdit, "Edit", AppTheme.INDIGO_DEEP, AppTheme.SNOW, AppTheme.RIM);
    styleButton(btnHapus, "Hapus", AppTheme.CORAL_TINT, AppTheme.SNOW, AppTheme.RIM);
    styleButton(btnClear, "Bersihkan", AppTheme.ELEVATED, AppTheme.SNOW, AppTheme.RIM);
    styleButton(btnKeluar, "Keluar", AppTheme.ELEVATED, AppTheme.SLATE, AppTheme.RIM);

    setupTablePembayaran();
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
    
    private void setupTablePembayaran() {
    tblPembayaran.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
    tblPembayaran.setFillsViewportHeight(true);
    tblPembayaran.setRowHeight(34);
    tblPembayaran.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

    tblPembayaran.setBackground(AppTheme.ABYSS);
    tblPembayaran.setForeground(AppTheme.SLATE);
    tblPembayaran.setGridColor(AppTheme.RIM);
    tblPembayaran.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
    tblPembayaran.setSelectionBackground(AppTheme.INDIGO_GHOST);
    tblPembayaran.setSelectionForeground(AppTheme.SNOW);
    tblPembayaran.setShowVerticalLines(false);
    tblPembayaran.setShowHorizontalLines(true);

    tblPembayaran.getTableHeader().setBackground(AppTheme.COURT);
    tblPembayaran.getTableHeader().setForeground(AppTheme.INDIGO_LIGHT);
    tblPembayaran.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));

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
        txtKodePembayaran = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtKeterangan = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
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
        tblPembayaran = new javax.swing.JTable();
        jLabel14 = new javax.swing.JLabel();
        cmbBooking = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        cmbMetodeBayar = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        txtNamaMember = new javax.swing.JTextField();
        txtNamaLapangan = new javax.swing.JTextField();
        txtTotalTagihan = new javax.swing.JTextField();
        txtJumlahBayar = new javax.swing.JTextField();
        cmbStatusBayar = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel3.setPreferredSize(new java.awt.Dimension(800, 800));

        jLabel5.setFont(new java.awt.Font("Arial Black", 1, 24)); // NOI18N
        jLabel5.setText("DATA BOOKING");

        jLabel6.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel6.setText("Kode Pembayaran:");

        txtKodePembayaran.addActionListener(this::txtKodePembayaranActionPerformed);

        jLabel7.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel7.setText("Status Booking:");

        jLabel8.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel8.setText("Lapangan:");

        jLabel9.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel9.setText("Total Tagihan:");

        jLabel10.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel10.setText("Keterangan:");

        txtKeterangan.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtKeterangan.addActionListener(this::txtKeteranganActionPerformed);

        jLabel11.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel11.setText("Nama Member:");

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

        tblPembayaran.setModel(new javax.swing.table.DefaultTableModel(
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
        tblPembayaran.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPembayaranMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblPembayaran);

        jLabel14.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel14.setText("Jumlah Bayar:");

        cmbBooking.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "pending", "konfirmasi", "selesai", "batal" }));
        cmbBooking.addActionListener(this::cmbBookingActionPerformed);

        jLabel15.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel15.setText("Metode Pembayaran:");

        cmbMetodeBayar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "tunai", "transfer", "qris", "debit", "kredit" }));
        cmbMetodeBayar.addActionListener(this::cmbMetodeBayarActionPerformed);

        jLabel16.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel16.setText("Status Bayar:");

        cmbStatusBayar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "lunas", "dp", "belum_bayar" }));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtKeterangan)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(txtCari)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtKodePembayaran, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(txtNamaMember)
                                .addGap(48, 48, 48)))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(cmbBooking, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(141, 141, 141)
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
                        .addComponent(btnKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(167, 167, 167)
                        .addComponent(jLabel14))
                    .addComponent(txtNamaLapangan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel15)
                            .addComponent(cmbMetodeBayar, 0, 200, Short.MAX_VALUE)
                            .addComponent(txtTotalTagihan))
                        .addGap(48, 48, 48)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtJumlahBayar)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(cmbStatusBayar, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addGap(5, 5, 5)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtKodePembayaran, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbBooking, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel11))
                .addGap(3, 3, 3)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNamaMember, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNamaLapangan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTotalTagihan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtJumlahBayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbMetodeBayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbStatusBayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtKeterangan, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 472, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 968, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtKodePembayaranActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKodePembayaranActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKodePembayaranActionPerformed

    private void txtKeteranganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKeteranganActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKeteranganActionPerformed

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        // TODO add your handling code here:
        DefaultTableModel tbl = new DefaultTableModel();
    tbl.addColumn("Kode Pembayaran");
    tbl.addColumn("Tanggal Bayar");
    tbl.addColumn("Kode Booking");
    tbl.addColumn("Nama Member");
    tbl.addColumn("Nama Lapangan");
    tbl.addColumn("Total Tagihan");
    tbl.addColumn("Jumlah Bayar");
    tbl.addColumn("Metode");
    tbl.addColumn("Status");

    tblPembayaran.setModel(tbl);

    try {
        Connection conn = koneksi.configDB();

        String sql = "SELECT p.kode_pembayaran, p.tanggal_bayar, b.kode_booking, "
                   + "m.nama_lengkap AS nama_member, l.nama_lapangan, "
                   + "b.total_harga, p.jumlah_bayar, p.metode_bayar, p.status_bayar "
                   + "FROM pembayaran p "
                   + "JOIN booking b ON p.id_booking = b.id_booking "
                   + "JOIN members m ON b.id_member = m.id_member "
                   + "JOIN lapangan l ON b.id_lapangan = l.id_lapangan "
                   + "WHERE p.kode_pembayaran LIKE ? "
                   + "OR b.kode_booking LIKE ? "
                   + "OR m.nama_lengkap LIKE ? "
                   + "OR l.nama_lapangan LIKE ? "
                   + "OR p.metode_bayar LIKE ? "
                   + "OR p.status_bayar LIKE ? "
                   + "ORDER BY p.id_pembayaran DESC";

        PreparedStatement pst = conn.prepareStatement(sql);
        String cari = "%" + txtCari.getText().trim() + "%";

        pst.setString(1, cari);
        pst.setString(2, cari);
        pst.setString(3, cari);
        pst.setString(4, cari);
        pst.setString(5, cari);
        pst.setString(6, cari);

        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            tbl.addRow(new Object[]{
                rs.getString("kode_pembayaran"),
                rs.getString("tanggal_bayar"),
                rs.getString("kode_booking"),
                rs.getString("nama_member"),
                rs.getString("nama_lapangan"),
                rs.getString("total_harga"),
                rs.getString("jumlah_bayar"),
                rs.getString("metode_bayar"),
                rs.getString("status_bayar")
            });
        }

        rs.close();
        pst.close();

        setupTablePembayaran();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal mencari pembayaran: " + e.getMessage());
    }
    }//GEN-LAST:event_btnCariActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        // TODO add your handling code here:
        if (cmbBooking.getSelectedItem() == null
            || txtJumlahBayar.getText().trim().isEmpty()) {

        JOptionPane.showMessageDialog(this, "Booking dan jumlah bayar wajib diisi!");
        return;
    }

    try {
        int idBooking = ambilIdCombo(cmbBooking.getSelectedItem().toString());
        int idUser = Session.idUser == 0 ? 1 : Session.idUser;
        double jumlahBayar = Double.parseDouble(txtJumlahBayar.getText().trim());

        Connection conn = koneksi.configDB();

        String sql = "INSERT INTO pembayaran "
                   + "(kode_pembayaran, id_booking, id_user, jumlah_bayar, metode_bayar, status_bayar, keterangan) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, txtKodePembayaran.getText());
        pst.setInt(2, idBooking);
        pst.setInt(3, idUser);
        pst.setDouble(4, jumlahBayar);
        pst.setString(5, cmbMetodeBayar.getSelectedItem().toString());
        pst.setString(6, cmbStatusBayar.getSelectedItem().toString());
        pst.setString(7, txtKeterangan.getText().trim());

        pst.executeUpdate();
        pst.close();

        JOptionPane.showMessageDialog(this, "Data pembayaran berhasil disimpan.");
        datatable();
        kosong();
        aktif();

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Jumlah bayar harus berupa angka!");
    } catch (java.sql.SQLIntegrityConstraintViolationException e) {
        JOptionPane.showMessageDialog(this, "Booking ini sudah memiliki pembayaran!");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal menyimpan pembayaran: " + e.getMessage());
    }
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        int baris = tblPembayaran.getSelectedRow();

    if (baris == -1) {
        JOptionPane.showMessageDialog(this, "Pilih data pembayaran yang ingin diedit!");
        return;
    }

    if (txtJumlahBayar.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Jumlah bayar wajib diisi!");
        return;
    }

    try {
        int idBooking = ambilIdCombo(cmbBooking.getSelectedItem().toString());
        double jumlahBayar = Double.parseDouble(txtJumlahBayar.getText().trim());

        Connection conn = koneksi.configDB();

        String sql = "UPDATE pembayaran SET "
                   + "id_booking = ?, "
                   + "jumlah_bayar = ?, "
                   + "metode_bayar = ?, "
                   + "status_bayar = ?, "
                   + "keterangan = ? "
                   + "WHERE kode_pembayaran = ?";

        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, idBooking);
        pst.setDouble(2, jumlahBayar);
        pst.setString(3, cmbMetodeBayar.getSelectedItem().toString());
        pst.setString(4, cmbStatusBayar.getSelectedItem().toString());
        pst.setString(5, txtKeterangan.getText().trim());
        pst.setString(6, txtKodePembayaran.getText());

        pst.executeUpdate();
        pst.close();

        JOptionPane.showMessageDialog(this, "Data pembayaran berhasil diedit.");
        datatable();
        kosong();
        aktif();

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Jumlah bayar harus berupa angka!");
    } catch (java.sql.SQLIntegrityConstraintViolationException e) {
        JOptionPane.showMessageDialog(this, "Booking ini sudah dipakai di pembayaran lain!");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal mengedit pembayaran: " + e.getMessage());
    }
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        kosong();
        aktif();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        // TODO add your handling code here:
        int baris = tblPembayaran.getSelectedRow();

    if (baris == -1) {
        JOptionPane.showMessageDialog(this, "Pilih data pembayaran yang ingin dihapus!");
        return;
    }

    int jawab = JOptionPane.showConfirmDialog(
            this,
            "Yakin ingin menghapus pembayaran ini?",
            "Konfirmasi",
            JOptionPane.YES_NO_OPTION
    );

    if (jawab != JOptionPane.YES_OPTION) {
        return;
    }

    try {
        Connection conn = koneksi.configDB();

        String sql = "DELETE FROM pembayaran WHERE kode_pembayaran = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, txtKodePembayaran.getText());

        pst.executeUpdate();
        pst.close();

        JOptionPane.showMessageDialog(this, "Data pembayaran berhasil dihapus.");
        datatable();
        kosong();
        aktif();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal menghapus pembayaran: " + e.getMessage());
    }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKeluarActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnKeluarActionPerformed

    private void tblPembayaranMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPembayaranMouseClicked
        // TODO add your handling code here:
        int baris = tblPembayaran.rowAtPoint(evt.getPoint());

    if (baris == -1) {
        return;
    }

    String kodePembayaran = tblPembayaran.getValueAt(baris, 0).toString();

    try {
        Connection conn = koneksi.configDB();

        String sql = "SELECT p.*, b.id_booking, b.total_harga, "
                   + "m.nama_lengkap, l.nama_lapangan "
                   + "FROM pembayaran p "
                   + "JOIN booking b ON p.id_booking = b.id_booking "
                   + "JOIN members m ON b.id_member = m.id_member "
                   + "JOIN lapangan l ON b.id_lapangan = l.id_lapangan "
                   + "WHERE p.kode_pembayaran = ?";

        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, kodePembayaran);

        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            txtKodePembayaran.setText(rs.getString("kode_pembayaran"));
            pilihBookingById(rs.getInt("id_booking"));
            txtNamaMember.setText(rs.getString("nama_lengkap"));
            txtNamaLapangan.setText(rs.getString("nama_lapangan"));
            txtTotalTagihan.setText(rs.getString("total_harga"));
            txtJumlahBayar.setText(rs.getString("jumlah_bayar"));
            cmbMetodeBayar.setSelectedItem(rs.getString("metode_bayar"));
            cmbStatusBayar.setSelectedItem(rs.getString("status_bayar"));
            txtKeterangan.setText(rs.getString("keterangan"));
        }

        rs.close();
        pst.close();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal mengambil detail pembayaran: " + e.getMessage());
    }
    }//GEN-LAST:event_tblPembayaranMouseClicked

    private void cmbMetodeBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMetodeBayarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbMetodeBayarActionPerformed

    private void cmbBookingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbBookingActionPerformed
        // TODO add your handling code here:
        detailBooking();
    }//GEN-LAST:event_cmbBookingActionPerformed

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

        javax.swing.UIManager.put("Button.arc", 16);
        javax.swing.UIManager.put("Component.arc", 14);
        javax.swing.UIManager.put("TextComponent.arc", 14);
        javax.swing.UIManager.put("ScrollBar.width", 12);
        javax.swing.UIManager.put("Table.rowHeight", 30);
        javax.swing.UIManager.put("Table.showHorizontalLines", true);
        javax.swing.UIManager.put("Table.showVerticalLines", true);

        com.formdev.flatlaf.FlatDarkLaf.setup();
    } catch (Exception e) {
        System.out.println("FlatLaf gagal dimuat: " + e.getMessage());
    }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new FormPembayaran().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JComboBox<String> cmbBooking;
    private javax.swing.JComboBox<String> cmbMetodeBayar;
    private javax.swing.JComboBox<String> cmbStatusBayar;
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
    private javax.swing.JTable tblPembayaran;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtJumlahBayar;
    private javax.swing.JTextField txtKeterangan;
    private javax.swing.JTextField txtKodePembayaran;
    private javax.swing.JTextField txtNamaLapangan;
    private javax.swing.JTextField txtNamaMember;
    private javax.swing.JTextField txtTotalTagihan;
    // End of variables declaration//GEN-END:variables
}
