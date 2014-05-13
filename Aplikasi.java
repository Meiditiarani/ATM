package model;

import database.Koneksi;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Aplikasi {
    private Koneksi koneksi;
    private String sessionIdNasabah;
    private String sessionIdAdmin;  
    
    
    public Aplikasi() {
        this.koneksi = new Koneksi();
    }

    public String getSessionIdAdmin() {
        return sessionIdAdmin;
    }

    public void setSessionIdAdmin(String sessionIdAdmin) {
        this.sessionIdAdmin = sessionIdAdmin;
    }

    public String getSessionIdNasabah() {
        return sessionIdNasabah;
    }

    public void setSessionIdNasabah(String sessionIdNasabah) {
        this.sessionIdNasabah = sessionIdNasabah;
    }

    public String getNamaNasabah() {
        String query="SELECT * FROM NASABAH WHERE id_nasabah='"+ sessionIdNasabah +"'";
        ResultSet rs = koneksi.getData(query);
        try {
            if (rs.next()) {
                return rs.getString(2);
            }
        } catch (SQLException ex) {
            System.out.println("ERROR - Gagal mendapatkan nama nasabah");
        }
        
        return "anymouse";
    }
    
    /* =========================================================================
     * method to Query select username, password nasabah
     * =========================================================================
     */    
    public ResultSet checkNasabah(String id, String password) {
        String query="SELECT * FROM NASABAH WHERE id_nasabah='"+id+"' and pin=password('"+password+"')";
        return koneksi.getData(query);
    }
        
    public ResultSet checkAdmin(String id, String password, String token) {
        String query="SELECT * FROM ADMIN WHERE id_admin='"+id+"' and pin=password('"+password+"') and token = " + token + "";
        return koneksi.getData(query);
    }
    
    public ResultSet getEmailAdmin(String id) {
        String query="SELECT * FROM ADMIN WHERE id_admin='"+id+"'";
        return koneksi.getData(query);
    }
    
    public ResultSet getNasabahById(String id) {
        String query="SELECT * FROM NASABAH WHERE id_nasabah='"+id+"'";
        return koneksi.getData(query);
    }
    
    public ResultSet getMhsByNasabah(String id) {
        String query="SELECT id_mhs FROM NASABAH WHERE id_nasabah='"+id+"'";
        return koneksi.getData(query);
    }
    
    public ResultSet getMhsById(String id) {
        String query="SELECT * FROM MAHASISWA WHERE id_mhs=(SELECT id_mhs FROM NASABAH WHERE id_nasabah= '"+ id +"')";
        return koneksi.getData(query);
    }
    
    public ResultSet checkSaldoNasabah(String id) {
        return getNasabahById(id);
    }
    
    public ResultSet getEmailByIdNasabah(String id) {
        return getNasabahById(id);
    }
    
    public void updateToken(String id, String token){
        String query = "UPDATE NASABAH SET token = '" + token + "' WHERE id_nasabah= '" + id + "'";
        koneksi.runQuery(query);
    }
    
    public void updateAdminToken(String id, String token){
        String query = "UPDATE ADMIN SET token = '" + token + "' WHERE id_admin= '" + id + "'";
        koneksi.runQuery(query);
    }
    
    public void deleteToken(String id){
        String query = "UPDATE NASABAH SET token = '' WHERE id_nasabah= '" + id + "'";
        koneksi.runQuery(query);
    }
    
    public ResultSet checkRekeningTujuan(String id) {
        return getNasabahById(id);
    }
    
    public ResultSet getTokenFromNasabah(String id) {
        return getNasabahById(id);
    }
    
    public void debetSaldoPengirim(String id, Double jumlah) {
        String query = "UPDATE NASABAH SET saldo = saldo - '" + jumlah + "' WHERE id_nasabah= '" + id + "'";
        koneksi.runQuery(query);
    }
    
    public void kreditSaldoPenerima(String id, Double jumlah) {
        String query = "UPDATE NASABAH SET saldo = saldo + '" + jumlah + "' WHERE id_nasabah= '" + id + "'";
        koneksi.runQuery(query);
    }
    
    public void insertToTransfer(Transfer t) {
        String query = "INSERT INTO TRANSFER VALUES ('" + t.getTglTransaksi() + "', '" + t.getRekeningTujuan() + "', '" + t.getJumlahTransfer() + "', '" + t.getSaldoAkhir() + "', '" + t.getIdNasabah() + "', 0)";
        koneksi.runQuery(query);
    }
    
    public void insertToMutasi(Mutasi m) {
        String query = "INSERT INTO MUTASI_NEW VALUES (0, '" + m.getTglMutasi() + "', '" + m.getKredit() + "', '" + m.getDebet() + "', '" + m.getSaldo() + "', '" + m.getIdTransfer() + "', '" + m.getIdPembayaran() + "', '" + m.getIdPengambilan() + "')";
        koneksi.runQuery(query);        
    }
    
    public ResultSet getIdTransferByNasabah(String id) {
        String query = "SELECT * FROM TRANSFER WHERE id_nasabah = '" + id + "'";
        return koneksi.getData(query);
    }
    
    public void pengambilanUang(Double jumlah, String id) {
        String query = "UPDATE NASABAH SET saldo = saldo - '" + jumlah + "' WHERE id_nasabah= '" + id + "'";
        koneksi.runQuery(query);
    }
    
    public void insertToPengambilan(Pengambilan p) {
        String query = "INSERT INTO Pengambilan VALUES ('" + p.getTglTransaksi() + "', '" + p.getJumlahAmbil() + "', '" + p.getSaldoAkhir() + "', '" + p.getIdNasabah() + "', 0)";
        koneksi.runQuery(query);
    }
    
    public ResultSet getIdPengambilanByNasabah(String id) {
        String query = "SELECT * FROM PENGAMBILAN WHERE id_nasabah = '" + id + "'";
        return koneksi.getData(query);
    }
        
    public void insertPengambilanToMutasi(Mutasi m) {
        String query = "INSERT INTO MUTASI_NEW VALUES (0, '" + m.getTglMutasi() + "', '" + m.getKredit() + "', '" + m.getDebet() + "', '" + m.getSaldo() + "', '" + m.getIdTransfer() + "', '" + m.getIdPembayaran() + "', '" + m.getIdPengambilan() + "')";
        koneksi.runQuery(query);        
    }
    
    public void updateStatusBayar(String id) {
        String query = "UPDATE MAHASISWA SET status_bayar = 'L' WHERE id_mhs=(SELECT id_mhs FROM NASABAH WHERE id_nasabah= '"+ id +"')";
        koneksi.runQuery(query);
    }
    
    public void insertToPembayaran(Pembayaran p) {
        String query = "INSERT INTO Pembayaran VALUES ('" + p.getTglPembayaran() + "', '" + p.getIdJenisPembayaran() + "', '" + p.getJumlahBayar() + "', '" + p.getSaldoAkhir() + "','" + p.getIdNasabah() + "', 0)";
        koneksi.runQuery(query);
    }
    
    public ResultSet getIdPembayaranByNasabah(String id) {
        String query = "SELECT * FROM PEMBAYARAN WHERE id_nasabah = '" + id + "'";
        return koneksi.getData(query);
    }
}
