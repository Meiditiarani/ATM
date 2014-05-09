/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author user
 */
public class Pengambilan {
    private String tglTransaksi;
    private Double jumlahAmbil;
    private Double saldoAkhir;
    private String idNasabah;

    public String getIdNasabah() {
        return idNasabah;
    }

    public void setIdNasabah(String idNasabah) {
        this.idNasabah = idNasabah;
    }

    public Double getJumlahAmbil() {
        return jumlahAmbil;
    }

    public void setJumlahAmbil(Double jumlahAmbil) {
        this.jumlahAmbil = jumlahAmbil;
    }

    public Double getSaldoAkhir() {
        return saldoAkhir;
    }

    public void setSaldoAkhir(Double saldoAkhir) {
        this.saldoAkhir = saldoAkhir;
    }

    public String getTglTransaksi() {
        return tglTransaksi;
    }

    public void setTglTransaksi(String tglTransaksi) {
        this.tglTransaksi = tglTransaksi;
    }
}
