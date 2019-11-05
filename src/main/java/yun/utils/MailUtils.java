package yun.utils;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import ninja.postoffice.Mail;
import ninja.postoffice.Postoffice;
import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;

import javax.mail.internet.AddressException;
import java.util.List;
import java.util.Objects;

@Singleton
public class MailUtils {

    @Inject
    Provider<Mail> mailProvider;

    @Inject
    Postoffice postoffice;

    @Inject
    Logger logger;

    public void send(String addressee, String subject, String body) {
        Objects.requireNonNull(addressee);
        send(Lists.newArrayList(addressee), subject, body);
    }

    public void send(List<String> addresses, String subject, String body) {
        Objects.requireNonNull(addresses);
        if (addresses.isEmpty())
            return;
        Mail mail = mailProvider.get();
        mail.setSubject(subject);
        mail.setFrom("turingunion@163.com");

        mail.addTo(addresses.stream().toArray(String[]::new));
        mail.setCharset("utf-8");
        mail.setBodyHtml(body);
        try {
            postoffice.send(mail);
        } catch (AddressException e) {
            logger.error(e.getMessage());
        } catch (EmailException e){
            logger.error(e.getMessage());
        } catch (Exception e){
            logger.error(e.getMessage());
        }
    }
}
