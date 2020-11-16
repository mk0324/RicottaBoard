/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hungrybird.back.AuthApp.event.listener;



import com.hungrybird.back.AuthApp.event.OnInvitationEvent;
import com.hungrybird.back.AuthApp.exception.MailSendException;
import com.hungrybird.back.AuthApp.service.MailService;
import freemarker.template.TemplateException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.IOException;

@Component
public class OnInvitationListener implements ApplicationListener<OnInvitationEvent> {

    private static final Logger logger = Logger.getLogger(OnInvitationListener.class);
    private final MailService mailService;

    @Autowired
    public OnInvitationListener(MailService mailService) {
        this.mailService = mailService;
    }


    @Override
    @Async
    public void onApplicationEvent( OnInvitationEvent onInvitationEvent) {
        resendEmailVerification(onInvitationEvent);
    }


    private void resendEmailVerification(OnInvitationEvent event) {
        String channelId = event.getMailSendRequest().getChannelId();
        String recipientAddress = channelId;
        String channelName = event.getChannelName();
        String from = event.getFrom();

        String inviteConfirmUrl = event.getRedirectUrl().toUriString();
        try {
            mailService.sendInviteEmail(inviteConfirmUrl, recipientAddress,channelName, from);
        } catch (IOException | TemplateException | MessagingException e) {
            logger.error(e);
            throw new MailSendException(recipientAddress, "invite circle");
        }
    }

}
