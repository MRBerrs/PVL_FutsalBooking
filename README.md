# ⚽ Aplikasi Booking Lapangan Futsal

Aplikasi Booking Lapangan Futsal adalah aplikasi desktop berbasis **Java Swing** dan **MySQL** yang dibuat untuk membantu proses pengelolaan booking lapangan futsal, mulai dari data member, data lapangan, booking, pembayaran, sampai pembuatan laporan PDF.

Project ini dibuat sebagai tugas kuliah dan dikembangkan menggunakan **NetBeans IDE**.

---

## 📌 Deskripsi Project

Aplikasi ini dirancang untuk memudahkan admin dalam mengelola transaksi booking lapangan futsal secara lebih terstruktur. Admin dapat melakukan login, mengelola data member, mengelola data lapangan, membuat booking, mencatat pembayaran, dan mencetak laporan dalam bentuk PDF.

Sistem ini menggunakan database **MySQL** sebagai penyimpanan data, **Java Swing** sebagai antarmuka desktop, serta **OpenPDF** untuk fitur printout laporan.

---

## ✨ Fitur Utama

- Login admin
- Dashboard menu utama
- Pengelolaan data member
- Pengelolaan data lapangan
- Pengelolaan data booking lapangan
- Pengelolaan data pembayaran
- Preview data laporan
- Cetak nota booking
- Cetak laporan jadwal lapangan
- Cetak laporan pendapatan
- Cetak laporan pembayaran
- Generate laporan PDF menggunakan OpenPDF

---

## 🛠️ Teknologi yang Digunakan

| Teknologi | Keterangan |
|---|---|
| Java | Bahasa pemrograman utama |
| Java Swing | Membuat tampilan aplikasi desktop |
| NetBeans IDE | IDE untuk pengembangan aplikasi |
| MySQL | Database penyimpanan data |
| JDBC / MySQL Connector | Koneksi Java ke MySQL |
| FlatLaf | Tampilan UI modern |
| JCalendar / JDateChooser | Input tanggal booking |
| OpenPDF | Generate nota dan laporan PDF |

---

## 📚 Library yang Digunakan

### Java Swing
Digunakan untuk membuat tampilan aplikasi desktop seperti form login, menu utama, form member, form lapangan, form booking, form pembayaran, dan form laporan.

### MySQL Connector / JDBC
Digunakan untuk menghubungkan aplikasi Java dengan database MySQL.

### FlatLaf
Digunakan untuk membuat tampilan aplikasi menjadi lebih modern dan rapi.

### JCalendar / JDateChooser
Digunakan untuk memilih tanggal pada form booking.

### OpenPDF
Digunakan untuk membuat file PDF seperti nota booking dan laporan. Printout pada aplikasi ini menggunakan **OpenPDF**.

---

## 🔄 Alur Program

1. User membuka aplikasi.
2. User melakukan login menggunakan akun admin.
3. Jika login berhasil, user masuk ke menu utama.
4. Pada menu utama, user dapat memilih fitur yang tersedia.
5. Admin dapat mengelola data member.
6. Admin dapat mengelola data lapangan.
7. Admin dapat membuat data booking berdasarkan member, lapangan, jadwal, tanggal main, dan jumlah jam.
8. Sistem menghitung total harga booking secara otomatis.
9. Admin dapat mencatat pembayaran dari data booking.
10. Admin membuka menu laporan untuk melihat preview dan mencetak laporan.
11. Laporan dibuat dalam bentuk PDF menggunakan OpenPDF.

---

## 🧭 Struktur Menu

- Login
- Menu Utama
- Data Member
- Data Lapangan
- Booking
- Pembayaran
- Laporan / Printout

---

## 🖨️ Jenis Printout

- Nota Booking
- Jadwal Lapangan
- Laporan Pendapatan
- Laporan Pembayaran

---

## 🗄️ Database

Aplikasi ini menggunakan database MySQL untuk menyimpan data:

- User
- Member
- Lapangan
- Jadwal
- Booking
- Pembayaran

---

## ▶️ Cara Menjalankan Project

1. Clone atau download repository ini.
2. Buka project menggunakan **NetBeans IDE**.
3. Import database MySQL yang sudah disediakan.
4. Pastikan konfigurasi koneksi database sudah sesuai.
5. Tambahkan semua library yang dibutuhkan ke dalam project.
6. Jalankan aplikasi dari file `Login.java`.
7. Login menggunakan akun admin.
8. Gunakan menu utama untuk mengakses fitur aplikasi.

---

## 👥 Anggota Kelompok

| No | Nama | NPM |
|---|---|---|
| 1 | Akbar Abidin | 202343500525 |
| 2 | Ahmad Fauzan | 202343500528 |
| 3 | Dwi Saputri | 202343500545 |
| 4 | Tantia Amenda | 202343500514 |
| 5 | Ghiva Syaekhila Juliano | 202343500493 |
| 6 | Muhamad Alfarizi | 202343500544 |
| 7 | Arifin Fakih | 202343500489 |

---

## 📌 Kesimpulan

Aplikasi Booking Lapangan Futsal ini dibuat untuk membantu proses pengelolaan booking lapangan secara lebih rapi dan terstruktur. Dengan adanya fitur data member, data lapangan, booking, pembayaran, dan laporan, aplikasi ini dapat mempermudah admin dalam mengelola transaksi booking lapangan futsal.

Project ini juga menjadi implementasi penggunaan **Java Swing**, **MySQL**, **JDBC**, **FlatLaf**, **JCalendar**, dan **OpenPDF** dalam pembuatan aplikasi desktop berbasis sistem informasi.

---

## 📷 Preview

Tambahkan screenshot aplikasi di bagian ini, misalnya:

```text
assets/preview-login.png
assets/preview-dashboard.png
assets/preview-booking.png
assets/preview-laporan.png
```

---

## 📝 Catatan

Project ini dibuat untuk kebutuhan pembelajaran dan tugas kuliah.
