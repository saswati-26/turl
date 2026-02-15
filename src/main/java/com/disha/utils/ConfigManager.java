package com.disha.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigManager {

    private static final String CONFIG_DIR = System.getProperty("user.home") + File.separator + ".turl";

    private static final String CONFIG_FILE = CONFIG_DIR + File.separator + "config.properties";

    private Properties properties;

    public ConfigManager() {
        this.properties = new Properties();
        loadConfigFile();
    }

    private void ensureConfigDir() {
        File dir = new File(CONFIG_DIR);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    private void loadConfigFile() {

        File file = new File(CONFIG_FILE);
        if (file.exists()) {
            try (InputStream inputStream = new FileInputStream(file)) {
                properties.load(inputStream);
            } catch (Exception e) {
                ConsoleUtil.printError(e.getMessage());
            }
        }

    }

    private void saveConfig() {
        ensureConfigDir();
        try (FileOutputStream outputStream = new FileOutputStream(CONFIG_FILE)) {
            properties.store(outputStream, "Save Turl Configurations");
        } catch (Exception e) {
            ConsoleUtil.printError(e.getMessage());
        }
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
        saveConfig();
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public void removeProperty(String key) {
        properties.remove(key);
        saveConfig();
    }

    public Map<String, String> getAllProperties() {
        return new HashMap<>(properties.stringPropertyNames().stream()
                .collect(HashMap::new, (m, k) -> m.put(k, properties.getProperty(k)), HashMap::putAll));
    }
}