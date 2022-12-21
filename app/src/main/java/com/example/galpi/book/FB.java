package com.example.galpi.book;



import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;


public class FB {

    FirebaseDatabase database;
    DatabaseReference df;

    public void FB(){

    }

    public FB(String path) {
        this.database = FirebaseDatabase.getInstance();
        this.df = database.getReference(path);
    }


    // 파이어베이스 데이터베이스 연동

    //DatabaseReference는 데이터베이스의 특정 위치로 연결하는 거라고 생각하면 된다.
    //현재 연결은 데이터베이스에만 딱 연결해놓고
    //키값(테이블 또는 속성)의 위치 까지는 들어가지는 않은 모습이다.

    // isbn : 테이블에 저장될 이름
    // b : 저장할 데이터
    public void pushBook(Book b){
        df.child(b.isbn).setValue(b);
    }
    public boolean check (String text){

        Query searchTitle =df.orderByChild("title");
        if(true){
            return true;}
        else {
            return false;
        }

    }

    public void getBook(String text) {

        Query searchTitle =df.orderByChild("title");


        searchTitle.startAt(text,"author").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot i:snapshot.getChildren()){
                    System.out.println(i.child("title").getValue());
                    System.out.println(i.getKey());
                    System.out.println("------------------------");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 검색 결과가 없습니다
            }
        });
    }
}