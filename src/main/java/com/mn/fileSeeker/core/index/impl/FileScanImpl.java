package com.mn.fileSeeker.core.index.impl;

import com.mn.fileSeeker.config.EverythingConfig;
import com.mn.fileSeeker.core.index.FileScan;
import com.mn.fileSeeker.core.interceptor.FileInterceptor;

import java.io.File;
import java.util.LinkedList;
import java.util.Set;

//什么时候拦截？在遍历到一个文件的时候，就打印
public class FileScanImpl implements FileScan {

    private final LinkedList<FileInterceptor> interceptors = new LinkedList<>();//放拦截器

    private EverythingConfig config = EverythingConfig.getInstance();
    @Override
    public void index(String path) {
        Set<String> excludePaths = config.getHandlerPath().getExcludePath();//排除目录
        for(String excludePath: excludePaths){
            if(path.startsWith(excludePath)){
                return;
            }
        }

        File file = new File(path);
        if(file.isDirectory()){
            File[] files = file.listFiles();
            if(files != null){
                for(File f: files){
                    index(f.getAbsolutePath());
                }
            }
        }
        for(FileInterceptor interceptor: this.interceptors){
            interceptor.apply(file);
        }
    }

    @Override
    public void interceptor(FileInterceptor interceptor) {
        this.interceptors.add(interceptor);
    }

}
