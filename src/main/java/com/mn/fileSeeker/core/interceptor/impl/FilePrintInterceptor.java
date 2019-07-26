package com.mn.fileSeeker.core.interceptor.impl;

import com.mn.fileSeeker.core.interceptor.FileInterceptor;

import java.io.File;

//传个文件，打印一下
public class FilePrintInterceptor implements FileInterceptor {

    @Override
    public void apply(File file) {
        System.out.println(file.getAbsolutePath());
    }
}
