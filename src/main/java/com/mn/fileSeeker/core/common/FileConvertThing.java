package com.mn.fileSeeker.core.common;

import com.mn.fileSeeker.core.model.FileType;
import com.mn.fileSeeker.core.model.Thing;

import java.io.File;

//文件对象转化成Thing对象的辅助类
public class FileConvertThing {
    public static Thing convert(File file){
        Thing thing = new Thing();
        String name = file.getName();
        thing.setName(name);
        thing.setPath(file.getAbsolutePath());
        //如果是目录，没有扩展名（*）
        //如果是文件，有扩展名，通过扩展名获取fileType
        int index = name.lastIndexOf(".");//最后一个.的位置
        String extend = "*";
        if(index != -1 && (index+1) < name.length()){
            //有扩展名,避免文件夹名为a.**
            extend = name.substring(index+1);//扩展名
        }
        thing.setFileType(FileType.lookupByExtend(extend));
        thing.setDepth(computePathDepth(file.getAbsolutePath()));
        return thing;
    }

    private static int computePathDepth(String path){
        //D:\a\b -> 2
        //D:\a\b\a.txt -> 3
        //windows下分隔符：\
        //Linux下分隔符：/
        int depth = 0;
        for(char c: path.toCharArray()){
            if(c == File.separatorChar){
                depth++;
            }
        }
        return depth;
    }

}
