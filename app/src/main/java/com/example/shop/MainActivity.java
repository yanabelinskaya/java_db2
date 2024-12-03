package com.example.shop;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    private EditText titleText, descriptionText, priceText;
    private Button addButton, updateButton, deleteButton, chooseImageButton;
    private ListView listView;
    private ImageView productImageView;
    private ArrayAdapter<String> adapter;
    private String selectedProductId;
    private String imagePath;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Paper.init(this);

        titleText = findViewById(R.id.titleText);
        descriptionText = findViewById(R.id.descriptionText);
        priceText = findViewById(R.id.priceText);
        addButton = findViewById(R.id.addButton);
        updateButton = findViewById(R.id.updateButton);
        deleteButton = findViewById(R.id.deleteButton);
        chooseImageButton = findViewById(R.id.chooseImageButton);
        productImageView = findViewById(R.id.productImageView);
        listView = findViewById(R.id.listView);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getProductTitles());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            selectedProductId = adapter.getItem(position);
            Product product = Paper.book().read(selectedProductId, null);
            if (product != null) {
                titleText.setText(product.getTitle());
                descriptionText.setText(product.getDescription());
                priceText.setText(product.getPrice());
                imagePath = product.getImagePath();
                Glide.with(this).load(Uri.parse(imagePath)).into(productImageView);
            }
        });

        addButton.setOnClickListener(v -> {
            String title = titleText.getText().toString();
            String description = descriptionText.getText().toString();
            String price = priceText.getText().toString();
            if (!title.isEmpty() && !description.isEmpty() && !price.isEmpty() && imagePath != null) {
                Product product = new Product(title, title, description, price, imagePath);
                Paper.book().write(title, product);
                updateProductList();
                clearInputs();
            }
        });

        updateButton.setOnClickListener(v -> {
            if (selectedProductId == null) {
                Toast.makeText(MainActivity.this, "Пожалуйста, сначала выберите товар", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = titleText.getText().toString();
            String description = descriptionText.getText().toString();
            String price = priceText.getText().toString();
            if (!title.isEmpty() && !description.isEmpty() && !price.isEmpty() && imagePath != null) {
                Product updatedProduct = new Product(title, title, description, price, imagePath);
                Paper.book().write(selectedProductId, updatedProduct);
                updateProductList();
                clearInputs();
                Toast.makeText(MainActivity.this, "Товар обновлен", Toast.LENGTH_SHORT).show();
            }
        });

        deleteButton.setOnClickListener(v -> {
            if (selectedProductId == null) {
                Toast.makeText(MainActivity.this, "Пожалуйста, сначала выберите товар", Toast.LENGTH_SHORT).show();
                return;
            }
            Paper.book().delete(selectedProductId);
            updateProductList();
            clearInputs();
        });

        chooseImageButton.setOnClickListener(v -> openImagePicker());
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            imagePath = selectedImageUri.toString();
            Glide.with(this).load(selectedImageUri).into(productImageView);
        }
    }

    private List<String> getProductTitles() {
        return new ArrayList<>(Paper.book().getAllKeys());
    }

    private void updateProductList() {
        adapter.clear();
        adapter.addAll(getProductTitles());
        adapter.notifyDataSetChanged();
    }

    private void clearInputs() {
        titleText.setText("");
        descriptionText.setText("");
        priceText.setText("");
        selectedProductId = null;
        imagePath = null;
        productImageView.setImageResource(0);
    }
}
