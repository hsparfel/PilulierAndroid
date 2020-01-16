package com.pouillos.monpilulier.activities.listallx;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.MyProfilActivity;
import com.pouillos.monpilulier.views.adapter.ListAllProfilAdapter;

import java.io.Serializable;

public class ListAllProfilActivity extends AppCompatActivity implements Serializable{

    private  ImageButton buttonAdd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all_profil);

        final RecyclerView rv = (RecyclerView) findViewById(R.id.list_all_profil_recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new ListAllProfilAdapter());

        buttonAdd = findViewById(R.id.buttonAdd);

        buttonAdd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent newProfilActivity = new Intent(ListAllProfilActivity.this, MyProfilActivity.class);
                        newProfilActivity.putExtra("activitySource", ListAllProfilActivity.class);
                        startActivity(newProfilActivity);
                        finish();
                    }
                }
        );
    }
}
