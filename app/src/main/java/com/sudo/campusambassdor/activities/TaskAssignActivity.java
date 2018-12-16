package com.sudo.campusambassdor.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sudo.campusambassdor.R;
import com.sudo.campusambassdor.adapters.TaskAdapter;
import com.sudo.campusambassdor.managers.DatabaseHelper;
import com.sudo.campusambassdor.model.Comment;
import com.sudo.campusambassdor.model.Task;
import com.sudo.campusambassdor.utils.LogUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class TaskAssignActivity extends BaseActivity {
    ArrayList<Task> arrayList;
    Integer PICK_IMAGE_REQUEST =124;

    private RecyclerView mRecyclerView;
    private TaskAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    DatabaseHelper databaseHelper;
    private Uri filePath;
    ImageView imageView;

    private FirebaseDatabase database;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;

    private DatabaseReference mDatabase;




    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_assign);

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        imageView = findViewById(R.id.taskuploadhelper);
        arrayList = new ArrayList<>();
        TaskList();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mRecyclerView = findViewById(R.id.taskAssignRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new TaskAdapter(arrayList);


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        //databaseHelper.uploadImage(Uri.parse())

        mAdapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                    chooseImage();


            }
        });


    }

    public void TaskList(){
        arrayList.add(new Task("Hackathon", "Organise a hackathon and upload the pictures", 0));
        arrayList.add(new Task("Event", "Organise a technical event in college", 0));
        arrayList.add(new Task("Incubate IND", "Make people aware about Incubate IND", 0));
        arrayList.add(new Task("Event2", "Organise a technical event in college", 0));
        arrayList.add(new Task("Event3", "Organise a technical event in college", 0));
        arrayList.add(new Task("Event4", "Organise a technical event in college", 0));
        arrayList.add(new Task("Event5", "Organise a technical event in college", 0));
        arrayList.add(new Task("Event6", "Organise a technical event in college", 0));
        arrayList.add(new Task("Event7", "Organise a technical event in college", 0));
        arrayList.add(new Task("Event 8", "Organise a technical event in college", 0));

    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
                uploadImage();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(TaskAssignActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(TaskAssignActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

//    public void createOrUpdatePoints(String commentText, final String postId) {
//        try {
//            String authorId = firebaseAuth.getCurrentUser().getUid();
//            DatabaseReference mCommentsReference = database.getReference().child("points/" + postId);
//            String commentId = mCommentsReference.push().getKey();
//            Comment comment = new Comment(commentText);
//            comment.setId(commentId);
//            comment.setAuthorId(authorId);
//
//            mCommentsReference.child(commentId).setValue(comment, new DatabaseReference.CompletionListener() {
//                @Override
//                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                    if (databaseError == null) {
//                        incrementCommentsCount(postId);
//                    } else {
//                        LogUtil.logError(TAG, databaseError.getMessage(), databaseError.toException());
//                    }
//                }
//
//                private void incrementCommentsCount(String postId) {
//                    DatabaseReference postRef = database.getReference("posts/" + postId + "/commentsCount");
//                    postRef.runTransaction(new Transaction.Handler() {
//                        @Override
//                        public Transaction.Result doTransaction(MutableData mutableData) {
//                            Integer currentValue = mutableData.getValue(Integer.class);
//                            if (currentValue == null) {
//                                mutableData.setValue(1);
//                            } else {
//                                mutableData.setValue(currentValue + 1);
//                            }
//
//                            return Transaction.success(mutableData);
//                        }
//
//                        @Override
//                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
//                            LogUtil.logInfo(TAG, "Updating comments count transaction is completed.");
//                        }
//                    });
//                }
//            });
//        } catch (Exception e) {
//            LogUtil.logError(TAG, "createOrUpdateComment()", e);
//        }
//    }


}
