package com.zerostic.androiddevelopment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.zerostic.androiddevelopment.Data.DataBaseHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class SecondActivity extends AppCompatActivity {
    ListView _dynamic;
    Button button;
    ArrayList<HashMap<String, String>> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        _dynamic = findViewById(R.id._dynamic);
        data = new ArrayList<>();
        button = findViewById(R.id.button);
        button.setOnClickListener(v ->{
            startJsonParsing();
        });
    }

    private void startJsonParsing() {
        try{
            JSONObject obj = new JSONObject(Objects.requireNonNull(loadJSONFromAsset()));
            JSONArray jsonArray = obj.getJSONArray("rootnode");
            for (int i = 0; i < jsonArray.length(); i++){
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int id = jsonObject.getInt("id");
                    String name = jsonObject.getString("name");
                    int age = jsonObject.getInt("age");
                    addDataToHashMap(id, name, age);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        ListAdapter adapter = new SimpleAdapter(SecondActivity.this, data, R.layout.list_item, new String[]{"id", "name", "age"}, new int[]{R.id.id, R.id.name, R.id.age});
        _dynamic.setAdapter(adapter);
    }

    private void addDataToHashMap(int id, String name, int age) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", String.valueOf(id));
        hashMap.put("name", name);
        hashMap.put("age", String.valueOf(age));
        data.add(hashMap);
    }

    private String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String (buffer, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return json;
    }
}