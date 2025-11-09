package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

@Service
public class SalesforceService {

    // ✅ Inject the auth service instead of calling it statically
    @Autowired
    private SalesforceAuthService authService;

    public void createCaseInSalesforce(String description, String caseNumber) {
        try {
            // ✅ Use the injected instance here
            JSONObject tokenResponse = authService.getAccessToken();
            if (tokenResponse == null || !tokenResponse.has("access_token")) {
                System.err.println("❌ Failed to get Salesforce token");
                return;
            }

            String accessToken = tokenResponse.getString("access_token");
            String instanceUrl = tokenResponse.getString("instance_url");

            String url = instanceUrl + "/services/data/v60.0/sobjects/ServiceNowCase__c";

            JSONObject body = new JSONObject();
            body.put("Description__c", description);
            body.put("CaseNumber__c", caseNumber);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);

            HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            System.out.println("✅ Salesforce response: " + response.getBody());
        } catch (Exception e) {
            System.err.println("❌ Error sending to Salesforce:");
            e.printStackTrace();
        }
    }
}
