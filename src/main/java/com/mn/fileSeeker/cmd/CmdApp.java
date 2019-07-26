package com.mn.fileSeeker.cmd;

import com.mn.fileSeeker.core.EverythingManager;
import com.mn.fileSeeker.core.model.Condition;

import java.util.Scanner;

public class CmdApp {
    public static void main(String[] args) {

        EverythingManager manager = EverythingManager.getInstance();
        Scanner scanner = new Scanner(System.in);
        System.out.println("欢迎使用 **搜索助手(fileSeeker)**");

        while (true) {
            System.out.print(">>");
            String commend = scanner.nextLine();
            switch (commend){
                case "help": {
                    manager.help();
                    break;
                }
                case "quit":{
                    manager.quit();
                    break;
                }
                case "index":{
                    manager.buildIndex();
                    break;
                }
                default:{
                    if(commend.startsWith("search")){
                        String[] segments = commend.split(" ");

                        if(segments.length >= 2){
                            String name = segments[1];
                            Condition condition = new Condition();
                            condition.setName(name);
                            if(segments.length >=3){
                                String type = segments[2];

                                condition.setFileType(type.toUpperCase());
                            }
                            manager.search(condition).forEach(thing -> {

                                System.out.println(thing.getPath());
                            });
                        }else{
                            manager.help();
                        }

                    }else{
                        manager.help();
                    }
                }
            }
        }

    }
}
