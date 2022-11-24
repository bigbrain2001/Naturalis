package com.chs.naturalis;

import static android.view.Window.FEATURE_NO_TITLE;
import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

import static java.util.logging.Logger.getLogger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.chs.naturalis.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class HomePageAdmin extends AppCompatActivity {

    private ListView productListView;
    private Button logoutButton;
    private Button addProductButton;

    private DatabaseReference database;
    private final List<Product> productList = new ArrayList<>();
    private final String DATABASE_NAME = "Product";
    private static final int SPLASH_SCREEN = 100;

    private static final Logger LOGGER = getLogger(HomePageAdmin.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Remove title bar
        this.requestWindowFeature(FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN);

        //set content view AFTER ABOVE sequence (to avoid crash)
        setContentView(R.layout.activity_home_page_admin);

        super.onCreate(savedInstanceState);
        identifyTheFieldsById();
        setListViewItems();
        transitionToAddProductActivity();
        pressLogoutButton();
    }

    private void identifyTheFieldsById() {
        productListView = findViewById(R.id.productListView);
    }

    private void transitionToAddProductActivity() {
        addProductButton = findViewById(R.id.addProductButton);

        addProductButton.setOnClickListener(v -> {
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(HomePageAdmin.this, AddProduct.class);
                startActivity(intent);
                finish();
            }, SPLASH_SCREEN);
        });
    }

    private void pressLogoutButton() {
        logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> {
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(HomePageAdmin.this, Login.class);
                startActivity(intent);
                finish();
            }, SPLASH_SCREEN);
        });

    }

    /**
     * Insert the products names from the database into the list view.
     */
    private void setListViewItems() {
        database = FirebaseDatabase.getInstance().getReference().child(DATABASE_NAME);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    LOGGER.info("Product database has been retrieved for setting the list view.");

                    productList.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        productList.add(snapshot.getValue(Product.class));
                    }

                    if (!productList.isEmpty()) {
                        LOGGER.info("List is not empty.");
                    }

                    //Retrieve the products from database and create a list with their names.
                    List<String> productsNameList = new ArrayList<>();
                    for (Product product : productList) {
                      //  if (product.getQuantity() > 0) {
                            productsNameList.add(product.getName());
                       // }
                    }

                    //Set the listview items as the productsNameList objects.
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(HomePageAdmin.this,
                            android.R.layout.simple_list_item_multiple_choice, productsNameList);
                    productListView.setAdapter(adapter);
                } else {
                    LOGGER.info("DataSnapshot error");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                LOGGER.info("Error on retrieving data from database.");
                makeText(HomePageAdmin.this, "Error on retrieving data from database.", LENGTH_LONG).show();
            }
        });
    }

}