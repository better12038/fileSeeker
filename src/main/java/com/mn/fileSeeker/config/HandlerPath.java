package com.mn.fileSeeker.config;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
//将来哪些目录要存到数据库里
@Getter
@ToString
//处理的目录
public class HandlerPath {
    private Set<String> includePath = new HashSet<>();//包含的目录

    private Set<String> excludePath = new HashSet<>();//排除的目录

    private HandlerPath(){

    }
    public void addIncludePath(String path){
        this.includePath.add(path);
    }
    public void addExcludePath(String path){
        this.excludePath.add(path);
    }

    public static HandlerPath getDefaultHandlerPath(){
        HandlerPath handlerPath = new HandlerPath();
        Iterable<Path> paths = FileSystems.getDefault().getRootDirectories();//返回盘符目录
        //默认要包含的路径
        paths.forEach(path -> {
            handlerPath.addIncludePath(path.toString());
        });
        //默认要排除的目录
        String osName = System.getProperty("os.name");
        if(osName.contains("Windows")){
            //windows
            //排除C://Windows....
            handlerPath.addExcludePath("C:\\Windows");
            handlerPath.addExcludePath("C:\\Program Files");
            handlerPath.addExcludePath("C:\\Program Files(x86)");
            handlerPath.addExcludePath("C:\\ProgramData");
        }else{
            //linux
            handlerPath.addExcludePath("/root");
            handlerPath.addExcludePath("/temp");
        }
        return handlerPath;
    }
    /*
    public static void main(String[] args) {
        System.out.println(HandlerPath.getDefaultHandlerPath());
    }
    */
}
