package com.mn.fileSeeker.core.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
//FileType模型类表示文件的扩展名归类
//支持的文件类型
public enum FileType {
    IMG("jpg","jpeg","png","bmp","gif"),
    DOC("doc","docx","txt","pdf","ppt","pptx","xls"),
    BIN("exe","jar","sh","msi"),
    ARCHIVE("zip","rar"),
    OTHER("*");
    private Set<String> extendName = new HashSet<>();

    FileType(String... extendName){
        this.extendName.addAll(Arrays.asList(extendName));
    }
    //根据我给的扩展名找对应的FileType
    public static FileType lookupByExtend(String extendName){
        for(FileType fileType: FileType.values()){
            if(fileType.extendName.contains(extendName)){
                return fileType;
            }
        }
        return FileType.OTHER;
    }
    public static FileType lookupByName(String name){
        for(FileType fileType: FileType.values()){
            if(fileType.name().contains(name)){
                return fileType;
            }
        }
        return FileType.OTHER;
    }
    /*
    public static void main(String[] args) {
        System.out.println(FileType.lookupByName("exe"));
        System.out.println(FileType.lookupByExtend("md"));
        System.out.println(FileType.lookupByExtend("exe"));
        System.out.println(FileType.lookupByName("md"));
    }*/

}
