package com.mn.fileSeeker.core.interceptor.impl;

import com.mn.fileSeeker.core.common.FileConvertThing;
import com.mn.fileSeeker.core.dao.FileIndexDao;
import com.mn.fileSeeker.core.interceptor.FileInterceptor;
import com.mn.fileSeeker.core.model.Thing;

import java.io.File;
//拦截器
//将File转换为Thing，然后写入数据库
//把文件放到数据库里

public class FileIndexInterceptor implements FileInterceptor {
    //File -> Thing ->写入数据库
    private final FileIndexDao fileIndexDao;
    public FileIndexInterceptor(FileIndexDao fileIndexDao){
        this.fileIndexDao = fileIndexDao;
    }
    @Override
    public void apply(File file) {
        Thing thing = FileConvertThing.convert(file);
        this.fileIndexDao.insert(thing);
    }
}
