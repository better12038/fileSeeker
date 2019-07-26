package com.mn.fileSeeker.core.model;

import lombok.Data;

//检索条件的模型类
@Data
public class Condition {
    private String name;
    private String fileType;
    private Integer limit;//限制的数量
    public Boolean orderByDepthAsc;
}
