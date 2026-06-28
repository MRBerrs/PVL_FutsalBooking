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
import javax.swing.JOptionPane;
import koneksi.koneksi;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.UIManager;

public class Login extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Login.class.getName());
    private javax.swing.JLabel lblLogo;
    /**
     * Creates new form Login
     */
    public Login() {
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(900, 560);
        setMinimumSize(new java.awt.Dimension(900, 560));
        setLocationRelativeTo(null);
        setResizable(false);
        rebuildLayoutLogin();
        styleLogin();
    }
    
     private void styleLogin() {
    panelBackground.setBackground(AppTheme.ABYSS);
    panelCard.setBackground(AppTheme.COURT);
    getContentPane().setBackground(AppTheme.ABYSS);

    java.awt.Component wrapper = panelBackground.getComponent(0);

    if (wrapper instanceof javax.swing.JPanel) {
        javax.swing.JPanel wrapperPanel = (javax.swing.JPanel) wrapper;
        wrapperPanel.setBackground(AppTheme.ABYSS);

        java.awt.Component brand = wrapperPanel.getComponent(0);

        if (brand instanceof javax.swing.JPanel) {
            javax.swing.JPanel brandPanel = (javax.swing.JPanel) brand;
            brandPanel.setBackground(AppTheme.MIDNIGHT);
            brandPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                    javax.swing.BorderFactory.createLineBorder(AppTheme.RIM, 1),
                    javax.swing.BorderFactory.createEmptyBorder(40, 40, 40, 40)
            ));

            for (java.awt.Component comp : brandPanel.getComponents()) {
                if (comp instanceof javax.swing.JLabel) {
                    javax.swing.JLabel label = (javax.swing.JLabel) comp;
                    label.setForeground(AppTheme.SNOW);
                }
            }
        }
    }

    setupLoginLogo();

    lblBrand.setForeground(AppTheme.INDIGO_LIGHT);
    lblTitle.setForeground(AppTheme.SNOW);
    lblSubtitle.setForeground(AppTheme.SLATE);
    lblUsername.setForeground(AppTheme.SLATE);
    lblPassword.setForeground(AppTheme.SLATE);

    lblBrand.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
    lblTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 32));
    lblSubtitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
    lblUsername.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
    lblPassword.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));

    styleTextField(txtUsername);
    stylePasswordField(txtPassword);

    chkShowPassword.setForeground(AppTheme.SLATE);
    chkShowPassword.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
    chkShowPassword.setText("Tampilkan Password");
    chkShowPassword.setOpaque(false);

    styleButton(btnLogin, "Masuk", AppTheme.INDIGO, AppTheme.SNOW, AppTheme.RIM);
    styleButton(btnCancel, "Keluar", AppTheme.ELEVATED, AppTheme.SLATE, AppTheme.RIM);

    panelCard.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(AppTheme.RIM, 1),
            javax.swing.BorderFactory.createEmptyBorder(42, 42, 42, 42)
    ));
    }
     
     private void styleTextField(javax.swing.JTextField field) {
    field.setBackground(AppTheme.MIDNIGHT);
    field.setForeground(AppTheme.SNOW);
    field.setCaretColor(AppTheme.INDIGO_LIGHT);
    field.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
    field.setOpaque(true);
    field.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(AppTheme.RIM, 1),
            javax.swing.BorderFactory.createEmptyBorder(10, 14, 10, 14)
    ));
    field.setPreferredSize(new java.awt.Dimension(300, 42));
    }
     
     private void styleButton(javax.swing.JButton button, String text, java.awt.Color bg,
        java.awt.Color fg, java.awt.Color border) {

    button.setText(text);
    button.setBackground(bg);
    button.setForeground(fg);
    button.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
    button.setFocusPainted(false);
    button.setOpaque(true);
    button.setContentAreaFilled(true);
    button.setBorderPainted(false);
    button.putClientProperty("JButton.buttonType", "square");
    button.setPreferredSize(new java.awt.Dimension(300, 44));
    button.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(border, 1),
            javax.swing.BorderFactory.createEmptyBorder(10, 18, 10, 18)
    ));
    }
     
    private void setupLoginLogo() {
    try {
        java.net.URL url = getClass().getResource("/assets/logo.png");

        if (url != null) {
            javax.swing.ImageIcon icon = new javax.swing.ImageIcon(url);
            java.awt.Image image = icon.getImage().getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH);
            lblLogo.setIcon(new javax.swing.ImageIcon(image));
            lblLogo.setText("");
        } else {
            lblLogo.setText("FB");
        }

    } catch (Exception e) {
        lblLogo.setText("FB");
    }

    lblLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    lblLogo.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
    lblLogo.setForeground(AppTheme.SNOW);
    lblLogo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 24));
    lblLogo.setOpaque(false);
    lblLogo.setBackground(new java.awt.Color(0, 0, 0, 0));
    lblLogo.setBorder(null);
    lblLogo.setPreferredSize(new java.awt.Dimension(170, 150));
    }
     
     private void stylePasswordField(javax.swing.JPasswordField field) {
    field.setBackground(AppTheme.MIDNIGHT);
    field.setForeground(AppTheme.SNOW);
    field.setCaretColor(AppTheme.INDIGO_LIGHT);
    field.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
    field.setOpaque(true);
    field.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(AppTheme.RIM, 1),
            javax.swing.BorderFactory.createEmptyBorder(10, 14, 10, 14)
    ));
    field.setPreferredSize(new java.awt.Dimension(300, 42));
    }
     
     private void rebuildLayoutLogin() {
    getContentPane().removeAll();
    getContentPane().setLayout(new java.awt.BorderLayout());

    lblLogo = new javax.swing.JLabel();

    panelBackground.removeAll();
    panelBackground.setLayout(new java.awt.GridBagLayout());
    panelBackground.setBorder(javax.swing.BorderFactory.createEmptyBorder(30, 30, 30, 30));

    javax.swing.JPanel wrapper = new javax.swing.JPanel(new java.awt.GridLayout(1, 2, 0, 0));
    wrapper.setPreferredSize(new java.awt.Dimension(820, 460));

    javax.swing.JPanel brandPanel = new javax.swing.JPanel(new java.awt.GridBagLayout());
    brandPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(40, 40, 40, 40));

    javax.swing.JPanel logoBox = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));
    logoBox.setOpaque(false);
    logoBox.setPreferredSize(new java.awt.Dimension(190, 150));
    logoBox.add(lblLogo);

    javax.swing.JLabel lblAppName = new javax.swing.JLabel("FUTSAL BOOKING");
    lblAppName.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 30));
    lblAppName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

    javax.swing.JLabel lblDesc = new javax.swing.JLabel("<html><div style='text-align:center;width:260px;'>Sistem pengelolaan booking lapangan futsal, pembayaran, dan laporan.</div></html>");
    lblDesc.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
    lblDesc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

    java.awt.GridBagConstraints gbcBrand = new java.awt.GridBagConstraints();
    gbcBrand.gridx = 0;
    gbcBrand.weightx = 1.0;
    gbcBrand.fill = java.awt.GridBagConstraints.HORIZONTAL;

    gbcBrand.gridy = 0;
    gbcBrand.fill = java.awt.GridBagConstraints.NONE;
    gbcBrand.insets = new java.awt.Insets(0, 0, 26, 0);
    brandPanel.add(logoBox, gbcBrand);
    gbcBrand.fill = java.awt.GridBagConstraints.HORIZONTAL;

    gbcBrand.gridy = 1;
    gbcBrand.insets = new java.awt.Insets(0, 0, 10, 0);
    brandPanel.add(lblAppName, gbcBrand);

    gbcBrand.gridy = 2;
    gbcBrand.insets = new java.awt.Insets(0, 0, 0, 0);
    brandPanel.add(lblDesc, gbcBrand);

    panelCard.removeAll();
    panelCard.setLayout(new java.awt.GridBagLayout());
    panelCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(42, 42, 42, 42));

    java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
    gbc.gridx = 0;
    gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1.0;

    lblBrand.setText("ADMIN PANEL");
    lblTitle.setText("Masuk Sistem");
    lblSubtitle.setText("Silakan masuk untuk mengelola aplikasi");

    gbc.gridy = 0;
    gbc.insets = new java.awt.Insets(0, 0, 8, 0);
    panelCard.add(lblBrand, gbc);

    gbc.gridy = 1;
    gbc.insets = new java.awt.Insets(0, 0, 4, 0);
    panelCard.add(lblTitle, gbc);

    gbc.gridy = 2;
    gbc.insets = new java.awt.Insets(0, 0, 28, 0);
    panelCard.add(lblSubtitle, gbc);

    lblUsername.setText("Username");
    gbc.gridy = 3;
    gbc.insets = new java.awt.Insets(0, 0, 6, 0);
    panelCard.add(lblUsername, gbc);

    gbc.gridy = 4;
    gbc.insets = new java.awt.Insets(0, 0, 16, 0);
    panelCard.add(txtUsername, gbc);

    lblPassword.setText("Password");
    gbc.gridy = 5;
    gbc.insets = new java.awt.Insets(0, 0, 6, 0);
    panelCard.add(lblPassword, gbc);

    gbc.gridy = 6;
    gbc.insets = new java.awt.Insets(0, 0, 10, 0);
    panelCard.add(txtPassword, gbc);

    gbc.gridy = 7;
    gbc.insets = new java.awt.Insets(0, 0, 20, 0);
    panelCard.add(chkShowPassword, gbc);

    gbc.gridy = 8;
    gbc.insets = new java.awt.Insets(0, 0, 12, 0);
    panelCard.add(btnLogin, gbc);

    gbc.gridy = 9;
    gbc.insets = new java.awt.Insets(0, 0, 0, 0);
    panelCard.add(btnCancel, gbc);

    wrapper.add(brandPanel);
    wrapper.add(panelCard);

    panelBackground.add(wrapper);

    getContentPane().add(panelBackground, java.awt.BorderLayout.CENTER);

    getContentPane().revalidate();
    getContentPane().repaint();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelBackground = new javax.swing.JPanel();
        panelCard = new javax.swing.JPanel();
        lblUsername = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        lblPassword = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        btnCancel = new javax.swing.JButton();
        btnLogin = new javax.swing.JButton();
        lblSubtitle = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        lblBrand = new javax.swing.JLabel();
        chkShowPassword = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login");
        setBackground(new java.awt.Color(245, 247, 250));
        setPreferredSize(new java.awt.Dimension(720, 480));
        setResizable(false);

        panelBackground.setBackground(new java.awt.Color(19, 18, 15));

        panelCard.setBackground(new java.awt.Color(28, 26, 22));
        panelCard.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        lblUsername.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblUsername.setForeground(new java.awt.Color(168, 152, 128));
        lblUsername.setText("Username");

        txtUsername.setBackground(new java.awt.Color(21, 19, 16));
        txtUsername.setForeground(new java.awt.Color(224, 216, 206));
        txtUsername.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(46, 42, 34), 1, true));
        txtUsername.setCaretColor(new java.awt.Color(232, 200, 160));
        txtUsername.setFocusCycleRoot(true);
        txtUsername.setFocusTraversalPolicyProvider(true);
        txtUsername.setHighlighter(null);

        lblPassword.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblPassword.setForeground(new java.awt.Color(168, 152, 128));
        lblPassword.setText("Password");

        txtPassword.setBackground(new java.awt.Color(21, 19, 16));
        txtPassword.setForeground(new java.awt.Color(224, 216, 206));
        txtPassword.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(46, 42, 34), 1, true));
        txtPassword.setCaretColor(new java.awt.Color(232, 200, 160));
        txtPassword.setFocusCycleRoot(true);

        btnCancel.setBackground(new java.awt.Color(28, 26, 22));
        btnCancel.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(168, 152, 128));
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(this::btnCancelActionPerformed);

        btnLogin.setBackground(new java.awt.Color(200, 168, 130));
        btnLogin.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnLogin.setForeground(new java.awt.Color(19, 18, 15));
        btnLogin.setText("Log in");
        btnLogin.addActionListener(this::btnLoginActionPerformed);

        lblSubtitle.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblSubtitle.setForeground(new java.awt.Color(106, 96, 88));
        lblSubtitle.setText("Silakan masuk untuk mengelola sistem");

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(224, 216, 206));
        lblTitle.setText("LOGIN ADMIN");

        lblBrand.setBackground(new java.awt.Color(200, 168, 130));
        lblBrand.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblBrand.setForeground(new java.awt.Color(200, 168, 130));
        lblBrand.setText("FUTSAL BOOKING");

        chkShowPassword.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        chkShowPassword.setForeground(new java.awt.Color(168, 152, 128));
        chkShowPassword.setText("Tampilkan Password");
        chkShowPassword.addActionListener(this::chkShowPasswordActionPerformed);

        javax.swing.GroupLayout panelCardLayout = new javax.swing.GroupLayout(panelCard);
        panelCard.setLayout(panelCardLayout);
        panelCardLayout.setHorizontalGroup(
            panelCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCardLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCardLayout.createSequentialGroup()
                        .addComponent(lblBrand)
                        .addGap(119, 119, 119))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCardLayout.createSequentialGroup()
                        .addComponent(lblSubtitle)
                        .addGap(62, 62, 62))))
            .addGroup(panelCardLayout.createSequentialGroup()
                .addGap(98, 98, 98)
                .addComponent(lblTitle)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panelCardLayout.createSequentialGroup()
                .addGroup(panelCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCardLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(panelCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblUsername)
                            .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblPassword)
                            .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chkShowPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelCardLayout.createSequentialGroup()
                        .addGap(136, 136, 136)
                        .addGroup(panelCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnLogin)
                            .addComponent(btnCancel))))
                .addGap(0, 35, Short.MAX_VALUE))
        );
        panelCardLayout.setVerticalGroup(
            panelCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCardLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(lblBrand)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSubtitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addComponent(lblUsername)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPassword)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkShowPassword)
                .addGap(11, 11, 11)
                .addComponent(btnLogin)
                .addGap(18, 18, 18)
                .addComponent(btnCancel)
                .addGap(31, 31, 31))
        );

        panelBackground.add(panelCard);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBackground, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBackground, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        // TODO add your handling code here:
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

    if (username.isEmpty() || password.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Username dan password harus diisi!");
        return;
    }

    try {
        Connection conn = koneksi.configDB();

        String sql = "SELECT * FROM users "
                   + "WHERE username = ? "
                   + "AND password = SHA2(?, 256) "
                   + "AND is_active = 1";

        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, username);
        pst.setString(2, password);

        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            Session.idUser = rs.getInt("id_user");
            Session.username = rs.getString("username");
            Session.namaLengkap = rs.getString("nama_lengkap");
            Session.role = rs.getString("role");

            JOptionPane.showMessageDialog(this, "Login berhasil. Selamat datang, "
            + rs.getString("nama_lengkap"));

            new MenuUtama().setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Username atau password salah!");
            txtPassword.setText("");
            txtUsername.requestFocus();
        }

        rs.close();
        pst.close();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Terjadi error saat login: " + e.getMessage());
    }
    }//GEN-LAST:event_btnLoginActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        int jawab = JOptionPane.showConfirmDialog(
            this,
            "Yakin ingin keluar?",
            "Konfirmasi",
            JOptionPane.YES_NO_OPTION
    );

    if (jawab == JOptionPane.YES_OPTION) {
        System.exit(0);
    }
    }//GEN-LAST:event_btnCancelActionPerformed

    private void chkShowPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkShowPasswordActionPerformed
        // TODO add your handling code here:
        if (chkShowPassword.isSelected()) {
        txtPassword.setEchoChar((char) 0);
    } else {
        txtPassword.setEchoChar('*');
    }
    }//GEN-LAST:event_chkShowPasswordActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new Login().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnLogin;
    private javax.swing.JCheckBox chkShowPassword;
    private javax.swing.JLabel lblBrand;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblSubtitle;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JPanel panelBackground;
    private javax.swing.JPanel panelCard;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
