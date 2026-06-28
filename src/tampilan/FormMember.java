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
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import koneksi.koneksi;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.UIManager;

public class FormMember extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FormMember.class.getName());

    /**
     * Creates new form FormMember
     */
    public FormMember() {
        initComponents();
    setLocationRelativeTo(null);
    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setMinimumSize(new java.awt.Dimension(1200, 720));
    setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
    setResizable(true);
    rebuildLayout();
    styleFormMember();
    datatable();
    setupTableMember();
    kosong();
    aktif();
    }
    
    private void aktif() {
        txtNama.requestFocus();
        txtKodeMember.setEditable(false);
    }
    
    private void kosong() {
        txtKodeMember.setText("");
        txtNama.setText("");
        txtNoTelp.setText("");
        txtAlamat.setText("");
        cmbJenisKelamin.setSelectedIndex(0);
        txtCari.setText("");
        autoKodeMember();;
    }
    
    private void autoKodeMember() {
            try {
        Connection conn = koneksi.configDB();

        String sql = "SELECT MAX(CAST(RIGHT(kode_member, 4) AS UNSIGNED)) AS kode "
                   + "FROM members "
                   + "WHERE kode_member REGEXP '^MBR-[0-9]{4}$'";

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        if (rs.next()) {
            int nomor = rs.getInt("kode");

            if (rs.wasNull()) {
                txtKodeMember.setText("MBR-0001");
            } else {
                nomor = nomor + 1;
                String hasil = String.format("MBR-%04d", nomor);
                txtKodeMember.setText(hasil);
            }
        }

        rs.close();
        st.close();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal membuat kode member: " + e.getMessage());
        }
    }
    
    private void datatable() {
    DefaultTableModel tbl = new DefaultTableModel();
    tbl.addColumn("ID");
    tbl.addColumn("Kode Member");
    tbl.addColumn("Nama Lengkap");
    tbl.addColumn("No WhatsApp");
    tbl.addColumn("Alamat");
    tbl.addColumn("Jenis Kelamin");
    tbl.addColumn("Tanggal Daftar");
    tbl.addColumn("Status");

    tblMember.setModel(tbl);

    try {
        Connection conn = koneksi.configDB();

        String sql = "SELECT id_member, kode_member, nama_lengkap, no_telp, alamat, "
                   + "jenis_kelamin, tanggal_daftar, is_active "
                   + "FROM members ORDER BY id_member DESC";

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            tbl.addRow(new Object[]{
                rs.getString("id_member"),
                rs.getString("kode_member"),
                rs.getString("nama_lengkap"),
                rs.getString("no_telp"),
                rs.getString("alamat"),
                rs.getString("jenis_kelamin"),
                rs.getString("tanggal_daftar"),
                rs.getInt("is_active") == 1 ? "Aktif" : "Tidak Aktif"
            });
        }

        rs.close();
        st.close();

        setupTableMember();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal menampilkan data member: " + e.getMessage());
    }
    }
    
    private void rebuildLayout() {
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

    jLabel3.setText("Kode Member");
    jLabel8.setText("Jenis Kelamin");
    addPair(formPanel, gbc, 1, jLabel3, txtKodeMember, jLabel8, cmbJenisKelamin);

    jLabel4.setText("Nama Lengkap");
    jLabel5.setText("No WhatsApp");
    addPair(formPanel, gbc, 3, jLabel4, txtNama, jLabel5, txtNoTelp);

    jLabel7.setText("Alamat");
    addFullField(formPanel, gbc, 5, jLabel7, txtAlamat);

    javax.swing.JPanel buttonPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 14, 0));
    buttonPanel.setOpaque(false);
    buttonPanel.add(btnSimpan);
    buttonPanel.add(btnEdit);
    buttonPanel.add(btnHapus);
    buttonPanel.add(btnClear);
    buttonPanel.add(btnKeluar);

    gbc.gridx = 0;
    gbc.gridy = 7;
    gbc.gridwidth = 4;
    gbc.insets = new java.awt.Insets(18, 0, 0, 18);
    formPanel.add(buttonPanel, gbc);

    leftPanel.add(searchPanel, java.awt.BorderLayout.NORTH);
    leftPanel.add(formPanel, java.awt.BorderLayout.CENTER);

    javax.swing.JPanel tablePanel = new javax.swing.JPanel(new java.awt.BorderLayout());
    tablePanel.setOpaque(false);
    tablePanel.setPreferredSize(new java.awt.Dimension(520, 10));
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
    button.setPreferredSize(new java.awt.Dimension(104, 42));
    button.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(border, 1),
            javax.swing.BorderFactory.createEmptyBorder(8, 14, 8, 14)
    ));
    }
        
    private void setupTableMember() {
    tblMember.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
    tblMember.setFillsViewportHeight(true);
    tblMember.setRowHeight(34);
    tblMember.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

    tblMember.setBackground(AppTheme.ABYSS);
    tblMember.setForeground(AppTheme.SLATE);
    tblMember.setGridColor(AppTheme.RIM);
    tblMember.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
    tblMember.setSelectionBackground(AppTheme.INDIGO_GHOST);
    tblMember.setSelectionForeground(AppTheme.SNOW);
    tblMember.setShowVerticalLines(false);
    tblMember.setShowHorizontalLines(true);

    tblMember.getTableHeader().setBackground(AppTheme.COURT);
    tblMember.getTableHeader().setForeground(AppTheme.INDIGO_LIGHT);
    tblMember.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));

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
    
    private void styleComboBox(javax.swing.JComboBox<String> combo, java.awt.Color bg,
        java.awt.Color fg, java.awt.Color border) {

    combo.setBackground(bg);
    combo.setForeground(fg);
    combo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
    combo.setBorder(javax.swing.BorderFactory.createLineBorder(border, 1));
    combo.setPreferredSize(new java.awt.Dimension(combo.getPreferredSize().width, 38));
    }
    
    private void styleFormMember() {
    getContentPane().setBackground(AppTheme.ABYSS);
    jPanel1.setBackground(AppTheme.ABYSS);
    jPanel1.setOpaque(true);

    jLabel1.setText("DATA MEMBER");
    jLabel1.setForeground(AppTheme.SNOW);
    jLabel1.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 34));

    jLabel2.setForeground(AppTheme.INDIGO_LIGHT);
    jLabel2.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));

    jLabel9.setForeground(AppTheme.INDIGO_LIGHT);
    jLabel9.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 24));

    javax.swing.JLabel[] labels = {
        jLabel3, jLabel4, jLabel5, jLabel7, jLabel8
    };

    for (javax.swing.JLabel label : labels) {
        label.setForeground(AppTheme.SLATE);
        label.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
    }

    styleTextField(txtCari, AppTheme.MIDNIGHT, AppTheme.SNOW, AppTheme.RIM, AppTheme.INDIGO_LIGHT);
    styleTextField(txtKodeMember, AppTheme.MIDNIGHT, AppTheme.SNOW, AppTheme.RIM, AppTheme.INDIGO_LIGHT);
    styleTextField(txtNama, AppTheme.MIDNIGHT, AppTheme.SNOW, AppTheme.RIM, AppTheme.INDIGO_LIGHT);
    styleTextField(txtNoTelp, AppTheme.MIDNIGHT, AppTheme.SNOW, AppTheme.RIM, AppTheme.INDIGO_LIGHT);
    styleTextField(txtAlamat, AppTheme.MIDNIGHT, AppTheme.SNOW, AppTheme.RIM, AppTheme.INDIGO_LIGHT);

    styleComboBox(cmbJenisKelamin, AppTheme.MIDNIGHT, AppTheme.SNOW, AppTheme.RIM);

    styleButton(btnCari, "Cari", AppTheme.INDIGO, AppTheme.SNOW, AppTheme.RIM);
    styleButton(btnSimpan, "Simpan", AppTheme.EMERALD_TINT, AppTheme.SNOW, AppTheme.RIM);
    styleButton(btnEdit, "Edit", AppTheme.INDIGO_DEEP, AppTheme.SNOW, AppTheme.RIM);
    styleButton(btnHapus, "Hapus", AppTheme.CORAL_TINT, AppTheme.SNOW, AppTheme.RIM);
    styleButton(btnClear, "Bersihkan", AppTheme.ELEVATED, AppTheme.SNOW, AppTheme.RIM);
    styleButton(btnKeluar, "Keluar", AppTheme.ELEVATED, AppTheme.SLATE, AppTheme.RIM);

    setupTableMember();
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
        txtKodeMember = new javax.swing.JTextField();
        txtNama = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtNoTelp = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtAlamat = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        cmbJenisKelamin = new javax.swing.JComboBox<>();
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
        tblMember = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setPreferredSize(new java.awt.Dimension(800, 800));

        jLabel1.setFont(new java.awt.Font("Arial Black", 1, 24)); // NOI18N
        jLabel1.setText("DATA MEMBER ");

        jLabel3.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel3.setText("Kode member            :");

        txtKodeMember.addActionListener(this::txtKodeMemberActionPerformed);

        txtNama.addActionListener(this::txtNamaActionPerformed);

        jLabel4.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel4.setText("Nama Lengkap           :");

        jLabel5.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel5.setText("No Telp                        :");

        txtNoTelp.addActionListener(this::txtNoTelpActionPerformed);

        jLabel7.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel7.setText("Alamat                         :");

        txtAlamat.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtAlamat.addActionListener(this::txtAlamatActionPerformed);

        jLabel8.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel8.setText("Jenis Kelamin              :");

        cmbJenisKelamin.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "L", "P" }));
        cmbJenisKelamin.addActionListener(this::cmbJenisKelaminActionPerformed);

        jLabel9.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel9.setText("Input Data");

        jLabel2.setText("Pencarian");

        btnCari.setText("Cari");
        btnCari.addActionListener(this::btnCariActionPerformed);

        btnSimpan.setText("Simpan");
        btnSimpan.addActionListener(this::btnSimpanActionPerformed);

        btnEdit.setText("Edit");
        btnEdit.addActionListener(this::btnEditActionPerformed);

        btnClear.setText("Hapus");
        btnClear.addActionListener(this::btnClearActionPerformed);

        btnHapus.setText("Hapus");
        btnHapus.addActionListener(this::btnHapusActionPerformed);

        btnKeluar.setText("Keluar");
        btnKeluar.addActionListener(this::btnKeluarActionPerformed);

        tblMember.setModel(new javax.swing.table.DefaultTableModel(
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
        tblMember.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMemberMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblMember);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(372, 372, 372))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtKodeMember, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel5)
                                            .addComponent(jLabel8))
                                        .addGap(66, 66, 66))
                                    .addComponent(cmbJenisKelamin, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtNoTelp, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(txtCari)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtAlamat)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel4)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(27, 27, 27)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCari))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(jLabel8)
                                .addGap(5, 5, 5))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtKodeMember, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbJenisKelamin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(3, 3, 3)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNoTelp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtAlamat, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnSimpan)
                            .addComponent(btnEdit)
                            .addComponent(btnHapus)
                            .addComponent(btnClear)
                            .addComponent(btnKeluar))
                        .addContainerGap())
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 604, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 705, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtKodeMemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKodeMemberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKodeMemberActionPerformed

    private void txtNamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNamaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaActionPerformed

    private void txtNoTelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNoTelpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNoTelpActionPerformed

    private void txtAlamatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAlamatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAlamatActionPerformed

    private void cmbJenisKelaminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbJenisKelaminActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbJenisKelaminActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        // TODO add your handling code here:
        if (txtNama.getText().trim().isEmpty()
            || txtNoTelp.getText().trim().isEmpty()
            || txtAlamat.getText().trim().isEmpty()) {

        JOptionPane.showMessageDialog(this, "Nama, No Telepon, dan Alamat wajib diisi!");
        return;
    }

    try {
        Connection conn = koneksi.configDB();

        String sql = "INSERT INTO members "
           + "(kode_member, nama_lengkap, no_telp, alamat, jenis_kelamin) "
           + "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, txtKodeMember.getText());
            pst.setString(2, txtNama.getText().trim());
            pst.setString(3, txtNoTelp.getText().trim());
            pst.setString(4, txtAlamat.getText().trim());
            pst.setString(5, cmbJenisKelamin.getSelectedItem().toString()); 

        pst.executeUpdate();
        pst.close();

        JOptionPane.showMessageDialog(this, "Data member berhasil disimpan.");
        datatable();
        kosong();
        aktif();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal menyimpan data member: " + e.getMessage());
    }
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        int baris = tblMember.getSelectedRow();

    if (baris == -1) {
        JOptionPane.showMessageDialog(this, "Pilih data member yang ingin diedit!");
        return;
    }

    if (txtNama.getText().trim().isEmpty()
            || txtNoTelp.getText().trim().isEmpty()
            || txtAlamat.getText().trim().isEmpty()) {

        JOptionPane.showMessageDialog(this, "Nama, No Telepon, dan Alamat wajib diisi!");
        return;
    }

    try {
        Connection conn = koneksi.configDB();

        String sql = "UPDATE members SET "
           + "nama_lengkap = ?, "
           + "no_telp = ?, "
           + "alamat = ?, "
           + "jenis_kelamin = ? "
           + "WHERE kode_member = ?";

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, txtNama.getText().trim());
            pst.setString(2, txtNoTelp.getText().trim());
            pst.setString(3, txtAlamat.getText().trim());
            pst.setString(4, cmbJenisKelamin.getSelectedItem().toString());
            pst.setString(5, txtKodeMember.getText());

        pst.executeUpdate();
        pst.close();

        JOptionPane.showMessageDialog(this, "Data member berhasil diedit.");
        datatable();
        kosong();
        aktif();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal mengedit data member: " + e.getMessage());
    }
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        // TODO add your handling code here:
        int baris = tblMember.getSelectedRow();

    if (baris == -1) {
        JOptionPane.showMessageDialog(this, "Pilih data member yang ingin dihapus!");
        return;
    }

    int jawab = JOptionPane.showConfirmDialog(
            this,
            "Yakin ingin menonaktifkan member ini?",
            "Konfirmasi",
            JOptionPane.YES_NO_OPTION
    );

    if (jawab != JOptionPane.YES_OPTION) {
        return;
    }

    try {
        Connection conn = koneksi.configDB();

        String sql = "UPDATE members SET is_active = 0 WHERE kode_member = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, txtKodeMember.getText());

        pst.executeUpdate();
        pst.close();

        JOptionPane.showMessageDialog(this, "Data member berhasil dinonaktifkan.");
        datatable();
        kosong();
        aktif();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal menghapus data member: " + e.getMessage());
    }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        kosong();
        aktif();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKeluarActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnKeluarActionPerformed

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        // TODO add your handling code here:
         DefaultTableModel tbl = new DefaultTableModel();
    tbl.addColumn("ID");
    tbl.addColumn("Kode Member");
    tbl.addColumn("Nama Lengkap");
    tbl.addColumn("No WhatsApp");
    tbl.addColumn("Alamat");
    tbl.addColumn("Jenis Kelamin");
    tbl.addColumn("Tanggal Daftar");
    tbl.addColumn("Status");

    tblMember.setModel(tbl);

    try {
        Connection conn = koneksi.configDB();

        String sql = "SELECT id_member, kode_member, nama_lengkap, no_telp, alamat, "
                   + "jenis_kelamin, tanggal_daftar, is_active "
                   + "FROM members "
                   + "WHERE kode_member LIKE ? OR nama_lengkap LIKE ? OR no_telp LIKE ? "
                   + "ORDER BY id_member DESC";

        PreparedStatement pst = conn.prepareStatement(sql);
        String cari = "%" + txtCari.getText().trim() + "%";
        pst.setString(1, cari);
        pst.setString(2, cari);
        pst.setString(3, cari);

        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            tbl.addRow(new Object[]{
                rs.getString("id_member"),
                rs.getString("kode_member"),
                rs.getString("nama_lengkap"),
                rs.getString("no_telp"),
                rs.getString("alamat"),
                rs.getString("jenis_kelamin"),
                rs.getString("tanggal_daftar"),
                rs.getInt("is_active") == 1 ? "Aktif" : "Tidak Aktif"
            });
        }

        rs.close();
        pst.close();

        setupTableMember();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal mencari data member: " + e.getMessage());
    }
    }//GEN-LAST:event_btnCariActionPerformed

    private void tblMemberMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMemberMouseClicked
        // TODO add your handling code here:
        int baris = tblMember.getSelectedRow();

    if (baris == -1) {
        return;
    }

    txtKodeMember.setText(tblMember.getValueAt(baris, 1).toString());
    txtNama.setText(tblMember.getValueAt(baris, 2).toString());
    txtNoTelp.setText(tblMember.getValueAt(baris, 3).toString());
    txtAlamat.setText(tblMember.getValueAt(baris, 4).toString());
    cmbJenisKelamin.setSelectedItem(tblMember.getValueAt(baris, 5).toString());
    }//GEN-LAST:event_tblMemberMouseClicked

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
        java.awt.EventQueue.invokeLater(() -> new FormMember().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JComboBox<String> cmbJenisKelamin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblMember;
    private javax.swing.JTextField txtAlamat;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtKodeMember;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtNoTelp;
    // End of variables declaration//GEN-END:variables
}
