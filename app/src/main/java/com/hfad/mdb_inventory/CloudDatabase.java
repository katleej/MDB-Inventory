package com.hfad.mdb_inventory;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A cloud database used for storing application data
 * Allows for accessing/modifying user information as well as purchases
 */
public class CloudDatabase {
    private FirebaseFirestore db;
    public CloudDatabase() {
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Push the additional user information to the database
     * @param user The user to push for. Note: you must have a valid session for this user or else ACLs will reject the write
     * @param name The display name of the user
     */
    public void finishUserRegistration(CloudAuthenticator.CloudUser user, String name) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("display_name",name);

        db.collection("users").document(user.getID()).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("CloudDatabase","User finish registration success");
                }else {
                    Log.e("CloudDatabase",task.getException().toString());
                }
            }
        });
    }

    /**
     * Get all purchases from the cloud database
     * @param successListener Call back with purchases
     * @param onFailureListener Call back on error
     */
    public void getPurchases(final OnSuccessListener<ArrayList<Model>> successListener, OnFailureListener onFailureListener) {
        db.collection("purchases").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<Model> purchases = new ArrayList<>();
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    try {
                        Model model = new Model();

                        model.setUid(document.getId());
                        model.setPrice((String)document.get("price"));
                        model.setItem((String)document.get("item"));
                        model.setLocation((String)document.get("purchase_location"));
                        model.setDescription((String)document.get("item_description"));
                        model.setImageURL((String)document.get("image_url"));
                        model.setDate((String)document.get("date"));

                        purchases.add(model);
                    }catch (NullPointerException | ClassCastException e) {
                        e.printStackTrace();
                    }
                }
                successListener.onSuccess(purchases);
            }
        }).addOnFailureListener(onFailureListener);
    }

    /**
     * Push a purchase to the cloud database. Creates a new record if it doesn't exist, otherwise overwrites.
     * @param purchase The purchase to push
     * @param onCompleteListener Callback on either failure or success
     */
    public void pushPurchase(Model purchase, OnCompleteListener<Void> onCompleteListener) {
        Map<String, Object> purchaseMap = new HashMap<>();
        purchaseMap.put("price",purchase.getPrice());
        purchaseMap.put("item",purchase.getItem());
        purchaseMap.put("purchase_location",purchase.getLocation());
        purchaseMap.put("item_description",purchase.getDescription());
        purchaseMap.put("image_url",purchase.getImageURL());
        purchaseMap.put("date",purchase.getDate());

        db.collection("purchases").document(purchase.getUid()).set(purchaseMap).addOnCompleteListener(onCompleteListener);
    }

    /**
     * Delete a purchase from the cloud database
     * @param purchase The purchase to remove
     * @param onCompleteListener Callback on either failure or success
     */
    public void deletePurcahse(Model purchase, OnCompleteListener<Void> onCompleteListener) {
        db.collection("purchases").document(purchase.getUid()).delete().addOnCompleteListener(onCompleteListener);
    }
}


