package com.example.project_android_server;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_android_server.Common.Common;
import com.example.project_android_server.Model.Food;
import com.example.project_android_server.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;


public class FoodList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    //RelativeLayout rootLayout;

    //Firebase
    FirebaseDatabase db;
    DatabaseReference foodList;
    FirebaseStorage storage;
    StorageReference storageReference;

    EditText editName, editDescription, editPrice, editDiscount;
    Button btnUpload, btnSelect, btnAddProduct;
    ImageView imgUploadProduct;

    Uri saveUri;
    String categoryId = "";
    //RelativeLayout drawer;
    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;

    Food newFood;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        //Init Firebase
        db = FirebaseDatabase.getInstance();
        foodList = db.getReference("Food");
        foodList.keepSynced(true);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        newFood = new Food();

        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnAddProduct.setOnClickListener(view -> showAddFoodDialog());

        //rootLayout = findViewById(R.id.rootLayout);

        //Load Menu
        recyclerView = findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Get intent Here
        if (getIntent() != null) {
            categoryId = getIntent().getStringExtra("CategoryId");
        }
        if (!categoryId.isEmpty()) {
            loadListFood(categoryId);
        }
    }

    private void showAddFoodDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FoodList.this);
        alertDialog.setTitle("Thêm sản phẩm mới");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_food_layout = inflater.inflate(R.layout.add_food_item, null);

        editName = add_food_layout.findViewById(R.id.editFoodName);
        editPrice = add_food_layout.findViewById(R.id.editPrice);
        editDescription = add_food_layout.findViewById(R.id.editFoodDescription);
        editDiscount = add_food_layout.findViewById(R.id.editDiscount);
        btnSelect = add_food_layout.findViewById(R.id.btnSelect1);
        btnUpload = add_food_layout.findViewById(R.id.btnUpload1);
        imgUploadProduct = add_food_layout.findViewById(R.id.imgUploadProduct);

        btnSelect.setOnClickListener(view -> chooseImage());

        btnUpload.setOnClickListener(view -> uploadImage());

        alertDialog.setView(add_food_layout);

        //set button
        alertDialog.setPositiveButton("OK", (dialogInterface, i) -> {
            dialogInterface.dismiss();
            if (newFood != null) {
                foodList.push().setValue(newFood);
                Toast.makeText(FoodList.this, "Sản phẩm " + newFood.getName() + " đã thêm", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.setNegativeButton("HUỶ", (dialogInterface, i) -> dialogInterface.dismiss());
        alertDialog.show();
    }

    private void uploadImage() {
        if (saveUri != null) {
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Đang tải lên....");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/" + imageName);
            imageFolder.putFile(saveUri).addOnSuccessListener(taskSnapshot -> {

                        mDialog.dismiss();

                        Toast.makeText(FoodList.this, "Ảnh đã được tải lên", Toast.LENGTH_SHORT).show();
                        imageFolder.getDownloadUrl().addOnSuccessListener(uri -> {
                            newFood = new Food();
                            newFood.setName(editName.getText().toString());
                            newFood.setDescription(editDescription.getText().toString());
                            newFood.setImage(uri.toString());
                            newFood.setPrice(editPrice.getText().toString());
                            newFood.setDiscount(editDiscount.getText().toString());
                            newFood.setMenuId(categoryId);
                            Picasso.with(FoodList.this).load(uri.toString()).into(imgUploadProduct);
                        });


                    })
                    .addOnFailureListener(exception -> {
                        mDialog.dismiss();
                        Toast.makeText(FoodList.this, "" + exception.getMessage(), Toast.LENGTH_LONG).show();
                    })
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        mDialog.setMessage("Đang tải " + ((int) progress) + "%...");
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            saveUri = data.getData();
            btnSelect.setText(R.string.image_selected);
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), Common.PICK_IMAGE_REQUEST);
    }

    private void loadListFood(String categoryId) {

        FirebaseRecyclerOptions<Food> op = new FirebaseRecyclerOptions.Builder<Food>().setQuery(foodList.orderByChild("menuId").equalTo(categoryId), Food.class).build();
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(op) {
            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
                return new FoodViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Food model) {
                Log.i("name", model.getName());
                Log.i("image", model.getImage());
                holder.food_name.setText(model.getName());
                holder.food_description.setText(model.getDescription());
                holder.food_price.setText(model.getPrice());
                holder.food_discount.setText(model.getDiscount());
                Picasso.with(getBaseContext()).load(model.getImage()).into(holder.food_image);
                final Food local = model;
                holder.btnFoodUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showUpdateFoodDialog(adapter.getRef(position).getKey(), adapter.getItem(position));
                    }
                });
                holder.btnFoodDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteFood(adapter.getRef(position).getKey());
                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    //Update and Delete

    private void deleteFood(String key) {
        foodList.child(key).removeValue();
        Toast.makeText(this, "Sản phẩm đã được xoá !!!!", Toast.LENGTH_SHORT);
    }

    private void showUpdateFoodDialog(final String key, final Food item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FoodList.this);
        alertDialog.setTitle("Cập nhật sản phẩm");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_food_layout = inflater.inflate(R.layout.add_food_item, null);

        editName = add_food_layout.findViewById(R.id.editFoodName);
        editDescription = add_food_layout.findViewById(R.id.editFoodDescription);
        editDiscount = add_food_layout.findViewById(R.id.editDiscount);
        editPrice = add_food_layout.findViewById(R.id.editPrice);
        btnSelect = add_food_layout.findViewById(R.id.btnSelect1);
        btnUpload = add_food_layout.findViewById(R.id.btnUpload1);
        imgUploadProduct = add_food_layout.findViewById(R.id.imgUploadProduct);
        //set default name
        editName.setText(item.getName());
        editDescription.setText(item.getDescription());
        editDiscount.setText(item.getDiscount());
        editPrice.setText(item.getPrice());
        Picasso.with(getBaseContext()).load(item.getImage()).into(imgUploadProduct);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeImage(item);
            }
        });

        alertDialog.setView(add_food_layout);

        //set button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                item.setName(editName.getText().toString());
                item.setDescription(editDescription.getText().toString());
                item.setPrice(editPrice.getText().toString());
                item.setDiscount(editDiscount.getText().toString());
                foodList.child(key).setValue(item);
                Toast.makeText(FoodList.this, "Sản phẩm " + item.getName() + " đã được sửa", Toast.LENGTH_SHORT).show();


            }
        });
        alertDialog.setNegativeButton("HUỶ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    private void changeImage(final Food item) {
        if (saveUri != null) {
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Đang tải lên....");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/" + imageName);
            imageFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            mDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Đã tải lên", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    item.setImage(uri.toString());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            mDialog.dismiss();
                            Toast.makeText(FoodList.this, "" + exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            mDialog.setMessage("Đang tải " + ((int) progress) + "%...");
                        }
                    });
        }
    }
}

