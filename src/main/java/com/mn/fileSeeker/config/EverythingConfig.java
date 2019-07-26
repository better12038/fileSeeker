package com.mn.fileSeeker.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//配置
@ToString
public class EverythingConfig {

    //单例
    private static volatile EverythingConfig config;
    //索引目录
    @Getter
    private HandlerPath handlerPath = HandlerPath.getDefaultHandlerPath();

    @Getter
    @Setter
    private Integer maxReturn = 30;//最大检索返回的结果数

    //是否构建索引，默认程序运行时索引关闭
    //在程序运行时，可以指定参数是否开启缩影
    //通过功能命令 index
    @Getter
    @Setter
    private Boolean enableBuildIndex = false;

    //排序按照：检索时depth深度的排序规则
    //true表示降序，也就是文件路径最长的在最前面;false表示升序
    @Getter
    @Setter
    private Boolean orderByDesc = false;//默认降序

    private EverythingConfig(){

    }
    public static EverythingConfig getInstance(){
        if(config == null){
            synchronized (EverythingConfig.class){
                if(config == null){
                    config = new EverythingConfig();
                }
            }
        }
        return config;
    }
}
