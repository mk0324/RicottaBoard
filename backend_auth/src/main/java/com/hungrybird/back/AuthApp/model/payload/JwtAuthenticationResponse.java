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
package com.hungrybird.back.AuthApp.model.payload;

import com.hungrybird.back.AuthApp.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtAuthenticationResponse {

    private String accessToken;

    private String refreshToken;

    private String tokenType;

    private Long expiryDuration;

    private User user;

    public JwtAuthenticationResponse(String accessToken, String refreshToken, Long expiryDuration) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiryDuration = expiryDuration;
        tokenType = "Bearer ";
    }

    public JwtAuthenticationResponse(String accessToken, String refreshToken, Long expiryDuration,User user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiryDuration = expiryDuration;
        tokenType = "Bearer ";
        this.user = user;
    }

}
