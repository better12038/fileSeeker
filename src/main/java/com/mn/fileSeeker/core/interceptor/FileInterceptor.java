package com.mn.fileSeeker.core.interceptor;

import java.io.File;
//文件拦截器，处理指定文件
public interface FileInterceptor {
    void apply(File file);
}
