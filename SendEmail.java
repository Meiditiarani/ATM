package model;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class SendEmail {

    public static void sendingEmail(String penerima, String token) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtps.host", "smtp.gmail.com");
        properties.put("mail.smtps.auth", "true");
        properties.put("mail.smtps.ssl.enable", "true");
        properties.put("mail.smtps.port", "465");//default port dari smptp

        Session session = Session.getInstance(properties);
        session.setDebug(true);

        MimeMessage pesan = new MimeMessage(session);
        pesan.setFrom("gps0394@gmail.com");//isi dengan gmail kalian sendiri, biasanya sama nanti dengan username
        pesan.setRecipient(Message.RecipientType.TO, new InternetAddress(penerima));//isi dengan tujuan email
        pesan.setSubject("Token ATM For Smart School");
//        pesan.setText("Token Anda : " + token + "");
        pesan.setContent("Token Anda : <b>" + token + "</b>", "text/html");

        String username = "";//isi dengan gmail kalian sendiri
        String password = "";//isi dengan password sendiri

        Transport transport = session.getTransport("smtps");
        transport.connect(username, password);
        transport.sendMessage(pesan, pesan.getAllRecipients());
        transport.close();
    }
}
