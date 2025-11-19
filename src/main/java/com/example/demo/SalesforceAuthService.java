package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

@Service
public class SalesforceAuthService {

    @Value("${salesforce.login.url}")
    private String loginUrl;

    @Value("${salesforce.client.id}")
    private String clientId;

    @Value("${salesforce.client.secret}")
    private String clientSecret;

    @Value("${salesforce.username}")
    private String username;

    @Value("${salesforce.password}")
    private String password;

    public JSONObject getAccessToken() {
        try {
            RestTemplate restTemplate = new RestTemplate();

            String body = "grant_type=password"
                    + "&client_id=" + clientId
                    + "&client_secret=" + clientSecret
                    + "&username=" + username
                    + "&password=" + password;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<String> entity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(loginUrl, entity, String.class);

            JSONObject json = new JSONObject(response.getBody());
            System.out.println("🔑 Got new Salesforce token!");
            return json;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
