/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.swing.JOptionPane;
import model.Aplikasi;
import model.Mutasi;
import model.Nasabah;
import model.Pembayaran;
import model.Pengambilan;
import model.SendEmail;
import model.Token;
import model.Transfer;
import view.InterfaceView;
import view.LoginAdmin;
import view.LoginNasabah;
import view.ViewAddNasabah;
import view.ViewMenuAdmin;
import view.ViewMutasi;
import view.ViewNasabah;
import view.ViewNasabahBayarSemesteran;
import view.ViewNasabahBayarTestEnglish;
import view.ViewNasabahBayarWisuda;
import view.ViewNasabahCekSaldo;
import view.ViewNasabahPengambilan;
import view.ViewNasabahPembayaran;
import view.ViewNasabahPengambilanManual;
import view.ViewNasabahPengambilanPilihNominal;
import view.ViewNasabahTransfer;
import view.ViewStruckPembayaran;
import view.ViewStrukPengambilan;
import view.ViewStrukSaldo;
import view.ViewStrukTransfer;

/**
 *
 * @author dupi
 */
public class Handler implements ActionListener {

    private Aplikasi model;
    private InterfaceView view;

    // set the default view
    public void setView(InterfaceView view) {
        this.view = view;
    }

    // add action listener to the view
    public void setListener() {
        view.addListener(this);
    }

    // model initialization
    public void setModel(Aplikasi model) {
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        LoginNasabah formLogin;
        ViewNasabah vn;
        ViewNasabahCekSaldo vncs;
        ViewNasabahTransfer vnt;
        ViewNasabahPengambilan vnp;
        ViewNasabahPengambilanManual vnpm;
        ViewNasabahPengambilanPilihNominal vnppn;
        ViewNasabahPembayaran pembayaran;
        ViewNasabahBayarSemesteran vnbs;
        ViewNasabahBayarTestEnglish vnbte;
        ViewNasabahBayarWisuda vnbw;
        ViewMenuAdmin vma;
        ViewAddNasabah van;
        ViewMutasi vm;
        LoginAdmin la;
        
        ViewStruckPembayaran vsps;
        ViewStrukPengambilan vsp;
        ViewStrukSaldo vss;
        ViewStrukTransfer vst;


        Object source = e.getSource();
        if (view instanceof LoginNasabah) {
            formLogin = (LoginNasabah) view;
            if (source.equals(formLogin.getBtnLogin())) {
                try {
                    if (formLogin.getTxtNIM().trim().equals("") || formLogin.getTxtPIN().trim().equals("")) {
                        view.viewErrorMsg("Field tidak boleh kosong");
                        return;
                    }

                    ResultSet rs = model.checkNasabah(formLogin.getTxtNIM(), formLogin.getTxtPIN());
                    if (rs.next()) {
                        view.viewErrorMsg("Log In Sukses sebagai Nasabah, klik OK untuk melanjutkan");
                        model.setSessionIdNasabah(formLogin.getTxtNIM().trim());
                        formLogin.setVisible(false);
                        setView(new ViewNasabah(model));
                        setListener();
                        view.setVisible(true);
                        return;
                    }

                    view.viewErrorMsg("Kombinasi Username dan Password yang anda masukan salah");
                } catch (SQLException ex) {
                    System.out.println("ERROR - Check Nasabah saat login gagal...");
                }
            } else if (source.equals(formLogin.getBtnAdmin())) {
                formLogin.setVisible(false);
                setView(new LoginAdmin());
                setListener();
                view.setVisible(true);   
            }
        } else if (view instanceof LoginAdmin) {
            la = (LoginAdmin) view;
            if (source.equals(la.getBtnLogin())) {
                try {
                    if (la.getTxtNIM().trim().equals("") || la.getTxtPIN().trim().equals("") || la.getTxtToken().equals("")) {
                        view.viewErrorMsg("Field tidak boleh kosong");
                        return;
                    }

                    ResultSet rs = model.checkAdmin(la.getTxtNIM(), la.getTxtPIN(), la.getTxtToken());
                    if (rs.next()) {
                        view.viewErrorMsg("Log In Sukses sebagai Administrator, klik OK untuk melanjutkan");
                        model.setSessionIdAdmin(la.getTxtNIM().trim());
                        la.setVisible(false);
                        setView(new ViewMenuAdmin(model));
                        setListener();
                        view.setVisible(true);
                        return;
                    }

                    view.viewErrorMsg("Kombinasi username, password dan token yang anda masukan tidak sesuai");
                } catch (SQLException ex) {
                    System.out.println("ERROR - Check Nasabah saat login gagal...");
                }
            } else if (source.equals(la.getBtnGenerateToken())) {
                try {
                    if (la.getTxtNIM().trim().equals("")) {
                        view.viewErrorMsg("Isi Nim dengan benar jika ingin generete token");
                        return;
                    }
                    String token = Token.generateActivationCode(8);

                    ResultSet rs = model.getEmailAdmin(la.getTxtNIM());
                    if (rs.next()) {
                        String penerima = rs.getString(6);
                        SendEmail.sendingEmail(penerima, token);
                        model.updateAdminToken(la.getTxtNIM(), token);

                        view.viewErrorMsg("Token telah dikirim ke email anda");
                    } else {
                        view.viewErrorMsg("Nim yang dimasukan salah");
                    }
                } catch (SQLException ex) {
                    view.viewErrorMsg("ERROR - gagal mendapatkan email nasabah...");
                    ex.printStackTrace();
                } catch (MessagingException ex) {
                    view.viewErrorMsg(ex.getMessage());
                }
            } else if (source.equals(la.getBtnNasabah())) {
                la.setVisible(false);
                setView(new LoginNasabah());
                setListener();
                view.setVisible(true);   
            }
        } else if (view instanceof ViewMenuAdmin) {
            vma = (ViewMenuAdmin) view;
            if (source.equals(vma.getBtnNasabah())) {
                vma.setVisible(false);
                setView(new ViewAddNasabah(model));
                setListener();
                view.setVisible(true);
            } else if (source.equals(vma.getBtnLihatMutasi())) {
                vma.setVisible(false);
                setView(new ViewMutasi(model));
                setListener();
                view.setVisible(true);
            } else if (source.equals(vma.getBtnKeluar())) {
                vma.setVisible(false);
                setView(new LoginAdmin());
                setListener();
                view.setVisible(true);
            }
        } else if (view instanceof ViewAddNasabah) {
            van = (ViewAddNasabah) view;
            if (source.equals(van.getBtnTambah())) {
                try {
                    if (van.validation()==false) return;
                    Nasabah nasabah = new Nasabah();
                    nasabah.setIdNasabah(van.getTxtNoRek());
                    nasabah.setNama(van.getTxtNamaNasabah());
                    nasabah.setAlamat(van.getTxtAlamat());
                    nasabah.setEmail(van.getTxtEmail());
                    nasabah.setNotelp(van.getTxtNoTelp());
                    nasabah.setSaldo(Double.valueOf(van.getTxtSaldo()));
                    nasabah.setPin(van.getTxtPassword());
                    nasabah.setId_mhs(van.getCboIdMhs());
                    
                    
                    //call method buat cek udah ada data apa belum
                    ResultSet rs = model.getNasabahById(nasabah.getIdNasabah());
                    if (rs.next()) {
                        if (rs.getInt(1)>0){
                            view.viewErrorMsg("Duplicate No Rekening");
                            van.clearAll();
                            return;
                        }
                    }
                    model.insertToNasabah(nasabah);
                    view.viewErrorMsg("Nasabah berhasil diinsert");
                    van.clearAll();
                } catch (SQLException ex) {
                    System.out.println("ERROR - Perjalanan gagal diinsert");
                    view.viewErrorMsg(ex.getMessage());
                }
            } else if (source.equals(van.getBtnUbah())) {
                try {
                    if (van.validation()==false) return;
                    Nasabah nasabah = new Nasabah();
                    nasabah.setIdNasabah(van.getTxtNoRek());
                    nasabah.setNama(van.getTxtNamaNasabah());
                    nasabah.setAlamat(van.getTxtAlamat());
                    nasabah.setEmail(van.getTxtEmail());
                    nasabah.setNotelp(van.getTxtNoTelp());
                    nasabah.setSaldo(Double.valueOf(van.getTxtSaldo()));
                    nasabah.setPin(van.getTxtPassword());
                    nasabah.setId_mhs(van.getCboIdMhs());
                    
                    model.updateNasabah(nasabah);
                    view.viewErrorMsg("Nasabah berhasil diupdate");
                    van.clearAll();
                } catch (Exception ex) {
                    System.out.println("ERROR - Perjalanan gagal diupdate");
                    view.viewErrorMsg(ex.getMessage());
                }
            } else if (source.equals(van.getBtnHapus())) {
//                try {
//                    if (JOptionPane.showConfirmDialog(vpj, "Yakin Hapus?","Info",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
//                        String tanggal = vpj.getCmbTahun() + "/" + vpj.getCmbBulan() + "/" + vpj.getCmbTanggal();
//                        Perjalanan perjalanan = new Perjalanan(vpj.getTxtIdPerjalanan(), vpj.getTxtNamaPerjalanan(), vpj.getCmbIdPaketWisata(), tanggal);
//                        ResultSet rs = model.getIdPerjalananInCustomerById(perjalanan.getIdPerjalanan());
//                        if (rs.next()) {
//                            if (rs.getInt(1)>0){
//                                view.viewErrorMsg("Data Perjalanan tidak dapat dihapus karena digunakan oleh Customer");
//                                return;
//                            }
//                        }                          
//                        model.deletePerjalanan(perjalanan);
//                        view.viewErrorMsg("Perjalanan berhasil dihapus");
//                        vpj.clearAll();
//                    }
//                } catch (Exception ex) {
//                    System.out.println("ERROR - Perjalanan gagal dihapus");
//                    view.viewErrorMsg(ex.getMessage());
//                }
            } else if (source.equals(van.getBtnBatal())) {
                van.clearAll();
            } else if (source.equals(van.getBtnKeluar())) {
                van.setVisible(false);
                setView(new ViewMenuAdmin(model));
                setListener();
                view.setVisible(true);
            }
        } else if (view instanceof ViewMutasi) {
            vm = (ViewMutasi) view;
//            if (source.equals(vm.getBtnCari())) {
//                String awal = vm.getCmbTahun() + "-" + vm.getCmbBulan() + "-" + vm.getCmbTanggal();
//                String akhir = vm.getCmbTahun1() + "-" + vm.getCmbBulan1() + "-" + vm.getCmbTanggal1();
//                
//                
//                
//            } else 
            if (source.equals(vm.getBtnKeluar())) {
                vm.setVisible(false);
                setView(new ViewMenuAdmin(model));
                setListener();
                view.setVisible(true);
            }
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        // ALL OF NASABAH
        } else if (view instanceof ViewNasabah) {
            vn = (ViewNasabah) view;
            if (source.equals(vn.getBtnPembayaran())) {
                vn.setVisible(false);
                setView(new ViewNasabahPembayaran(model));
                setListener();
                view.setVisible(true);        
            } else if (source.equals(vn.getBtnTransfer())) {
                vn.setVisible(false);
                setView(new ViewNasabahTransfer(model));
                setListener();
                view.setVisible(true);
            } else if (source.equals(vn.getBtnPengambilan())) {
                vn.setVisible(false);
                setView(new ViewNasabahPengambilan(model));
                setListener();
                view.setVisible(true);
            } else if (source.equals(vn.getBtnCekSaldo())) {
                vn.setVisible(false);
                setView(new ViewNasabahCekSaldo(model));
                setListener();
                view.setVisible(true);
            } else if (source.equals(vn.getBtnKeluar())) {
                vn.setVisible(false);
                setView(new LoginNasabah());
                setListener();
                view.setVisible(true);
            }
        } else if (view instanceof ViewNasabahCekSaldo) {
            vncs = (ViewNasabahCekSaldo) view;
            if (source.equals(vncs.getBtnKembali())) {
                vncs.setVisible(false);
                setView(new ViewNasabah(model));
                setListener();
                view.setVisible(true);
            } else if (source.equals(vncs.getBtnCetak())) {
                vncs.setVisible(false);
                setView(new ViewStrukSaldo(model));
                setListener();
                view.setVisible(true);                
            }
        } else if (view instanceof ViewNasabahTransfer) {
            vnt = (ViewNasabahTransfer) view;
            if (source.equals(vnt.getBtnCekRek())) {
                String noRekTujuan = vnt.getTxtNoRek();
                if (noRekTujuan.equals(model.getSessionIdNasabah())) {
                    view.viewErrorMsg("Tidak dapat mentransfer dana ke rekening sendiri");
                    vnt.setTxtNoRek("");
                    vnt.setLblNoRek("-");
                    vnt.setLblNamaRek("-");
                    vnt.setLblAlamat("-");
                    return;
                }

                try {
                    ResultSet rs = model.checkRekeningTujuan(noRekTujuan);
                    if (rs.next()) {
                        vnt.setLblNoRek(rs.getString(1));
                        vnt.setLblNamaRek(rs.getString(2));
                        vnt.setLblAlamat(rs.getString(3));
                    } else {
                        view.viewErrorMsg("No. Rekening tujuan tidak ditemukan");
                        vnt.setTxtNoRek("");
                        vnt.setLblNoRek("-");
                        vnt.setLblNamaRek("-");
                        vnt.setLblAlamat("-");
                    }
                } catch (SQLException ex) {
                    view.viewErrorMsg("ERROR - gagal mendapatkan data rekening tujuan...");
                    ex.printStackTrace();
                }

            } else if (source.equals(vnt.getBtnGenerateToken())) {
                try {
                    //view.viewErrorMsg(Token.generateActivationCode(8));
                    String token = Token.generateActivationCode(8);

                    ResultSet rs = model.getEmailByIdNasabah(model.getSessionIdNasabah());
                    if (rs.next()) {
                        String penerima = rs.getString(4);
                        SendEmail.sendingEmail(penerima, token);
                        model.updateToken(model.getSessionIdNasabah(), token);

                        view.viewErrorMsg("Token telah dikirim ke email anda");
                    }
                } catch (SQLException ex) {
                    view.viewErrorMsg("ERROR - gagal mendapatkan email nasabah...");
                    ex.printStackTrace();
                } catch (MessagingException ex) {
                    view.viewErrorMsg(ex.getMessage());
                }
            } else if (source.equals(vnt.getBtnKembali())) {
                vnt.setVisible(false);
                setView(new ViewNasabah(model));
                setListener();
                view.setVisible(true);
            } else if (source.equals(vnt.getBtnKirim())) {
                //validasi kosong
                if (vnt.getTxtNoRek().equals("")) {
                    view.viewErrorMsg("No. Rekening tujuan harus diisi !");
                    return;
                }
                if (vnt.getTxtJumlah().equals("")) {
                    view.viewErrorMsg("Jumlah dana harus diisi !");
                    return;
                }
                if (vnt.getTxtToken().equals("")) {
                    view.viewErrorMsg("Token harus diisi !");
                    return;
                }

                //validasi token
                try {
                    ResultSet rs = model.getTokenFromNasabah(model.getSessionIdNasabah());
                    if (rs.next()) {
                        if (!vnt.getTxtToken().equals(rs.getString(8))) {
                            view.viewErrorMsg("Token yang anda masukan salah...");
                            return;
                        }
                    }
                } catch (SQLException ex) {
                    view.viewErrorMsg("ERROR - gagal mendapatkan token nasabah...");
                    ex.printStackTrace();
                }

                Double saldoNasabah = 0.0;
                Double jumlah = 0.0;
                //validasi cek saldo pengirim > jumlah bayar
                try {
                    ResultSet rs = model.checkSaldoNasabah(model.getSessionIdNasabah());
                    if (rs.next()) {
                        saldoNasabah = Double.valueOf(rs.getString(6));
                        jumlah = Double.valueOf(vnt.getTxtJumlah());
                        if (saldoNasabah < jumlah) {
                            view.viewErrorMsg("Saldo anda tidak mencukupi");
                            return;
                        }
                    }
                } catch (SQLException ex) {
                    view.viewErrorMsg("ERROR - gagal mendapatkan saldo nasabah...");
                    ex.printStackTrace();
                }

                //mengurangi saldo pengirim
                model.debetSaldoPengirim(model.getSessionIdNasabah(), jumlah);
                //menambah saldo penerima
                model.kreditSaldoPenerima(vnt.getTxtNoRek(), jumlah);

                Transfer transfer = new Transfer();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                transfer.setTglTransaksi(sdf.format(new Date()));
                transfer.setIdNasabah(model.getSessionIdNasabah());
                transfer.setRekeningTujuan(vnt.getTxtNoRek());
                transfer.setJumlahTransfer(jumlah);
                transfer.setSaldoAkhir(saldoNasabah - jumlah);

                model.insertToTransfer(transfer);

                String idTransfer = "";
                try {
                    ResultSet rs = model.getIdTransferByNasabah(model.getSessionIdNasabah());
                    if (rs.last()) {
                        idTransfer = rs.getString(6);
                    }
                } catch (SQLException ex) {
                    view.viewErrorMsg("ERROR - gagal mendapatkan saldo penerima...");
                    ex.printStackTrace();
                }


                Mutasi mutasiPengirim = new Mutasi();
                mutasiPengirim.setTglMutasi(sdf.format(new Date()));
                mutasiPengirim.setKredit(0.0);
                mutasiPengirim.setDebet(jumlah);
                mutasiPengirim.setSaldo(saldoNasabah - jumlah);
                mutasiPengirim.setIdTransfer(idTransfer);
                mutasiPengirim.setIdPembayaran("0");
                mutasiPengirim.setIdPengambilan("0");


                Double saldoPenerima = 0.0;
                try {
                    ResultSet rs = model.checkRekeningTujuan(vnt.getTxtNoRek());
                    if (rs.next()) {
                        saldoPenerima = Double.valueOf(rs.getString(6));
                    }
                } catch (SQLException ex) {
                    view.viewErrorMsg("ERROR - gagal mendapatkan saldo penerima...");
                    ex.printStackTrace();
                }

                Mutasi mutasiPenerima = new Mutasi();
                mutasiPenerima.setTglMutasi(sdf.format(new Date()));
                mutasiPenerima.setKredit(jumlah);
                mutasiPenerima.setDebet(0.0);
                mutasiPenerima.setSaldo(saldoPenerima);
                mutasiPenerima.setIdTransfer(idTransfer);
                mutasiPenerima.setIdPembayaran("0");
                mutasiPenerima.setIdPengambilan("0");

                model.insertToMutasi(mutasiPengirim);
                model.insertToMutasi(mutasiPenerima);

                //update token jadi kosong
                model.deleteToken(model.getSessionIdNasabah());
                
                view.viewErrorMsg("Transfer berhasil, Klik Ok untuk melanjutkan");

                vnt.setVisible(false);
                setView(new ViewStrukTransfer(model));
                setListener();
                view.setVisible(true);
                
                //bersihkan isi field
//                vnt.setTxtNoRek("");
//                vnt.setLblNamaRek("-");
//                vnt.setLblNoRek("-");
//                vnt.setLblAlamat("-");
//                vnt.setTxtJumlah("");
//                vnt.setTxtToken("");

            }
        } else if (view instanceof ViewNasabahPengambilan) {
            vnp = (ViewNasabahPengambilan) view;
            if (source.equals(vnp.getBtnInputManual())) {
                vnp.setVisible(false);
                setView(new ViewNasabahPengambilanManual(model));
                setListener();
                view.setVisible(true);
            } else if (source.equals(vnp.getBtnPilihNominal())) {
                vnp.setVisible(false);
                setView(new ViewNasabahPengambilanPilihNominal(model));
                setListener();
                view.setVisible(true);
            } else if (source.equals(vnp.getBtnKembali())) {
                vnp.setVisible(false);
                setView(new ViewNasabah(model));
                setListener();
                view.setVisible(true);
            }
        } else if (view instanceof ViewNasabahPengambilanManual) {
            vnpm = (ViewNasabahPengambilanManual) view;
            if (source.equals(vnpm.getBtnOk())) {
                if (vnpm.viewConfirmMsg()) {
                    ambilUang(Double.valueOf(vnpm.getTxtInputNominal()));
                    
                    vnpm.setVisible(false);
                    setView(new ViewStrukPengambilan(model));
                    setListener();
                    view.setVisible(true);
                }
            } else if (source.equals(vnpm.getBtnBatal())) {
                vnpm.setTxtInputNominal("");
            } else if (source.equals(vnpm.getBtnKembali())) {
                vnpm.setVisible(false);
                setView(new ViewNasabahPengambilan(model));
                setListener();
                view.setVisible(true);
            }
        } else if (view instanceof ViewNasabahPengambilanPilihNominal) {
            vnppn = (ViewNasabahPengambilanPilihNominal) view;
            if (source.equals(vnppn.getBtn100())) {
                if (vnppn.viewConfirmMsg()) {
                    ambilUang(Double.valueOf("100000"));
                    vnppn.setVisible(false);
                    setView(new ViewStrukPengambilan(model));
                    setListener();
                    view.setVisible(true);
                }
            } else if (source.equals(vnppn.getBtn200())) {
                if (vnppn.viewConfirmMsg()) {
                    ambilUang(Double.valueOf("200000"));
                    vnppn.setVisible(false);
                    setView(new ViewStrukPengambilan(model));
                    setListener();
                    view.setVisible(true);
                }
            } else if (source.equals(vnppn.getBtn300())) {
                if (vnppn.viewConfirmMsg()) {
                    ambilUang(Double.valueOf("300000"));
                    vnppn.setVisible(false);
                    setView(new ViewStrukPengambilan(model));
                    setListener();
                    view.setVisible(true);
                }
            } else if (source.equals(vnppn.getBtn500())) {
                if (vnppn.viewConfirmMsg()) {
                    ambilUang(Double.valueOf("500000"));
                    vnppn.setVisible(false);
                    setView(new ViewStrukPengambilan(model));
                    setListener();
                    view.setVisible(true);
                }
            } else if (source.equals(vnppn.getBtn1000())) {
                if (vnppn.viewConfirmMsg()) {
                    ambilUang(Double.valueOf("1000000"));
                    vnppn.setVisible(false);
                    setView(new ViewStrukPengambilan(model));
                    setListener();
                    view.setVisible(true);
                }
            } else if (source.equals(vnppn.getBtn2000())) {
                if (vnppn.viewConfirmMsg()) {
                    ambilUang(Double.valueOf("2000000"));
                    vnppn.setVisible(false);
                    setView(new ViewStrukPengambilan(model));
                    setListener();
                    view.setVisible(true);
                }
            } else if (source.equals(vnppn.getBtnKembali())) {
                vnppn.setVisible(false);
                setView(new ViewNasabahPengambilan(model));
                setListener();
                view.setVisible(true);
            }
        } else if (view instanceof ViewNasabahPembayaran) {
            pembayaran = (ViewNasabahPembayaran) view;
            if (source.equals(pembayaran.getBtnSemesteran())) {
                pembayaran.setVisible(false);
                setView(new ViewNasabahBayarSemesteran(model));
                setListener();
                view.setVisible(true);
            } else if (source.equals(pembayaran.getBtnTestEnglish())) {
                pembayaran.setVisible(false);
                setView(new ViewNasabahBayarTestEnglish(model));
                setListener();
                view.setVisible(true);
            } else if (source.equals(pembayaran.getBtnWisuda())) {
                pembayaran.setVisible(false);
                setView(new ViewNasabahBayarWisuda(model));
                setListener();
                view.setVisible(true);
            } else if (source.equals(pembayaran.getBtnKembali())) {
                pembayaran.setVisible(false);
                setView(new ViewNasabah(model));
                setListener();
                view.setVisible(true);
            }
        } else if (view instanceof ViewNasabahBayarSemesteran) {
            vnbs = (ViewNasabahBayarSemesteran) view;
            if (source.equals(vnbs.getBtnGenerateToken())) {
                try {
                    String token = Token.generateActivationCode(8);

                    ResultSet rs = model.getEmailByIdNasabah(model.getSessionIdNasabah());
                    if (rs.next()) {
                        String penerima = rs.getString(4);
                        SendEmail.sendingEmail(penerima, token);
                        model.updateToken(model.getSessionIdNasabah(), token);

                        view.viewErrorMsg("Token telah dikirim ke email anda");
                    }
                } catch (SQLException ex) {
                    view.viewErrorMsg("ERROR - gagal mendapatkan email nasabah...");
                    ex.printStackTrace();
                } catch (MessagingException ex) {
                    view.viewErrorMsg(ex.getMessage());
                }                
            } else if (source.equals(vnbs.getBtnBayar())) {
                //validasi status sudah L atau B
                try {
                    ResultSet rs = model.getMhsById(model.getSessionIdNasabah());
                    if (rs.next()) {
                        if (rs.getString(7).equals("L")) {
                            view.viewErrorMsg("Uang Semester untuk semester ini telah Lunas");
                            return;
                        }
                    }
                } catch (SQLException ex) {
                    view.viewErrorMsg("ERROR - gagal mendapatkan status bayar...");
                    ex.printStackTrace();
                }
                
                if (vnbs.getTxtToken().equals("")) {
                    view.viewErrorMsg("Token harus diisi !");
                    return;
                }

                //validasi token
                try {
                    ResultSet rs = model.getTokenFromNasabah(model.getSessionIdNasabah());
                    if (rs.next()) {
                        if (!vnbs.getTxtToken().equals(rs.getString(8))) {
                            view.viewErrorMsg("Token yang anda masukan salah...");
                            return;
                        }
                    }
                } catch (SQLException ex) {
                    view.viewErrorMsg("ERROR - gagal mendapatkan token nasabah...");
                    ex.printStackTrace();
                }

                Double saldoNasabah = 0.0;
                Double jumlah = 0.0;
                //validasi cek saldo pengirim > jumlah bayar
                try {
                    ResultSet rs = model.checkSaldoNasabah(model.getSessionIdNasabah());
                    if (rs.next()) {
                        saldoNasabah = Double.valueOf(rs.getString(6));
                        jumlah = Double.valueOf(vnbs.getLblTotalBayar());
                        if (saldoNasabah < jumlah) {
                            view.viewErrorMsg("Saldo anda tidak mencukupi");
                            return;
                        }
                    }
                } catch (SQLException ex) {
                    view.viewErrorMsg("ERROR - gagal mendapatkan saldo nasabah...");
                    ex.printStackTrace();
                }
                
                //kurangi saldo nasabah
                model.debetSaldoPengirim(model.getSessionIdNasabah(), jumlah);
                //update status bayar jadi L
                model.updateStatusBayar(model.getSessionIdNasabah());
                
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                
                Pembayaran p = new Pembayaran();
                p.setTglPembayaran(sdf.format(new Date()));
                p.setIdNasabah(model.getSessionIdNasabah());
                p.setIdJenisPembayaran("1");
                p.setJumlahBayar(jumlah);
                p.setSaldoAkhir(saldoNasabah-jumlah);
                
                model.insertToPembayaran(p);
                
                String idPembayaran = "";
                try {
                    ResultSet rs = model.getIdPembayaranByNasabah(model.getSessionIdNasabah());
                    if (rs.last()) {
                        idPembayaran = rs.getString(6);
                    }
                } catch (SQLException ex) {
                    view.viewErrorMsg("ERROR - gagal mendapatkan id pembayar...");
                    ex.printStackTrace();
                }
                
                Mutasi mutasi = new Mutasi();
                mutasi.setTglMutasi(sdf.format(new Date()));
                mutasi.setKredit(0.0);
                mutasi.setDebet(jumlah);
                mutasi.setSaldo(saldoNasabah-jumlah);
                mutasi.setIdTransfer("0");
                mutasi.setIdPembayaran(idPembayaran);
                mutasi.setIdPengambilan("0");
                
                model.insertToMutasi(mutasi);
                
                view.viewErrorMsg("Pembayaran Berhasil, Klik Oke Untuk Melanjutkan");
                                
                vnbs.setVisible(false);
                setView(new ViewStruckPembayaran(model));
                setListener();
                view.setVisible(true);
            } else if (source.equals(vnbs.getBtnKembali())) {
                vnbs.setVisible(false);
                setView(new ViewNasabahPembayaran(model));
                setListener();
                view.setVisible(true);
            }
        } else if (view instanceof ViewNasabahBayarTestEnglish) {
            vnbte = (ViewNasabahBayarTestEnglish) view;
            if (source.equals(vnbte.getBtnGenerateToken())) {
                try {
                    String token = Token.generateActivationCode(8);

                    ResultSet rs = model.getEmailByIdNasabah(model.getSessionIdNasabah());
                    if (rs.next()) {
                        String penerima = rs.getString(4);
                        SendEmail.sendingEmail(penerima, token);
                        model.updateToken(model.getSessionIdNasabah(), token);

                        view.viewErrorMsg("Token telah dikirim ke email anda");
                    }
                } catch (SQLException ex) {
                    view.viewErrorMsg("ERROR - gagal mendapatkan email nasabah...");
                    ex.printStackTrace();
                } catch (MessagingException ex) {
                    view.viewErrorMsg(ex.getMessage());
                }                
            } else if (source.equals(vnbte.getBtnBayar())) {
                if (vnbte.getTxtToken().equals("")) {
                    view.viewErrorMsg("Token harus diisi !");
                    return;
                }

                if (vnbte.getLblTotalBayar().equals("0.0")) {
                    view.viewErrorMsg("Pilih pembayaran EPRT atau ECCT");
                    return;
                }
                //validasi token
                try {
                    ResultSet rs = model.getTokenFromNasabah(model.getSessionIdNasabah());
                    if (rs.next()) {
                        if (!vnbte.getTxtToken().equals(rs.getString(8))) {
                            view.viewErrorMsg("Token yang anda masukan salah...");
                            return;
                        }
                    }
                } catch (SQLException ex) {
                    view.viewErrorMsg("ERROR - gagal mendapatkan token nasabah...");
                    ex.printStackTrace();
                }

                Double saldoNasabah = 0.0;
                Double jumlah = 0.0;
                //validasi cek saldo pengirim > jumlah bayar
                try {
                    ResultSet rs = model.checkSaldoNasabah(model.getSessionIdNasabah());
                    if (rs.next()) {
                        saldoNasabah = Double.valueOf(rs.getString(6));
                        jumlah = Double.valueOf(vnbte.getLblTotalBayar());
                        if (saldoNasabah < jumlah) {
                            view.viewErrorMsg("Saldo anda tidak mencukupi");
                            return;
                        }
                    }
                } catch (SQLException ex) {
                    view.viewErrorMsg("ERROR - gagal mendapatkan saldo nasabah...");
                    ex.printStackTrace();
                }
                
                //kurangi saldo nasabah
                model.debetSaldoPengirim(model.getSessionIdNasabah(), jumlah);
                
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                
                Pembayaran p = new Pembayaran();
                p.setTglPembayaran(sdf.format(new Date()));
                p.setIdNasabah(model.getSessionIdNasabah());
                p.setIdJenisPembayaran("2"); //test english
                p.setJumlahBayar(jumlah);
                p.setSaldoAkhir(saldoNasabah-jumlah);
                
                model.insertToPembayaran(p);
                
                String idPembayaran = "";
                try {
                    ResultSet rs = model.getIdPembayaranByNasabah(model.getSessionIdNasabah());
                    if (rs.last()) {
                        idPembayaran = rs.getString(6);
                    }
                } catch (SQLException ex) {
                    view.viewErrorMsg("ERROR - gagal mendapatkan id pembayar...");
                    ex.printStackTrace();
                }
                
                Mutasi mutasi = new Mutasi();
                mutasi.setTglMutasi(sdf.format(new Date()));
                mutasi.setKredit(0.0);
                mutasi.setDebet(jumlah);
                mutasi.setSaldo(saldoNasabah-jumlah);
                mutasi.setIdTransfer("0");
                mutasi.setIdPembayaran(idPembayaran);
                mutasi.setIdPengambilan("0");
                
                model.insertToMutasi(mutasi);
                
                view.viewErrorMsg("Pembayaran Berhasil, Klik Oke Untuk Melanjutkan");
                                
                vnbte.setVisible(false);
                setView(new ViewStruckPembayaran(model));
                setListener();
                view.setVisible(true);
            } else if (source.equals(vnbte.getBtnKembali())) {
                vnbte.setVisible(false);
                setView(new ViewNasabahPembayaran(model));
                setListener();
                view.setVisible(true);
            } else if (source.equals(vnbte.getCboEPRT())) {
                if (vnbte.getCboEPRT().isSelected() == true) {
                    Double total = new Double(vnbte.getLblTotalBayar()) + new Double(vnbte.getLblEPRT());
//                    vnbte.setLblTotalBayar(NumberFormat.getNumberInstance().format(total));
                    vnbte.setLblTotalBayar(total.toString());
                } else {
                    Double total = new Double(vnbte.getLblTotalBayar()) - new Double(vnbte.getLblEPRT());
//                    vnbte.setLblTotalBayar(NumberFormat.getNumberInstance().format(total));
                    vnbte.setLblTotalBayar(total.toString());
                }
            } else if (source.equals(vnbte.getCboEcct())) {
                if (vnbte.getCboEcct().isSelected() == true) {
                    Double total = new Double(vnbte.getLblTotalBayar()) + new Double(vnbte.getLblECCT());
//                    vnbte.setLblTotalBayar(NumberFormat.getNumberInstance().format(total));
                    vnbte.setLblTotalBayar(total.toString());
                } else {
                    Double total = new Double(vnbte.getLblTotalBayar()) - new Double(vnbte.getLblECCT());
//                    vnbte.setLblTotalBayar(NumberFormat.getNumberInstance().format(total));
                    vnbte.setLblTotalBayar(total.toString());
                }
            }
            // end of pembayaran test english
        } else if (view instanceof ViewNasabahBayarWisuda) {
            vnbw = (ViewNasabahBayarWisuda) view;
            if (source.equals(vnbw.getBtnGenerateToken())) {
                try {
                    String token = Token.generateActivationCode(8);

                    ResultSet rs = model.getEmailByIdNasabah(model.getSessionIdNasabah());
                    if (rs.next()) {
                        String penerima = rs.getString(4);
                        SendEmail.sendingEmail(penerima, token);
                        model.updateToken(model.getSessionIdNasabah(), token);

                        view.viewErrorMsg("Token telah dikirim ke email anda");
                    }
                } catch (SQLException ex) {
                    view.viewErrorMsg("ERROR - gagal mendapatkan email nasabah...");
                    ex.printStackTrace();
                } catch (MessagingException ex) {
                    view.viewErrorMsg(ex.getMessage());
                }                
            } else if (source.equals(vnbw.getBtnBayar())) {
                if (vnbw.getTxtToken().equals("")) {
                    view.viewErrorMsg("Token harus diisi !");
                    return;
                }

                //validasi token
                try {
                    ResultSet rs = model.getTokenFromNasabah(model.getSessionIdNasabah());
                    if (rs.next()) {
                        if (!vnbw.getTxtToken().equals(rs.getString(8))) {
                            view.viewErrorMsg("Token yang anda masukan salah...");
                            return;
                        }
                    }
                } catch (SQLException ex) {
                    view.viewErrorMsg("ERROR - gagal mendapatkan token nasabah...");
                    ex.printStackTrace();
                }

                Double saldoNasabah = 0.0;
                Double jumlah = 0.0;
                //validasi cek saldo pengirim > jumlah bayar
                try {
                    ResultSet rs = model.checkSaldoNasabah(model.getSessionIdNasabah());
                    if (rs.next()) {
                        saldoNasabah = Double.valueOf(rs.getString(6));
                        jumlah = Double.valueOf(vnbw.getLblTotalBayar());
                        if (saldoNasabah < jumlah) {
                            view.viewErrorMsg("Saldo anda tidak mencukupi");
                            return;
                        }
                    }
                } catch (SQLException ex) {
                    view.viewErrorMsg("ERROR - gagal mendapatkan saldo nasabah...");
                    ex.printStackTrace();
                }
                
                //kurangi saldo nasabah
                model.debetSaldoPengirim(model.getSessionIdNasabah(), jumlah);
                
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                
                Pembayaran p = new Pembayaran();
                p.setTglPembayaran(sdf.format(new Date()));
                p.setIdNasabah(model.getSessionIdNasabah());
                p.setIdJenisPembayaran("3"); //wisuda
                p.setJumlahBayar(jumlah);
                p.setSaldoAkhir(saldoNasabah-jumlah);
                
                model.insertToPembayaran(p);
                
                String idPembayaran = "";
                try {
                    ResultSet rs = model.getIdPembayaranByNasabah(model.getSessionIdNasabah());
                    if (rs.last()) {
                        idPembayaran = rs.getString(6);
                    }
                } catch (SQLException ex) {
                    view.viewErrorMsg("ERROR - gagal mendapatkan id pembayar...");
                    ex.printStackTrace();
                }
                
                Mutasi mutasi = new Mutasi();
                mutasi.setTglMutasi(sdf.format(new Date()));
                mutasi.setKredit(0.0);
                mutasi.setDebet(jumlah);
                mutasi.setSaldo(saldoNasabah-jumlah);
                mutasi.setIdTransfer("0");
                mutasi.setIdPembayaran(idPembayaran);
                mutasi.setIdPengambilan("0");
                
                model.insertToMutasi(mutasi);
                
                view.viewErrorMsg("Pembayaran Berhasil, Klik Oke Untuk Melanjutkan");
                                
                vnbw.setVisible(false);
                setView(new ViewStruckPembayaran(model));
                setListener();
                view.setVisible(true);
            } else if (source.equals(vnbw.getBtnKembali())) {
                vnbw.setVisible(false);
                setView(new ViewNasabahPembayaran(model));
                setListener();
                view.setVisible(true);
            }
            //end of pembayaran wisuda
        } else if (view instanceof ViewStrukSaldo) {
             vss = (ViewStrukSaldo) view;
             if (source.equals(vss.getBtnOk())) {
                vss.setVisible(false);
                setView(new ViewNasabah(model));
                setListener();
                view.setVisible(true);
            }
        } else if (view instanceof ViewStrukTransfer) {
             vst = (ViewStrukTransfer) view;
             if (source.equals(vst.getBtnOk())) {
                vst.setVisible(false);
                setView(new ViewNasabah(model));
                setListener();
                view.setVisible(true);
            }
        } else if (view instanceof ViewStrukPengambilan) {
             vsp = (ViewStrukPengambilan) view;
             if (source.equals(vsp.getBtnOk())) {
                vsp.setVisible(false);
                setView(new ViewNasabah(model));
                setListener();
                view.setVisible(true);
            }
        } else if (view instanceof ViewStruckPembayaran) {
             vsps = (ViewStruckPembayaran) view;
             if (source.equals(vsps.getBtnOk())) {
                vsps.setVisible(false);
                setView(new ViewNasabah(model));
                setListener();
                view.setVisible(true);
            }
        }
    }

    private void ambilUang(Double jumlah) {
        Double saldoNasabah = 0.0;
        //validasi cek saldo pengirim > jumlah bayar
        try {
            ResultSet rs = model.checkSaldoNasabah(model.getSessionIdNasabah());
            if (rs.next()) {
                saldoNasabah = Double.valueOf(rs.getString(6));
                if (saldoNasabah < jumlah) {
                    view.viewErrorMsg("Saldo anda tidak mencukupi");
                    return;
                }
            }
        } catch (SQLException ex) {
            view.viewErrorMsg("ERROR - gagal mendapatkan saldo nasabah...");
            ex.printStackTrace();
        }
        
        model.pengambilanUang(jumlah, model.getSessionIdNasabah());
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                
        Pengambilan pengambilan = new Pengambilan();
        pengambilan.setTglTransaksi(sdf.format(new Date()));
        pengambilan.setJumlahAmbil(jumlah);
        pengambilan.setSaldoAkhir(saldoNasabah - jumlah);
        pengambilan.setIdNasabah(model.getSessionIdNasabah());
        
        model.insertToPengambilan(pengambilan);
        
        String idPengambilan = "";
        try {
            ResultSet rs = model.getIdPengambilanByNasabah(model.getSessionIdNasabah());
            if (rs.last()) {
                idPengambilan = rs.getString(5);
            }
        } catch (SQLException ex) {
            view.viewErrorMsg("ERROR - gagal mendapatkan saldo penerima...");
            ex.printStackTrace();
        }
        
        
        Mutasi mutasi = new Mutasi();
        mutasi.setTglMutasi(sdf.format(new Date()));
        mutasi.setKredit(0.0);
        mutasi.setDebet(jumlah);
        mutasi.setSaldo(saldoNasabah - jumlah);
        mutasi.setIdTransfer("0");
        mutasi.setIdPembayaran("0");
        mutasi.setIdPengambilan(idPengambilan);
        
        model.insertToMutasi(mutasi);
     
        view.viewErrorMsg("Pengambilan Berhasil, Klik Ok untuk Melanjutkan");
    }
}
