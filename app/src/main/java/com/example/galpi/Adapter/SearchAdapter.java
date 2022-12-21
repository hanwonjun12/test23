package com.example.galpi.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.galpi.R;
import com.example.galpi.book.Book;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Locale;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.CustomViewHolder> {

    private ArrayList<Book> arrayList;
    private Context context;
    private OnBookListener mOnBookListener;

    public SearchAdapter(ArrayList<Book> arrayList, Context context, OnBookListener onBookListener) {
        this.arrayList = arrayList;
        this.context = context;
        this.mOnBookListener = onBookListener;
    }


    @NonNull
    @Override
    public SearchAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_list, parent, false);

        return new CustomViewHolder(v, mOnBookListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.CustomViewHolder holder, int position) {

        // Book 데이터 연동
        Book book = arrayList.get(position);

        // Glide 를 이용한 이미지 로드
        Glide.with(holder.itemView)
                .load(book.image)
                .into(holder.iv_bookImg);

        holder.tv_bookName.setText(book.title);
        holder.tv_bookAuthor.setText(book.author + " 저");
        holder.tv_bookPrice.setText(book.discount + " 원");
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iv_bookImg;
        TextView tv_bookName;
        TextView tv_bookAuthor;
        TextView tv_bookPrice;
        OnBookListener onBookListener;

        public CustomViewHolder(@NonNull View itemView, OnBookListener onBookListener) {
            super(itemView);
            iv_bookImg = itemView.findViewById(R.id.iv_bookImg);
            tv_bookName = itemView.findViewById(R.id.tv_title);
            tv_bookAuthor = itemView.findViewById(R.id.tv_author);
            tv_bookPrice = itemView.findViewById(R.id.tv_price);
            this.onBookListener = onBookListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onBookListener.onBookClick(getAdapterPosition());
        }
    }

    // Using interface to detect the Click
    // implements it to SearchActivity
    public interface OnBookListener {
        void onBookClick(int position);
    }
}
