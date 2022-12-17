package com.xiaoyun.qiangke;

import com.xiaoyun.ZF.CourseXK;
import com.xiaoyun.ZF.SimpleClassXK;

public class DATA_PACK_VIEW {
    public int id;
    public CourseXK cou;
   public SimpleClassXK[] scls;
    public DATA_PACK_VIEW(int id, CourseXK cou, SimpleClassXK[] scls){
        this.id=id;
        this.cou=cou;
        this.scls=scls;
    }

    public CourseXK getCou(){
        return this.cou;
    }

    public SimpleClassXK[] getScls(){
        return this.scls;
    }
}
