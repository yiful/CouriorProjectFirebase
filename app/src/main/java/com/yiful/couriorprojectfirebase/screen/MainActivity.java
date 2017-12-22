package com.yiful.couriorprojectfirebase.screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;
import com.yiful.couriorprojectfirebase.R;
import com.yiful.couriorprojectfirebase.model.LoginRegisterImplementation;
import com.yiful.couriorprojectfirebase.model.MyParcel;
import com.yiful.couriorprojectfirebase.presenter.LoginRegisterPresenter;
import com.yiful.couriorprojectfirebase.util.ParcelAdapter;
import com.yiful.couriorprojectfirebase.view.MainActivityView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainActivityView, View.OnClickListener {
    private static final int RC_SIGN_IN = 123;
    private static final int REQUEST_CODE_CAPTURE_IMAGE = 0x0000c0de;
    private static final String TAG = "MainActivity";
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private LoginRegisterPresenter loginRegisterPresenter;
    private MainActivityView mainActivityView;
    private Button btnLogin;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private TextView tvName, tvEmail;
    private ImageView ivUser;
    private FirebaseUser user;
    private List<MyParcel> parcelList;
    private ParcelAdapter adapter;
    private FirebaseFirestore db;
    private String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);

        loginRegisterPresenter = new LoginRegisterImplementation(this, this);
        parcelList = new ArrayList<>();
        adapter = new ParcelAdapter(MainActivity.this, parcelList);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(adapter);
        db = FirebaseFirestore.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View headerView = navigationView.getHeaderView(0);
        btnLogin = headerView.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        tvName = headerView.findViewById(R.id.tvName);
        tvEmail = headerView.findViewById(R.id.tvEmail);
        ivUser = headerView.findViewById(R.id.ivUser);
        setHeader();
        loadUserParcels();
    }

    public void loadUserParcels(){
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("parcels").whereEqualTo("userId", userId)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                Log.d(TAG, "success");

                for(DocumentSnapshot documentSnapshot : documentSnapshots){
                    MyParcel parcel = documentSnapshot.toObject(MyParcel.class);
                    parcelList.add(parcel);
                    Log.d(TAG, "retrieved"+parcel.getName());
                }
                adapter.notifyDataSetChanged();
                addDataUpdatesListener();
            }
        });
    }

    private void addDataUpdatesListener() {
        db.collection("parcels").whereEqualTo("userId", userId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if(e!=null){
                            Log.w(TAG, "listen failed",e );
                        }
                        parcelList.clear();
                        for(DocumentSnapshot doc : documentSnapshots){
                            if(doc != null){
                                parcelList.add(doc.toObject(MyParcel.class));
                                Log.d(TAG, "parcel is added "+doc.toObject(MyParcel.class).getName());
                            }
                        }
                        adapter.notifyDataSetChanged();
                        /*for(DocumentChange change: documentSnapshots.getDocumentChanges()){
                            switch (change.getType()){
                                case ADDED:

                            }
                        }*/
                    }
                });
    }

    @Override
    public void onBackPressed() {
        //   DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                loadUserParcels();
                break;
            case R.id.create_parcel:
                Intent intent = new Intent(MainActivity.this, CreateParcelActivity.class);
                startActivity(intent);
                break;
            case R.id.scanBarcode:
                IntentIntegrator integrator=new IntentIntegrator(MainActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("SCAN");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // create a parcel
            Intent intent = new Intent(MainActivity.this, CreateParcelActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
           loadUserParcels();
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == Activity.RESULT_OK) {
                loadUserParcels();
                loginSuccess();
            } else {
                loginFailure();
            }
        }
        else if(requestCode==REQUEST_CODE_CAPTURE_IMAGE){
            if(intentResult!=null){
                if(intentResult.getContents()==null){
                    Toast.makeText(this,"You cancelled scanning!",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this,intentResult.getContents(),Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void loginSuccess() {
        //  Toast.makeText(this, "You have logged in.", Toast.LENGTH_SHORT).show();
        user = FirebaseAuth.getInstance().getCurrentUser();
        View headerView = navigationView.getHeaderView(0);
        setHeader();
        drawer.closeDrawers();
    }

    private void setHeader() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            btnLogin.setText("Login");
            tvName.setVisibility(View.INVISIBLE);
            tvEmail.setVisibility(View.INVISIBLE);
            ivUser.setVisibility(View.INVISIBLE);
        } else {
            btnLogin.setText("Logout");
            tvName.setVisibility(View.VISIBLE);
            tvEmail.setVisibility(View.VISIBLE);
            ivUser.setVisibility(View.VISIBLE);
            tvName.setText(user.getDisplayName());
            tvEmail.setText(user.getEmail());
            Picasso.with(this).load(user.getPhotoUrl()).into(ivUser);
        }
    }

    @Override
    public void loginFailure() {
        Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void logoutSuccess() {
        Toast.makeText(this, "You have logged out!", Toast.LENGTH_SHORT).show();
        /*btnLogin.setText("Login");
        drawer.closeDrawers();
        tvName.setVisibility(View.INVISIBLE);
        tvEmail.setVisibility(View.INVISIBLE);
        ivUser.setVisibility(View.INVISIBLE);*/
        setHeader();
        drawer.closeDrawer(Gravity.LEFT);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                //logout
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    loginRegisterPresenter.logout();
                    parcelList.clear();
                    adapter.notifyDataSetChanged();
                    logoutSuccess();
                } else {
                    //login
                    loginRegisterPresenter.loginRegister(); //will return activity for result
                }

                break;
        }
    }


}
