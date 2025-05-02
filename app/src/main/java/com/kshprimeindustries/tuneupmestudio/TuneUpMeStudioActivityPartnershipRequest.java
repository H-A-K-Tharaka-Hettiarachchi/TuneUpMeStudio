package com.kshprimeindustries.tuneupmestudio;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kshprimeindustries.tuneupmestudio.adapter.TuneUpMeStudioPartnershipRequestItemAdapter;
import com.kshprimeindustries.tuneupmestudio.model.TuneUpMeStudioPartnershipRequestItem;

import java.util.ArrayList;

public class TuneUpMeStudioActivityPartnershipRequest extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRecycler();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tune_up_me_studio_partnership_request);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


    }

    private void loadRecycler() {

        RecyclerView recyclerViewPartnershipRequest = findViewById(R.id.recyclerViewPartnershipRequest);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewPartnershipRequest.setLayoutManager(linearLayoutManager);

        ArrayList<TuneUpMeStudioPartnershipRequestItem> tuneUpMeStudioPartnershipRequestItemArrayList = new ArrayList<>();

        TuneUpMeStudioPartnershipRequestItemAdapter tuneUpMeStudioPartnershipRequestItemAdapter = new TuneUpMeStudioPartnershipRequestItemAdapter(tuneUpMeStudioPartnershipRequestItemArrayList);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("band_req_status")
                .whereEqualTo("status", "pending")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);

                                firebaseFirestore.collection("band_request")
                                        .whereEqualTo("band_req_status_id", documentSnapshot.getId())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @SuppressLint("NotifyDataSetChanged")
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    QuerySnapshot querySnapshot = task.getResult();
                                                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                                        for (DocumentSnapshot documentSnapshot : querySnapshot) {

                                                            tuneUpMeStudioPartnershipRequestItemArrayList.add(new TuneUpMeStudioPartnershipRequestItem(
                                                                    documentSnapshot.getId(),
                                                                    documentSnapshot.getString("name"),
                                                                    documentSnapshot.getString("email"),
                                                                    documentSnapshot.getString("mobile")
                                                            ));

                                                        }
                                                        tuneUpMeStudioPartnershipRequestItemAdapter.notifyDataSetChanged();
                                                    }
                                                }
                                            }
                                        });

                            }
                        }
                    }
                });


        recyclerViewPartnershipRequest.setAdapter(tuneUpMeStudioPartnershipRequestItemAdapter);

    }

}