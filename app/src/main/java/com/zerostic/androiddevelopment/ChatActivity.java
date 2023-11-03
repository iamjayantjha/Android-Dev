package com.zerostic.androiddevelopment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity {
    List<Message> messages;
    RecyclerView recycler_view;
    MessageAdapter messageAdapter;
    EditText userMessage;
    ImageButton sendMessage;

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(120, TimeUnit.SECONDS)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        recycler_view = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recycler_view.setLayoutManager(linearLayoutManager);
        userMessage = findViewById(R.id.userMessage);
        sendMessage = findViewById(R.id.sendMessage);
        messages = new ArrayList<>();
        messageAdapter = new MessageAdapter(messages);
        recycler_view.setAdapter(messageAdapter);
        sendMessage.setOnClickListener(v -> {
            String message = userMessage.getText().toString();
            if (!message.isEmpty()) {
                messages.add(new Message(message, Message.SENT_BY_USER));
                messageAdapter.notifyDataSetChanged();
                userMessage.setText("");
                callAPI(message);
            }
        });
    }

    private void callAPI(String message) {
        messages.add(new Message("Thinking...", Message.SENT_BY_BOT));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("model","gpt-3.5-turbo");
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("role","user");
            jsonObject1.put("content",message);
            jsonArray.put(jsonObject1);
            jsonObject.put("messages",jsonArray);
        }catch (Exception e){
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(jsonObject.toString(),JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .addHeader("Authorization", "Bearer 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")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addToChat("Something went wrong! "+e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    String res = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        String text = jsonObject.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
                        addToChat(text.trim());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else {
                    addToChat("Something went wrong! "+response.message());
                }
            }
        });
    }

    private void addToChat(String msg) {
        messages.remove(messages.size()-1);
        runOnUiThread(() -> {
            messages.add(new Message(msg, Message.SENT_BY_BOT));
            messageAdapter.notifyDataSetChanged();
            recycler_view.smoothScrollToPosition(messageAdapter.getItemCount());
        });
    }
}
