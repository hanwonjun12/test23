package com.example.galpi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.galpi.book.FB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;     // 파이어베이스 인증
    private DatabaseReference mDatabaseRef; // 실시간 데이터베이스
    private EditText mEtEmail, mEtPwd;      // 로그인 입력필드

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FB fb=new FB("Galpi");
        mFirebaseAuth = FirebaseAuth.getInstance(); // 객체 만들기
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Galpi"); // 객체 만들기

        mEtEmail = findViewById(R.id.et_email); // 연동
        mEtPwd = findViewById(R.id.et_pwd);


        Button btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() { // 이벤트 처리
            @Override
            public void onClick(View view) // 클릭 시
            {
                // 로그인 요청
                String strEmail = mEtEmail.getText().toString(); // 입력값 변수에 저장
                String strPwd = mEtPwd.getText().toString();

                mFirebaseAuth.signInWithEmailAndPassword(strEmail, strPwd)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(Task<AuthResult> task)
                    {
                            if (task.isSuccessful()) {
                                // 로그인 성공 !!!

                                Intent intent = new Intent(LoginActivity.this, SearchActivity.class);
                                startActivity(intent);
                                finish();   // 현재 액티비티 파괴
                            } else if (strEmail.equals("") || strPwd.equals("")) {
                                Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "로그인 실패..!", Toast.LENGTH_SHORT).show();
                            }
                    }
                });
            }
        });

        Button test = findViewById(R.id.test);

        //임시임시임시임시임시임시임시임시임시임시임시임시임시임시임시임시임시임시임시임시임시임시임시임시임시임시임시임시임시임시
        test.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(LoginActivity.this, SearchActivity.class);
                startActivity(intent);


            }

        });
        Button btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                // 회원가입 화면으로 이동
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}