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
package com.hungrybird.back.AuthApp.event;


import com.hungrybird.back.AuthApp.model.payload.MailSendRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import org.springframework.web.util.UriComponentsBuilder;

@Getter
@Setter
public class OnInvitationEvent extends ApplicationEvent {

    private transient UriComponentsBuilder redirectUrl;
    private MailSendRequest mailSendRequest;
    private String channelName;
    private String from;

    public OnInvitationEvent(MailSendRequest mailSendRequest, UriComponentsBuilder redirectUrl,
                             String channelName, String from) {
        super(mailSendRequest);
        this.mailSendRequest = mailSendRequest;
        this.redirectUrl = redirectUrl;
        this.channelName = channelName;
        this.from = from;
    }


}
