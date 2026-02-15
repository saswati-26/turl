package com.disha.utils;

import java.io.FileWriter;

public class FileUtil {

    public static void saveToFile(String filePath, String content) {
        try (FileWriter fileWriter = new FileWriter(filePath)) {

            fileWriter.write(content);
            
        } catch (Exception e) {
            ConsoleUtil.printError(e.getMessage());
        }   
    }
    
}
