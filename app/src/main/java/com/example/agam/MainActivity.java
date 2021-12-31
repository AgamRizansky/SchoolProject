package com.example.agam;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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


import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;


public class MainActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ImageView addChat;
    private EditText code;
    private ListView list1;
    private Button addChat1;
    private Dialog d;
    private Button btnDialogCode;
    private Button btnLogout;
    private TextView idDisplay;
    private String userId;

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    //private DatabaseReference usersDb = db.getInstance().getReference("Users:");


    ArrayList<String> dataList = new ArrayList<>();
    ArrayAdapter<String> adapter1;

        FirebaseAuth mAuth;
        FirebaseFirestore mStore;

       DatabaseReference usersDb;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);


            btnLogout = findViewById(R.id.btnLogout);
            list1 = findViewById(R.id.list1);
            addChat = findViewById(R.id.addChat);
            idDisplay = findViewById(R.id.idDisplay);

            mAuth = FirebaseAuth.getInstance();
            mStore = FirebaseFirestore.getInstance();


            //dataList.add("string 1");
            adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);

            list1.setAdapter(adapter1);

            list1.setOnItemClickListener(this);
            btnLogout.setOnClickListener(this);


        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
                int d = item.getItemId();
                if(d==R.id.logout_btn){
                    signOutUser();
                }
                return true;
    }



    @Override
    protected void onStart() {
            //checks if there is an account logged in://
            // if positive --> showing the mainActiviy page.//
            //if negetive --> showing the login page.//
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }else {


            userId = mAuth.getCurrentUser().getUid();

            DocumentReference documentReference = mStore.collection("users").document(userId);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    idDisplay.setText(documentSnapshot.getString("uniqID "));
                }
            });
        }
    }

    public void createLoginDialog(){//opens the dialog for adding chats//
            d = new Dialog(this);
            d.setContentView(R.layout.chat_dialog);
            d.setTitle("Add Chat");
            d.setCancelable(true);
            code = (EditText)d.findViewById(R.id.code);
            btnDialogCode = (Button)d.findViewById(R.id.btnDialogCode);
            btnDialogCode.setOnClickListener(this);
            d.show();

        }


    @Override
    public void onClick(View view) {  //checks when buttons clicked//
        if (view == addChat){//opens dialog//
            createLoginDialog();

        }

        else if (view == btnDialogCode){
            String editedCode = code.getText().toString();
            dataList.add(editedCode);
            list1.setAdapter(adapter1);
            d.hide();
        }

        else if (view == btnLogout){//enabling the logOut button//
            idDisplay.setText("");
            if (mAuth != null){
                signOutUser();
                //startActivity(new Intent(MainActivity.this, LoginActivity.class));
                System.exit(0);
            }

        }


    }

    private void signOutUser() {
        try {
            mAuth.signOut();
            Toast.makeText(this, "User Sign out!", Toast.LENGTH_SHORT).show();
        }catch (Exception e) {
            Log.e("SIGN OUT", "onClick: Exception "+e.getMessage(),e );
        }

    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
           //dataList.get(position);
        Intent intent = new Intent(this,ChatActivity.class);
        startActivity(intent);
        //when item is clicked in the list -> opens a new chat page//

    }
}
