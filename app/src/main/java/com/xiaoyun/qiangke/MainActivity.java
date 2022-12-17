package com.xiaoyun.qiangke;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.xiaoyun.ZF.User;
import com.xiaoyun.ZF.ZF;

public class MainActivity extends AppCompatActivity {
     public EditText user_edit;
     public EditText pwd_edit;
     User user_zf;
    SharedPreferences sp;
    Context con;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler()
    {
        // 这个方法是在主线程里面执行的
        @Override
        public void handleMessage(android.os.Message msg) {
            Bundle b=msg.getData();
            Toast.makeText(MainActivity.this, b.getString("toast"), Toast.LENGTH_LONG).show();
            if(b.getInt("code")==200){
                //保存账号和密码，以便下次直接使用
                if(sp!=null){
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("user", user_zf.user);
                    editor.putString("pwd", user_zf.pwd);
                    editor.putString("url", user_zf.URL);
                    editor.putString("cookie", user_zf.cookie);
                    editor.commit();
                    //跳转界面
                    Intent intent = new Intent(con, Home.class);
                    startActivity(intent);

                }else{
                    Toast.makeText(MainActivity.this, "自动保存异常!", Toast.LENGTH_LONG).show();
                }

            }
        }
    };
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.forget_pwd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://218.197.80.13/jwglxt/xtgl/login_slogin.html");
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
            }
        });
        findViewById(R.id.login).setOnClickListener(new Login_click());
        user_edit=findViewById(R.id.user);
        pwd_edit=findViewById(R.id.pwd);
        sp=getSharedPreferences("settings",MODE_PRIVATE);
        user_edit.setText(sp.getString("user",""));
        pwd_edit.setText(sp.getString("pwd",""));
        con=this;

        //test
        //Intent intent = new Intent(con, Home.class);
        //startActivity(intent);

    }
    //绑定登录事件
    class Login_click implements Button.OnClickListener{
        @Override
        public void onClick(View view) {
            String user=user_edit.getText().toString();
            String pwd=pwd_edit.getText().toString();
            new Thread(){
            public void run() {
                Bundle b=new Bundle();
                if (pwd.isEmpty() || user.isEmpty()) {
                    b.putInt("code",-1);
                    b.putString("toast","用户名和密码不可以为空哦~");
                } else {
                    ZF zf = new ZF();
                    user_zf = zf.Login(user, pwd);
                    if (user_zf.LoginCode == 200) {
                        b.putInt("code",200);
                        b.putString("toast","登陆成功");
                    } else {
                        b.putInt("code",-1);
                        b.putString("toast","登陆失败,"+user_zf.LoginMsg);
                    }
                }
                Message msg=new Message();
                msg.setData(b);
                handler.sendMessage(msg);
            }}.start();

        }
    }




}

