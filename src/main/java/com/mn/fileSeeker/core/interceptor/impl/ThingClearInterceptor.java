package com.mn.fileSeeker.core.interceptor.impl;

import com.mn.fileSeeker.core.dao.FileIndexDao;
import com.mn.fileSeeker.core.interceptor.ThingInterceptor;
import com.mn.fileSeeker.core.model.Thing;

import java.util.Queue;

//清除已经删除的文件
public class ThingClearInterceptor implements Runnable, ThingInterceptor {
    private final FileIndexDao fileIndexDao;
    private final Queue<Thing> thingQueue;

    public ThingClearInterceptor(FileIndexDao fileIndexDao, Queue<Thing> thingQueue){
        this.fileIndexDao = fileIndexDao;
        this.thingQueue = thingQueue;
    }
    @Override
    public void apply(Thing thing) {
        this.fileIndexDao.delete(thing);
    }

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Thing thing = this.thingQueue.poll();//返回头结点
            if(thing != null){
                this.apply(thing);
            }
        }
    }
}
