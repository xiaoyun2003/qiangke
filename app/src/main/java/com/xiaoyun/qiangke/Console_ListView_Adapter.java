package com.xiaoyun.qiangke;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class Console_ListView_Adapter extends BaseAdapter {

    public ArrayList<Console_Text> cs=new ArrayList<>();
    public Context con;
    static class ViewHolder{
        TextView t1;
    }
    public Console_ListView_Adapter(Context con){
        this.con=con;
    }
    @Override
    public int getCount() {
        return cs.size();
    }

    @Override
    public Object getItem(int i) {
        return cs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Console_Text c=cs.get(i);
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(con).inflate(R.layout.console_listview_item, null);
            holder = new ViewHolder();
            holder.t1= view.findViewById(R.id.con_t1);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String data="["+ss.format(c.time)+"]"+c.head+":"+c.data;
        holder.t1.setText(data);
        holder.t1.setTextColor(Color.parseColor(c.color));
        return view;
    }

    public void add_data(Console_Text c){
        if(cs.size()>100){
            cs=new ArrayList<>();
            this.notifyDataSetChanged();
        }
        if(c.time==0){
            c.time=System.currentTimeMillis();
        }
        cs.add(c);
        this.notifyDataSetChanged();
    }



}
