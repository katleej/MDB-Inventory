package com.hfad.mdb_inventory;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

/**
 * Abstracted access to the authentication backend
 * (currently uses Firebase)
 */
public class CloudAuthenticator {
    public class CloudUser {
        private FirebaseUser firebaseUser;
        private CloudUser(FirebaseUser firebaseUser) {
            this.firebaseUser = firebaseUser;
        }

        public String getID() {
            return firebaseUser.getUid();
        }
    }

    private FirebaseAuth mAuth;
    private CloudUser currentUser;

    public CloudAuthenticator(Context context) {
        //initialize the Firebase app on each init. This is safe because initializeApp falls through on repeats.
        FirebaseApp.initializeApp(context);

        mAuth = FirebaseAuth.getInstance();
        retranslateUser();
    }


    public CloudUser getCurrentUser() {
        return currentUser;
    }

    private void retranslateUser() {
        FirebaseUser currentFBUser =  mAuth.getCurrentUser();
        if (currentFBUser != null) {
            currentUser = new CloudUser(currentFBUser);
        }else {
            currentUser = null;
        }
    }

    public boolean isUserSignedIn() {
        return getCurrentUser() != null;
    }

    public void createUserWithEmailAndPassword(String email, String password, OnCompleteListener<Boolean> onCompleteListener) {
        mAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                retranslateUser();
            }
        }).addOnCompleteListener(translateFromFirebaseOnComplete(onCompleteListener));
    }

    public void signInWithEmailAndPassword(String email, String password, OnCompleteListener<Boolean> onCompleteListener) {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(translateFromFirebaseOnComplete(onCompleteListener));
    }

    private OnCompleteListener<AuthResult> translateFromFirebaseOnComplete(final OnCompleteListener<Boolean> old) {
        return new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull final Task<AuthResult> task) {
                old.onComplete(new Task<Boolean>() {
                    @Override
                    public boolean isComplete() {
                        return task.isComplete();
                    }

                    @Override
                    public boolean isSuccessful() {
                        return task.isSuccessful();
                    }

                    @Override
                    public boolean isCanceled() {
                        return task.isCanceled();
                    }

                    @Nullable
                    @Override
                    public Boolean getResult() {
                        return task.isSuccessful();
                    }

                    @Nullable
                    @Override
                    public <X extends Throwable> Boolean getResult(@NonNull Class<X> aClass) throws X {
                        return null;
                    }

                    @Nullable
                    @Override
                    public Exception getException() {
                        return task.getException();
                    }

                    @NonNull
                    @Override
                    public Task<Boolean> addOnSuccessListener(@NonNull OnSuccessListener<? super Boolean> onSuccessListener) {
                        return null;
                    }

                    @NonNull
                    @Override
                    public Task<Boolean> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super Boolean> onSuccessListener) {
                        return null;
                    }

                    @NonNull
                    @Override
                    public Task<Boolean> addOnSuccessListener(@NonNull Activity activity, @NonNull OnSuccessListener<? super Boolean> onSuccessListener) {
                        return null;
                    }

                    @NonNull
                    @Override
                    public Task<Boolean> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
                        return null;
                    }

                    @NonNull
                    @Override
                    public Task<Boolean> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
                        return null;
                    }

                    @NonNull
                    @Override
                    public Task<Boolean> addOnFailureListener(@NonNull Activity activity, @NonNull OnFailureListener onFailureListener) {
                        return null;
                    }
                });
            }
        };
    }
}
