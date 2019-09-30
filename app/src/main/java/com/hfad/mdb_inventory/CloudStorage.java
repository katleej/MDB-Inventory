package com.hfad.mdb_inventory;

import android.net.Uri;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

/**
 * Abstracted access to file based cloud storage backend
 * (currently uses Firebase)
 */
public class CloudStorage {
    private FirebaseStorage storage;
    public CloudStorage() {
        storage = FirebaseStorage.getInstance();
    }

    /**
     * Upload a file from disk to the cloud, getting an http URL
     * @param path The file path to upload
     * @param successListener success callback
     * @param onFailureListener failure callback
     */
    public void upload(Uri path, final OnSuccessListener<String> successListener, final OnFailureListener onFailureListener) {
        final StorageReference storageReference = storage.getReference("/pictures/" + UUID.randomUUID().toString() + ".jpeg");
        storageReference.putFile(path).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        successListener.onSuccess(uri.toString());
                    }
                }).addOnFailureListener(onFailureListener);
            }
        }).addOnFailureListener(onFailureListener);
    }
}
