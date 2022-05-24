package com.example.agam;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.agam.ui.login.LoginActivity;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


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
    private EditText searchField;

    private final DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
   // private DatabaseReference mUsersDatabase;


    private final FirebaseDatabase db = FirebaseDatabase.getInstance();

    private String shortId;

    ArrayList<String> dataList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;

        FirebaseAuth mAuth;
        FirebaseFirestore mStore;

    FirebaseDatabase rootNode;
    DatabaseReference reference;



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            //mUsersDatabase = FirebaseDatabase.getInstance().getReference("users");

            btnLogout = findViewById(R.id.btnLogout);
            list1 = findViewById(R.id.list1);
            addChat = findViewById(R.id.addChat);
            idDisplay = findViewById(R.id.idDisplay);
            searchField = findViewById(R.id.searchField);




            mAuth = FirebaseAuth.getInstance();
            mStore = FirebaseFirestore.getInstance();


            //dataList.add("string 1");
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);


            list1.setAdapter(arrayAdapter);

            list1.setOnItemClickListener(this);
            btnLogout.setOnClickListener(this);


            searchField.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    MainActivity.this.arrayAdapter.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

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
        //startActivity(new Intent(MainActivity.this, LoginActivity.class));
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }else {


            userId = mAuth.getCurrentUser().getUid();
            reference = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String userUniqId = snapshot.child("userUniqId").getValue().toString();
                    String userEmail = snapshot.child("userEmail").getValue().toString();
                    shortId = userUniqId;
                    idDisplay.setText(userUniqId);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });





//            DocumentReference documentReference = mStore.collection("users").document(userId);
//            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
//                @Override
//                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                    idDisplay.setText(documentSnapshot.getString("uniqID "));
//                    shortId = (documentSnapshot.getString("uniqID "));
//                }
//            });
            reference = FirebaseDatabase.getInstance().getReference().child("chats");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                    Set<String> set = new HashSet<String>();
                    Iterator i = datasnapshot.getChildren().iterator();

                    while (i.hasNext()){
                        set.add(((DataSnapshot)i.next()).getKey());
                    }
                    dataList.clear();
                    dataList.addAll(set);

                    arrayAdapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

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
//            String editedCode = code.getText().toString();
//            dataList.add(editedCode);
//            list1.setAdapter(arrayAdapter);
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("chats");

            Map<String,Object> map = new HashMap<String, Object>();
            map.put(code.getText().toString(),"");
            reference.updateChildren(map);
            Chat chat = new Chat();

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
        intent.putExtra("user_id", userId);
        intent.putExtra("user_short_id", shortId);
        intent.putExtra("room_name",((TextView)view).getText().toString() );
        startActivity(intent);
        //when item is clicked in the list -> opens a new chat page//

    }

}
