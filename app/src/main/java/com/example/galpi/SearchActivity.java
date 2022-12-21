package com.example.galpi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galpi.book.Book;
import com.example.galpi.Adapter.SearchAdapter;
import com.example.galpi.book.FS;
import com.example.galpi.book.naverApi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.zip.Inflater;

public class SearchActivity extends AppCompatActivity implements SearchAdapter.OnBookListener {


    RecyclerView recyclerView;
    ArrayList<Book> BookArrayList;
    SearchAdapter searchAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    Button but_year, but_priceLow, btn_Search, btn_Test;
    EditText Search;
    ImageButton btn_profile;

    ArrayList<Book> b_list=new ArrayList<Book>();
    String text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // 데이터 로딩 중의 로딩 창
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        but_year = findViewById(R.id.but_year);
        but_priceLow = findViewById(R.id.but_priceLow);
        btn_profile = findViewById(R.id.btn_profile);
        btn_Search = findViewById(R.id.btn_search);
        btn_Test=findViewById(R.id.btnTest);
        Search = findViewById(R.id.searchBox);

        // activity_search.xml 의 리사이클러뷰
        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 기존성능 강화
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        // Book arrayList
        BookArrayList = new ArrayList<Book>();
        // Adapter, Activity 연결
        searchAdapter = new SearchAdapter(BookArrayList, SearchActivity.this, this);

        recyclerView.setAdapter(searchAdapter);

        FS fs = new FS();
        naverApi api = new naverApi();

        EventChangeListner();

        // test 버튼 클릭 시 Book list clear 후, 사용자가 입력한 text 를 가져온다.
        btn_Test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b_list.clear();
                text = Search.getText().toString();
                fs.getBook(text);
                btn_Test.setVisibility(View.INVISIBLE);
                btn_Search.setVisibility(View.VISIBLE);
            }
        });

        // Search 버튼 클릭 시 NaverAPI 로 검색 로직이 실행
        btn_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread thread = new Thread() {
                    public  void run() {

                        api.main(text, 1, 50);
                        for (int i = 0; i < api.B_List.size(); i++) {
                            Book temp = api.B_List.get(i);
                            fs.addBook(temp.isbn, temp);

                        }
                    }
                };
                Log.e("-------", "검색 로직 실행 ---------------------------------" );
                b_list.addAll(fs.list);
                Log.e("-------","현재 페이지의 booklist로 이동 ---------------------------------" );
                if(fs.list.isEmpty()){
                    api.B_List.clear();
                    Log.e("-------","api 북리스트 초기화 ---------------------------------" );
                    thread.start();
                    Log.e("-------","naver api 실행 ---------------------------------" );
                    try {
                        thread.sleep(1000);
                    } catch (Exception e) {
                    } finally {
                        thread.interrupt();
                    }
                    Log.e("-------", "onClick: ---------------------------------" );
                    b_list.addAll(api.B_List);
                }
                Log.e("-------", b_list.toString());

                btn_Test.setVisibility(View.VISIBLE);
                btn_Search.setVisibility(View.INVISIBLE);
                BookArrayList.clear();
                BookArrayList.addAll(b_list);
                searchAdapter.notifyDataSetChanged();
            }
        });

        // 마이페이지로 이동
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });


        // 도서 정렬 버튼
        but_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(BookArrayList, Book.BookPubdateComparator);
                Toast.makeText(SearchActivity.this, "도서 최신순", Toast.LENGTH_SHORT).show();
                searchAdapter.notifyDataSetChanged();
            }
        });

        but_priceLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(BookArrayList, Book.BookDiscountLowComparator);
                Toast.makeText(SearchActivity.this, "도서 낮은 가격순", Toast.LENGTH_SHORT).show();
                searchAdapter.notifyDataSetChanged();
            }
        });

    }

    // 이벤트 발생 시
    private void EventChangeListner() {

        db.collection("Book").orderBy("title")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {

                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                            Log.e("Firestore error", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {

                            if (dc.getType() == DocumentChange.Type.ADDED)

                                BookArrayList.add(dc.getDocument().toObject(Book.class));

                        }

                        searchAdapter.notifyDataSetChanged();
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                });

    }

    // 리사이클러뷰가 클릭 됐을 시
    @Override
    public void onBookClick(int position) {

        Intent intent = new Intent(this, BookInformActivity.class);

        // BookArrayList 의 position 을 갖고 해당되는 데이터를 넘겨준다
        intent.putExtra("title", BookArrayList.get(position).getTitle());
        intent.putExtra("author", BookArrayList.get(position).getAuthor());
        intent.putExtra("publisher", BookArrayList.get(position).getPublisher());
        intent.putExtra("discount", BookArrayList.get(position).getDiscount());
        intent.putExtra("link", BookArrayList.get(position).getLink());
        intent.putExtra("description", BookArrayList.get(position).getDescription());
        intent.putExtra("image", BookArrayList.get(position).getImage());

        startActivity(intent);
    }


}
