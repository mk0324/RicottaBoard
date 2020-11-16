package com.hungrybird.back.AuthApp.model.payload;

import com.hungrybird.back.AuthApp.validation.annotation.NullOrNotBlank;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ApiModel(value = "invitation Request", description = "invitation request payload")
public class MailSendRequest {

    @NullOrNotBlank(message = "Registration channelId can be null but not blank")
    @ApiModelProperty(value = "A valid channel", allowableValues = "NonEmpty String")
    private String channelId;

    @ApiModelProperty(value = "A valid email", required = true, allowableValues = "NonEmpty String")
    private List<String> email;


    @NullOrNotBlank(message = "Registration channelName can be null but not blank")
    @ApiModelProperty(value = "A valid channelName", allowableValues = "NonEmpty String")
    private String channelName;


    @NullOrNotBlank(message = "Registration FromWho can be null but not blank")
    @ApiModelProperty(value = "A valid user from", allowableValues = "NonEmpty String")
    private String from;
}
