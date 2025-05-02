package com.kshprimeindustries.tuneupmestudio;

import static com.kshprimeindustries.tuneupmestudio.TuneUpMeStudioApplication.ngrock_url;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TuneUpMeStudioActivityPartnershipRequestItemView extends AppCompatActivity {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tune_up_me_studio_partnership_request_item_view);
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

        Bundle bundle = new Bundle();

        TextView textViewPartnershipRequestItemViewBandName = findViewById(R.id.textViewPartnershipRequestItemViewBandName);
        TextView textViewPartnershipRequestItemViewBandType = findViewById(R.id.textViewPartnershipRequestItemViewBandType);
        TextView textViewPartnershipRequestItemViewCustomerName = findViewById(R.id.textViewPartnershipRequestItemViewCustomerName);
        TextView textViewPartnershipRequestItemViewBandEmail = findViewById(R.id.textViewPartnershipRequestItemViewBandEmail);
        TextView textViewPartnershipRequestItemViewBandMobile = findViewById(R.id.textViewPartnershipRequestItemViewBandMobile);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("band_request")
                .document(Objects.requireNonNull(getIntent().getStringExtra("itemId")))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshotBandRequest = task.getResult();
                            if (documentSnapshotBandRequest.exists()) {
                                firebaseFirestore.collection("band_type")
                                        .document(Objects.requireNonNull(documentSnapshotBandRequest.getString("band_type_id")))
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot documentSnapshotBandType = task.getResult();
                                                    if (documentSnapshotBandType.exists()) {
                                                        firebaseFirestore.collection("customer")
                                                                .document(documentSnapshotBandRequest.getString("customer_id"))
                                                                .get()
                                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                        if (task.isSuccessful()) {
                                                                            DocumentSnapshot documentSnapshotCustomer = task.getResult();
                                                                            if (documentSnapshotBandType.exists()) {

                                                                                textViewPartnershipRequestItemViewBandName.setText(documentSnapshotBandRequest.getString("name"));
                                                                                textViewPartnershipRequestItemViewBandType.setText(documentSnapshotBandType.getString("type"));
                                                                                textViewPartnershipRequestItemViewCustomerName.setText(documentSnapshotCustomer.getString("name"));
                                                                                textViewPartnershipRequestItemViewBandEmail.setText(documentSnapshotBandRequest.getString("email"));
                                                                                textViewPartnershipRequestItemViewBandMobile.setText(documentSnapshotBandRequest.getString("mobile"));

                                                                                bundle.putString("itemId", documentSnapshotBandRequest.getId());
                                                                                bundle.putString("name", documentSnapshotBandRequest.getString("name"));
                                                                                bundle.putString("mobile", documentSnapshotBandRequest.getString("mobile"));
                                                                                bundle.putString("email", documentSnapshotBandRequest.getString("email"));
                                                                                bundle.putString("band_type_id", documentSnapshotBandRequest.getString("band_type_id"));

                                                                            }
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });


        Button buttonPartnershipRequestItemViewDecline = findViewById(R.id.buttonPartnershipRequestItemViewDecline);
        buttonPartnershipRequestItemViewDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseFirestore.collection("band_req_status")
                        .whereEqualTo("status", "rejected")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    QuerySnapshot querySnapshot = task.getResult();
                                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                        DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);

                                        if (documentSnapshot.exists()) {

                                            HashMap<String, Object> hashMap = new HashMap<>();
                                            hashMap.put("band_req_status_id", documentSnapshot.getId());

                                            firebaseFirestore.collection("band_request")
                                                    .document(Objects.requireNonNull(bundle.getString("itemId")))
                                                    .update(hashMap)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            TuneUpMeSendEmailPartnershipReview(bundle.getString("email"), "rejected", bundle.getString("name"));
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toasty.error(TuneUpMeStudioActivityPartnershipRequestItemView.this, "Oops Something went wrong !", Toast.LENGTH_SHORT, true).show();
                                                        }
                                                    });
                                        }

                                    }
                                }
                            }
                        });


            }
        });

        Button buttonPartnershipRequestItemViewAccept = findViewById(R.id.buttonPartnershipRequestItemViewAccept);
        buttonPartnershipRequestItemViewAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("band_req_status")
                        .whereEqualTo("status", "accepted")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    QuerySnapshot querySnapshot = task.getResult();
                                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                        DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);

                                        if (documentSnapshot.exists()) {

                                            HashMap<String, Object> hashMap = new HashMap<>();
                                            hashMap.put("band_req_status_id", documentSnapshot.getId());

                                            firebaseFirestore.collection("band_request")
                                                    .document(Objects.requireNonNull(bundle.getString("itemId")))
                                                    .update(hashMap)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {

                                                            HashMap<String, Object> bandMap = new HashMap<>();
                                                            bandMap.put("name", bundle.getString("name"));
                                                            bandMap.put("mobile", bundle.getString("mobile"));
                                                            bandMap.put("email", bundle.getString("email"));
                                                            bandMap.put("band_type_id", bundle.getString("band_type_id"));
                                                            bandMap.put("description", "");
                                                            bandMap.put("price_per_hour", "");
                                                            bandMap.put("profile_image_path", "");
                                                            bandMap.put("verification_code", "");
                                                            bandMap.put("status", false);

                                                            firebaseFirestore.collection("band")
                                                                    .add(bandMap)
                                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                        @Override
                                                                        public void onSuccess(DocumentReference documentReference) {
                                                                            TuneUpMeSendEmailPartnershipReview(bundle.getString("email"), "accepted", bundle.getString("name"));
                                                                            Toasty.success(TuneUpMeStudioActivityPartnershipRequestItemView.this, "Request Accepted!", Toast.LENGTH_SHORT, true).show();
                                                                            Intent intent = new Intent(TuneUpMeStudioActivityPartnershipRequestItemView.this, TuneUpMeStudioActivityHome.class);
                                                                            startActivity(intent);
                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Toasty.error(TuneUpMeStudioActivityPartnershipRequestItemView.this, "Oops Something went wrong !", Toast.LENGTH_SHORT, true).show();
                                                                        }
                                                                    });


                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toasty.error(TuneUpMeStudioActivityPartnershipRequestItemView.this, "Oops Something went wrong !", Toast.LENGTH_SHORT, true).show();
                                                        }
                                                    });
                                        }

                                    }
                                }
                            }
                        });

            }
        });

    }

    private void TuneUpMeSendEmailPartnershipReview(String email, String msg, String name) {


        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("email", email);
                jsonObject.addProperty("msg", String.valueOf(msg));
                jsonObject.addProperty("name", String.valueOf(name));

                RequestBody body = RequestBody.create(
                        jsonObject.toString(),
                        MediaType.get("application/json; charset=utf-8")
                );

                Request request = new Request.Builder()
                        .url(ngrock_url + "/TuneUpMe/TuneUpMeSendEmailPartnershipReview")
                        .post(body)
                        .build();


                try {
                    Response response = okHttpClient.newCall(request).execute();

                    if (response.isSuccessful() && response.body() != null) {

                        String resText = response.body().string();
                        JsonObject responseJson = JsonParser.parseString(resText).getAsJsonObject();

                        boolean status = responseJson.get("status").getAsBoolean();

                        if (status) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (msg.equals("rejected")) {
                                        Toasty.warning(TuneUpMeStudioActivityPartnershipRequestItemView.this, "Partnership Request Declined!", Toast.LENGTH_SHORT, true).show();
                                        onBackPressed();
                                    } else if (msg.equals("accepted")) {
                                        Toasty.success(TuneUpMeStudioActivityPartnershipRequestItemView.this, "Partnership Request Accepted!", Toast.LENGTH_SHORT, true).show();
                                        onBackPressed();
                                    }
                                }
                            });
                        }
                    } else {
                        Log.e("Error", String.valueOf(response.code()));
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }).start();

    }

}