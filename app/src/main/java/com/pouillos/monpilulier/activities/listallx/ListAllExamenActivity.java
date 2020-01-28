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
import com.pouillos.monpilulier.activities.newx.NewExamenActivity;
import com.pouillos.monpilulier.views.adapter.ListAllExamenAdapter;

import java.io.Serializable;

public class ListAllExamenActivity extends AppCompatActivity implements Serializable{

    private  ImageButton buttonAdd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all_examen);

        final RecyclerView rv = (RecyclerView) findViewById(R.id.list_all_examen_recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new ListAllExamenAdapter());

        buttonAdd = findViewById(R.id.buttonAdd);

        buttonAdd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent newExamenActivity = new Intent(ListAllExamenActivity.this, NewExamenActivity.class);
                        newExamenActivity.putExtra("activitySource", ListAllExamenActivity.class);
                        startActivity(newExamenActivity);
                        finish();
                    }
                }
        );
    }
}