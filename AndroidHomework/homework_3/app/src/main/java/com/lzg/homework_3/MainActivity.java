package com.lzg.homework_3;


import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;

import java.util.*;

public class MainActivity extends AppCompatActivity {

    private String[] names = {"Lion", "Tiger", "Monkey", "Dog", "Cat", "Elephant"};
    private int[] images = {
            R.drawable.lion,
            R.drawable.tiger,
            R.drawable.monkey,
            R.drawable.dog,
            R.drawable.cat,
            R.drawable.elephant
    };

    private static final String CHANNEL_ID = "animal_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


      //   initAnimalList();   // 👉 显示动物列表界面
//         showLoginDialog();  // 👉 显示自定义登录对话框
 //       startActivity(new Intent(this, MenuXmlActivity.class));
       startActivity(new Intent(this, ActionModeActivity.class));
    }


    // ---------------------------------------------------------
    // ① 封装：动物列表界面
    // ---------------------------------------------------------
    private void initAnimalList() {
        ListView listView = findViewById(R.id.listView);

        List<Map<String, Object>> data = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("image", images[i]);
            item.put("name", names[i]);
            data.add(item);
        }

        SimpleAdapter adapter = new SimpleAdapter(
                this,
                data,
                R.layout.list_item,
                new String[]{"image", "name"},
                new int[]{R.id.animal_image, R.id.animal_name}
        );

        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selected = names[position];
            Toast.makeText(MainActivity.this, "You selected: " + selected, Toast.LENGTH_SHORT).show();

            // 点击后发送通知
            sendNotification(selected);
        });
    }


    // ---------------------------------------------------------
    // ② 封装：自定义登录弹窗
    // ---------------------------------------------------------
    private void showLoginDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.login_dialog, null);

        EditText etUsername = dialogView.findViewById(R.id.etUsername);
        EditText etPassword = dialogView.findViewById(R.id.etPassword);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setTitle("ANDROID APP")
                .setPositiveButton("Sign in", (dialog, which) -> {
                    String user = etUsername.getText().toString();
                    String pwd = etPassword.getText().toString();
                    Toast.makeText(this, "账号：" + user + "\n密码：" + pwd, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null);

        builder.create().show();
    }


    // ---------------------------------------------------------
    // ③ 封装：通知逻辑
    // ---------------------------------------------------------
    private void sendNotification(String animalName) {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Animal Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            manager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(animalName)
                .setContentText("You clicked on " + animalName)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        manager.notify(1, builder.build());
    }
}
