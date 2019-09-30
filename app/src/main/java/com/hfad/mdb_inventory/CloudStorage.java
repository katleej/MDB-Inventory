package com.hfad.mdb_inventory;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CloudStorage {
    private FirebaseStorage storage;
    public CloudStorage() {
        storage = FirebaseStorage.getInstance();
    }

    public void upload(String path) {
        StorageReference storageReference = storage.getReference("");
    }
}
