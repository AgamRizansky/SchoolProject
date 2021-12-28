package com.example.agam;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.agam.ui.login.LoginActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;


public class MainActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ImageView addChat;
    private EditText code;
    private ListView list1;
    private Button addChat1;
    private Dialog d;
    private Button btnDialogCode;
    private Button btnLogout;

    ArrayList<String> dataList = new ArrayList<>();
    ArrayAdapter<String> adapter1;

        FirebaseAuth mAuth;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            //Intent intent2 = new Intent(this, RegisterActivity.class);
            //startActivity(intent2);

            btnLogout = findViewById(R.id.btnLogout);
            list1 = findViewById(R.id.list1);
            addChat = findViewById(R.id.addChat);

            mAuth = FirebaseAuth.getInstance();

            //dataList.add("string 1");
            adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);

            list1.setAdapter(adapter1);

            list1.setOnItemClickListener(this);

            btnLogout.setOnClickListener(view ->{
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                //enabling the logOut button//
            });
        }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){

            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
        //checks if there is an account logged in://
        // if positive --> showing the mainActiviy page.//
        //if negetive --> showing the login page.//
    }

    public void createLoginDialog(){
            d = new Dialog(this);
            d.setContentView(R.layout.chat_dialog);
            d.setTitle("Add Chat");
            d.setCancelable(true);
            code = (EditText)d.findViewById(R.id.code);
            btnDialogCode = (Button)d.findViewById(R.id.btnDialogCode);
            btnDialogCode.setOnClickListener(this);
            d.show();
            //opens the dialog for adding chats//
        }


    @Override
    public void onClick(View view) {
        //String editedCode = code.getText().toString();
        if (view == addChat){
            createLoginDialog();
            //Toast.makeText(this, "clicked +", Toast.LENGTH_SHORT).show();
            //the line above is for test if the + button is clickable//
        }
        else if (view == btnDialogCode){
            String editedCode = code.getText().toString();
            dataList.add(editedCode);
            list1.setAdapter(adapter1);
            d.hide();
        }
    //checks when buttons clicked//
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
           // dataList.get(position);
        Intent intent = new Intent(this,ChatActivity.class);
        startActivity(intent);
        //when item is clicked in the list -> opens a new chat page//

    }
}
