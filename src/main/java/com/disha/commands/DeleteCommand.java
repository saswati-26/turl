package com.disha.commands;

import java.util.Map;

import com.disha.http.ApiClient;
import com.disha.http.HttpResponse;
import com.disha.utils.ConsoleUtil;
import com.disha.utils.JsonUtil;

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
    private String url;

    @Option(
        names = {"-H", "--header"},
        description = "Header to add to the request"
    )
    private Map<String, String> headers;

    @Option(
        names = {"-p", "--pretty"},
        description = "Pretty print the response"
    )
    private boolean prettyPrint;
    
    @Override
    public void run() {

        ApiClient client = new ApiClient();

        try {

            HttpResponse response = client.delete(url);

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
                    ConsoleUtil.printError(response.getBody());
                }

            } catch (Exception e) {
                ConsoleUtil.printError(e.getMessage());
            }

        } catch (Exception e) {
            ConsoleUtil.printError(e.getMessage());
        }
    }
}