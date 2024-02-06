package com.example.cryptofuturestrader;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class Utility {
    static CollectionReference getCollectionReferenceForDiary(){
        FirebaseUser currentUser =FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("Trades Diary").document(currentUser.getUid()).collection("My Trades");

    }
}
