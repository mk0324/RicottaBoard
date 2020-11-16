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

import com.hungrybird.back.AuthApp.validation.annotation.NullOrNotBlank;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ApiModel(value = "Registration Request", description = "The registration request payload")
public class RegistrationRequest {

    @NullOrNotBlank(message = "Registration username can be null but not blank")
    @ApiModelProperty(value = "A valid username", allowableValues = "NonEmpty String")
    private String username;

    @NullOrNotBlank(message = "Registration nickname can be null but not blank")
    @ApiModelProperty(value = "A valid nickname", allowableValues = "NonEmpty String")
    private String nickname;

    @NullOrNotBlank(message = "Registration email can be null but not blank")
    @ApiModelProperty(value = "A valid email", required = true, allowableValues = "NonEmpty String")
    private String email;

    @NotNull(message = "Registration password cannot be null")
    @ApiModelProperty(value = "A valid password string", required = true, allowableValues = "NonEmpty String")
    private String password;

    @NotNull(message = "Specify whether the user has to be registered as an admin or not")
    @ApiModelProperty(value = "Flag denoting whether the user is an admin or not", required = true,
            dataType = "boolean", allowableValues = "true, false")
    private Boolean registerAsAdmin;

}
