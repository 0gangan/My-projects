package com.lzg.homework_3;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MenuXmlActivity extends AppCompatActivity {

    private TextView testText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_test);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // ⚠️ 必须绑定 Toolbar

        testText = findViewById(R.id.testText);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // 字体大小
        if (id == R.id.small) {
            testText.setTextSize(10);
            return true;
        } else if (id == R.id.medium) {
            testText.setTextSize(16);
            return true;
        } else if (id == R.id.large) {
            testText.setTextSize(20);
            return true;
        }

        // 普通菜单
        else if (id == R.id.common) {
            Toast.makeText(this, "你点击了普通菜单项", Toast.LENGTH_SHORT).show();
            return true;
        }

        // 字体颜色
        else if (id == R.id.red) {
            testText.setTextColor(Color.RED);
            return true;
        } else if (id == R.id.black) {
            testText.setTextColor(Color.BLACK);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
