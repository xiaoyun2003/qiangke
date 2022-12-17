package com.xiaoyun.qiangke;

public class Console_Text {
    public Console_Text(String h,String data,String color){
        this.data=data;
        this.head=h;
        this.color=color;
        if(color ==null || color.isEmpty()){
            this.color=GREEN;
        }

    }
    public String color;
    //主体信息
    public String data;
    //时间
    public long time;
    //头部信息
    public String head;
    public int type;
    public static final String RED="#FF0033";
    public static final String GREEN="#33CC52";
    public static final String BLUE="#0066FF";
    public static final String ORANGE="#FF9900";
    public static final String YELLOW="#FFFF00";
}
