package com.xiaoyun.qiangke;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatCheckBox;

import com.xiaoyun.ZF.SimpleClassXK;

import java.util.ArrayList;
import java.util.HashMap;

public class ExpendAdapter extends BaseExpandableListAdapter {
   public DATA_PACK_VIEW data;
   public Context con;
   public ArrayList<SimpleClassXK> selected;
   public HashMap<Integer, View> VIEW_MAP= new HashMap<>();

    static class ViewHolder{
        TextView t1;
        TextView t2;
        TextView t3;
        TextView t4;
        ImageView img1;
        AppCompatCheckBox ck1;
    }
    public ExpendAdapter(Context con, DATA_PACK_VIEW data,ArrayList<SimpleClassXK> selected){
        this.data=data;
        this.con=con;
        this.selected=selected;
    }
    @Override
    public int getGroupCount() {
        return data.getScls().length;
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return data.getScls();
    }

    @Override
    public Object getChild(int i, int i1) {
        return data.getScls()[i];
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @SuppressLint({"InflateParams", "UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        SimpleClassXK scl = data.getScls()[i];
        String[] kclist = scl.jxbmc.split("-");
        String mc = kclist[kclist.length - 1];
        if(view==null) {
            view = LayoutInflater.from(con).inflate(R.layout.expendlist_group_item, null);
            holder= new ViewHolder();
            holder.img1 = view.findViewById(R.id.sub_up_down);
            holder.t1 = view.findViewById(R.id.kcmc);
            holder.t2 = view.findViewById(R.id.xf);
            view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();
        }
        if (b) {
            holder.img1.setBackground(con.getResources().getDrawable(R.drawable.down));
        } else {
            holder.img1.setBackground(con.getResources().getDrawable(R.drawable.up));
        }
        holder.t1.setText(scl.kcmc + "(" + mc + ")");
        holder.t2.setText("学分:" + scl.xf);

        return view;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        String KB = "  ";
        String L="\n";
        SimpleClassXK s = data.getScls()[i];
        ViewHolder holder;
        if(VIEW_MAP.get(i)==null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(con).inflate(R.layout.expend_child_item, null);
            holder.ck1=view.findViewById(R.id.c1);
            holder.t1 = view.findViewById(R.id.line1);
            holder.t2 = view.findViewById(R.id.line2);
            view.setTag(holder);
            VIEW_MAP.put(i,view);
        }else{
            view=VIEW_MAP.get(i);
            holder= (ViewHolder) view.getTag();
        }

        holder.ck1.setChecked(selected.contains(s));
        holder.ck1.setOnClickListener(view1 -> {
            if(holder.ck1.isChecked()){
                System.out.println("添加数据");
                selected.add(s);
            }else{
                System.out.println("删除数据");
                selected.remove(s);
            }
        });
        //System.out.println(selected.contains(s));
        String l1="地点:"+s.jxdd+L+"时间:"+s.sksj+KB;
        String l2="老师:"+s.jsxx+L+"容量:"+s.yxzrs+"/"+s.jxbrl;
        holder.t1.setText(l1.replace("<br/>",""));
        holder.t2.setText(l2);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }



}
