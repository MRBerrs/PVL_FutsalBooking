# Panduan Upload ke GitHub

## Cara paling gampang lewat website GitHub

1. Buka https://github.com
2. Login akun GitHub
3. Klik tombol New Repository
4. Isi nama repository, contoh:
   aplikasi-booking-futsal
5. Pilih Public atau Private
6. Jangan centang Add README dulu kalau kamu sudah punya README.md
7. Klik Create Repository
8. Klik uploading an existing file
9. Drag semua file project NetBeans ke halaman GitHub
10. Pastikan file README.md dan .gitignore ikut masuk
11. Isi commit message:
    Initial commit aplikasi booking futsal
12. Klik Commit changes

## Cara lewat Git command

Buka terminal di folder project, lalu jalankan:

```bash
git init
git add .
git commit -m "Initial commit aplikasi booking futsal"
git branch -M main
git remote add origin https://github.com/USERNAME/NAMA-REPO.git
git push -u origin main
```

Ganti USERNAME dan NAMA-REPO sesuai akun GitHub kamu.
