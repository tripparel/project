package com.example.tripparel;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class SelectCategoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);

        findViewById(R.id.btn_next).setOnClickListener(v -> navigateToMain());
    }

    private void navigateToMain() {
        ArrayList<String> selectedCategories = new ArrayList<>();

        if (((CheckBox) findViewById(R.id.cb_vintage)).isChecked()) selectedCategories.add("빈티지");
        if (((CheckBox) findViewById(R.id.cb_minimal)).isChecked()) selectedCategories.add("미니멀");
        if (((CheckBox) findViewById(R.id.cb_casual)).isChecked()) selectedCategories.add("캐주얼");
        if (((CheckBox) findViewById(R.id.cb_sports)).isChecked()) selectedCategories.add("스포츠");
        if (((CheckBox) findViewById(R.id.cb_gorpcore)).isChecked()) selectedCategories.add("고프코어");
        if (((CheckBox) findViewById(R.id.cb_street)).isChecked()) selectedCategories.add("스트릿");
        if (((CheckBox) findViewById(R.id.cb_amekaji)).isChecked()) selectedCategories.add("아메카지");
        if (((CheckBox) findViewById(R.id.cb_editshop)).isChecked()) selectedCategories.add("편집샵");
        if (((CheckBox) findViewById(R.id.cb_classic)).isChecked()) selectedCategories.add("클래식");
        if (((CheckBox) findViewById(R.id.cb_spa)).isChecked()) selectedCategories.add("SPA");
        if (((CheckBox) findViewById(R.id.cb_luxury)).isChecked()) selectedCategories.add("명품");

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("categories", selectedCategories);
        intent.putExtra("gender", getIntent().getStringExtra("gender"));
        startActivity(intent);
    }
}

