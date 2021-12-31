package com.yotor.solution.mytimetable.portal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

//import com.crashlytics.android.Crashlytics;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.yotor.solution.mytimetable.Analytics;
import com.yotor.solution.mytimetable.R;
import com.yotor.solution.mytimetable.portal.database.StudentDB;
import com.yotor.solution.mytimetable.portal.model.GpaList;

import java.util.List;

public class GpaListActivity extends AppCompatActivity {
    private ListView listView;
    private StudentDB db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.applyConfig(this);
        setContentView(R.layout.gpa_list);
        db = new StudentDB(this);
        List<GpaList> gpaLists = db.getGpaList();
        if (gpaLists.size() == 0)
            startActivity(new Intent(this, GpaCalculator.class));
        listView = findViewById(R.id.gpa_listview);
        listView.setEmptyView(findViewById(R.id.empty_view));
        FloatingActionButton fab = findViewById(R.id.gpa_fab);
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), GpaCalculator.class);
                    startActivity(intent);
                   //Analytics.logClickEvent("GpaListActivity","create gpa fab clicked");
                }
            });

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            final GpaListAdapter adapter = new GpaListAdapter(this, db.getGpaList());
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                   //Analytics.logClickEvent("GpaListActivity","Grade List Item clicked pos "+i);
                    Intent intent = new Intent(getApplicationContext(), GpaCalculator.class);
                    String name = ((GpaList) adapterView.getItemAtPosition(i)).getName();
                    intent.putExtra("gpaName", name);
                    startActivity(intent);
                }
            });
        } catch (Exception e) {
           //Crashlytics.logException(e);
        }
    }
}
