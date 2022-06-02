package com.example.agam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    TextInputEditText etRegEmail;
    TextInputEditText etRegPassword;
    TextView tvLoginHere;
    Button btnRegister;

    String userID;
    String uniqID; //id that used like nicknames//

    FirebaseAuth mAuth;
    FirebaseFirestore mStore;



    FirebaseDatabase rootNode;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etRegEmail = findViewById(R.id.etRegEmail);
        etRegPassword = findViewById(R.id.etRegPass);
        tvLoginHere = findViewById(R.id.tvLoginHere);
        btnRegister = findViewById(R.id.btnRegister);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        btnRegister.setOnClickListener(view -> {
            createUser();
        });

        tvLoginHere.setOnClickListener(view -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });
    }

    private void createUser() {
        String email = etRegEmail.getText().toString();
        String password = etRegPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            etRegEmail.setError("Email cannot be empty");
            etRegEmail.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            etRegPassword.setError("Password cannot be empty");
            etRegPassword.requestFocus();
        } else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                       Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                       userID = mAuth.getCurrentUser().getUid();
                       uniqID = createUniqID(10); //"123456789";
                        rootNode = FirebaseDatabase.getInstance();
                        reference = rootNode.getReference("users");
                        String userEmail = email.toString();
                        String userId = userID.toString();
                        String userUniqId = uniqID.toString();

                        Users helperClass = new Users(userEmail, userId,userUniqId);

                        reference.child(userId).setValue(helperClass);




//                        DocumentReference documentReference = mStore.collection("users").document(userID);
//                        Map<String,Object> user = new HashMap<>();
//                        user.put("ID " , userID);
//                        user.put("email " , email);
//                        user.put("uniqID " , uniqID);
//                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                Log.d(TAG, "onSuccess: user profile is created for "+ userID);
//                            }
//                        });



//                        usersDb.setValue(email);
//                        HashMap<String, String> userMap = new HashMap<>();
//                        userMap.put("Email ", email);
//                        userMap.put("Public ID ", uniqID);
//                        usersDb.push().setValue(userMap);


                       startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    }else{
                        Toast.makeText(RegisterActivity.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                private String createUniqID(int length) {
                    String str = "";
                    Random rand = new Random();
                    for (int i =0; i< length; i++){
                        str += "" + (rand.nextInt(9)+1);
                    }
                    return str;
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.login_btn:
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
