package com.novomind.ecom.app.iagent.custom.crm.training.common.plugin;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import com.novomind.ecom.api.iagent.model.App;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import com.novomind.ecom.api.iagent.exception.PersistencyException;
import com.novomind.ecom.api.iagent.exception.WrongTypeException;
import com.novomind.ecom.api.iagent.frontend.IssueViewContext;
import com.novomind.ecom.api.iagent.persistence.storage.Storage;
import com.novomind.ecom.app.iagent.custom.crm.training.shared.CrmTrainingConstants;
import com.novomind.ecom.common.api.frontend.CustomBean;
import com.novomind.ecom.common.api.frontend.CustomManagedBean;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@CustomManagedBean("CrmTrainingTabBean")
public class CrmTrainingTabBean implements CustomBean {

    private String CRM_URL;
    private String CRM_AUTH;
    private String BASE_CONTACT_URL;
    private String DEFAULT_CONTACT_URL;
    private String CRM_CONTACT_CREATE_URL;
    private String CRM_PHONE_CREATE_URL;
    private String EDIT_CONTACT_URL;
    
    private String contactId;
    private String contactPhone;
    private String crmLink;
    private String sourceId;
    private String callId;

    private String logIssueId;
    private String logUsername;
    
    @Inject
    private Logger log;

    @Inject
    private IssueViewContext context;

    @Inject
    private App app;

    @PostConstruct
    public void init() {
        loadConfiguration();
        loadLoggingData();
        crmLink = DEFAULT_CONTACT_URL; // Set default CRM link
        loadSourceIdAndCallId();
        if (sourceId != null && !sourceId.isEmpty()) {
            contactPhone = sourceId;
            saveContactId();
        }
    }

    private void loadConfiguration() {
        Storage config = null;
        try {
            config = app.getConfig();
        } catch (PersistencyException e) {
            log.warn("Failure getting config: ", e);
        }

        if (config != null) {
            try {
                String site = config.getString(CrmTrainingConstants.KEY_SITE);
                String userKey = config.getString(CrmTrainingConstants.KEY_USER_KEY);

                CRM_URL = "https://" + site + "/civicrm/ajax/api4/Phone/get";
                CRM_AUTH = "Bearer " + userKey;
                BASE_CONTACT_URL = "https://" + site + "/wp-admin/admin.php?page=CiviCRM&q=civicrm%2Fcontact%2Fview&reset=1&cid=";
                DEFAULT_CONTACT_URL = "https://" + site + "/wp-admin/admin.php?page=CiviCRM&q=civicrm%2Fcontact%2Fadd&ct=Individual&reset=1";
                CRM_CONTACT_CREATE_URL = "https://" + site + "/civicrm/ajax/api4/Contact/create";
                CRM_PHONE_CREATE_URL = "https://" + site + "/civicrm/ajax/api4/Phone/create";
                EDIT_CONTACT_URL = "https://" + site + "/wp-admin/admin.php?page=CiviCRM&q=civicrm%2Fcontact%2Fadd&reset=1&action=update&cid=";
            } catch (WrongTypeException e) {
                log.warn("Wrong type: ", e);
            }
        }
    }

    private void loadLoggingData() {
        if (context == null) {
            logIssueId = "null";
            logUsername = "null";
        } else {
            logIssueId = context.getIssue() != null ? String.valueOf(context.getIssue().getId()) : "null";
            logUsername = context.getUser() != null ? context.getUser().getUsername() : "null";
        }
    }

    private void loadSourceIdAndCallId() {
        try {
            if (context != null && context.getIssue() != null && context.getIssue().getStorage() != null) {
                long startTime = System.currentTimeMillis();
                sourceId = context.getIssue().getStorage().getString("sourceId");
                callId = context.getIssue().getStorage().getString("callId");
                if (sourceId != null && !sourceId.isEmpty()) {
                    sourceId = cleanSourceId(sourceId);
                }
                log.info("[{}|{}] Source ID and Call ID loaded in {} ms.", logIssueId, logUsername, (System.currentTimeMillis() - startTime));
            } else {
                log.warn("[{}|{}] Source ID and Call ID could not be loaded. Reason: context, issue or storage = null", logIssueId, logUsername);
            }
        } catch (PersistencyException | WrongTypeException e) {
            log.error("[{}|{}] Error occurred while loading the source ID and call ID!", logIssueId, logUsername, e);
        }
    }

    private String cleanSourceId(String sourceId) {
        // Extract the phone number from the Optional format
        if (sourceId.startsWith("Optional[")) {
            sourceId = sourceId.substring(9, sourceId.length() - 1); // Remove "Optional[" and the ending "]"
        }
        // Remove the country code if present
        if (sourceId.startsWith("+65")) {
            sourceId = sourceId.substring(3);
        }
        return sourceId;
    }

    @SuppressWarnings("unused") // used by UI
    public void searchSourceId() {
        loadSourceIdAndCallId();
        saveWhateverId();
    }

    @SuppressWarnings("unused") // used by UI
    public void removeSourceId() {
        try {
            if (context != null && context.getIssue() != null && context.getIssue().getStorage() != null) {
                long startTime = System.currentTimeMillis();
                Storage issueStorage = context.getIssue().getStorage();
                issueStorage.remove("sourceId");
                issueStorage.remove("callId");
                issueStorage.store();
                log.info("[{}|{}] Source ID and Call ID removed in {} ms.", logIssueId, logUsername, (System.currentTimeMillis() - startTime));
                loadSourceIdAndCallId();
            } else {
                log.warn("[{}|{}] Source ID and Call ID could not be removed. Reason: context, issue or storage = null", logIssueId, logUsername);
            }
        } catch (PersistencyException e) {
            log.error("[{}|{}] Error occurred while removing the source ID and call ID!", logIssueId, logUsername, e);
        }
    }

    @SuppressWarnings("unused") // used by UI
    public String getSourceId() {
        return sourceId;
    }

    @SuppressWarnings("unused") // used by UI
    public String getCallId() {
        return callId;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getCrmLink() {
        return crmLink;
    }

    public void setCrmLink(String crmLink) {
        this.crmLink = crmLink;
    }

    public void saveContactId() {
        if (contactPhone != null && !contactPhone.isEmpty()) {
            String jsonResponse = fetchDataFromAPI(contactPhone);
            if (jsonResponse != null) {
                int contactId = searchPhoneNumber(jsonResponse, contactPhone); // Updated parameter
                if (contactId == -1) {
                    contactId = createContact(); // Pass CRM_CONTACT_CREATE_URL and CRM_AUTH as parameters
                    if (contactId > 0) {
                        addPhoneToContact(contactId, "65" + contactPhone); // Add prefix 65
                        // Set the crmLink to the edit contact URL after creating the contact
                        crmLink = EDIT_CONTACT_URL + contactId;
                        System.out.println("New Contact Edit URL: " + crmLink);
                    }
                }
            }
        }
    }

    public void saveWhateverId() {
        if (contactPhone != null && !contactPhone.isEmpty()) {
            String jsonResponse = fetchDataFromAPI(contactPhone);
            if (jsonResponse != null) {
                findPhoneNumber(jsonResponse, contactPhone);
            }
        }
    }

    private String fetchDataFromAPI(String phoneNumber) {
        try {
            URL url = new URL(CRM_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("X-Civi-Auth", CRM_AUTH);
            con.setDoOutput(true);

            String params = "{\"where\":[[\"phone\",\"CONTAINS\",\"" + phoneNumber + "\"]],\"limit\":5000}";
            String urlParameters = "params=" + java.net.URLEncoder.encode(params, "UTF-8");

            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(urlParameters);
            out.flush();
            out.close();

            int responseCode = con.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } catch (Exception e) {
            log.error("Error fetching data from API", e);
            return null;
        }
    }

    private int searchPhoneNumber(String jsonResponse, String phoneNumber) { // Change return type to int
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray valuesArray = jsonObject.getJSONArray("values");

            for (int i = 0; i < valuesArray.length(); i++) {
                JSONObject phoneObject = valuesArray.getJSONObject(i);
                if (phoneObject.getString("phone").contains(phoneNumber)) {
                    int contactId = phoneObject.getInt("contact_id");
                    setContactId(String.valueOf(contactId));
                    setCrmLink(BASE_CONTACT_URL + contactId);
                    return contactId; // Return the contactId when found
                }
            }
            // If no matches are found, set the CRM link to the default contact URL
            setCrmLink(DEFAULT_CONTACT_URL);
        } catch (JSONException e) {
            log.error("Error parsing JSON response", e);
            // Set the CRM link to the default contact URL in case of an exception
            setCrmLink(DEFAULT_CONTACT_URL);
        }
        return -1; // Return -1 if not found
    }
    
    private int createContact() {
        try {
            URL url = new URL(CRM_CONTACT_CREATE_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("X-Civi-Auth", CRM_AUTH);

            // Prepare contact creation payload with contact sub-type as "Client"
            String postData = "params=" + URLEncoder.encode(
                    "{\"values\":{\"contact_type\":\"Individual\",\"contact_sub_type\":[\"Client\"]}}",
                    StandardCharsets.UTF_8.toString());

            try (DataOutputStream out = new DataOutputStream(con.getOutputStream())) {
                out.writeBytes(postData);
                out.flush();
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
            }

            JSONObject responseJson = new JSONObject(response.toString());
            System.out.println("Create Contact Response: " + responseJson.toString());

            if (!responseJson.optBoolean("is_error")) {
                JSONArray valuesArray = responseJson.optJSONArray("values");
                if (valuesArray != null && valuesArray.length() > 0) {
                    JSONObject contactObject = valuesArray.getJSONObject(0);
                    int contactId = contactObject.getInt("id");
                    System.out.println("New Contact ID: " + contactId);
                    System.out.println("New Contact URL: " + EDIT_CONTACT_URL + contactId);
                    // Create and assign a tag to the contact
                    createTagForContact(contactId, "CMS");
                    // Add the contact to the "Client Group"
                    // addContactToGroup(contactId);

                    return contactId;
                }
            } else {
                System.out.println("Failed to create contact: " + responseJson.optString("error_message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


    private void addContactToGroup(int contactId) {
        try {
            String CRM_GROUP_ADD_URL = "https://ahcs.socialservicesconnect.com/civicrm/ajax/api4/GroupContact/create";

            URL url = new URL(CRM_GROUP_ADD_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("X-Civi-Auth", CRM_AUTH);

            // Prepare the payload to add contact to "Client Group"
            JSONObject params = new JSONObject();
            JSONObject values = new JSONObject();
            values.put("group_id:label", "Client Group");
            values.put("group_id.name", "Client_Group_34");
            values.put("contact_id", contactId);
            params.put("values", values);

            String postData = "params=" + URLEncoder.encode(params.toString(), StandardCharsets.UTF_8.toString());

            try (DataOutputStream out = new DataOutputStream(con.getOutputStream())) {
                out.writeBytes(postData);
                out.flush();
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
            }

            JSONObject responseJson = new JSONObject(response.toString());
            System.out.println("Add to Group Response: " + responseJson.toString());

            if (!responseJson.optBoolean("is_error")) {
                System.out.println("Contact added to Client Group successfully.");
            } else {
                System.out.println("Failed to add contact to group: " + responseJson.optString("error_message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void addPhoneToContact(int contactId, String phoneNumber) { // Remove static modifier
        try {
            URL url = new URL(CRM_PHONE_CREATE_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("X-Civi-Auth", CRM_AUTH);

            // Prepare phone addition payload with location_type_id set to 3
            int locationTypeId = 3; // Assuming 3 is the ID for the desired location type (e.g., Mobile)
            String postData = "params=" + URLEncoder.encode(
                    "{\"values\":{\"phone\":\"" + phoneNumber + "\",\"contact_id\":" + contactId + ",\"location_type_id\":" + locationTypeId + "}}",
                    StandardCharsets.UTF_8.toString()
            );

            try (DataOutputStream out = new DataOutputStream(con.getOutputStream())) {
                out.writeBytes(postData);
                out.flush();
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
            }

            JSONObject responseJson = new JSONObject(response.toString());
            System.out.println("Add Phone Response: " + responseJson.toString());

            if (!responseJson.optBoolean("is_error")) {
                System.out.println("Phone number added successfully.");
            } else {
                System.out.println("Failed to add phone: " + responseJson.optString("error_message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void createTagForContact(int contactId, String tagName) {
        try {
            String CRM_TAG_CREATE_URL = "https://ahcs.socialservicesconnect.com/civicrm/ajax/api4/Tag/create";

            URL url = new URL(CRM_TAG_CREATE_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("X-Civi-Auth", CRM_AUTH);

            // Prepare the payload to create a tag and associate it with the contact
            JSONObject params = new JSONObject();
            JSONObject values = new JSONObject();
            values.put("contact_id", contactId);
            values.put("tag_id:label", tagName);
            params.put("values", values);

            String postData = "params=" + URLEncoder.encode(params.toString(), StandardCharsets.UTF_8.toString());

            try (DataOutputStream out = new DataOutputStream(con.getOutputStream())) {
                out.writeBytes(postData);
                out.flush();
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
            }

            JSONObject responseJson = new JSONObject(response.toString());
            System.out.println("Create Tag Response: " + responseJson.toString());

            if (!responseJson.optBoolean("is_error")) {
                System.out.println("Tag created and assigned to contact successfully.");
            } else {
                System.out.println("Failed to create tag: " + responseJson.optString("error_message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void findPhoneNumber(String jsonResponse, String phoneNumber) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray valuesArray = jsonObject.getJSONArray("values");

            for (int i = 0; i < valuesArray.length(); i++) {
                JSONObject phoneObject = valuesArray.getJSONObject(i);
                if (phoneObject.getString("phone").contains(phoneNumber)) {
                    int contactId = phoneObject.getInt("contact_id");
                    setContactId(String.valueOf(contactId));
                    setCrmLink(BASE_CONTACT_URL + contactId);
                    return;
                }
            }
            // If no matches are found, set the CRM link to the default contact URL
            setCrmLink(DEFAULT_CONTACT_URL);
        } catch (JSONException e) {
            log.error("Error parsing JSON response", e);
            // Set the CRM link to the default contact URL in case of an exception
            setCrmLink(DEFAULT_CONTACT_URL);
        }
    }
}
