package com.disha.commands;

import java.util.Map;

import com.disha.http.ApiClient;
import com.disha.http.HttpResponse;
import com.disha.utils.ConsoleUtil;
import com.disha.utils.FileUtil;
import com.disha.utils.JsonUtil;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(
    name = "post",
    description = "Send a POST request to the specified URL",
    mixinStandardHelpOptions = true,
    header = "Usage: turl post <url>",
    footer = {
        "Example:",

        "# simple post request",
        "turl post http://example.com/users",

        "# with body",
        "turl post http://example.com/users -b '{\"name\": \"Saswati Choudhury\", \"email\": \"Saswati@example.com\"}'",

        "# with custom headers",
        "turl post http://example.com/users -H \"Authorization=Bearer token\"",

        "# save response to a file",
        "turl post http://example.com/users -s response.json",  

        "# with timeout",
        "turl post http://example.com/users -t 5000",

        "# with query parameters",
        "turl post http://example.com/users?q=test",
    }
)
public class PostCommand implements Runnable {

    @Parameters(
        index = "0", 
        description = "The URL to send the POST request to"
    )
    private String url;

    @Option(
        names = {"-b", "--body"},
        description = "Request body data in JSON format"
    )
    private String requestBody;

    @Option(
        names = {"-H", "--header"},
        description = "Custom headers (format: key=value)"
    )
    private Map<String, String> headers;

    @Option(
        names = {"-p", "--pretty"},
        description = "Pretty print the response"
    )
    private boolean prettyPrint;

    @Option(
        names = {"-s", "--save"},
        description = "Save response to a file"
    )
    private String saveFile;

    public void run() {
        
        ApiClient client = new ApiClient();
        
        try {            
            HttpResponse response = client.post(url, requestBody);

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

                System.out.println(saveFile);

                if (saveFile != null) {
                    ConsoleUtil.printWarning("Saving to file...");
                    FileUtil.saveToFile(saveFile, fileContent);
                    ConsoleUtil.printSuccess("Response saved to " + saveFile);
                } else {
                    ConsoleUtil.printWarning("Response not saved to " + saveFile);
                    ConsoleUtil.printWarning(fileContent);
                }

            } catch (Exception e) {
                ConsoleUtil.printError(e.getMessage());
            }

        } catch (Exception e) {
            ConsoleUtil.printError(e.getMessage());
        }
    }
}