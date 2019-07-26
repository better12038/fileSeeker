package com.mn.fileSeeker.core.dao;

import com.mn.fileSeeker.core.model.Condition;
import com.mn.fileSeeker.core.model.Thing;

import java.util.List;

//数据库访问的对象
public interface FileIndexDao {
    void insert(Thing thing);
    void delete(Thing thing);
    List<Thing> query(Condition condition);


}
