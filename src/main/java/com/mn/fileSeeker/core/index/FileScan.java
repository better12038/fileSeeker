package com.mn.fileSeeker.core.index;

import com.mn.fileSeeker.core.dao.DataSourceFactory;
import com.mn.fileSeeker.core.dao.FileIndexDao;
import com.mn.fileSeeker.core.dao.FileIndexDaoImpl;
import com.mn.fileSeeker.core.index.impl.FileScanImpl;
import com.mn.fileSeeker.core.interceptor.FileInterceptor;
import com.mn.fileSeeker.core.interceptor.impl.FileIndexInterceptor;
import com.mn.fileSeeker.core.interceptor.impl.FilePrintInterceptor;

import javax.sql.DataSource;

//把本地的文件路径变成文件对象然后加到数据库里面去
public interface FileScan {
//    将指定path路径下的所有目录和文件以及子目录和文件递归扫描
    void index(String path);
    void interceptor(FileInterceptor interceptor);//文件扫描的时候增加一个拦截器对象

    public static void main(String[] args) {
        FileScan fileScan = new FileScanImpl();
        //打印输出拦截器
        fileScan.interceptor(new FilePrintInterceptor());
        //索引文件到数据库的拦截器
        DataSource dataSource = DataSourceFactory.getInstance();
        FileIndexDao fileIndexDao = new FileIndexDaoImpl(dataSource);
        fileScan.interceptor(new FileIndexInterceptor(fileIndexDao));

        fileScan.index("F:\\2019学校相关文档");
    }
}
