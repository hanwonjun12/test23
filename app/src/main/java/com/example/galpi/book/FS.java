package com.example.galpi.book;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;


import java.util.ArrayList;


public class FS {
    naverApi api=new naverApi();
    boolean temp=false;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    public ArrayList<Book> list=new ArrayList<Book>();

    public void FS(){

    }

    public void addBook(String isbn,Book book) {
        db.collection("Book").document(isbn)
                .set(book)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error writing document", e);
                    }
                });
    }



    public void getBook(String title) {

        list.clear();
        Log.e("clear list", list.toString());

        db.collection("Book")
                .whereArrayContains("keyword_title", title)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {//컬렉션 아래에 있는 모든 정보를 가져온다.
                            if(task.getResult().isEmpty()){
                                return;
                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                list.add(document.toObject(Book.class));
                            }

                            Log.e("check list", list.toString());
                        } else { //
                            Log.e("err", "파이어 스토어 로그인 실패");
                        }
                    }
                });


    }

}