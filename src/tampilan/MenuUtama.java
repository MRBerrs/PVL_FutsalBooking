/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package tampilan;

import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.UIManager;

/**
 *
 * @author Administrator
 */
public class MenuUtama extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MenuUtama.class.getName());
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblTanggal;
    private javax.swing.JLabel lblJam;
    private javax.swing.JLabel lblSubTitle;
    private javax.swing.JLabel lblWelcome;
    private javax.swing.Timer timerJam;
    /**
     * Creates new form MenuUtama
     */
    public MenuUtama() {
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1200, 720));
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        rebuildLayoutMenuUtama();
        styleMenuUtama();
        mulaiJam();
    }

    
    private void styleMenuButton(javax.swing.JButton button, String text, java.awt.Color bg,
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
    button.setPreferredSize(new java.awt.Dimension(220, 46));
    button.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(border, 1),
            javax.swing.BorderFactory.createEmptyBorder(10, 18, 10, 18)
    ));
    }
    
    private void styleMenuUtama() {
    getContentPane().setBackground(AppTheme.ABYSS);
    jPanel1.setBackground(AppTheme.ABYSS);
    jPanel2.setBackground(AppTheme.MIDNIGHT);

    jLabel1.setText("FUTSAL");
    jLabel1.setForeground(AppTheme.SNOW);
    jLabel1.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));

    lblSubTitle.setText("Booking System");
    lblSubTitle.setForeground(AppTheme.SLATE);
    lblSubTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));

    lblWelcome.setText("Selamat Datang, Admin");
    lblWelcome.setForeground(AppTheme.SNOW);
    lblWelcome.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 30));

    lblJam.setForeground(AppTheme.INDIGO_LIGHT);
    lblJam.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 30));
    lblJam.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

    lblTanggal.setForeground(AppTheme.SLATE);
    lblTanggal.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
    lblTanggal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

    jLabel3.setText("MENU UTAMA");
    jLabel3.setForeground(AppTheme.INDIGO_LIGHT);
    jLabel3.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));

    setupLogo();

    styleMenuButton(btnMember, "Data Member", AppTheme.INDIGO, AppTheme.SNOW, AppTheme.RIM);
    styleMenuButton(btnLapangan, "Data Lapangan", AppTheme.INDIGO, AppTheme.SNOW, AppTheme.RIM);
    styleMenuButton(btnBooking, "Booking", AppTheme.INDIGO, AppTheme.SNOW, AppTheme.RIM);
    styleMenuButton(btnPembayaran, "Pembayaran", AppTheme.INDIGO, AppTheme.SNOW, AppTheme.RIM);
    styleMenuButton(btnLaporan, "Laporan", AppTheme.INDIGO, AppTheme.SNOW, AppTheme.RIM);

    styleMenuButton(btnLogout, "Keluar", AppTheme.CORAL_TINT, AppTheme.SNOW, AppTheme.RIM);

    jPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 1, AppTheme.RIM));
    }
    
    private void rebuildLayoutMenuUtama() {
    getContentPane().removeAll();
    getContentPane().setLayout(new java.awt.BorderLayout());

    lblLogo = new javax.swing.JLabel();
    lblTanggal = new javax.swing.JLabel();
    lblJam = new javax.swing.JLabel();
    lblSubTitle = new javax.swing.JLabel();
    lblWelcome = new javax.swing.JLabel();

    jPanel1.removeAll();
    jPanel1.setLayout(new java.awt.BorderLayout());

    jPanel2.removeAll();
    jPanel2.setLayout(new java.awt.BorderLayout(0, 18));
    jPanel2.setPreferredSize(new java.awt.Dimension(280, 10));
    jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(26, 22, 26, 22));

    javax.swing.JPanel brandPanel = new javax.swing.JPanel(new java.awt.BorderLayout(14, 0));
    brandPanel.setOpaque(false);

    javax.swing.JPanel logoBox = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));
    logoBox.setOpaque(false);
    logoBox.setPreferredSize(new java.awt.Dimension(58, 58));
    logoBox.add(lblLogo);

    javax.swing.JPanel brandTextPanel = new javax.swing.JPanel(new java.awt.GridLayout(2, 1, 0, 0));
    brandTextPanel.setOpaque(false);
    brandTextPanel.add(jLabel1);
    brandTextPanel.add(lblSubTitle);

    brandPanel.add(logoBox, java.awt.BorderLayout.WEST);
    brandPanel.add(brandTextPanel, java.awt.BorderLayout.CENTER);

    javax.swing.JPanel menuPanel = new javax.swing.JPanel(new java.awt.GridLayout(5, 1, 0, 14));
    menuPanel.setOpaque(false);
    menuPanel.add(btnMember);
    menuPanel.add(btnLapangan);
    menuPanel.add(btnBooking);
    menuPanel.add(btnPembayaran);
    menuPanel.add(btnLaporan);

    javax.swing.JPanel logoutPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
    logoutPanel.setOpaque(false);
    logoutPanel.add(btnLogout, java.awt.BorderLayout.SOUTH);

    jPanel2.add(brandPanel, java.awt.BorderLayout.NORTH);
    jPanel2.add(menuPanel, java.awt.BorderLayout.CENTER);
    jPanel2.add(logoutPanel, java.awt.BorderLayout.SOUTH);

    javax.swing.JPanel mainPanel = new javax.swing.JPanel(new java.awt.BorderLayout(0, 24));
    mainPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(34, 34, 34, 34));
    mainPanel.setOpaque(false);

    javax.swing.JPanel headerPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
    headerPanel.setOpaque(false);

    javax.swing.JPanel headerLeft = new javax.swing.JPanel(new java.awt.GridLayout(2, 1, 0, 4));
    headerLeft.setOpaque(false);
    headerLeft.add(lblWelcome);

    javax.swing.JLabel lblInfo = new javax.swing.JLabel("Kelola data member, lapangan, booking, pembayaran, dan laporan futsal.");
    lblInfo.setForeground(AppTheme.SLATE);
    lblInfo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
    headerLeft.add(lblInfo);

    javax.swing.JPanel timePanel = new javax.swing.JPanel(new java.awt.GridLayout(2, 1, 0, 4));
    timePanel.setOpaque(false);
    timePanel.add(lblJam);
    timePanel.add(lblTanggal);

    headerPanel.add(headerLeft, java.awt.BorderLayout.WEST);
    headerPanel.add(timePanel, java.awt.BorderLayout.EAST);

    javax.swing.JPanel cardPanel = new javax.swing.JPanel(new java.awt.GridLayout(2, 3, 18, 18));
    cardPanel.setOpaque(false);

    cardPanel.add(createDashboardCard("Data Member", "Kelola data member dan nomor WhatsApp."));
    cardPanel.add(createDashboardCard("Data Lapangan", "Kelola lapangan, harga, kapasitas, dan status."));
    cardPanel.add(createDashboardCard("Booking", "Kelola pemesanan lapangan futsal."));
    cardPanel.add(createDashboardCard("Pembayaran", "Kelola transaksi dan status pembayaran."));
    cardPanel.add(createDashboardCard("Laporan", "Cetak nota, jadwal, pendapatan, dan pembayaran."));
    cardPanel.add(createDashboardCard("Sistem", "Aplikasi Booking Futsal berbasis Java dan MySQL."));

    mainPanel.add(headerPanel, java.awt.BorderLayout.NORTH);
    mainPanel.add(cardPanel, java.awt.BorderLayout.CENTER);

    jPanel1.add(jPanel2, java.awt.BorderLayout.WEST);
    jPanel1.add(mainPanel, java.awt.BorderLayout.CENTER);

    getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

    getContentPane().revalidate();
    getContentPane().repaint();
    }
    
    private javax.swing.JPanel createDashboardCard(String title, String desc) {
    javax.swing.JPanel card = new javax.swing.JPanel(new java.awt.BorderLayout(0, 10));
    card.setBackground(AppTheme.COURT);
    card.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(AppTheme.RIM, 1),
            javax.swing.BorderFactory.createEmptyBorder(22, 22, 22, 22)
    ));

    javax.swing.JLabel lblTitle = new javax.swing.JLabel(title);
    lblTitle.setForeground(AppTheme.SNOW);
    lblTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));

    javax.swing.JLabel lblDesc = new javax.swing.JLabel("<html>" + desc + "</html>");
    lblDesc.setForeground(AppTheme.SLATE);
    lblDesc.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));

    card.add(lblTitle, java.awt.BorderLayout.NORTH);
    card.add(lblDesc, java.awt.BorderLayout.CENTER);

    return card;
    }
    
    private void setupLogo() {
    try {
        java.net.URL url = getClass().getResource("/assets/logo.png");

        if (url != null) {
            javax.swing.ImageIcon icon = new javax.swing.ImageIcon(url);
            java.awt.Image image = icon.getImage().getScaledInstance(54, 54, java.awt.Image.SCALE_SMOOTH);
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
    lblLogo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
    lblLogo.setOpaque(false);
    lblLogo.setBackground(new java.awt.Color(0, 0, 0, 0));
    lblLogo.setBorder(null);
    lblLogo.setPreferredSize(new java.awt.Dimension(58, 58));
    }
    
    private void mulaiJam() {
    timerJam = new javax.swing.Timer(1000, e -> updateJamTanggal());
    timerJam.start();
    updateJamTanggal();
    }
    
    private void updateJamTanggal() {
    java.util.Date sekarang = new java.util.Date();

    java.text.SimpleDateFormat formatJam = new java.text.SimpleDateFormat("HH:mm:ss");
    java.text.SimpleDateFormat formatTanggal = new java.text.SimpleDateFormat("EEEE, dd MMMM yyyy", new java.util.Locale("id", "ID"));

    lblJam.setText(formatJam.format(sekarang));
    lblTanggal.setText(formatTanggal.format(sekarang));
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
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        btnMember = new javax.swing.JButton();
        btnLapangan = new javax.swing.JButton();
        btnBooking = new javax.swing.JButton();
        btnPembayaran = new javax.swing.JButton();
        btnLaporan = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(153, 153, 153));

        jLabel3.setText("Futsal");

        btnMember.setText("[ Data Member ] ");
        btnMember.addActionListener(this::btnMemberActionPerformed);

        btnLapangan.setText("[ Data Lapangan ] ");
        btnLapangan.addActionListener(this::btnLapanganActionPerformed);

        btnBooking.setText("[ Booking ] ");
        btnBooking.addActionListener(this::btnBookingActionPerformed);

        btnPembayaran.setText("[ Pembayaran ] ");
        btnPembayaran.addActionListener(this::btnPembayaranActionPerformed);

        btnLaporan.setText("[ Laporan ] ");
        btnLaporan.addActionListener(this::btnLaporanActionPerformed);

        btnLogout.setText("[ Logout ]");
        btnLogout.addActionListener(this::btnLogoutActionPerformed);

        jLabel1.setText("Booking Sistem");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnPembayaran, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnLapangan, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                    .addComponent(btnBooking, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnMember, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnLaporan, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(39, 39, 39))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addComponent(btnLogout))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jLabel3))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(1, 1, 1)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnMember)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnLapangan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnBooking)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnPembayaran)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnLaporan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addComponent(btnLogout))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(252, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnMemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMemberActionPerformed
        // TODO add your handling code here:
        new FormMember().setVisible(true);
    }//GEN-LAST:event_btnMemberActionPerformed

    private void btnLapanganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLapanganActionPerformed
        // TODO add your handling code here:
        new FormLapangan().setVisible(true);
    }//GEN-LAST:event_btnLapanganActionPerformed

    private void btnBookingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBookingActionPerformed
        // TODO add your handling code here:
        new FormBooking().setVisible(true);
    }//GEN-LAST:event_btnBookingActionPerformed

    private void btnPembayaranActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPembayaranActionPerformed
        // TODO add your handling code here:
        new FormPembayaran().setVisible(true);
    }//GEN-LAST:event_btnPembayaranActionPerformed

    private void btnLaporanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLaporanActionPerformed
        // TODO add your handling code here:
        new FormLaporan().setVisible(true);
    }//GEN-LAST:event_btnLaporanActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        // TODO add your handling code here:
        int jawab = javax.swing.JOptionPane.showConfirmDialog(
            this,
            "Yakin ingin logout?",
            "Konfirmasi",
            javax.swing.JOptionPane.YES_NO_OPTION
    );

    if (jawab == javax.swing.JOptionPane.YES_OPTION) {
        new Login().setVisible(true);
        this.dispose();
    }
    }//GEN-LAST:event_btnLogoutActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new MenuUtama().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBooking;
    private javax.swing.JButton btnLapangan;
    private javax.swing.JButton btnLaporan;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnMember;
    private javax.swing.JButton btnPembayaran;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
