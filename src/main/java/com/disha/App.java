package com.disha;

import com.disha.commands.DeleteCommand;
import com.disha.commands.GetCommand;
import com.disha.commands.PostCommand;
import com.disha.commands.PutCommand;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "turl", 
description = "A CLI tool for REST API Testing",
subcommands = {
    GetCommand.class,
    PostCommand.class,
    PutCommand.class,
    DeleteCommand.class
},
mixinStandardHelpOptions = true, 
version = "turl 1.0-SNAPSHOT",
header = "TURL - REST API Testing CLI Tool",
footer = "Copyright (c) 2026 Disha Technologies")
public class App implements Runnable {

    @Override
    public void run() {
        
        System.out.println("Welcome to TURL - REST API Testing CLI Tool");
        System.out.println("Use 'turl --help' to see available commands");
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new App()).execute(args);
        System.exit(exitCode);
    }
}