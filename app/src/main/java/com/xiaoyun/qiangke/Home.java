package com.xiaoyun.qiangke;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.xiaoyun.ZF.CourseXK;
import com.xiaoyun.ZF.SimpleClassXK;
import com.xiaoyun.ZF.User;
import com.xiaoyun.ZF.ZF;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Home extends AppCompatActivity {
    ViewPager viewpager;
    Button start_xk_button;
    TabLayout tab;
    Context con;
    TextView time_view;
   Console_ListView_Adapter console_adpater;
    Selected_ListView_Adapter selected_adpater;
    ArrayList<Home_Sub_View> views;
    FragmentViewAdapter adapter;
    ArrayList<SimpleClassXK> selected_classes=new ArrayList<>();

    long Start_time=0;
    String Start_time_text="";
    User user_zf;
    QK_Core qk ;
    SharedPreferences sp;

    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler()
    {
        // 这个方法是在主线程里面执行的
        @Override
        public void handleMessage(android.os.Message msg) {
            Bundle b=msg.getData();
            adapter.notifyDataSetChanged();
            //数据加载完毕

            //加载中延迟
            if(b.getInt("code")==201) {
                Toast.makeText(Home.this, b.getString("msg"), Toast.LENGTH_LONG).show();
            }
            if(b.getInt("code")==200){
                console_adpater.add_data(new Console_Text(b.getString("head"),b.getString("msg"),b.getString("color")));
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        con=this;
        viewpager=(ViewPager) findViewById(R.id.viewpager);
        start_xk_button=(Button)findViewById(R.id.xk);


        tab=findViewById(R.id.tab);
        views=new ArrayList<Home_Sub_View>();
        sp=getSharedPreferences("settings",MODE_PRIVATE);

        //线程获取数据
        new GET_DATA_VIEW_HTTP().start();
        views.add(new Home_Sub_View(new DATA_PACK_VIEW(1,null,null),selected_classes));
        adapter=new FragmentViewAdapter(getSupportFragmentManager(),0,views);
        viewpager.setOffscreenPageLimit(10);
        viewpager.setAdapter(adapter);
        tab.setupWithViewPager(viewpager);
        console_adpater=new Console_ListView_Adapter(con);
        console_adpater.add_data(new Console_Text("WelCome!","欢迎使用本程序,如有bug请向开发者反应,谢谢~",null));
        console_adpater.add_data(new Console_Text("WelCome!","基于ZFJAVA协议编写",null));
        console_adpater.add_data(new Console_Text("WelCome!","开发者QQ:3483421977",null));
        console_adpater.add_data(new Console_Text("ZFJAVA","开始初始化数据...",null));
        console_adpater.add_data(new Console_Text("ZFJAVA","当前版本:1.0",Console_Text.ORANGE));





        //绑定监听数据
        viewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                setHome(position);
            }
        });

        viewpager.post(new Runnable() {
                    @Override
                    public void run() {
                        View view=viewpager.getChildAt(0);
                        if(view!=null){
                            System.out.println("home not null");
                            setHome(0);
                            time_view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    setTime();
                                }
                            });

                        }

                        //ui渲染完成之后再绑定事件
                        start_xk_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Button bt=view.findViewById(R.id.xk);


                                if(bt.getText().toString().equals("开始抢课")){

                                if(selected_classes.size()==0){
                                    Bundle b=new Bundle();
                                    Message msg=new Message();
                                    b.putString("msg","请先选择要抢的课程哦~");
                                    b.putInt("code",201);
                                    msg.setData(b);
                                    handler.sendMessage(msg);
                                }else {

                                    if(Start_time==0){
                                        Bundle b=new Bundle();
                                        Message msg=new Message();
                                        b.putString("msg","请先设置教务处抢课开始哦~");
                                        b.putInt("code",201);
                                        msg.setData(b);
                                        handler.sendMessage(msg);
                                        setTime();
                                    }else{
                                        qk=new QK_Core(user_zf,Start_time);
                                        qk.setScls(selected_classes);
                                        qk.setListenner(new QK_Core.Listenner() {
                                            Bundle b=new Bundle();
                                            Message msg=new Message();
                                            @Override
                                            public void setOnStart(User user_zf) {
                                                handler_sendMsg("QK_Core","开始监听抢课...",Console_Text.BLUE);
                                               handler_sendMsg("QK_Core","当前用户:"+user_zf.user,Console_Text.BLUE);

                                            }
                                            @Override
                                            public void setOnRun(SimpleClassXK c, String res) {
                                                handler_sendMsg("QK-Core",res,Console_Text.BLUE);
                                            }

                                            @Override
                                            public void setOnEnd(int num) {
                                                handler_sendMsg("QK-Core","当前剩余量:"+(num),Console_Text.BLUE);
                                            }

                                            @Override
                                            public void setOnDataChanged(int type, String msg) {
                                                handler_sendMsg("QK-Core",msg,Console_Text.BLUE);
                                            }

                                            @Override
                                            public void setOnError(Exception e, String msg) {
                                                handler_sendMsg("QK-Core",msg,Console_Text.RED);
                                            }
                                        });
                                        qk.start();
                                        bt.setText("停止抢课");

                                    }

                                }
                            }else{
                                    //停止抢课
                                    bt.setText("开始抢课");
                                    handler_sendMsg("QK-Core","抢课已停止",Console_Text.BLUE);
                                    if(qk!=null){
                                        qk.setRUN_FLAG(0);
                                    }
                        }
                        }
                    });

                    }
                });


    }
public void handler_sendMsg(String head,String data,String color){
    Bundle b=new Bundle();
    Message msg=new Message();
    b.putString("msg",data);
    b.putString("head",head);
    b.putInt("code",200);
    b.putString("color",color);
    msg.setData(b);
    handler.sendMessage(msg);
}
    public void setTime(){
        Calendar ca = Calendar.getInstance();
        int  mYear = ca.get(Calendar.YEAR);
        int  mMonth = ca.get(Calendar.MONTH);
        int  mDay = ca.get(Calendar.DAY_OF_MONTH);
        int hour=ca.get(Calendar.HOUR);
        int min=ca.get(Calendar.MINUTE);


       //设置日期
        DatePickerDialog datePickerDialog = new DatePickerDialog(con,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        //设置时间
                        TimePickerDialog timePicker = new TimePickerDialog(con, new TimePickerDialog.OnTimeSetListener() {
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                Start_time_text=String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(dayOfMonth)+" "+String.valueOf(hourOfDay)+":"+String.valueOf(minute)+":00";
                                time_view.setText("教务处抢课时间:"+Start_time_text+"(点击设置)");
                                SharedPreferences.Editor editor = sp.edit();
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                try {
                                    Date tmp_d=formatter.parse(Start_time_text);
                                    Start_time=tmp_d.getTime();
                                    editor.putString("time",String.valueOf(Start_time));
                                    editor.commit();
                                    console_adpater.add_data(new Console_Text("Time_SET","当前时间已修改为"+Start_time_text,Console_Text.ORANGE));
                                    if (qk != null) {
                                        qk.setStartTime(Start_time);
                                    }

                                } catch (ParseException e) {
                                    console_adpater.add_data(new Console_Text("Time_SET","时间保存错误"+Start_time_text,Console_Text.RED));
                                }


                            }
                        }, hour, min, true);
                        timePicker.show();
                    }
                },
                mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    //更新主页数据的函数
    public void setHome(int position){
        if(position==0){
            System.out.println("滑动1");
            View view=viewpager.getChildAt(position);
            TextView t1=view.findViewById(R.id.t1);
            if(t1!=null) {
                time_view=view.findViewById(R.id.time);
                Start_time=Long.parseLong(sp.getString("time","0"));
                System.out.println(Start_time);
                if(Start_time!=0) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Start_time_text= simpleDateFormat.format(Start_time);
                    time_view.setText("教务处抢课时间:"+Start_time_text+"(点击设置)");
                }else{
                    time_view.setText("教务处抢课时间:暂未设置(点击本文字设置)");
                    console_adpater.add_data(new Console_Text("Time_SET","未解析到时间",Console_Text.ORANGE));
                }
                System.out.println("t1不为null");
                if(selected_classes.size()>0){
                    t1.setText("");
                    System.out.println("有数据，标识取消");
                }
                ListView selected_view=view.findViewById(R.id.selected);
                ListView console_view=view.findViewById(R.id.console);
                selected_adpater=new Selected_ListView_Adapter(con,selected_classes);
                console_view.setAdapter(console_adpater);
                selected_view.setAdapter(selected_adpater);
            }
        }

    }
class GET_DATA_VIEW_HTTP extends Thread{
    @Override
    public void run() {
        Bundle b=new Bundle();
        Message msg=new Message();
        b.putInt("code",201);
        b.putString("msg","正在更新数据，请稍等一下下~");
        msg.setData(b);
        handler.sendMessage(msg);
        ZF zf=new ZF();
        user_zf=new User();
        user_zf.user=sp.getString("user","");

        user_zf.pwd=sp.getString("pwd","");
        user_zf.URL=sp.getString("url","http://218.197.80.10");
        user_zf.cookie=sp.getString("cookie","");

        zf.getHomeDataXK(user_zf);
        CourseXK[] cs=zf.getCoursesXKByHomeData(null);

        if(cs==null){
            user_zf=zf.Login(user_zf.user, user_zf.pwd);
            zf.getHomeDataXK(user_zf);
            cs=zf.getCoursesXKByHomeData(null);

        }
        for(int i=0;i<cs.length;i++){
            if(i==2){
                b.putInt("code",201);
                b.putString("msg","当前板块(通识必修课)数据量较大，请稍等几分钟~");
                msg=new Message();
                msg.setData(b);
                handler.sendMessage(msg);
            }
            SimpleClassXK[] scls=zf.getAllClassesXK(user_zf,cs[i],1,200);
            views.add(new Home_Sub_View(new DATA_PACK_VIEW(2,cs[i],scls),selected_classes));


            handler_sendMsg("ZFJAVA","("+cs[i].name+")板块数据加载完毕",null);


        }
    }
}

}
