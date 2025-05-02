package com.kshprimeindustries.tuneupmestudio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.kshprimeindustries.tuneupmestudio.model.SQLiteHelper;

import es.dmoral.toasty.Toasty;

public class TuneUpMeStudioActivityAdminLogIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tune_up_me_studio_admin_log_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }

        EditText logUserNameEditeText = findViewById(R.id.logUserNameEditeText);
        EditText logPasswordEditeText = findViewById(R.id.logPasswordEditeText);

        Button logInButton = findViewById(R.id.logInButton);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = logUserNameEditeText.getText().toString();
                String password = logPasswordEditeText.getText().toString();

                if (username.isEmpty() || username.trim().isEmpty()) {
                    Toasty.error(TuneUpMeStudioActivityAdminLogIn.this, "Please Enter Your User Name.", Toast.LENGTH_LONG, true).show();
                } else if (password.isEmpty() || password.trim().isEmpty()) {
                    Toasty.error(TuneUpMeStudioActivityAdminLogIn.this, "Please Enter Your Password.", Toast.LENGTH_LONG, true).show();
                } else {

                    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                    firebaseFirestore.collection("admin")
                            .where(
                                    Filter.and(
                                            Filter.equalTo("username", username),
                                            Filter.equalTo("password", password)
                                    )
                            ).get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        QuerySnapshot querySnapshot = task.getResult();
                                        if (querySnapshot != null && !querySnapshot.isEmpty()) {

                                            DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);

                                            Toasty.success(TuneUpMeStudioActivityAdminLogIn.this, "Log In Success.", Toast.LENGTH_SHORT, true).show();

                                            SharedPreferences sharedPreferences = getSharedPreferences("com.kshprimeindustries.tuneupmestudio.data", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putBoolean("logged", true);
                                            editor.putString("id", documentSnapshot.getId());
                                            editor.apply();

                                            SQLiteHelper sqLiteHelper = new SQLiteHelper(TuneUpMeStudioActivityAdminLogIn.this, "TuneUpMeStudio", null, 1);
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    SQLiteDatabase sqLiteDatabase = sqLiteHelper.getWritableDatabase();
                                                    sqLiteDatabase.execSQL("INSERT INTO admin (username, name, mobile, password)" +
                                                            "VALUES (" +
                                                            "'"+documentSnapshot.getString("username")+"', " +
                                                            "'"+documentSnapshot.getString("name")+"', " +
                                                            "'"+documentSnapshot.getString("mobile")+"', " +
                                                            "'"+documentSnapshot.getString("password")+"'" +
                                                            ");");
                                                }
                                            }).start();

                                            Intent intent = new Intent(TuneUpMeStudioActivityAdminLogIn.this, TuneUpMeStudioActivityHome.class);
                                            startActivity(intent);
                                            finish();


                                        } else {
                                            Toasty.error(TuneUpMeStudioActivityAdminLogIn.this, "Invalid Log In Credentials.", Toast.LENGTH_LONG, true).show();
                                        }
                                    }
                                }
                            });

                }

            }
        });

    }
}