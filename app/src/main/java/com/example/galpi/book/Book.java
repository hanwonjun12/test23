package com.example.galpi.book;

import java.util.ArrayList;
import java.util.Comparator;

public class Book {
    public String title;
    public String author;
    public String link;
    public String image;
    public String discount;
    public String publisher;
    public String pubdate;
    public String isbn;
    public String description;
    public ArrayList<String> keyword_title=new ArrayList<String>();

    public static int like=0;
    public static int dislike=0;

    public static Book get(int position) {
        return null;
    }

    public Book() {}

    // Book Constructor
    public Book(String title, String author, String image, String discount, String publisher, String pubdate, String description, String link) {
        this.title = title;
        this.author = author;
        this.image = image;
        this.discount = discount;
        this.publisher = publisher;
        this.pubdate = pubdate;
        this.description = description;
        this.link = link;
    }

    // 도서 정렬 시 필요한 Comparator
    public static Comparator<Book> BookPubdateComparator = new Comparator<Book>() {
        @Override
        public int compare(Book b1, Book b2) {
            return b2.getPubdate().compareTo(b1.getPubdate());
        }
    };

    public static Comparator<Book> BookDiscountLowComparator = new Comparator<Book>() {
        @Override
        public int compare(Book b1, Book b2) {
            return b1.getDiscount().compareTo(b2.getDiscount());
        }
    };


    // 도서에 관한 getter 와 setter
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getLink() { return link;}

    public void setLink(String link) { this.link = link; }

    public String getPublisher() { return publisher;}

    public void setPublisher(String publisher) { this.publisher = publisher;}

    public String getPubdate() { return pubdate; }

    public void setPubdate(String pubdate) { this.pubdate = pubdate; }


    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public void print(){
        System.out.println("-------------------");
        System.out.println(this.title);
        System.out.println(this.image);
        System.out.println(this.link);
        System.out.println(this.author);
        System.out.println(this.discount);
        System.out.println(this.publisher);
        System.out.println(this.pubdate);
        System.out.println(this.isbn);
        System.out.println(this.description);

        System.out.println("-------------------");

    }

}
