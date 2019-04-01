package br.edu.fatecriopreto.dezani.englishapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText txtEmail, txtPassword;
    private Button btnEntrar, btnNovo;

    FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.txtEmail = findViewById(R.id.txtEmail);
        this.txtPassword = findViewById(R.id.txtPassword);

        this.btnEntrar = findViewById(R.id.btnEntrar);
        this.btnNovo = findViewById(R.id.btnNovo);

        this.btnEntrar.setOnClickListener(this);
        this.btnNovo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == btnEntrar) {

           entrar();
        }
        else if(v == btnNovo) {
            novo();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            Uri uri = data.getData(); // content://media/external/audio/media/1
            MediaPlayer player = MediaPlayer.create(this, uri);
            player.start();

            // Create a storage reference from our app
            StorageReference storageRef = storage.getReference();

            Uri file = Uri.fromFile(new File(uri.getPath()));
            StorageReference riversRef = storageRef.child("audios/"+file.getLastPathSegment());

            UploadTask uploadTask = riversRef.putFile(file);

            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Log.e("FIREBASE STORAGE", exception.getMessage());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                      StorageMetadata metadata = taskSnapshot.getMetadata();// contains file metadata such as size, content-type, etc.

                }
            });
        }
    }

    private void entrar() {
        String email = txtEmail.getText().toString();
        String senha = txtPassword.getText().toString();

        Intent it = new Intent(this, ClassroomActivity.class);
        startActivity(it);
    }

    private void novo() {

        Intent it = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        startActivityForResult(it, 0);
    }
}
