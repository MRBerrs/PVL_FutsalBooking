/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package tampilan;

/**
 *
 * @author Administrator
 */
import com.formdev.flatlaf.FlatDarkLaf;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import koneksi.koneksi;

public class FormLapangan extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FormLapangan.class.getName());

    /**
     * Creates new form FormLapangan
     */
    public FormLapangan() {
        initComponents();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1200, 720));
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        rebuildLayoutLapanganLikeBooking();
        styleFormLapangan();
        datatable();
        setupTableLapangan();
        kosong();
        aktif();
    }
    
    private void aktif() {
    txtNamaLapangan.requestFocus();
    txtKodeLapangan.setEditable(false);
    }
    
    private void kosong() {
    txtKodeLapangan.setText("");
    txtNamaLapangan.setText("");
    cmbJenisLantai.setSelectedIndex(0);
    txtKapasitas.setText("");
    txtHargaPerJam.setText("");
    txtDeskripsi.setText("");
    cmbStatus.setSelectedIndex(0);
    txtCari.setText("");
    autoKodeLapangan();
    }
    
    private void autoKodeLapangan() {
    try {
        Connection conn = koneksi.configDB();

        String sql = "SELECT kode_lapangan FROM lapangan "
                   + "WHERE kode_lapangan LIKE 'LAP-%' "
                   + "ORDER BY kode_lapangan DESC LIMIT 1";

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        if (rs.next()) {
            String kodeTerakhir = rs.getString("kode_lapangan"); // contoh: LAP-G
            String hurufTerakhir = kodeTerakhir.substring(4);    // ambil G

            char huruf = hurufTerakhir.charAt(0);
            huruf++;

            txtKodeLapangan.setText("LAP-" + huruf);
        } else {
            txtKodeLapangan.setText("LAP-A");
        }

        rs.close();
        st.close();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal membuat kode lapangan: " + e.getMessage());
    }
    }
    
    private void datatable() {
    DefaultTableModel tbl = new DefaultTableModel();
    tbl.addColumn("ID");
    tbl.addColumn("Kode Lapangan");
    tbl.addColumn("Nama Lapangan");
    tbl.addColumn("Jenis Lantai");
    tbl.addColumn("Kapasitas");
    tbl.addColumn("Harga Per Jam");
    tbl.addColumn("Deskripsi");
    tbl.addColumn("Status");

    tblLapangan.setModel(tbl);

    try {
        Connection conn = koneksi.configDB();

        String sql = "SELECT id_lapangan, kode_lapangan, nama_lapangan, jenis_lantai, "
                   + "kapasitas, harga_per_jam, deskripsi, status "
                   + "FROM lapangan ORDER BY id_lapangan DESC";

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            tbl.addRow(new Object[]{
                rs.getString("id_lapangan"),
                rs.getString("kode_lapangan"),
                rs.getString("nama_lapangan"),
                rs.getString("jenis_lantai"),
                rs.getString("kapasitas"),
                rs.getString("harga_per_jam"),
                rs.getString("deskripsi"),
                rs.getString("status")
            });
        }

        rs.close();
        st.close();

        setupTableLapangan();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal menampilkan data lapangan: " + e.getMessage());
    }
    }
    
    private String ambilDataTabel(int baris, int kolom) {
    Object value = tblLapangan.getValueAt(baris, kolom);
    return value == null ? "" : value.toString();
    }
    
    private void rebuildLayoutLapanganLikeBooking() {
    getContentPane().removeAll();
    getContentPane().setLayout(new java.awt.BorderLayout());

    jPanel1.removeAll();
    jPanel1.setLayout(new java.awt.BorderLayout(0, 18));
    jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(28, 28, 28, 8));

    javax.swing.JPanel headerPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
    headerPanel.setOpaque(false);
    headerPanel.add(jLabel1, java.awt.BorderLayout.WEST);

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

    jLabel2.setText("PENCARIAN");

    searchPanel.add(jLabel2, java.awt.BorderLayout.NORTH);
    searchPanel.add(searchInputPanel, java.awt.BorderLayout.CENTER);

    javax.swing.JPanel formPanel = new javax.swing.JPanel(new java.awt.GridBagLayout());
    formPanel.setOpaque(false);

    java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
    gbc.insets = new java.awt.Insets(6, 0, 6, 18);
    gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1.0;

    jLabel9.setText("INPUT DATA");

    addTitle(formPanel, gbc, 0, jLabel9);

    jLabel3.setText("Kode Lapangan");
    jLabel4.setText("Nama Lapangan");
    addPair(formPanel, gbc, 1, jLabel3, txtKodeLapangan, jLabel4, txtNamaLapangan);

    jLabel8.setText("Jenis Lantai");
    jLabel5.setText("Kapasitas");
    addPair(formPanel, gbc, 3, jLabel8, cmbJenisLantai, jLabel5, txtKapasitas);

    jLabel6.setText("Harga Per Jam");
    jLabel10.setText("Status");
    addPair(formPanel, gbc, 5, jLabel6, txtHargaPerJam, jLabel10, cmbStatus);

    jLabel7.setText("Deskripsi");
    addFullField(formPanel, gbc, 7, jLabel7, txtDeskripsi);

    javax.swing.JPanel buttonPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 14, 0));
    buttonPanel.setOpaque(false);
    buttonPanel.add(btnSimpan);
    buttonPanel.add(btnEdit);
    buttonPanel.add(btnHapus);
    buttonPanel.add(btnClear);
    buttonPanel.add(btnKeluar);

    gbc.gridx = 0;
    gbc.gridy = 9;
    gbc.gridwidth = 4;
    gbc.insets = new java.awt.Insets(18, 0, 0, 18);
    formPanel.add(buttonPanel, gbc);

    leftPanel.add(searchPanel, java.awt.BorderLayout.NORTH);
    leftPanel.add(formPanel, java.awt.BorderLayout.CENTER);

    javax.swing.JPanel tablePanel = new javax.swing.JPanel(new java.awt.BorderLayout());
    tablePanel.setOpaque(false);
    tablePanel.setPreferredSize(new java.awt.Dimension(560, 10));
    tablePanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);

    contentPanel.add(leftPanel, java.awt.BorderLayout.CENTER);
    contentPanel.add(tablePanel, java.awt.BorderLayout.EAST);

    jPanel1.add(headerPanel, java.awt.BorderLayout.NORTH);
    jPanel1.add(contentPanel, java.awt.BorderLayout.CENTER);

    getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

    getContentPane().revalidate();
    getContentPane().repaint();
    }
    
    private void addTitle(javax.swing.JPanel panel, java.awt.GridBagConstraints gbc, int row, java.awt.Component component) {
    gbc.gridx = 0;
    gbc.gridy = row;
    gbc.gridwidth = 4;
    gbc.weightx = 1.0;
    gbc.insets = new java.awt.Insets(0, 0, 10, 18);
    panel.add(component, gbc);
    }
    
    private void addPair(javax.swing.JPanel panel, java.awt.GridBagConstraints gbc, int row,
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
    
    private void addFullField(javax.swing.JPanel panel, java.awt.GridBagConstraints gbc, int row,
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
    
    private void styleComboBox(javax.swing.JComboBox<String> combo, java.awt.Color bg,
        java.awt.Color fg, java.awt.Color border) {

    combo.setBackground(bg);
    combo.setForeground(fg);
    combo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
    combo.setBorder(javax.swing.BorderFactory.createLineBorder(border, 1));
    combo.setPreferredSize(new java.awt.Dimension(combo.getPreferredSize().width, 38));
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

    button.setBorder(javax.swing.BorderFactory.createCompoundBorder(
        javax.swing.BorderFactory.createLineBorder(border, 1),
        javax.swing.BorderFactory.createEmptyBorder(6, 16, 6, 16)
    ));
    }
    
    private void setupTableLapangan() {
    tblLapangan.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
    tblLapangan.setFillsViewportHeight(true);
    tblLapangan.setRowHeight(34);
    tblLapangan.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

    tblLapangan.setBackground(AppTheme.ABYSS);
    tblLapangan.setForeground(AppTheme.SLATE);
    tblLapangan.setGridColor(AppTheme.RIM);
    tblLapangan.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
    tblLapangan.setSelectionBackground(AppTheme.INDIGO_GHOST);
    tblLapangan.setSelectionForeground(AppTheme.SNOW);
    tblLapangan.setShowVerticalLines(false);
    tblLapangan.setShowHorizontalLines(true);

    tblLapangan.getTableHeader().setBackground(AppTheme.COURT);
    tblLapangan.getTableHeader().setForeground(AppTheme.INDIGO_LIGHT);
    tblLapangan.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));

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
    
    private void styleFormLapangan() {
    getContentPane().setBackground(AppTheme.ABYSS);
    jPanel1.setBackground(AppTheme.ABYSS);
    jPanel1.setOpaque(true);

    jLabel1.setText("DATA LAPANGAN");
    jLabel1.setForeground(AppTheme.SNOW);
    jLabel1.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 34));

    jLabel2.setForeground(AppTheme.INDIGO_LIGHT);
    jLabel2.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));

    jLabel9.setForeground(AppTheme.INDIGO_LIGHT);
    jLabel9.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 24));

    javax.swing.JLabel[] labels = {
        jLabel3, jLabel4, jLabel5, jLabel6, jLabel7, jLabel8, jLabel10
    };

    for (javax.swing.JLabel label : labels) {
        label.setForeground(AppTheme.SLATE);
        label.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
    }

    styleTextField(txtCari, AppTheme.MIDNIGHT, AppTheme.SNOW, AppTheme.RIM, AppTheme.INDIGO_LIGHT);
    styleTextField(txtKodeLapangan, AppTheme.MIDNIGHT, AppTheme.SNOW, AppTheme.RIM, AppTheme.INDIGO_LIGHT);
    styleTextField(txtNamaLapangan, AppTheme.MIDNIGHT, AppTheme.SNOW, AppTheme.RIM, AppTheme.INDIGO_LIGHT);
    styleTextField(txtKapasitas, AppTheme.MIDNIGHT, AppTheme.SNOW, AppTheme.RIM, AppTheme.INDIGO_LIGHT);
    styleTextField(txtHargaPerJam, AppTheme.MIDNIGHT, AppTheme.SNOW, AppTheme.RIM, AppTheme.INDIGO_LIGHT);
    styleTextField(txtDeskripsi, AppTheme.MIDNIGHT, AppTheme.SNOW, AppTheme.RIM, AppTheme.INDIGO_LIGHT);

    styleComboBox(cmbJenisLantai, AppTheme.MIDNIGHT, AppTheme.SNOW, AppTheme.RIM);
    styleComboBox(cmbStatus, AppTheme.MIDNIGHT, AppTheme.SNOW, AppTheme.RIM);

    styleButton(btnCari, "Cari", AppTheme.INDIGO, AppTheme.SNOW, AppTheme.RIM);
    styleButton(btnSimpan, "Simpan", AppTheme.EMERALD_TINT, AppTheme.SNOW, AppTheme.RIM);
    styleButton(btnEdit, "Edit", AppTheme.INDIGO_DEEP, AppTheme.SNOW, AppTheme.RIM);
    styleButton(btnHapus, "Hapus", AppTheme.CORAL_TINT, AppTheme.SNOW, AppTheme.RIM);
    styleButton(btnClear, "Bersihkan", AppTheme.ELEVATED, AppTheme.SNOW, AppTheme.RIM);
    styleButton(btnKeluar, "Keluar", AppTheme.ELEVATED, AppTheme.SLATE, AppTheme.RIM);

    setupTableLapangan();
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
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtKodeLapangan = new javax.swing.JTextField();
        txtNamaLapangan = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtKapasitas = new javax.swing.JTextField();
        txtHargaPerJam = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtDeskripsi = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        cmbJenisLantai = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtCari = new javax.swing.JTextField();
        btnCari = new javax.swing.JButton();
        btnSimpan = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnKeluar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblLapangan = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        cmbStatus = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setPreferredSize(new java.awt.Dimension(800, 800));

        jLabel1.setFont(new java.awt.Font("Arial Black", 1, 24)); // NOI18N
        jLabel1.setText("DATA LAPANGAN");

        jLabel3.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel3.setText("Kode lapangan:");

        txtKodeLapangan.addActionListener(this::txtKodeLapanganActionPerformed);

        txtNamaLapangan.addActionListener(this::txtNamaLapanganActionPerformed);

        jLabel4.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel4.setText("Nama Lapangan:");

        jLabel5.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel5.setText("Kapasitas:");

        txtKapasitas.addActionListener(this::txtKapasitasActionPerformed);

        txtHargaPerJam.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtHargaPerJam.addActionListener(this::txtHargaPerJamActionPerformed);

        jLabel6.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel6.setText("Harga Per jam:");

        jLabel7.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel7.setText("Deskripsi:");

        txtDeskripsi.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtDeskripsi.addActionListener(this::txtDeskripsiActionPerformed);

        jLabel8.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel8.setText("Jenis Lantai:");

        cmbJenisLantai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "vinyl", "rumput_sintetis", "parket", "semen" }));
        cmbJenisLantai.addActionListener(this::cmbJenisLantaiActionPerformed);

        jLabel9.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel9.setText("Input Data");

        jLabel2.setText("Pencarian");

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

        tblLapangan.setModel(new javax.swing.table.DefaultTableModel(
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
        tblLapangan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblLapanganMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblLapangan);

        jLabel10.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel10.setText("Status:");

        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "tersedia", "maintenance", "tidak_aktif" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDeskripsi)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtKodeLapangan, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(cmbJenisLantai, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtKapasitas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNamaLapangan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(txtCari)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(250, 250, 250)
                                .addComponent(jLabel4))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(269, 269, 269)
                                .addComponent(jLabel5))
                            .addComponent(jLabel7)
                            .addComponent(jLabel2)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel9))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtHargaPerJam, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(161, 161, 161))
                            .addComponent(cmbStatus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(27, 27, 27)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCari))
                .addGap(53, 53, 53)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtKodeLapangan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNamaLapangan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel8))
                .addGap(3, 3, 3)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtKapasitas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbJenisLantai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtHargaPerJam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDeskripsi, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSimpan)
                    .addComponent(btnEdit)
                    .addComponent(btnHapus)
                    .addComponent(btnClear)
                    .addComponent(btnKeluar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 507, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 964, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtKodeLapanganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKodeLapanganActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKodeLapanganActionPerformed

    private void txtNamaLapanganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNamaLapanganActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaLapanganActionPerformed

    private void txtKapasitasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKapasitasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKapasitasActionPerformed

    private void txtHargaPerJamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHargaPerJamActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHargaPerJamActionPerformed

    private void txtDeskripsiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDeskripsiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDeskripsiActionPerformed

    private void cmbJenisLantaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbJenisLantaiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbJenisLantaiActionPerformed

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        // TODO add your handling code here:
        DefaultTableModel tbl = new DefaultTableModel();
    tbl.addColumn("ID");
    tbl.addColumn("Kode Lapangan");
    tbl.addColumn("Nama Lapangan");
    tbl.addColumn("Jenis Lantai");
    tbl.addColumn("Kapasitas");
    tbl.addColumn("Harga Per Jam");
    tbl.addColumn("Deskripsi");
    tbl.addColumn("Status");

    tblLapangan.setModel(tbl);

    try {
        Connection conn = koneksi.configDB();

        String sql = "SELECT id_lapangan, kode_lapangan, nama_lapangan, jenis_lantai, "
                   + "kapasitas, harga_per_jam, deskripsi, status "
                   + "FROM lapangan "
                   + "WHERE kode_lapangan LIKE ? "
                   + "OR nama_lapangan LIKE ? "
                   + "OR jenis_lantai LIKE ? "
                   + "OR status LIKE ? "
                   + "ORDER BY id_lapangan DESC";

        PreparedStatement pst = conn.prepareStatement(sql);
        String cari = "%" + txtCari.getText().trim() + "%";

        pst.setString(1, cari);
        pst.setString(2, cari);
        pst.setString(3, cari);
        pst.setString(4, cari);

        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            tbl.addRow(new Object[]{
                rs.getString("id_lapangan"),
                rs.getString("kode_lapangan"),
                rs.getString("nama_lapangan"),
                rs.getString("jenis_lantai"),
                rs.getString("kapasitas"),
                rs.getString("harga_per_jam"),
                rs.getString("deskripsi"),
                rs.getString("status")
            });
        }

        rs.close();
        pst.close();

        setupTableLapangan();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal mencari data lapangan: " + e.getMessage());
    }
    }//GEN-LAST:event_btnCariActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        // TODO add your handling code here:
         if (txtNamaLapangan.getText().trim().isEmpty()
            || txtKapasitas.getText().trim().isEmpty()
            || txtHargaPerJam.getText().trim().isEmpty()) {

        JOptionPane.showMessageDialog(this, "Nama lapangan, kapasitas, dan harga wajib diisi!");
        return;
    }

    try {
        int kapasitas = Integer.parseInt(txtKapasitas.getText().trim());
        double harga = Double.parseDouble(txtHargaPerJam.getText().trim());

        Connection conn = koneksi.configDB();

        String sql = "INSERT INTO lapangan "
                   + "(kode_lapangan, nama_lapangan, jenis_lantai, kapasitas, harga_per_jam, deskripsi, status) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, txtKodeLapangan.getText());
        pst.setString(2, txtNamaLapangan.getText().trim());
        pst.setString(3, cmbJenisLantai.getSelectedItem().toString());
        pst.setInt(4, kapasitas);
        pst.setDouble(5, harga);
        pst.setString(6, txtDeskripsi.getText().trim());
        pst.setString(7, cmbStatus.getSelectedItem().toString());

        pst.executeUpdate();
        pst.close();

        JOptionPane.showMessageDialog(this, "Data lapangan berhasil disimpan.");
        datatable();
        kosong();
        aktif();

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Kapasitas dan harga harus berupa angka!");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal menyimpan data lapangan: " + e.getMessage());
    }
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        int baris = tblLapangan.getSelectedRow();

    if (baris == -1) {
        JOptionPane.showMessageDialog(this, "Pilih data lapangan yang ingin diedit!");
        return;
    }

    if (txtNamaLapangan.getText().trim().isEmpty()
            || txtKapasitas.getText().trim().isEmpty()
            || txtHargaPerJam.getText().trim().isEmpty()) {

        JOptionPane.showMessageDialog(this, "Nama lapangan, kapasitas, dan harga wajib diisi!");
        return;
    }

    try {
        int kapasitas = Integer.parseInt(txtKapasitas.getText().trim());
        double harga = Double.parseDouble(txtHargaPerJam.getText().trim());

        Connection conn = koneksi.configDB();

        String sql = "UPDATE lapangan SET "
                   + "nama_lapangan = ?, "
                   + "jenis_lantai = ?, "
                   + "kapasitas = ?, "
                   + "harga_per_jam = ?, "
                   + "deskripsi = ?, "
                   + "status = ? "
                   + "WHERE kode_lapangan = ?";

        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, txtNamaLapangan.getText().trim());
        pst.setString(2, cmbJenisLantai.getSelectedItem().toString());
        pst.setInt(3, kapasitas);
        pst.setDouble(4, harga);
        pst.setString(5, txtDeskripsi.getText().trim());
        pst.setString(6, cmbStatus.getSelectedItem().toString());
        pst.setString(7, txtKodeLapangan.getText());

        pst.executeUpdate();
        pst.close();

        JOptionPane.showMessageDialog(this, "Data lapangan berhasil diedit.");
        datatable();
        kosong();
        aktif();

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Kapasitas dan harga harus berupa angka!");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal mengedit data lapangan: " + e.getMessage());
    }
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        kosong();
        aktif();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        // TODO add your handling code here:
        int baris = tblLapangan.getSelectedRow();

    if (baris == -1) {
        JOptionPane.showMessageDialog(this, "Pilih data lapangan yang ingin dihapus!");
        return;
    }

    int jawab = JOptionPane.showConfirmDialog(
            this,
            "Yakin ingin menonaktifkan lapangan ini?",
            "Konfirmasi",
            JOptionPane.YES_NO_OPTION
    );

    if (jawab != JOptionPane.YES_OPTION) {
        return;
    }

    try {
        Connection conn = koneksi.configDB();

        String sql = "UPDATE lapangan SET status = 'tidak_aktif' WHERE kode_lapangan = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, txtKodeLapangan.getText());

        pst.executeUpdate();
        pst.close();

        JOptionPane.showMessageDialog(this, "Data lapangan berhasil dinonaktifkan.");
        datatable();
        kosong();
        aktif();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal menghapus data lapangan: " + e.getMessage());
    }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKeluarActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnKeluarActionPerformed

    private void tblLapanganMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblLapanganMouseClicked
        // TODO add your handling code here:
        int baris = tblLapangan.rowAtPoint(evt.getPoint());

    txtKodeLapangan.setText(ambilDataTabel(baris, 1));
    txtNamaLapangan.setText(ambilDataTabel(baris, 2));
    cmbJenisLantai.setSelectedItem(ambilDataTabel(baris, 3));
    txtKapasitas.setText(ambilDataTabel(baris, 4));
    txtHargaPerJam.setText(ambilDataTabel(baris, 5));
    txtDeskripsi.setText(ambilDataTabel(baris, 6));
    cmbStatus.setSelectedItem(ambilDataTabel(baris, 7));
    }//GEN-LAST:event_tblLapanganMouseClicked

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
        UIManager.put("Button.arc", 16);
        UIManager.put("Component.arc", 14);
        UIManager.put("TextComponent.arc", 14);
        UIManager.put("ScrollBar.width", 12);
        UIManager.put("Table.rowHeight", 30);
        UIManager.put("Table.showHorizontalLines", true);
        UIManager.put("Table.showVerticalLines", true);

        FlatDarkLaf.setup();
    } catch (Exception e) {
        System.out.println("FlatLaf gagal dimuat: " + e.getMessage());
    }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new FormLapangan().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JComboBox<String> cmbJenisLantai;
    private javax.swing.JComboBox<String> cmbStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblLapangan;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtDeskripsi;
    private javax.swing.JTextField txtHargaPerJam;
    private javax.swing.JTextField txtKapasitas;
    private javax.swing.JTextField txtKodeLapangan;
    private javax.swing.JTextField txtNamaLapangan;
    // End of variables declaration//GEN-END:variables
}
