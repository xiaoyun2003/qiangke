package com.xiaoyun.qiangke;

import android.os.Looper;

import com.xiaoyun.ZF.SimpleClassXK;
import com.xiaoyun.ZF.User;
import com.xiaoyun.ZF.ZF;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

//抢课核心算法1.0
public class QK_Core extends Thread{
    int RUN_FLAG=1;
    public ArrayList<SimpleClassXK> scls=new ArrayList<>();
    public long start_time=0;
    public User user_zf;
    public Listenner l=new Listenner() {
        @Override
        public void setOnStart(User user_zf) {
            System.out.println("用户"+user_zf.user+"初始化中...");
        }

        @Override
        public void setOnRun(SimpleClassXK c, String res) {
            System.out.println(res);
            System.out.println(c.kcmc);
        }

        @Override
        public void setOnEnd(int num) {
            System.out.println(1);
        }

        @Override
        public void setOnDataChanged(int type, String msg) {
            System.out.println("数据改变:"+type+":"+msg);
        }

        @Override
        public void setOnError(Exception e, String msg) {
            System.out.println(msg);
        }
    };
    public QK_Core(User user_zf, long start_time){
        this.start_time=start_time;
        this.user_zf=user_zf;
    }

    public boolean reLogin(ZF zf){
        //重登
        user_zf=zf.Login(user_zf.user, user_zf.pwd);
        if(user_zf.LoginCode==200){
            l.setOnDataChanged(0,"用户"+user_zf.user+"重登完毕,状态码200");
        }else{
            l.setOnError(new Exception("用户"+user_zf.user+"无法重登"),user_zf.LoginMsg);
            l.setOnEnd(scls.size());
            return false;
        }
        return true;
    }

    public void qk(ZF zf){
        //开始并发抢课
        for(int i=0;i<scls.size();i++){
            try{
            SimpleClassXK s=scls.get(i);
            String mc="";
            try {
                String[] kclist = s.jxbmc.split("-");
                mc = kclist[kclist.length - 1];
            }catch (Exception e){
                l.setOnError(e,s.kcmc+"名称解析失败");
            }
            l.setOnRun(s,"正在抢"+s.kcmc+"("+mc+")"+"...");
            String res=zf.selectClass(user_zf,s);
            if(!res.isEmpty() && !res.contains("flag\":\"0\"}")){
                l.setOnRun(s,"已抢中"+s.kcmc+"("+mc+")");
                scls.remove(s);
            }else{
                l.setOnRun(s,res);
            }
        }catch(Exception e){
                l.setOnError(e,"抢课错误");
            }
        }
    }

    public void setRUN_FLAG(int flag){
        this.RUN_FLAG=flag;
    }

    public void run(){
        //调用开始函数
        Looper.prepare();
        l.setOnStart(user_zf);
        if(user_zf.user==null  || user_zf.pwd==null || user_zf.user.isEmpty() || user_zf.pwd.isEmpty()){
            l.setOnError(new Exception("用户名和密码不能为空"),"用户名和密码不能为空");
            l.setOnEnd(scls.size());
            return ;
        }
        ZF zf=new ZF();
        //初始化数据
        if(!zf.getHomeDataXK(user_zf)){
            if(!reLogin(zf)){
                return ;
            }
        }
        //开始执行
        while(RUN_FLAG==1){
            long time=System.currentTimeMillis();
            //死循环
            if(scls.size()>0 && start_time!=0){
                long jg=start_time-time;
                //大于-5秒执行
                if(jg>=-10000 || scls.size()>0){
                    //大于12小时
                        try {
                            if(jg<=3000){
                                l.setOnRun(null,"开始冲刺抢课,冲啊!!!!!!");
                                qk(zf);
                            }
                            if(jg>5000 && jg<=10000){
                                l.setOnRun(null,"阻塞等待2s");
                                if(!zf.getHomeDataXK(user_zf)){
                                    if(!reLogin(zf)){
                                        return ;
                                    }
                                }
                                qk(zf);
                                Thread.sleep(2000);
                                continue;
                            }
                            if(jg>20*1000 && jg<=40*1000){
                                l.setOnRun(null,"阻塞等待10s");
                                if(!zf.getHomeDataXK(user_zf)){
                                    if(!reLogin(zf)){
                                        return ;
                                    }
                                }
                                qk(zf);
                                Thread.sleep(10*1000);
                                continue;
                            }



                        } catch (InterruptedException e) {
                            l.setOnError(e,"time sleep error");
                        }

                        System.out.println("线程运行..."+jg);
                }else{
                    l.setOnEnd(scls.size());
                    break;
                }
            }
        }
        l.setOnEnd(scls.size());
        Looper.loop();
    }

    public int getNum(){
        return scls.size();
    }
    public void addSimpleClassXK(SimpleClassXK c){
        scls.add(c);
        l.setOnDataChanged(2,"选课数据改变:新增加课程:"+c.kcmc);
    }
    public void setScls(ArrayList<SimpleClassXK> s){
        for(int i=0;i<s.size();i++){
            scls.add(s.get(i));
        }
    }
    //回调函数
    public void setListenner(Listenner l){
     this.l=l;
    }

    public void setStartTime(long start_time){
        this.start_time=start_time;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String data = simpleDateFormat.format(start_time);
        l.setOnDataChanged(1,"时间改变:当前时间:"+(data));
    }
    public interface Listenner{
        //启动程序时执行
        public void setOnStart(User user_zf);
        //每次执行都会回调
        public void setOnRun(SimpleClassXK c,String res);
        //结束时执行
        public void setOnEnd(int num);
        //数据发生改变时调用
        public void setOnDataChanged(int type,String msg);
        //发生错误时调用
        public void setOnError(Exception e,String msg);

    }

}
