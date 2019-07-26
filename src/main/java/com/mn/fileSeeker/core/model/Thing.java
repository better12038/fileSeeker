package com.mn.fileSeeker.core.model;

import lombok.Data;

@Data
public class Thing {
    private String name;//文件名
    private String path;
    private Integer depth;//文件路径深度
    private FileType fileType;
}
