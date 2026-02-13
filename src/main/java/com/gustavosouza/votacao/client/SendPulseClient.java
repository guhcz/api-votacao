package com.gustavosouza.votacao.client;

import com.gustavosouza.votacao.client.dto.AccessTokenRequest;
import com.gustavosouza.votacao.client.dto.AccessTokenResponse;
import com.gustavosouza.votacao.client.dto.EmailResponse;
import com.gustavosouza.votacao.client.dto.RootDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "send-pulse", url = "${send.pulse-url}")
public interface SendPulseClient {


    @PostMapping("oauth/access_token")
    AccessTokenResponse authorization (@RequestBody AccessTokenRequest accessTokenRequest);

    @PostMapping("smtp/emails")
    EmailResponse sendEmail(@RequestBody RootDto rootDto, @RequestHeader("Authorization") String token);

}
