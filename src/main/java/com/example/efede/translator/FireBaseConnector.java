package com.example.efede.translator;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class FireBaseConnector {
    //Declare to boolean variable
    private static boolean addWordIsSuccessful = false,deleteWordIsSuccessful = false;


    public static boolean FireBaseAddWord(String source,String target,String userId){

        // Firebase Database connection and add word into database.

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference("words").child(userId); // Firebase database table path.

        Map wordMap = new HashMap(); // Set word information in map

        wordMap.put("source",source);
        wordMap.put("target",target);
        wordMap.put("time",ServerValue.TIMESTAMP);

        root.push().updateChildren(wordMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {  // Update Database
                if(task.isSuccessful()){
                   setAddWordIsSuccessful(true);
                }else {
                    setAddWordIsSuccessful(false);
                }
            }
        });


        return addWordIsSuccessful;


    }

    public static boolean FireBaseDeleteWord(String userId, final String key){

        // Firebase Database connection and delete word into database.

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference root = firebaseDatabase.getReference("words").child(userId); // Firebase database table path.

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(key)){
                    root.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {  // Check whether the database has a key and delete word
                            if(task.isSuccessful()){
                                setDeleteWordIsSuccessful(true);
                            }else {
                                setDeleteWordIsSuccessful(false);
                            }
                        }
                    });
                }else {
                    deleteWordIsSuccessful = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return deleteWordIsSuccessful;
    }


    private static void setAddWordIsSuccessful(boolean result){
        addWordIsSuccessful = result;
    }

    private static void setDeleteWordIsSuccessful(boolean result){
        deleteWordIsSuccessful = result;
    }
}
