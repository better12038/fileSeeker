package com.mn.fileSeeker.core.search;

import com.mn.fileSeeker.core.model.Condition;
import com.mn.fileSeeker.core.model.Thing;

import java.util.List;

//根据Condition检索数据
public interface ThingSearch {
    List<Thing> search(Condition condition);
}
