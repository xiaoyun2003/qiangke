package com.xiaoyun.qiangke;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiaoyun.ZF.SimpleClassXK;

import java.util.ArrayList;

public class Selected_ListView_Adapter extends BaseAdapter {
    public ArrayList<SimpleClassXK> scls;
    public Context con;
    static class ViewHolder{
        TextView t1;
    }

    public Selected_ListView_Adapter(Context con,ArrayList<SimpleClassXK> scls){
        this.scls=scls;
        this.con=con;
    }
    @Override
    public int getCount() {
        return scls.size();
    }

    @Override
    public Object getItem(int i) {
        return scls.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        String L="\n";
        SimpleClassXK scl=scls.get(i);
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(con).inflate(R.layout.selected_listview_item, null);
            holder = new ViewHolder();
            holder.t1= view.findViewById(R.id.sel_t1);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();

        }
        String[] kclist = scl.jxbmc.split("-");
        String mc = kclist[kclist.length - 1];
        String data="课程名称:"+scl.kcmc+"("+mc+")"+L+"教学地点:"+scl.jxdd+L+"老师:"+scl.jsxx;
        holder.t1.setText(data);
        return view;
    }
}
