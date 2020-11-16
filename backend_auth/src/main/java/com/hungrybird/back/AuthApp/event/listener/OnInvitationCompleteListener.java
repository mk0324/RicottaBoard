package com.hungrybird.back.AuthApp.event.listener;

import com.hungrybird.back.AuthApp.event.OnInvitationCompleteEvent;
import com.hungrybird.back.AuthApp.exception.MailSendException;
import com.hungrybird.back.AuthApp.model.User;
import com.hungrybird.back.AuthApp.service.MailService;
import freemarker.template.TemplateException;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class OnInvitationCompleteListener implements ApplicationListener<OnInvitationCompleteEvent> {

    private static final Logger logger = Logger.getLogger(OnUserRegistrationCompleteListener.class);
    private final MailService mailService;

    @Autowired
    public OnInvitationCompleteListener(MailService mailService) {
        this.mailService = mailService;
    }


    @SneakyThrows
    @Override
    @Async
    public void onApplicationEvent(OnInvitationCompleteEvent onInvitationCompleteEvent) {
        sendEmailVerification(onInvitationCompleteEvent);
    }

    private void sendEmailVerification(OnInvitationCompleteEvent event) throws TemplateException, IOException, MessagingException {
        List<User> user = new ArrayList<>();
        for (int i=0;i<event.getMember().size();i++){
            User check = event.getMember().get(i).getUser();
            if(check == null) {
                System.out.println("====== WARNING : There is no user =====");
                mailService.sendRegistrationMail(event.getMember().get(i).getEmail(), event.getChannelName(), event.getFrom());
            } else user.add(check);
        }


        List<String> recipientAddress = new ArrayList<>();
        for (User u:user) {
            recipientAddress.add(u.getEmail());
        }
        List<String> email = event.getEmail();
        String channelId = event.getChannelId();

        for (int i=0;i<recipientAddress.size();i++) {
            System.out.println(event.getEmail().get(i)+" parameter");
        String emailConfirmationUrl = event.getRedirectUrl().
                        queryParam("email", email.get(i)).
                        queryParam("channelId",channelId).toUriString();
        System.out.println("--------EVENTLISTNER CALLED------------");

        try {
                mailService.sendInviteEmail(emailConfirmationUrl, recipientAddress.get(i), event.getChannelName(), event.getFrom());

            } catch (IOException | TemplateException | MessagingException e) {
                logger.error(e);
                throw new MailSendException(recipientAddress.get(i), "Email Verification");
            }
        }

    }
}
