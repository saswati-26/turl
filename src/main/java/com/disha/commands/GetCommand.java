package com.disha.commands;

import com.disha.http.ApiClient;
import com.disha.http.HttpResponse;
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
        names = {
            "-p", "--pretty"
        },
        description = "Pretty print the response"
    )
    private boolean prettyPrint;

    @Override
    public void run() {
        ApiClient client = new ApiClient();
        try {            
            HttpResponse response = client.get(endpoint);  
            
            if (prettyPrint) {
                System.out.println(JsonUtil.prettyPrint(response.getBody()));
            } else {
                System.out.println(response.getBody()); 
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}