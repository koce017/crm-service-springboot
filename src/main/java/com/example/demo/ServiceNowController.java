package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.json.JSONObject;

@RestController
@RequestMapping("/servicenow")
public class ServiceNowController {

    // ✅ Ask Spring to inject SalesforceService automatically
    @Autowired
    private SalesforceService salesforceService;

    @PostMapping("/case")
    public String receiveCase(@RequestBody String payload) {
        try {
            System.out.println("Received ServiceNow payload: " + payload);

            JSONObject json = new JSONObject(payload);
            String description = json.optString("description", "No description provided");
            String caseNumber = json.optString("case_number", "SN-" + System.currentTimeMillis());

            System.out.println("Final description being sent: " + description);
            System.out.println("Final case number being sent: " + caseNumber);

            // ✅ Call the injected SalesforceService
            salesforceService.createCaseInSalesforce(description, caseNumber);

            return "Case received and sent to Salesforce successfully.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error processing case.";
        }
    }
}
