package com.disha.commands;

import java.util.LinkedHashMap;
import java.util.Map;

import com.disha.http.ApiClient;
import com.disha.http.HttpResponse;
import com.disha.utils.ConsoleUtil;
import com.disha.utils.JsonUtil;
import com.disha.utils.ConfigManager;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(
    name = "put",
    description = "Send a PUT request to the specified URL",
    mixinStandardHelpOptions = true,
    header = "Usage: turl put <url>",
    footer = {
        "Example:",

        "# with body",
        "turl put http://example.com/users/1 -b '{\"name\": \"Saswati Choudhury\", \"email\": \"Saswati@example.com\"}'",

        "# with custom headers",
        "turl put http://example.com/users/1 -H \"Authorization=Bearer token\"",

        "# save response to a file",
        "turl put http://example.com/users/1 -s response.json",

        "# with timeout",
        "turl put http://example.com/users/1 --timeout 30"
    }
)
public class PutCommand implements Runnable {

    @Parameters(
        index = "0",
        description = "The URL to send the PUT request to"
    )
    private String url;
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
        names = { "-b", "--body" },
        description = "Request body (JSON format)",
        required = true
    )
    private String requestBody;

    @Option(
        names = { "-p", "--pretty" },
        description = "Pretty print the response"
    )
    private boolean prettyPrint;

    @Option(
        names = { "-H", "--header" },
        description = "Custom headers (format: key:value)",
        arity = "1..*"
    )
    private String[] headers;

    @Override
    public void run() {
        ApiClient client = createClient();

        try {
            HttpResponse response = client.put(url, requestBody);

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