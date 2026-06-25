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
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.UIManager;

public class Login extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Login.class.getName());

    /**
     * Creates new form Login
     */
    public Login() {
        initComponents();
        setLocationRelativeTo(null);
        setSize(720, 500);
        setResizable(false);

        styleLogin();
    }
    
     private void styleLogin() {
    java.awt.Color abyss = new java.awt.Color(12, 12, 16);        // #0c0c10
    java.awt.Color canvas = new java.awt.Color(17, 17, 20);       // #111114
    java.awt.Color surface1 = new java.awt.Color(26, 26, 31);     // #1a1a1f
    java.awt.Color surface2 = new java.awt.Color(34, 34, 40);     // #222228
    java.awt.Color inputDark = new java.awt.Color(12, 12, 16);    // #0c0c10

    java.awt.Color border = new java.awt.Color(42, 42, 50);       // #2a2a32
    java.awt.Color borderHover = new java.awt.Color(56, 56, 62);  // #38383e

    java.awt.Color textPrimary = new java.awt.Color(232, 232, 246);   // #e8e8f6
    java.awt.Color textSecondary = new java.awt.Color(152, 152, 168); // #9898a8
    java.awt.Color textMuted = new java.awt.Color(95, 94, 106);       // #5f5e6a

    java.awt.Color purpleMain = new java.awt.Color(83, 74, 183);      // #534ab7
    java.awt.Color purpleHover = new java.awt.Color(60, 52, 137);     // #3c3489
    java.awt.Color purpleLight = new java.awt.Color(175, 169, 236);   // #afa9ec

    panelBackground.setBackground(canvas);
    panelCard.setBackground(surface1);
    getContentPane().setBackground(canvas);

    lblBrand.setForeground(purpleLight);
    lblTitle.setForeground(textPrimary);
    lblSubtitle.setForeground(textMuted);
    lblUsername.setForeground(textSecondary);
    lblPassword.setForeground(textSecondary);

    lblBrand.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
    lblTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 30));
    lblSubtitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
    lblUsername.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
    lblPassword.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));

    txtUsername.setBackground(inputDark);
    txtUsername.setForeground(textPrimary);
    txtUsername.setCaretColor(purpleLight);
    txtUsername.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
    txtUsername.setOpaque(true);
    txtUsername.setBorder(javax.swing.BorderFactory.createCompoundBorder(
        javax.swing.BorderFactory.createLineBorder(border, 1),
        javax.swing.BorderFactory.createEmptyBorder(10, 14, 10, 14)
    ));

    txtPassword.setBackground(inputDark);
    txtPassword.setForeground(textPrimary);
    txtPassword.setCaretColor(purpleLight);
    txtPassword.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
    txtPassword.setOpaque(true);
    txtPassword.setBorder(javax.swing.BorderFactory.createCompoundBorder(
        javax.swing.BorderFactory.createLineBorder(border, 1),
        javax.swing.BorderFactory.createEmptyBorder(10, 14, 10, 14)
    ));

    chkShowPassword.setBackground(surface1);
    chkShowPassword.setForeground(textMuted);
    chkShowPassword.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
    chkShowPassword.setText("Lihat Password");
    chkShowPassword.setOpaque(false);

    btnLogin.setBackground(purpleMain);
    btnLogin.setForeground(textPrimary);
    btnLogin.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
    btnLogin.setFocusPainted(false);
    btnLogin.setOpaque(true);
    btnLogin.setBorder(javax.swing.BorderFactory.createEmptyBorder(11, 32, 11, 32));
    btnLogin.setPreferredSize(new java.awt.Dimension(280, 44));

    btnCancel.setBackground(surface1);
    btnCancel.setForeground(textMuted);
    btnCancel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
    btnCancel.setFocusPainted(false);
    btnCancel.setOpaque(true);
    btnCancel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
        javax.swing.BorderFactory.createLineBorder(border, 1),
        javax.swing.BorderFactory.createEmptyBorder(10, 32, 10, 32)
    ));
    btnCancel.setPreferredSize(new java.awt.Dimension(280, 42));

    panelCard.setBorder(javax.swing.BorderFactory.createCompoundBorder(
        javax.swing.BorderFactory.createLineBorder(border, 1),
        javax.swing.BorderFactory.createEmptyBorder(32, 32, 32, 32)
    ));
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

        FlatLightLaf.setup();
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
