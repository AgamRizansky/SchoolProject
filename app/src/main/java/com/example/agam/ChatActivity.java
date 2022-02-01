package com.example.agam;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_send_msg;
    private EditText input_msg;
    private TextView chat_conversation;

    private final int key = 1;

    private String user_id,room_name,short_id;
    private DatabaseReference root ;
    private String temp_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        btn_send_msg = findViewById(R.id.btn_send);
        input_msg = findViewById(R.id.msg_input);
        chat_conversation = findViewById(R.id.textView);

        short_id = getIntent().getExtras().get("user_short_id").toString();
        user_id = getIntent().getExtras().get("user_id").toString();
        room_name = getIntent().getExtras().get("room_name").toString();
        setTitle(" Room - "+room_name);

        root = FirebaseDatabase.getInstance().getReference().child(room_name);

        btn_send_msg.setOnClickListener(this);

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    String encrypted = "";
    @Override
    public void onClick(View view) {
        if (view == btn_send_msg) {
            Map<String, Object> map = new HashMap<String, Object>();
            temp_key = root.push().getKey();
            root.updateChildren(map);

            DatabaseReference message_root = root.child(temp_key);
            Map<String, Object> map2 = new HashMap<String, Object>();

            char[] letters = input_msg.getText().toString().toCharArray();
                for (char c : letters){
                    c+= key;
                    encrypted += c;

                }

            //map2.put("id", user_id);
            map2.put("encrypt_msg", encrypted);
            //map2.put("msg", input_msg.getText().toString());
            map2.put("shortId", short_id);
            message_root.updateChildren(map2);
            encrypted = "";
            input_msg.setText("");
        }
    }

    private String chat_msg,chat_user_name;
    private String encrypt_msg = "";

    private void append_chat_conversation(DataSnapshot dataSnapshot){
        Iterator i = dataSnapshot.getChildren().iterator();

        while (i.hasNext()){
            chat_msg = (String) ((DataSnapshot)i.next()).getValue();
           //encrypt_msg = (String) ((DataSnapshot)i.next()).getValue();
            chat_user_name = (String) ((DataSnapshot)i.next()).getValue();

            char[] to_text = chat_msg.toCharArray();
                for (char b : to_text){
                    b -=key;
                    encrypt_msg +=b;
                }



            //chat_conversation.append(chat_msg +" : "+chat_user_name +" \n");
            chat_conversation.append(chat_user_name +" : "+ encrypt_msg +" \n");
                encrypt_msg = "";
        }
    }


}