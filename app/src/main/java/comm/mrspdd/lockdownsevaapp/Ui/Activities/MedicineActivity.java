package comm.mrspdd.lockdownsevaapp.Ui.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ssduo.lockdownsevaapp.R;

import java.util.ArrayList;
import java.util.List;

import comm.mrspdd.lockdownsevaapp.Models.ShopsModelClass;
import comm.mrspdd.lockdownsevaapp.Adapters.ShopsViewsAdapter;
import comm.mrspdd.lockdownsevaapp.customLoadingBar;

public class MedicineActivity extends AppCompatActivity implements ShopsViewsAdapter.OnNoteListener {
    private RecyclerView mRecyclerView;
    comm.mrspdd.lockdownsevaapp.customLoadingBar customLoadingBar = new customLoadingBar(this);
    private ShopsViewsAdapter mAdapter;
    Bundle basket;
    AlertDialog dialog;
    final Point p = new Point();
    private DatabaseReference mDatabaseRef, ref;
    private List<ShopsModelClass> shops;
    String pethname;
    String Category = "Medical Store";
    String name, phone, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);
        Bundle bundle1 = getIntent().getExtras();
        name = bundle1.getString("name");
        phone = bundle1.getString("phone");
        address = bundle1.getString("address");
        pethname = bundle1.getString("Peth");
        Toast.makeText(this, "peth name is "+ pethname, Toast.LENGTH_SHORT).show();

        customLoadingBar.startLoader();

        mRecyclerView = findViewById(R.id.recyclerViewShopList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        shops = new ArrayList<>();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        ref = mDatabaseRef.child("Shops").child("Medical Store").child(pethname);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ShopsModelClass upload = postSnapshot.getValue(ShopsModelClass.class);
                    shops.add(upload);
                    //Toast.makeText(Resources.this, "  "+postSnapshot.getKey().toString(), Toast.LENGTH_SHORT).show();
                }
                setadapter(shops);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                customLoadingBar.dismissLoader();
                Toast.makeText(MedicineActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavBarMedicines);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        finish();
                        break;
                    case R.id.action_messages:
                        Toast.makeText(MedicineActivity.this, "Will Be Added Soon", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });


    }

    private void setadapter(List<ShopsModelClass> mUploads) {
        mAdapter = new ShopsViewsAdapter(MedicineActivity.this, mUploads, this);

        mRecyclerView.setAdapter(mAdapter);
        customLoadingBar.dismissLoader();
    }


    @Override
    public void onNoteClickkk(int position) {
        basket = new Bundle();
        basket.putString("name", name);
        basket.putString("phone", phone);
        basket.putString("address", address);

        Intent intent = new Intent(this, ShopChatActivity.class);
        basket.putString("ShopName", shops.get(position).getName());
        basket.putString("ShopImage", shops.get(position).getImage());
        basket.putString("ShopAddress", shops.get(position).getAddress());
        basket.putString("ShopPhone", shops.get(position).getPhone());
        basket.putString("Peth", pethname);
        basket.putString("Category", Category);
        intent.putExtras(basket);
        startActivity(intent);
    }
}
