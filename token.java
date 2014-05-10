public class Transfer {
    private String tglTransaksi;
    private String rekeningTujuan;
    private Double jumlahTransfer;
    private Double saldoAkhir;
    private String idNasabah;

    public String getIdNasabah() {
        return idNasabah;
    }

    public void setIdNasabah(String idNasabah) {
        this.idNasabah = idNasabah;
    }

    public Double getJumlahTransfer() {
        return jumlahTransfer;
    }

    public void setJumlahTransfer(Double jumlahTransfer) {
        this.jumlahTransfer = jumlahTransfer;
    }

    public String getRekeningTujuan() {
        return rekeningTujuan;
    }

    public void setRekeningTujuan(String rekeningTujuan) {
        this.rekeningTujuan = rekeningTujuan;
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
