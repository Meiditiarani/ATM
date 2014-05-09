/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author user
 */
public class Pembayaran {
    private String tglPembayaran;
    private String idJenisPembayaran;
    private Double jumlahBayar;
    private Double saldoAkhir;
    private String idNasabah;

    public String getIdJenisPembayaran() {
        return idJenisPembayaran;
    }

    public void setIdJenisPembayaran(String idJenisPembayaran) {
        this.idJenisPembayaran = idJenisPembayaran;
    }

    public String getIdNasabah() {
        return idNasabah;
    }

    public void setIdNasabah(String idNasabah) {
        this.idNasabah = idNasabah;
    }

    public Double getJumlahBayar() {
        return jumlahBayar;
    }

    public void setJumlahBayar(Double jumlahBayar) {
        this.jumlahBayar = jumlahBayar;
    }

    public Double getSaldoAkhir() {
        return saldoAkhir;
    }

    public void setSaldoAkhir(Double saldoAkhir) {
        this.saldoAkhir = saldoAkhir;
    }

    public String getTglPembayaran() {
        return tglPembayaran;
    }

    public void setTglPembayaran(String tglPembayaran) {
        this.tglPembayaran = tglPembayaran;
    }
}
