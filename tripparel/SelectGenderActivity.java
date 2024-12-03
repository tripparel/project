package com.example.tripparel;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class SelectGenderActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_gender);

        findViewById(R.id.btn_male).setOnClickListener(v -> navigateToCategory("남성"));
        findViewById(R.id.btn_female).setOnClickListener(v -> navigateToCategory("여성"));
        findViewById(R.id.btn_any).setOnClickListener(v -> navigateToCategory("상관없음"));
    }

    private void navigateToCategory(String gender) {
        Intent intent = new Intent(this, SelectCategoryActivity.class);
        intent.putExtra("gender", gender);
        startActivity(intent);
    }
}

