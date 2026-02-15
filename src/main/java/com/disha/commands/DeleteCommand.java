package com.disha.commands;

import com.disha.http.ApiClient;
import com.disha.http.HttpResponse;
import com.disha.utils.ConsoleUtil;
import com.disha.utils.JsonUtil;
import com.disha.utils.ConfigManager;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(
    name = "delete",
    description = "Delete a URL",
    mixinStandardHelpOptions = true,
    header = "Delete a URL",
    footer = {
        "Examples:",
            "# simple delete request",
            "turl delete https://example.com/users/1",

            "# with custom headers",
            "turl delete -H Authorization=BearerToken https://example.com/protected/1"
    }
)
public class DeleteCommand implements Runnable {

    @Parameters(
        index = "0", description = "URL to delete"
    )
    private String endpoint;

    @Option(
        names = {"-u", "--url"},
        description = "Base URL of the api"
    )
    private String baseUrl;

    @Option(
        names = {"-a", "--auth"},
        description = "Authentication Token (Bearer)"
    )
    private String authToken;

    @Option(
        names = {"-H", "--header"},
        description = "Header to add to the request",
        arity = "1..*"
    )
    private String[] headers;

    @Option(
        names = {"-p", "--pretty"},
        description = "Pretty print the response"
    )
    private boolean prettyPrint;
    
    @Override
    public void run() {

        ApiClient client = createClient();

        try {

            HttpResponse response = client.delete(endpoint);

            ConsoleUtil.printWarning("Status Code: " + response.getStatusCode());
            ConsoleUtil.printWarning("Response Time: " + response.getResponseTime());

            try {                
                if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                    if (prettyPrint) {
                        ConsoleUtil.printSuccess(JsonUtil.prettyPrint(response.getBody()));
                    } else {
                        ConsoleUtil.printSuccess(response.getBody()); 
                    }
                } else {
                    if (prettyPrint) {
                        ConsoleUtil.printError(JsonUtil.prettyPrint(response.getBody()));
                    } else {
                        ConsoleUtil.printError(response.getBody());
                    }
                }

            } catch (Exception e) {
                ConsoleUtil.printError(e.getMessage());
            }

        } catch (Exception e) {
            ConsoleUtil.printError(e.getMessage());
        }
    }

    private ApiClient createClient() {
        ConfigManager configManager = new ConfigManager();
        String url = baseUrl != null ? baseUrl : configManager.getProperty("api.base-url");

        ApiClient apiClient = new ApiClient(url);

        if (authToken != null) {
            apiClient.addAuthorizationBearer(authToken);
        } else {
            String token = configManager.getProperty("api.auth-token");
            if (token != null) {
                apiClient.addAuthorizationBearer(token);
            }
        }
        if (headers != null) {
            for (String header : headers) {
                String[] parts = header.split(":", 2);

                if (parts.length == 2) {
                    apiClient.setDefaultHeaders(parts[0], parts[1]);
                }
            }
        }
        apiClient.setDefaultHeaders("Content-Type", "application/json");

        return apiClient;
    }
}