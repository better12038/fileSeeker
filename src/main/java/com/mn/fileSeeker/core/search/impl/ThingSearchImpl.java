package com.mn.fileSeeker.core.search.impl;

import com.mn.fileSeeker.core.dao.FileIndexDao;
import com.mn.fileSeeker.core.interceptor.impl.ThingClearInterceptor;
import com.mn.fileSeeker.core.model.Condition;
import com.mn.fileSeeker.core.model.Thing;
import com.mn.fileSeeker.core.search.ThingSearch;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class ThingSearchImpl implements ThingSearch {
    private final FileIndexDao fileIndexDao;
    private final ThingClearInterceptor interceptor;
    //文件移除掉之后放到队列里
    private final Queue<Thing> thingQueue = new ArrayBlockingQueue<>(1024);

    public  ThingSearchImpl(FileIndexDao fileIndexDao){

        this.fileIndexDao = fileIndexDao;
        this.interceptor = new ThingClearInterceptor(this.fileIndexDao,thingQueue);
        this.backgroundClearThread();
    }
    @Override
    public List<Thing> search(Condition condition) {
        /*
        * 如果本地文件系统将文件删除，数据库中仍然存储索引信息，此时如果查询结果
        * 还存在已经在本地文件系统中删除的文件，那么需要在数据库中清除掉该文件的索引信息
        * */
        List<Thing> things = this.fileIndexDao.query(condition);
        //查完之后做个遍历，如果发现文件不在文件系统中，就把结果集里的删掉，同时，也删掉数据库里的
        Iterator<Thing> iterator = things.iterator();
        while(iterator.hasNext()){
            Thing thing = iterator.next();
            File file = new File(thing.getPath());
            if(!file.exists()){
                iterator.remove();//CPU级别的
                //interceptor.apply(thing);//IO级别的,优化

                this.thingQueue.add(thing);
            }
        }
        return things;
    }
    //后台清理掉数据库中已经不存在的记录
    private void backgroundClearThread(){
        //进行后台清理工作
        Thread thread = new Thread(this.interceptor);
        thread.setName("Thread-Clear");
        thread.setDaemon(true);//设置为守护线程
        thread.start();
    }
}
