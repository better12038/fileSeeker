package com.mn.fileSeeker.core;

import com.mn.fileSeeker.config.EverythingConfig;
import com.mn.fileSeeker.config.HandlerPath;
import com.mn.fileSeeker.core.dao.DataSourceFactory;
import com.mn.fileSeeker.core.dao.FileIndexDao;
import com.mn.fileSeeker.core.dao.FileIndexDaoImpl;
import com.mn.fileSeeker.core.index.FileScan;
import com.mn.fileSeeker.core.index.impl.FileScanImpl;
import com.mn.fileSeeker.core.interceptor.impl.FileIndexInterceptor;
import com.mn.fileSeeker.core.interceptor.impl.FilePrintInterceptor;
import com.mn.fileSeeker.core.model.Condition;
import com.mn.fileSeeker.core.model.Thing;
import com.mn.fileSeeker.core.search.ThingSearch;
import com.mn.fileSeeker.core.search.impl.ThingSearchImpl;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
* 核心统一调度器(把代码组装到一起)
* 1.调索引
* 2.调检索
* 3.单例
* */
public class EverythingManager {

    private static volatile EverythingManager manager;

    private FileScan fileScan;
    private ThingSearch thingSearch;
    private FileIndexDao fileIndexDao;
    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2);

    private EverythingConfig config = EverythingConfig.getInstance();
    private EverythingManager(){
        this.fileScan = new FileScanImpl();
        this.fileIndexDao = new FileIndexDaoImpl(DataSourceFactory.getInstance());
        this.fileScan.interceptor(new FilePrintInterceptor());
        this.fileScan.interceptor(new FileIndexInterceptor(this.fileIndexDao));

        this.thingSearch = new ThingSearchImpl(this.fileIndexDao);
    }
    public static EverythingManager getInstance(){
        if(manager == null){
            synchronized (EverythingManager.class){
                if(manager == null){
                    manager = new EverythingManager();
                }
            }
        }
        return manager;
    }
    //构建索引
    public void buildIndex(){
        DataSourceFactory.databaseInit(true);//+1
        HandlerPath handlerPath = config.getHandlerPath();
        Set<String> includePaths = handlerPath.getIncludePath();
        new Thread(()-> {
            System.out.println("Build index start...");
            final CountDownLatch countDownLatch = new CountDownLatch(includePaths.size());
            for (String path:includePaths) {
                executorService.submit(()-> {
                    fileScan.index(path);
                    countDownLatch.countDown();
                });
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Build index complete");
        }).start();

    }
    //检索功能
    public List<Thing> search(Condition condition){
        condition.setLimit(config.getMaxReturn());
        condition.setOrderByDepthAsc(!config.getOrderByDesc());

        return this.thingSearch.search(condition);
    }
    //帮助
    public void help(){
        System.out.println("命令列表");
        System.out.println("退出请输入>> quit");
        System.out.println("帮助请输入>> help");
        System.out.println("添加索引请输入>> index");
        System.out.println("搜索文件请输入>> search <name> [<file-Type> img | doc | bin | archive | other]");
    }
    //退出
    public void quit(){
        System.out.println("感谢您的使用，再见！");
        System.exit(0);
    }

}
