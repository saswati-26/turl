package com.disha.commands;

import com.disha.http.ApiClient;
import com.disha.http.HttpResponse;
import com.disha.utils.ConfigManager;
import com.disha.utils.ConsoleUtil;
import com.disha.utils.FileUtil;
import com.disha.utils.JsonUtil;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(
    name = "get",
    description = "retrive the resources from API endpoint",
    mixinStandardHelpOptions = true,
    header = "Usage: turl get <resource>",
    footer = {  
            "Examples:",
                "# simple get request",
                "turl get http://example.com/users",
                
                "# with query parameters",
                "turl get https://example.com/search?q=test",

                "# with custom headers",
                "turl get -H \"Authorization: Bearer token\" https://example.com/protected-resource",

                "# save response to a file",
                "turl get -s response.json /users"
        }
)
public class GetCommand implements Runnable{

    @Parameters(
        index = "0", 
        description = "The api endpoint or api url to retrieve"
    )
    private String endpoint;
    
    @Option(
        names = {"-u", "--url"},
        description = "Base URL of the api"
    )
    private String baseUrl;

    @Option(
        names = {"-H", "--header"},
        description = "Set custom headers",
        arity = "1..*"
    )
    private String[] headers;

    @Option(
        names = {"-a", "--auth"},
        description = "Authentication Token (Bearer)"
    )
    private String authToken;

    @Option(
        names = {
            "-p", "--pretty"
        },
        description = "Pretty print the response"
    )
    private boolean prettyPrint;

    @Option(
        names = {
            "-s", "--save"
        },
        description = "Save response to a file"
    )
    private String saveFile;

    @Override
    public void run() {
        ApiClient client = createClient();

        try {            
            HttpResponse response = client.get(endpoint);
            
            String fileContent;
            
            ConsoleUtil.printWarning("Status Code: " + response.getStatusCode());
            ConsoleUtil.printWarning("Response Time: " + response.getResponseTime());
            try {                
                if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                    if (prettyPrint) {        
                        fileContent = JsonUtil.prettyPrint(response.getBody());
                        ConsoleUtil.printSuccess(JsonUtil.prettyPrint(response.getBody()));
                    } else {
                        fileContent = response.getBody();
                        ConsoleUtil.printSuccess(response.getBody()); 
                    }
                } else {
                    fileContent = response.getBody();
                    ConsoleUtil.printError(response.getBody());
                }

                if (saveFile != null) {
                    ConsoleUtil.printWarning("Saving to file...");
                    FileUtil.saveToFile(saveFile, fileContent);
                    ConsoleUtil.printSuccess("Response saved to " + saveFile);
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

        System.out.println("Headers: " + headers);
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