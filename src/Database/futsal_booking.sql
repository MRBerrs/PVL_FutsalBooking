-- ============================================================
--  DATABASE: futsal_booking
--  Rancang Bangun Aplikasi Desktop untuk Pemesanan Lapangan Futsal
--  Dibuat untuk NetBeans (Maven) + MySQL
-- ============================================================

CREATE DATABASE IF NOT EXISTS futsal_booking
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE futsal_booking;

-- ------------------------------------------------------------
-- 1. TABEL USERS (Admin & Staff)
-- ------------------------------------------------------------
CREATE TABLE users (
    id_user       INT AUTO_INCREMENT PRIMARY KEY,
    username      VARCHAR(50)  NOT NULL UNIQUE,
    password      VARCHAR(255) NOT NULL,          -- simpan hash (bcrypt/SHA-256)
    nama_lengkap  VARCHAR(100) NOT NULL,
    email         VARCHAR(100) NOT NULL UNIQUE,
    no_telp       VARCHAR(15),
    role          ENUM('admin', 'staff') NOT NULL DEFAULT 'staff',
    is_active     TINYINT(1)   NOT NULL DEFAULT 1,
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- 2. TABEL MEMBERS (Pelanggan)
-- ------------------------------------------------------------
CREATE TABLE members (
    id_member     INT AUTO_INCREMENT PRIMARY KEY,
    kode_member   VARCHAR(20)  NOT NULL UNIQUE,   -- contoh: MBR-0001
    nama_lengkap  VARCHAR(100) NOT NULL,
    no_telp       VARCHAR(15)  NOT NULL,
    email         VARCHAR(100),
    alamat        TEXT,
    jenis_kelamin ENUM('L','P') DEFAULT NULL,
    tanggal_daftar DATE        NOT NULL DEFAULT (CURRENT_DATE),
    is_active     TINYINT(1)  NOT NULL DEFAULT 1,
    created_at    DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- 3. TABEL LAPANGAN
-- ------------------------------------------------------------
CREATE TABLE lapangan (
    id_lapangan   INT AUTO_INCREMENT PRIMARY KEY,
    kode_lapangan VARCHAR(10)  NOT NULL UNIQUE,   -- contoh: LAP-A, LAP-B
    nama_lapangan VARCHAR(100) NOT NULL,
    jenis_lantai  ENUM('vinyl','rumput_sintetis','parket','semen') NOT NULL DEFAULT 'vinyl',
    kapasitas     INT          NOT NULL DEFAULT 10,
    harga_per_jam DECIMAL(10,2) NOT NULL,
    deskripsi     TEXT,
    status        ENUM('tersedia','maintenance','tidak_aktif') NOT NULL DEFAULT 'tersedia',
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- 4. TABEL JADWAL (Slot Jam Operasional)
-- ------------------------------------------------------------
CREATE TABLE jadwal (
    id_jadwal     INT AUTO_INCREMENT PRIMARY KEY,
    jam_mulai     TIME NOT NULL,
    jam_selesai   TIME NOT NULL,
    label         VARCHAR(30),                    -- contoh: "07.00 - 08.00"
    is_active     TINYINT(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- 5. TABEL BOOKING
-- ------------------------------------------------------------
CREATE TABLE booking (
    id_booking    INT AUTO_INCREMENT PRIMARY KEY,
    kode_booking  VARCHAR(20)  NOT NULL UNIQUE,   -- contoh: BK-20250101-001
    id_member     INT          NOT NULL,
    id_lapangan   INT          NOT NULL,
    id_jadwal     INT          NOT NULL,
    id_user       INT          NOT NULL,           -- staff yg proses booking
    tanggal_main  DATE         NOT NULL,
    jumlah_jam    INT          NOT NULL DEFAULT 1,
    total_harga   DECIMAL(10,2) NOT NULL,
    catatan       TEXT,
    status_booking ENUM('pending','konfirmasi','selesai','batal') NOT NULL DEFAULT 'pending',
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_booking_member   FOREIGN KEY (id_member)   REFERENCES members(id_member),
    CONSTRAINT fk_booking_lapangan FOREIGN KEY (id_lapangan) REFERENCES lapangan(id_lapangan),
    CONSTRAINT fk_booking_jadwal   FOREIGN KEY (id_jadwal)   REFERENCES jadwal(id_jadwal),
    CONSTRAINT fk_booking_user     FOREIGN KEY (id_user)     REFERENCES users(id_user),

    -- Mencegah double booking (lapangan + tanggal + jadwal harus unik)
    UNIQUE KEY uq_booking_slot (id_lapangan, tanggal_main, id_jadwal)
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- 6. TABEL PEMBAYARAN
-- ------------------------------------------------------------
CREATE TABLE pembayaran (
    id_pembayaran   INT AUTO_INCREMENT PRIMARY KEY,
    kode_pembayaran VARCHAR(20)   NOT NULL UNIQUE,  -- contoh: PAY-20250101-001
    id_booking      INT           NOT NULL UNIQUE,  -- 1 booking = 1 pembayaran
    id_user         INT           NOT NULL,          -- kasir/staff
    jumlah_bayar    DECIMAL(10,2) NOT NULL,
    metode_bayar    ENUM('tunai','transfer','qris','debit','kredit') NOT NULL DEFAULT 'tunai',
    bukti_transfer  VARCHAR(255),                   -- path file bukti (opsional)
    tanggal_bayar   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status_bayar    ENUM('lunas','dp','belum_bayar') NOT NULL DEFAULT 'lunas',
    keterangan      TEXT,

    CONSTRAINT fk_bayar_booking FOREIGN KEY (id_booking) REFERENCES booking(id_booking),
    CONSTRAINT fk_bayar_user    FOREIGN KEY (id_user)    REFERENCES users(id_user)
) ENGINE=InnoDB;

-- ============================================================
--  DATA AWAL (SEED DATA)
-- ============================================================

-- Users (password: 'admin123' — ganti dengan hash di production)
INSERT INTO users (username, password, nama_lengkap, email, no_telp, role) VALUES
('admin',  SHA2('admin123', 256),  'Administrator',  'admin@futsal.com',  '081200000001', 'admin'),
('staff1', SHA2('staff123', 256),  'Budi Santoso',   'budi@futsal.com',   '081200000002', 'staff'),
('staff2', SHA2('staff123', 256),  'Siti Rahayu',    'siti@futsal.com',   '081200000003', 'staff');

-- Members
INSERT INTO members (kode_member, nama_lengkap, no_telp, email, alamat, jenis_kelamin) VALUES
('MBR-0001', 'Andi Wijaya',      '081311111111', 'andi@gmail.com',  'Jl. Merdeka No.1',    'L'),
('MBR-0002', 'Rizky Pratama',    '081322222222', 'rizky@gmail.com', 'Jl. Sudirman No.5',   'L'),
('MBR-0003', 'Dewi Kusuma',      '081333333333', 'dewi@gmail.com',  'Jl. Gatot Subroto 3', 'P'),
('MBR-0004', 'Fajar Nugroho',    '081344444444', 'fajar@gmail.com', 'Jl. Ahmad Yani 10',   'L'),
('MBR-0005', 'Hendra Saputra',   '081355555555', 'hendra@gmail.com','Jl. Diponegoro 7',    'L');

-- Lapangan (lebih dari 5)
INSERT INTO lapangan (kode_lapangan, nama_lapangan, jenis_lantai, harga_per_jam, deskripsi, status) VALUES
('LAP-A', 'Lapangan A - Vinyl',          'vinyl',            100000, 'Lapangan indoor, lantai vinyl premium',    'tersedia'),
('LAP-B', 'Lapangan B - Vinyl',          'vinyl',            100000, 'Lapangan indoor, lantai vinyl premium',    'tersedia'),
('LAP-C', 'Lapangan C - Sintetis',       'rumput_sintetis',  120000, 'Lapangan outdoor, rumput sintetis',        'tersedia'),
('LAP-D', 'Lapangan D - Sintetis',       'rumput_sintetis',  120000, 'Lapangan outdoor, rumput sintetis',        'tersedia'),
('LAP-E', 'Lapangan E - Parket',         'parket',           150000, 'Lapangan VIP, lantai parket kayu',         'tersedia'),
('LAP-F', 'Lapangan F - Parket',         'parket',           150000, 'Lapangan VIP, lantai parket kayu',         'tersedia'),
('LAP-G', 'Lapangan G - Outdoor Semen',  'semen',             80000, 'Lapangan outdoor standar',                 'maintenance');

-- Jadwal Slot (07.00 - 22.00)
INSERT INTO jadwal (jam_mulai, jam_selesai, label) VALUES
('07:00', '08:00', '07.00 - 08.00'),
('08:00', '09:00', '08.00 - 09.00'),
('09:00', '10:00', '09.00 - 10.00'),
('10:00', '11:00', '10.00 - 11.00'),
('11:00', '12:00', '11.00 - 12.00'),
('12:00', '13:00', '12.00 - 13.00'),
('13:00', '14:00', '13.00 - 14.00'),
('14:00', '15:00', '14.00 - 15.00'),
('15:00', '16:00', '15.00 - 16.00'),
('16:00', '17:00', '16.00 - 17.00'),
('17:00', '18:00', '17.00 - 18.00'),
('18:00', '19:00', '18.00 - 19.00'),
('19:00', '20:00', '19.00 - 20.00'),
('20:00', '21:00', '20.00 - 21.00'),
('21:00', '22:00', '21.00 - 22.00');

-- Booking Contoh
INSERT INTO booking (kode_booking, id_member, id_lapangan, id_jadwal, id_user, tanggal_main, jumlah_jam, total_harga, status_booking) VALUES
('BK-20250427-001', 1, 1, 7,  2, '2025-04-27', 2, 200000, 'konfirmasi'),
('BK-20250427-002', 2, 3, 10, 2, '2025-04-27', 1, 120000, 'konfirmasi'),
('BK-20250427-003', 3, 5, 13, 3, '2025-04-27', 2, 300000, 'pending'),
('BK-20250428-001', 4, 2, 8,  2, '2025-04-28', 1, 100000, 'konfirmasi'),
('BK-20250428-002', 5, 4, 11, 3, '2025-04-28', 3, 360000, 'konfirmasi');

-- Pembayaran Contoh
INSERT INTO pembayaran (kode_pembayaran, id_booking, id_user, jumlah_bayar, metode_bayar, status_bayar) VALUES
('PAY-20250427-001', 1, 2, 200000, 'tunai',    'lunas'),
('PAY-20250427-002', 2, 2, 120000, 'qris',     'lunas'),
('PAY-20250428-001', 4, 2, 100000, 'transfer', 'lunas'),
('PAY-20250428-002', 5, 3, 360000, 'tunai',    'lunas');

-- ============================================================
--  VIEWS UNTUK PRINTOUT / LAPORAN
-- ============================================================

-- View 1: Detail Booking Lengkap
CREATE OR REPLACE VIEW v_detail_booking AS
SELECT
    b.kode_booking,
    b.tanggal_main,
    m.kode_member,
    m.nama_lengkap  AS nama_member,
    m.no_telp,
    l.kode_lapangan,
    l.nama_lapangan,
    j.label         AS slot_waktu,
    b.jumlah_jam,
    l.harga_per_jam,
    b.total_harga,
    b.status_booking,
    u.nama_lengkap  AS diproses_oleh,
    b.created_at    AS waktu_booking
FROM booking b
JOIN members  m ON b.id_member   = m.id_member
JOIN lapangan l ON b.id_lapangan = l.id_lapangan
JOIN jadwal   j ON b.id_jadwal   = j.id_jadwal
JOIN users    u ON b.id_user     = u.id_user;

-- View 2: Laporan Pendapatan Per Lapangan
CREATE OR REPLACE VIEW v_laporan_pendapatan AS
SELECT
    l.kode_lapangan,
    l.nama_lapangan,
    COUNT(b.id_booking)     AS total_booking,
    SUM(b.jumlah_jam)       AS total_jam,
    SUM(p.jumlah_bayar)     AS total_pendapatan,
    DATE_FORMAT(b.tanggal_main, '%Y-%m') AS bulan
FROM booking b
JOIN lapangan   l ON b.id_lapangan  = l.id_lapangan
JOIN pembayaran p ON b.id_booking   = p.id_booking
WHERE b.status_booking IN ('konfirmasi','selesai')
GROUP BY l.id_lapangan, bulan;

-- View 3: Ketersediaan Lapangan Hari Ini
CREATE OR REPLACE VIEW v_ketersediaan_hari_ini AS
SELECT
    l.kode_lapangan,
    l.nama_lapangan,
    l.harga_per_jam,
    j.id_jadwal,
    j.label AS slot_waktu,
    CASE WHEN b.id_booking IS NOT NULL THEN 'Terpesan' ELSE 'Tersedia' END AS status_slot
FROM lapangan l
CROSS JOIN jadwal j
LEFT JOIN booking b
    ON b.id_lapangan   = l.id_lapangan
    AND b.id_jadwal     = j.id_jadwal
    AND b.tanggal_main  = CURRENT_DATE
    AND b.status_booking IN ('pending','konfirmasi')
WHERE l.status = 'tersedia'
  AND j.is_active = 1
ORDER BY l.kode_lapangan, j.jam_mulai;

-- View 4: Rekap Transaksi Pembayaran
CREATE OR REPLACE VIEW v_rekap_pembayaran AS
SELECT
    p.kode_pembayaran,
    p.tanggal_bayar,
    b.kode_booking,
    b.tanggal_main,
    m.nama_lengkap  AS nama_member,
    l.nama_lapangan,
    p.jumlah_bayar,
    p.metode_bayar,
    p.status_bayar,
    u.nama_lengkap  AS kasir
FROM pembayaran p
JOIN booking  b ON p.id_booking  = b.id_booking
JOIN members  m ON b.id_member   = m.id_member
JOIN lapangan l ON b.id_lapangan = l.id_lapangan
JOIN users    u ON p.id_user     = u.id_user;

-- ============================================================
-- QUERY PRINTOUT SIAP PAKAI
-- ============================================================

-- PRINTOUT 1: Nota / Bukti Booking
-- SELECT * FROM v_detail_booking WHERE kode_booking = 'BK-20250427-001';

-- PRINTOUT 2: Jadwal Lapangan Per Hari
-- SELECT * FROM v_ketersediaan_hari_ini;

-- PRINTOUT 3: Laporan Pendapatan Bulanan
-- SELECT * FROM v_laporan_pendapatan ORDER BY bulan DESC, total_pendapatan DESC;

-- PRINTOUT 4: Laporan Transaksi Pembayaran
-- SELECT * FROM v_rekap_pembayaran ORDER BY tanggal_bayar DESC;
