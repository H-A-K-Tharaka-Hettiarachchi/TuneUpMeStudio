package com.kshprimeindustries.tuneupmestudio;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kshprimeindustries.tuneupmestudio.model.SQLiteHelper;

import java.util.ArrayList;
import java.util.Random;

public class TuneUpMeStudioActivityHome extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setSubtitle("Home");

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Intent getIntent = getIntent();
        finish();
        startActivity(getIntent);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (view instanceof EditText) {
                Rect outRect = new Rect();
                view.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    view.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    int bandCount = 0;
    int customerCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tune_up_me_studio_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        DrawerLayout drawerLayout = findViewById(R.id.drawerLayoutCustomerHome);
        toolbar = findViewById(R.id.toolbarCustomerHome);
        toolbar.setTitle("TuneUpMe Studio");
        toolbar.setSubtitle("Home");
        toolbar.setSubtitleTextColor(getColor(R.color.white));
        NavigationView navigationView = findViewById(R.id.navigationViewCustomerHome);

        @SuppressLint("InternalInsetResource") int statusBarHeight = getApplicationContext().getResources().getDimensionPixelSize(
                getApplicationContext().getResources().getIdentifier("status_bar_height", "dimen", "android")
        );

//        int safeWidth = getSafeAreaWidth();
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) navigationView.getLayoutParams();
        layoutParams.topMargin = statusBarHeight;
//        layoutParams.setMarginStart(safeWidth);
        navigationView.setLayoutParams(layoutParams);

        View headerView = navigationView.getHeaderView(0);


        TextView textViewProfileNameNavHeaderAdmin = headerView.findViewById(R.id.textViewProfileNameNavHeaderAdmin);
        TextView textViewProfileEmailNavHeaderAdmin = headerView.findViewById(R.id.textViewProfileEmailNavHeaderAdmin);
        ImageView imageViewProfilePictureNavHeaderAdmin = headerView.findViewById(R.id.imageViewProfilePictureNavHeaderAdmin);
        ImageView imageViewSignOutButton = headerView.findViewById(R.id.imageViewSignOutButton);

        new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteHelper sqLiteHelper = new SQLiteHelper(TuneUpMeStudioActivityHome.this, "TuneUpMeStudio", null, 1);
                SQLiteDatabase sqLiteDatabase = sqLiteHelper.getReadableDatabase();

                Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM `admin`", new String[]{});

                while (cursor.moveToNext()) {
                    textViewProfileNameNavHeaderAdmin.setText(String.valueOf(cursor.getString(1)));
                    textViewProfileEmailNavHeaderAdmin.setText(cursor.getString(0));
                }
            }
        }).start();

        imageViewSignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewSignOutButton.setPressed(true);
                imageViewSignOutButton.invalidate();
                imageViewSignOutButton.postDelayed(() -> imageViewSignOutButton.setPressed(false), 200);

                imageViewSignOutButton.animate()
                        .scaleX(0.95f)
                        .scaleY(0.95f)
                        .setDuration(100)
                        .withEndAction(() -> imageViewSignOutButton.animate()
                                .scaleX(1.0f)
                                .scaleY(1.0f)
                                .setDuration(100))
                        .start();

                SharedPreferences sharedPreferences = getSharedPreferences("com.kshprimeindustries.tuneupmestudio.data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("logged", false);
                editor.apply();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SQLiteHelper sqLiteHelper = new SQLiteHelper(TuneUpMeStudioActivityHome.this, "TuneUpMeStudio", null, 1);
                        SQLiteDatabase sqLiteDatabase = sqLiteHelper.getReadableDatabase();
                        sqLiteDatabase.delete("admin", null, null);
                    }
                }).start();
                Intent intent = new Intent(TuneUpMeStudioActivityHome.this, TuneUpMeStudioActivityAdminLogIn.class);
                startActivity(intent);
                finish();

            }
        });

        toolbar.setNavigationOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("band")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {


                                firebaseFirestore.collection("customer")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    QuerySnapshot querySnap = task.getResult();
                                                    if (querySnap != null && !querySnap.isEmpty()) {

                                                        PieChart pieChartAdminHomeTop = findViewById(R.id.pieChartAdminHomeTop);

                                                        ArrayList<PieEntry> pieEntryList = new ArrayList<>();

                                                        pieEntryList.add(new PieEntry(querySnapshot.size(), "Music Bands"));
                                                        pieEntryList.add(new PieEntry(querySnap.size(), "Customers"));

                                                        /*Set Entry list*/
                                                        PieDataSet pieDataSet = new PieDataSet(pieEntryList, "");

                                                        /*Set Colors*/
                                                        ArrayList<Integer> colorArrayList = new ArrayList<>();
                                                        for (int i = 0; i < pieEntryList.size(); i++) {
                                                            Random random = new Random();
                                                            int color = Color.rgb(
                                                                    random.nextInt(256),
                                                                    random.nextInt(256),
                                                                    random.nextInt(256)
                                                            );
                                                            colorArrayList.add(color);
                                                        }
                                                        pieDataSet.setColors(colorArrayList);

                                                        PieData pieData = new PieData();
                                                        /*Set Pie Data*/
                                                        pieData.setDataSet(pieDataSet);
                                                        pieData.setValueTextSize(16);
                                                        pieData.setValueTextColor(getColor(R.color.black));

                                                        pieChartAdminHomeTop.setData(pieData);
                                                        pieChartAdminHomeTop.animateY(2000, Easing.EaseInCirc);

                                                        pieChartAdminHomeTop.setCenterText("Users");
                                                        pieChartAdminHomeTop.setCenterTextColor(getColor(R.color.blue));
                                                        pieChartAdminHomeTop.setCenterTextSize(18);

                                                        pieChartAdminHomeTop.setDescription(null);
                                                        pieChartAdminHomeTop.invalidate();

                                                    }
                                                }
                                            }
                                        });


                            }
                        }
                    }
                });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.nav_menu_partnership_request) {
                    Intent intent = new Intent(TuneUpMeStudioActivityHome.this, TuneUpMeStudioActivityPartnershipRequest.class);
                    startActivity(intent);
                }

                toolbar.setSubtitle(item.getTitle());
                drawerLayout.closeDrawers();
                return true;
            }
        });

    }


}