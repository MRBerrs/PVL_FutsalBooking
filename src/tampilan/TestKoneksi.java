/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tampilan;

/**
 *
 * @author Administrator
 */
import java.sql.Connection;
import koneksi.koneksi;
public class TestKoneksi {
    public static void main(String[] args) {
        try {
            Connection conn = koneksi.configDB();
            if (conn != null) {
                System.out.println("Koneksi database berhasil.");
            }
        } catch (Exception e) {
            System.out.println("Koneksi gagal: " + e.getMessage());
        }
    }
}
