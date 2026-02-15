package com.disha.commands;

import com.disha.utils.ConfigManager;
import com.disha.utils.ConsoleUtil;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
    name = "config",
    description = "Configure turl settings",
    mixinStandardHelpOptions = true,
    header = "Configure turl settings",
    footer = {
        "Example:",

        "# set the base-url",
        "turl config --set api.base-url=https://example.com",

        "# list all configurations",
        "turl config --list",
        
        "# get the specific value",
        "turl config --get api.base-url",

        "# delete a configuration",
        "turl config --delete api.base-url"
    }
)
public class ConfigCommand implements Runnable{

    @Option(
        names = {"-s", "--set"},
        description = "Set the value"
    )
    private String setValue;

    @Option(
        names = {"-l", "--list"},
        description = "List all configuarations"
    )
    private boolean listAll;

    @Option(
        names = {"-g", "--get"},
        description = "Get the value"
    )
    private String getKey;

    @Option(
        names = {"-d", "--delete"},
        description = "Delete a configuration"
    )
    private String deleteKey;
    
    @Override
    public void run() {

        ConfigManager configManager = new ConfigManager();
        if (listAll) {
            listConfigs(configManager);
        }
        else if (setValue != null) {
            setConfig(configManager, setValue);
        }
        else if (getKey != null) {
            getConfig(configManager, getKey);
        }
        else if (deleteKey != null) {
            deleteConfig(configManager, deleteKey);  
        }      
        else {
            ConsoleUtil.printInfo("Use -h or --help for more information.");
        }
    }
    private void setConfig(ConfigManager config, String keyValue) {
        String[] parts = keyValue.split("=", 2);
        if (parts.length != 2) {
            ConsoleUtil.printError("Invalid format. Use: key=value");
            return;
        }
        config.setProperty(parts[0].trim(), parts[1].trim());
        ConsoleUtil.printSuccess("Configuration saved: " + parts[0].trim());
    }

    private void getConfig(ConfigManager config, String key) {
        String value = config.getProperty(key);
        if (value != null) {
            System.out.println(key + "=" + value);
        } else {
            ConsoleUtil.printWarning("No value found for key: " + key);
        }
    }

    private void listConfigs(ConfigManager config) {
        config.getAllProperties().forEach((k, v) -> {
            // Hide sensitive data
            if (k.toLowerCase().contains("token") || k.toLowerCase().contains("password")) {
                System.out.println(k + "=***");
            } else {
                System.out.println(k + "=" + v);
            }
        });
    }

    private void deleteConfig(ConfigManager config, String key) {
        config.removeProperty(key);
        ConsoleUtil.printSuccess("Configuration deleted: " + key);
    }
}
