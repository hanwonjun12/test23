package com.example.galpi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.galpi.book.Book;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BookInformActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser user;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_inform);

        // SearchActivity 에서 전달된 데이터 받기
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String author = intent.getStringExtra("author");
        String publisher = intent.getStringExtra("publisher");
        String discount = intent.getStringExtra("discount");
        String link = intent.getStringExtra("link");
        String description = intent.getStringExtra("description");
        String image = intent.getStringExtra("image");


        TextView bookTitle = findViewById(R.id.BookTitle);
        ImageView bookImg = findViewById(R.id.BookImg);
        TextView bookAuth = findViewById(R.id.BookAuthor);
        TextView bookPub = findViewById(R.id.BookPublisher);
        TextView bookIntro = findViewById(R.id.BookIntro);
        TextView bookPrice = findViewById(R.id.BookDiscount);
        TextView bookLink = findViewById(R.id.BookLink);

        TextView likeCount = findViewById(R.id.like_count);
        TextView dislikeCount = findViewById(R.id.dislike_count);
        CheckBox btn_like = findViewById(R.id.btn_Like);
        CheckBox btn_dislike = findViewById(R.id.btn_dislike);

        // 저장돼있는 like 수로 text 초기 설정 (dislike 도)
        likeCount.setText(String.valueOf(Book.like));
        dislikeCount.setText(String.valueOf(Book.dislike));

        // 위에서 intent 로 받아온 데이터 값을 사용자에게 보여지는 책 정보의 관한 Text, image 로 설정
        bookTitle.setText(title);
        // Glide 이용하여 이미지 로드
        Glide.with(this)
                .load(image)
                .into(bookImg);
        bookAuth.setText(author + " 저 ");
        bookPub.setText(publisher + " 출판 ");
        bookIntro.setText(description);
        bookPrice.setText(discount + " 원 ");
        bookLink.setText(link);

        // 좋아요 버튼
        btn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (btn_like.isChecked()) {
                    // Book 클래스의 like 수 증가
                    Book.like++;
                    likeCount.setText(String.valueOf(Book.like));
                } else if (btn_like.isChecked() != true) {
                    // Book 클래스의 like 수 감소
                    Book.like--;
                    likeCount.setText(String.valueOf(Book.like));
                }
            }

        });

        // 싫어요 버튼
        btn_dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btn_dislike.isChecked()) {
                    // Book 클래스의 dislike 수 증가
                    Book.dislike++;
                    dislikeCount.setText(String.valueOf(Book.dislike));
                } else if (btn_dislike.isChecked() != true) {
                    // Book 클래스의 dislike 수 증가
                    Book.dislike--;
                    dislikeCount.setText(String.valueOf(Book.dislike));
                }
            }

        });
    }
}
