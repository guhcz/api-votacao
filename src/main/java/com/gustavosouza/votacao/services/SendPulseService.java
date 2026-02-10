package com.gustavosouza.votacao.services;

import com.gustavosouza.votacao.client.SendPulseClient;
import com.gustavosouza.votacao.client.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendPulseService {

    @Value("@{send.pulse-client-id}")
    private String clientId;

    @Value("@{send.pulse-client-secret}")
    private String clientSecret;

    private final SendPulseClient sendPulseClient;


    public AccessTokenResponse authorization(){
        AccessTokenRequest accessTokenRequest =
                new AccessTokenRequest("client_credentials", clientId, clientSecret);
        return sendPulseClient.authorization(accessTokenRequest);
    }

    public EmailResponse toEmail(RootDto emailRequestDto){
        AccessTokenResponse accessTokenResponse = authorization();
        return sendPulseClient.sendEmail(emailRequestDto, accessTokenResponse.token_type() + ' ' + accessTokenResponse.access_token());
    }



}
