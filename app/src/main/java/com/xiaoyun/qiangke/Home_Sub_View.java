package com.xiaoyun.qiangke;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.xiaoyun.ZF.SimpleClassXK;

import java.util.ArrayList;

public class Home_Sub_View extends Fragment {

    public Activity con;
    public DATA_PACK_VIEW data;
   ExpendAdapter adapter_list;
   ArrayList<SimpleClassXK> selected_classes;



    public Home_Sub_View(DATA_PACK_VIEW data, ArrayList<SimpleClassXK> selected_classes){
        this.data=data;
        this.selected_classes=selected_classes;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.con=getActivity();
    }
    @SuppressLint("ResourceType")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if(data.id==2) {
            View view = inflater.inflate(R.layout.classview_sub, container, false);
            ExpandableListView exlist = view.findViewById(R.id.class_list);
            adapter_list=new ExpendAdapter(con,data,selected_classes);
            exlist.setAdapter(adapter_list);
            return view;
        }else{
            View view = inflater.inflate(R.layout.selectview, container, false);
            return view;
        }
    }


    public String getTitle(){
        if(this.data.getCou()!=null) {
            return this.data.getCou().name;
        }else{
            return "Home";
        }
    }

    public void setData(DATA_PACK_VIEW data){
        this.data=data;
    }



}
