package com.disha.utils;

public class ConsoleUtil {

    public static final String RESET = "\u001B[0m";
    public static final String GREEN = "\u001B[32m";
    public static final String RED = "\u001B[31m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";
     
    public static void printSuccess(String message) {
        System.out.println(GREEN + message + RESET);
    }

    public static void printWarning(String message) {
        System.out.println(YELLOW + message + RESET);
    }

    public static void printError(String message) {
        System.out.println(RED + message + RESET);
    }

    public static void printInfo(String message) {
        System.out.println(BLUE + message + RESET);
    }

    
}