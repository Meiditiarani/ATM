/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author dupi
 */
public class Nasabah {
    private String idNasabah;
    private String nama;
    private String alamat;
    private String email;
    private String notelp;
    private Double saldo;
    private String pin;
    private String id_mhs;

    public String getAlamat() {
        return alamat;
    }
 public Nasabah(){
        
    }

    public Nasabah(String idNabasah, String nama, String alamat, String email, String notelp, Double saldo, String pin, String id_mhs){
        this.idNasabah = idNabasah;
        this.nama = nama;
        this.alamat = alamat;
        this.email = email;
        this.notelp = notelp;
        this.saldo = saldo;
        this.pin = pin;
        this.id_mhs = id_mhs;
    }
    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdNasabah() {
        return idNasabah;
    }

    public void setIdNasabah(String idNasabah) {
        this.idNasabah = idNasabah;
    }

    public String getId_mhs() {
        return id_mhs;
    }

    public void setId_mhs(String id_mhs) {
        this.id_mhs = id_mhs;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNotelp() {
        return notelp;
    }

    public void setNotelp(String notelp) {
        this.notelp = notelp;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }
}
