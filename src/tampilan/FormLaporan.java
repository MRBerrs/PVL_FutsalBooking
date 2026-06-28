/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package tampilan;

/**
 *
 * @author Administrator
 */

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.lowagie.text.Rectangle;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.table.DefaultTableModel;
import com.lowagie.text.Rectangle;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import koneksi.koneksi;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Image;

public class FormLaporan extends javax.swing.JFrame {
    private String judulLaporan = "LAPORAN FUTSAL";
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FormLaporan.class.getName());

    /**
     * Creates new form FormLaporan
     */
    public FormLaporan() {
         initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1200, 720));
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        rebuildLayoutLaporan();
        styleFormLaporan();
        loadKodeBooking();
        setupTablePreview();
        cmbKodeBooking.addActionListener(e -> previewNotaByPilihan());
    }
    
private void loadKodeBooking() {
    cmbKodeBooking.removeAllItems();
    cmbKodeBooking.addItem("-- Pilih Kode Booking --");

    try {
        Connection conn = koneksi.configDB();

        String sql = "SELECT kode_booking FROM booking ORDER BY id_booking DESC";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            cmbKodeBooking.addItem(rs.getString("kode_booking"));
        }

        rs.close();
        st.close();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal load kode booking: " + e.getMessage());
    }
}

    private void previewNotaByPilihan() {
    if (cmbKodeBooking.getSelectedItem() == null) {
        return;
    }

    String kodeBooking = cmbKodeBooking.getSelectedItem().toString();

    if (kodeBooking.equals("-- Pilih Kode Booking --")) {
        tblPreview.setModel(new DefaultTableModel());
        setupTablePreview();
        return;
    }

    try {
        Connection conn = koneksi.configDB();

        String sql = "SELECT * FROM v_detail_booking WHERE kode_booking = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, kodeBooking);

        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            isiPreviewNota(rs);
        } else {
            tblPreview.setModel(new DefaultTableModel());
            setupTablePreview();
        }

        rs.close();
        pst.close();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal menampilkan preview booking: " + e.getMessage());
    }
    }
private File buatFilePDF(String namaFile) throws Exception {
    File folder = new File(
            System.getProperty("user.dir")
            + File.separator + "src"
            + File.separator + "laporan"
    );

    if (!folder.exists()) {
        folder.mkdirs();
    }

    String waktu = String.valueOf(System.currentTimeMillis());
    String namaFinal = namaFile.replace(".pdf", "_" + waktu + ".pdf");

    return new File(folder, namaFinal);
}

private File fileLogo() {
    File file = new File(
            System.getProperty("user.dir")
            + File.separator + "src"
            + File.separator + "assets"
            + File.separator + "logo.png"
    );

    if (file.exists()) {
        return file;
    }

    return null;
}

private PdfPCell cellLogoLaporan(int align) throws Exception {
    PdfPCell cell = new PdfPCell();
    cell.setBorder(PdfPCell.NO_BORDER);
    cell.setHorizontalAlignment(align);
    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    cell.setPadding(0);

    File logo = fileLogo();

    if (logo != null) {
        Image img = Image.getInstance(logo.getAbsolutePath());
        img.scaleToFit(90, 90);
        img.setAlignment(align);
        cell.addElement(img);
    }

    return cell;
}

private void tambahLogoNota(Document doc) throws Exception {
    File logo = fileLogo();

    if (logo == null) {
        return;
    }

    Image img = Image.getInstance(logo.getAbsolutePath());
    img.scaleToFit(46, 46);
    img.setAlignment(Element.ALIGN_CENTER);
    img.setSpacingAfter(3);

    doc.add(img);
}

private void tambahLogoLaporan(Document doc) throws Exception {
    File logo = fileLogo();

    if (logo == null) {
        return;
    }

    Image img = Image.getInstance(logo.getAbsolutePath());
    img.scaleToFit(64, 64);
    img.setAlignment(Element.ALIGN_CENTER);
    img.setSpacingAfter(6);

    doc.add(img);
}

private void bukaPDF(File file) {
    try {
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "File PDF tidak ditemukan:\n" + file.getAbsolutePath());
            return;
        }

        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().open(file);
                return;
            } catch (Exception e) {
                // fallback ke command Windows
            }
        }

        Runtime.getRuntime().exec(new String[]{
            "cmd", "/c", "start", "", file.getAbsolutePath()
        });

    } catch (Exception e) {
        JOptionPane.showMessageDialog(
                this,
                "PDF berhasil dibuat, tapi gagal dibuka otomatis.\n"
                + "Buka manual di:\n" + file.getAbsolutePath()
        );
    }
}

private String rupiah(String nilai) {
    try {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');

        DecimalFormat df = new DecimalFormat("#,##0", symbols);
        return "Rp " + df.format(Double.parseDouble(nilai));
    } catch (Exception e) {
        return nilai == null ? "" : nilai;
    }
}

private String tanggalCetak() {
    return new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());
}

private PdfPCell cellHeader(String text) {
    Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, Color.WHITE);

    PdfPCell cell = new PdfPCell(new Phrase(text, font));
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    cell.setPadding(6);
    cell.setBackgroundColor(new Color(45, 45, 45));

    return cell;
}

private PdfPCell cellData(String text) {
    Font font = FontFactory.getFont(FontFactory.HELVETICA, 8);

    PdfPCell cell = new PdfPCell(new Phrase(text == null ? "" : text, font));
    cell.setPadding(5);
    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

    return cell;
}

private PdfPCell cellDataStriped(String text, boolean striped) {
    PdfPCell cell = cellData(text);

    if (striped) {
        cell.setBackgroundColor(new Color(240, 240, 240));
    }

    return cell;
}

private PdfPCell cellNoBorder(String text, Font font, int align) {
    PdfPCell cell = new PdfPCell(new Phrase(text == null ? "" : text, font));
    cell.setBorder(PdfPCell.NO_BORDER);
    cell.setHorizontalAlignment(align);
    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    cell.setPadding(2);

    return cell;
}

    private void tambahJudul(Document doc, String judul) throws Exception {
    Font fontBrand = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
    Font fontJudul = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
    Font fontKecil = FontFactory.getFont(FontFactory.HELVETICA, 9);

    PdfPTable header = new PdfPTable(3);
    header.setWidthPercentage(100);
    header.setWidths(new float[]{20, 60, 20});
    header.setSpacingAfter(14);

    header.addCell(cellLogoLaporan(Element.ALIGN_LEFT));

    PdfPCell tengah = new PdfPCell();
    tengah.setBorder(PdfPCell.NO_BORDER);
    tengah.setHorizontalAlignment(Element.ALIGN_CENTER);
    tengah.setVerticalAlignment(Element.ALIGN_MIDDLE);
    tengah.setPadding(0);

    Paragraph nama = new Paragraph("FUTSAL BOOKING", fontBrand);
    nama.setAlignment(Element.ALIGN_CENTER);
    nama.setSpacingAfter(3);

    Paragraph sub = new Paragraph("Sistem Booking Lapangan Futsal", fontKecil);
    sub.setAlignment(Element.ALIGN_CENTER);
    sub.setSpacingAfter(12);

    Paragraph p = new Paragraph(judul, fontJudul);
    p.setAlignment(Element.ALIGN_CENTER);
    p.setSpacingAfter(4);

    Paragraph tgl = new Paragraph("Tanggal Cetak: " + tanggalCetak(), fontKecil);
    tgl.setAlignment(Element.ALIGN_CENTER);
    tgl.setSpacingAfter(4);

    Paragraph tempat = new Paragraph(tanggalJakarta(), fontKecil);
    tempat.setAlignment(Element.ALIGN_CENTER);

    tengah.addElement(nama);
    tengah.addElement(sub);
    tengah.addElement(p);
    tengah.addElement(tgl);
    tengah.addElement(tempat);

    header.addCell(tengah);
    header.addCell(cellLogoLaporan(Element.ALIGN_RIGHT));

    doc.add(header);
    }

private void addCenter(Document doc, String text, Font font) throws Exception {
    Paragraph p = new Paragraph(text, font);
    p.setAlignment(Element.ALIGN_CENTER);
    p.setSpacingAfter(2);
    doc.add(p);
}

private void addLine(Document doc) throws Exception {
    Font font = FontFactory.getFont(FontFactory.COURIER, 8);
    Paragraph p = new Paragraph("--------------------------------", font);
    p.setAlignment(Element.ALIGN_CENTER);
    p.setSpacingBefore(3);
    p.setSpacingAfter(3);
    doc.add(p);
}

private void addInfoRow(Document doc, String label, String value, Font font) throws Exception {
    PdfPTable table = new PdfPTable(2);
    table.setWidthPercentage(100);
    table.setWidths(new float[]{35, 65});

    table.addCell(cellNoBorder(label, font, Element.ALIGN_LEFT));
    table.addCell(cellNoBorder(value, font, Element.ALIGN_LEFT));

    doc.add(table);
}

    private void isiPreviewNota(ResultSet rs) {
    try {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Keterangan");
        model.addColumn("Data");

        model.addRow(new Object[]{"Kode Booking", rs.getString("kode_booking")});
        model.addRow(new Object[]{"Tanggal Main", rs.getString("tanggal_main")});
        model.addRow(new Object[]{"Nama Member", rs.getString("nama_member")});
        model.addRow(new Object[]{"No WhatsApp", rs.getString("no_telp")});
        model.addRow(new Object[]{"Lapangan", rs.getString("nama_lapangan")});
        model.addRow(new Object[]{"Slot Waktu", rs.getString("slot_waktu")});
        model.addRow(new Object[]{"Jumlah Jam", rs.getString("jumlah_jam")});
        model.addRow(new Object[]{"Harga Per Jam", rupiah(rs.getString("harga_per_jam"))});
        model.addRow(new Object[]{"Total Harga", rupiah(rs.getString("total_harga"))});
        model.addRow(new Object[]{"Status Booking", rs.getString("status_booking")});
        model.addRow(new Object[]{"Sumber Booking", rs.getString("sumber_booking")});
        model.addRow(new Object[]{"Diproses Oleh", rs.getString("diproses_oleh")});

        tblPreview.setModel(model);
        setupTablePreview();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal menampilkan preview nota: " + e.getMessage());
    }
    }

    private void tambahTandaTanganLaporan(Document doc) throws Exception {
    Font normal = FontFactory.getFont(FontFactory.HELVETICA, 10);
    Font bold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);

    Paragraph jarak = new Paragraph("\n");
    doc.add(jarak);

    PdfPTable table = new PdfPTable(2);
    table.setWidthPercentage(100);
    table.setWidths(new float[]{60, 40});

    PdfPCell kiri = new PdfPCell(new Phrase(""));
    kiri.setBorder(PdfPCell.NO_BORDER);
    table.addCell(kiri);

    PdfPCell kanan = new PdfPCell();
    kanan.setBorder(PdfPCell.NO_BORDER);
    kanan.setHorizontalAlignment(Element.ALIGN_CENTER);

    Paragraph tanggal = new Paragraph(tanggalJakarta(), normal);
    tanggal.setAlignment(Element.ALIGN_CENTER);

    Paragraph mengetahui = new Paragraph("Mengetahui,", normal);
    mengetahui.setAlignment(Element.ALIGN_CENTER);

    Paragraph jabatan = new Paragraph("Admin Booking Futsal", normal);
    jabatan.setAlignment(Element.ALIGN_CENTER);

    Paragraph nama = new Paragraph("\n\n\n( Administrator Booking )", bold);
    nama.setAlignment(Element.ALIGN_CENTER);

    kanan.addElement(tanggal);
    kanan.addElement(mengetahui);
    kanan.addElement(jabatan);
    kanan.addElement(nama);

    table.addCell(kanan);
    doc.add(table);
    }

    private void tambahTandaTanganNota(Document doc, String namaAdmin, Font fontNormal, Font fontBold) throws Exception {
    addCenter(doc, tanggalJakarta(), fontNormal);
    addCenter(doc, "Mengetahui,", fontNormal);
    addCenter(doc, "Admin Booking Futsal", fontNormal);
    addCenter(doc, "\n\n( " + namaAdmin + " )", fontBold);
    }
    
private void cetakNotaBookingPDF() {
    if (cmbKodeBooking.getSelectedItem() == null
            || cmbKodeBooking.getSelectedItem().toString().equals("-- Pilih Kode Booking --")) {

        JOptionPane.showMessageDialog(this, "Pilih kode booking terlebih dahulu!");
        return;
    }

    String kodeBooking = cmbKodeBooking.getSelectedItem().toString();

    Rectangle ukuranStruk = new Rectangle(226, 700); // lebar struk 80mm
    Document doc = new Document(ukuranStruk, 12, 12, 12, 12);

    try {
        File file = buatFilePDF("nota_booking_" + kodeBooking + ".pdf");
        PdfWriter.getInstance(doc, new FileOutputStream(file));

        doc.open();

        Font fontToko = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font fontSub = FontFactory.getFont(FontFactory.HELVETICA, 7);
        Font fontNormal = FontFactory.getFont(FontFactory.COURIER, 8);
        Font fontBold = FontFactory.getFont(FontFactory.COURIER_BOLD, 8);
        Font fontTotal = FontFactory.getFont(FontFactory.COURIER_BOLD, 11);

        Connection conn = koneksi.configDB();

        String sql = "SELECT * FROM v_detail_booking WHERE kode_booking = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, kodeBooking);

        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            isiPreviewNota(rs);

            tambahLogoNota(doc);

            addCenter(doc, "FUTSAL BOOKING", fontToko);
            addCenter(doc, "Bukti Pemesanan Lapangan", fontSub);
            addCenter(doc, "Jl. Lapangan Futsal No. 1", fontSub);
            addCenter(doc, "Telp: 0812-0000-0000", fontSub);

            addLine(doc);

            addInfoRow(doc, "No", rs.getString("kode_booking"), fontNormal);
            addInfoRow(doc, "Cetak", tanggalCetak(), fontNormal);
            addInfoRow(doc, "Kasir", rs.getString("diproses_oleh"), fontNormal);

            addLine(doc);

            addInfoRow(doc, "Member", rs.getString("nama_member"), fontNormal);
            addInfoRow(doc, "Telp", rs.getString("no_telp"), fontNormal);
            addInfoRow(doc, "Tanggal", rs.getString("tanggal_main"), fontNormal);
            addInfoRow(doc, "Jam", rs.getString("slot_waktu"), fontNormal);
            addInfoRow(doc, "Status", rs.getString("status_booking"), fontNormal);
            addInfoRow(doc, "Sumber", rs.getString("sumber_booking"), fontNormal);

            addLine(doc);

            PdfPTable item = new PdfPTable(3);
            item.setWidthPercentage(100);
            item.setWidths(new float[]{52, 14, 34});

            item.addCell(cellNoBorder("Item", fontBold, Element.ALIGN_LEFT));
            item.addCell(cellNoBorder("Jam", fontBold, Element.ALIGN_CENTER));
            item.addCell(cellNoBorder("Harga", fontBold, Element.ALIGN_RIGHT));

            item.addCell(cellNoBorder(rs.getString("nama_lapangan"), fontNormal, Element.ALIGN_LEFT));
            item.addCell(cellNoBorder(rs.getString("jumlah_jam"), fontNormal, Element.ALIGN_CENTER));
            item.addCell(cellNoBorder(rupiah(rs.getString("harga_per_jam")), fontNormal, Element.ALIGN_RIGHT));

            doc.add(item);

            addLine(doc);

            PdfPTable totalTable = new PdfPTable(2);
            totalTable.setWidthPercentage(100);
            totalTable.setWidths(new float[]{40, 60});

            totalTable.addCell(cellNoBorder("TOTAL", fontTotal, Element.ALIGN_LEFT));
            totalTable.addCell(cellNoBorder(rupiah(rs.getString("total_harga")), fontTotal, Element.ALIGN_RIGHT));

            doc.add(totalTable);

            addLine(doc);

            addCenter(doc, "Terima kasih", fontSub);
            addCenter(doc, "Simpan nota ini sebagai bukti booking", fontSub);
            addCenter(doc, "Harap datang 10 menit sebelum jadwal", fontSub);
            addLine(doc);
            tambahTandaTanganNota(doc, rs.getString("diproses_oleh"), fontSub, fontBold);

        } else {
            JOptionPane.showMessageDialog(this, "Data booking tidak ditemukan!");
            doc.close();
            return;
        }

        rs.close();
        pst.close();
        doc.close();

        JOptionPane.showMessageDialog(this, "Nota booking berhasil dibuat.");
        bukaPDF(file);

    } catch (Exception e) {
        if (doc.isOpen()) {
            doc.close();
        }

        JOptionPane.showMessageDialog(this, "Gagal membuat nota booking: " + e.getMessage());
    }
}

private void cetakLaporanTabelPDF(String judul, String namaFile, String sql, String[] headers, String[] fields) {
    Document doc = new Document(PageSize.A4.rotate(), 25, 25, 30, 30);

    try {
        File file = buatFilePDF(namaFile);
        PdfWriter.getInstance(doc, new FileOutputStream(file));

        doc.open();
        tambahJudul(doc, judul);

        Connection conn = koneksi.configDB();
        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        PdfPTable table = new PdfPTable(headers.length);
        table.setWidthPercentage(100);
        table.setHeaderRows(1);

        DefaultTableModel previewModel = new DefaultTableModel();

        for (String header : headers) {
            table.addCell(cellHeader(header));
            previewModel.addColumn(header);
        }

        int jumlahData = 0;

        while (rs.next()) {
            Object[] rowPreview = new Object[fields.length];
            boolean striped = jumlahData % 2 == 1;

            for (int i = 0; i < fields.length; i++) {
                String value = rs.getString(fields[i]);

                if (fields[i].equalsIgnoreCase("total_pendapatan")
                        || fields[i].equalsIgnoreCase("jumlah_bayar")
                        || fields[i].equalsIgnoreCase("harga_per_jam")) {
                    value = rupiah(value);
                }

                rowPreview[i] = value;
                table.addCell(cellDataStriped(value, striped));
            }

            previewModel.addRow(rowPreview);
            jumlahData++;
        }

        tblPreview.setModel(previewModel);
        setupTablePreview();

        if (jumlahData == 0) {
            JOptionPane.showMessageDialog(this, "Data laporan tidak ditemukan.");
            doc.close();
            return;
        }

        doc.add(table);

        Font fontInfo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);

        Paragraph totalData = new Paragraph("\nJumlah Data: " + jumlahData, fontInfo);
        totalData.setAlignment(Element.ALIGN_RIGHT);
        doc.add(totalData);
        tambahTandaTanganLaporan(doc);
        
        rs.close();
        pst.close();
        doc.close();

        JOptionPane.showMessageDialog(this, judul + " berhasil dibuat.");
        bukaPDF(file);

    } catch (Exception e) {
        if (doc.isOpen()) {
            doc.close();
        }

        JOptionPane.showMessageDialog(this, "Gagal membuat " + judul + ": " + e.getMessage());
    }
}
    
    private void rebuildLayoutLaporan() {
    getContentPane().removeAll();
    getContentPane().setLayout(new java.awt.BorderLayout());

    jPanel3.removeAll();
    jPanel3.setLayout(new java.awt.BorderLayout(0, 18));
    jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(28, 28, 28, 8));

    javax.swing.JPanel headerPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
    headerPanel.setOpaque(false);
    headerPanel.add(jLabel1, java.awt.BorderLayout.WEST);

    javax.swing.JPanel contentPanel = new javax.swing.JPanel(new java.awt.BorderLayout(8, 0));
    contentPanel.setOpaque(false);

    javax.swing.JPanel leftPanel = new javax.swing.JPanel(new java.awt.BorderLayout(0, 22));
    leftPanel.setOpaque(false);

    javax.swing.JPanel formPanel = new javax.swing.JPanel(new java.awt.GridBagLayout());
    formPanel.setOpaque(false);

    java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
    gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1.0;

    jLabel2.setText("PILIH DATA");
    jLabel3.setText("Kode Booking");
    jLabel4.setText("CETAK LAPORAN");

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.insets = new java.awt.Insets(0, 0, 10, 18);
    formPanel.add(jLabel2, gbc);

    gbc.gridy = 1;
    gbc.insets = new java.awt.Insets(6, 0, 4, 18);
    formPanel.add(jLabel3, gbc);

    gbc.gridy = 2;
    gbc.insets = new java.awt.Insets(0, 0, 20, 18);
    formPanel.add(cmbKodeBooking, gbc);

    gbc.gridy = 3;
    gbc.insets = new java.awt.Insets(6, 0, 10, 18);
    formPanel.add(jLabel4, gbc);

    javax.swing.JPanel reportPanel = new javax.swing.JPanel(new java.awt.GridLayout(4, 1, 0, 12));
    reportPanel.setOpaque(false);
    reportPanel.add(btnNotaBooking);
    reportPanel.add(btnJadwalLapangan);
    reportPanel.add(btnPendapatan);
    reportPanel.add(btnPembayaran);

    gbc.gridy = 4;
    gbc.insets = new java.awt.Insets(0, 0, 20, 18);
    formPanel.add(reportPanel, gbc);

    javax.swing.JPanel bottomPanel = new javax.swing.JPanel(new java.awt.GridLayout(1, 2, 14, 0));
    bottomPanel.setOpaque(false);
    bottomPanel.add(btnRefresh);
    bottomPanel.add(btnKeluar);

    gbc.gridy = 5;
    gbc.insets = new java.awt.Insets(4, 0, 0, 18);
    formPanel.add(bottomPanel, gbc);

    leftPanel.add(formPanel, java.awt.BorderLayout.NORTH);

    javax.swing.JPanel tablePanel = new javax.swing.JPanel(new java.awt.BorderLayout());
    tablePanel.setOpaque(false);
    tablePanel.setPreferredSize(new java.awt.Dimension(760, 10));
    tablePanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);

    contentPanel.add(leftPanel, java.awt.BorderLayout.CENTER);
    contentPanel.add(tablePanel, java.awt.BorderLayout.EAST);

    jPanel3.add(headerPanel, java.awt.BorderLayout.NORTH);
    jPanel3.add(contentPanel, java.awt.BorderLayout.CENTER);

    getContentPane().add(jPanel3, java.awt.BorderLayout.CENTER);

    getContentPane().revalidate();
    getContentPane().repaint();
    }
    
    private void styleFormLaporan() {
    getContentPane().setBackground(AppTheme.ABYSS);
    jPanel3.setBackground(AppTheme.ABYSS);
    jPanel3.setOpaque(true);

    jLabel1.setText("LAPORAN / PRINTOUT");
    jLabel1.setForeground(AppTheme.SNOW);
    jLabel1.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 34));

    jLabel2.setForeground(AppTheme.INDIGO_LIGHT);
    jLabel2.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 24));

    jLabel4.setForeground(AppTheme.INDIGO_LIGHT);
    jLabel4.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 24));

    jLabel3.setForeground(AppTheme.SLATE);
    jLabel3.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));

    styleComboBox(cmbKodeBooking, AppTheme.MIDNIGHT, AppTheme.SNOW, AppTheme.RIM);

    styleButton(btnNotaBooking, "Nota Booking", AppTheme.INDIGO, AppTheme.SNOW, AppTheme.RIM);
    styleButton(btnJadwalLapangan, "Jadwal Lapangan", AppTheme.INDIGO_DEEP, AppTheme.SNOW, AppTheme.RIM);
    styleButton(btnPendapatan, "Laporan Pendapatan", AppTheme.EMERALD_TINT, AppTheme.SNOW, AppTheme.RIM);
    styleButton(btnPembayaran, "Laporan Pembayaran", AppTheme.AMBER_TINT, AppTheme.SNOW, AppTheme.RIM);
    styleButton(btnRefresh, "Refresh", AppTheme.ELEVATED, AppTheme.SNOW, AppTheme.RIM);
    styleButton(btnKeluar, "Keluar", AppTheme.CORAL_TINT, AppTheme.SNOW, AppTheme.RIM);

    setupTablePreview();
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
    button.setPreferredSize(new java.awt.Dimension(220, 46));
    button.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(border, 1),
            javax.swing.BorderFactory.createEmptyBorder(10, 14, 10, 14)
    ));
    }
    
    private void setupTablePreview() {
    tblPreview.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
    tblPreview.setFillsViewportHeight(true);
    tblPreview.setRowHeight(34);
    tblPreview.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

    tblPreview.setBackground(AppTheme.ABYSS);
    tblPreview.setForeground(AppTheme.SLATE);
    tblPreview.setGridColor(AppTheme.RIM);
    tblPreview.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
    tblPreview.setSelectionBackground(AppTheme.INDIGO_GHOST);
    tblPreview.setSelectionForeground(AppTheme.SNOW);
    tblPreview.setShowVerticalLines(false);
    tblPreview.setShowHorizontalLines(true);

    tblPreview.getTableHeader().setBackground(AppTheme.COURT);
    tblPreview.getTableHeader().setForeground(AppTheme.INDIGO_LIGHT);
    tblPreview.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));

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
    
    private String tanggalJakarta() {
    String[] hari = {
        "Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu"
    };

    String[] bulan = {
        "Januari", "Februari", "Maret", "April", "Mei", "Juni",
        "Juli", "Agustus", "September", "Oktober", "November", "Desember"
    };

    java.util.Calendar cal = java.util.Calendar.getInstance();

    String namaHari = hari[cal.get(java.util.Calendar.DAY_OF_WEEK) - 1];
    int tanggal = cal.get(java.util.Calendar.DAY_OF_MONTH);
    String namaBulan = bulan[cal.get(java.util.Calendar.MONTH)];
    int tahun = cal.get(java.util.Calendar.YEAR);

    return "Jakarta, " + namaHari + ", " + tanggal + " " + namaBulan + " " + tahun;
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cmbKodeBooking = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        btnNotaBooking = new javax.swing.JButton();
        btnPendapatan = new javax.swing.JButton();
        btnJadwalLapangan = new javax.swing.JButton();
        btnPembayaran = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        btnKeluar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPreview = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel3.setPreferredSize(new java.awt.Dimension(800, 800));

        jLabel1.setFont(new java.awt.Font("Arial Black", 0, 18)); // NOI18N
        jLabel1.setText("LAPORAN / PRINTOUT ");

        jLabel2.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel2.setText("Pilih Data");

        jLabel3.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jLabel3.setText("Kode Booking:");

        cmbKodeBooking.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel4.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel4.setText("Cetak Laporan");

        btnNotaBooking.setText("Nota Booking");
        btnNotaBooking.addActionListener(this::btnNotaBookingActionPerformed);

        btnPendapatan.setText("Laporan Pendapatan");
        btnPendapatan.addActionListener(this::btnPendapatanActionPerformed);

        btnJadwalLapangan.setText("Jadwal Lapangan");
        btnJadwalLapangan.addActionListener(this::btnJadwalLapanganActionPerformed);

        btnPembayaran.setText("Laporan Pembayaran");
        btnPembayaran.addActionListener(this::btnPembayaranActionPerformed);

        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(this::btnRefreshActionPerformed);

        btnKeluar.setText("Keluar");
        btnKeluar.addActionListener(this::btnKeluarActionPerformed);

        tblPreview.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblPreview);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(cmbKodeBooking, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnNotaBooking, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnPendapatan, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnPembayaran, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnJadwalLapangan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(32, 32, 32)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cmbKodeBooking, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNotaBooking, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnJadwalLapangan, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPendapatan, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPembayaran, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(376, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 472, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 962, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNotaBookingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNotaBookingActionPerformed
        // TODO add your handling code here:
        cetakNotaBookingPDF();
    }//GEN-LAST:event_btnNotaBookingActionPerformed

    private void btnJadwalLapanganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJadwalLapanganActionPerformed
        // TODO add your handling code here:
         String[] headers = {
        "Kode", "Lapangan", "Harga / Jam", "ID Jadwal", "Slot Waktu", "Status"
    };

    String[] fields = {
        "kode_lapangan", "nama_lapangan", "harga_per_jam", "id_jadwal", "slot_waktu", "status_slot"
    };

    cetakLaporanTabelPDF(
            "JADWAL LAPANGAN HARI INI",
            "jadwal_lapangan.pdf",
            "SELECT * FROM v_ketersediaan_hari_ini",
            headers,
            fields
    );
    }//GEN-LAST:event_btnJadwalLapanganActionPerformed

    private void btnPendapatanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPendapatanActionPerformed
        // TODO add your handling code here:
         String[] headers = {
        "Kode", "Lapangan", "Booking", "Jam", "Pendapatan", "Bulan"
    };

    String[] fields = {
        "kode_lapangan", "nama_lapangan", "total_booking", "total_jam", "total_pendapatan", "bulan"
    };

    cetakLaporanTabelPDF(
            "LAPORAN PENDAPATAN",
            "laporan_pendapatan.pdf",
            "SELECT * FROM v_laporan_pendapatan ORDER BY bulan DESC, total_pendapatan DESC",
            headers,
            fields
    );
    }//GEN-LAST:event_btnPendapatanActionPerformed

    private void btnPembayaranActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPembayaranActionPerformed
        // TODO add your handling code here:
      String[] headers = {
        "Kode Bayar", "Tgl Bayar", "Booking", "Tgl Main", "Member",
        "Lapangan", "Bayar", "Metode", "Status", "Kasir"
    };

    String[] fields = {
        "kode_pembayaran", "tanggal_bayar", "kode_booking", "tanggal_main", "nama_member",
        "nama_lapangan", "jumlah_bayar", "metode_bayar", "status_bayar", "kasir"
    };

    cetakLaporanTabelPDF(
            "LAPORAN PEMBAYARAN",
            "laporan_pembayaran.pdf",
            "SELECT * FROM v_rekap_pembayaran ORDER BY tanggal_bayar DESC",
            headers,
            fields
    );
    }//GEN-LAST:event_btnPembayaranActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // TODO add your handling code here:
        loadKodeBooking();
        tblPreview.setModel(new DefaultTableModel());
        setupTablePreview();
        JOptionPane.showMessageDialog(this, "Data berhasil direfresh.");
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKeluarActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnKeluarActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new FormLaporan().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnJadwalLapangan;
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnNotaBooking;
    private javax.swing.JButton btnPembayaran;
    private javax.swing.JButton btnPendapatan;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JComboBox<String> cmbKodeBooking;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblPreview;
    // End of variables declaration//GEN-END:variables
}
