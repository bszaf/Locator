package com.example.bartoszszafran.locator;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class BuildingsActivity extends AppCompatActivity {

    final BuildingArchive ba = App.getBuildingArchive();
    private ArrayAdapter adapter;
    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buildings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listview = (ListView) findViewById(R.id.building_list);
        this.adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        String [] buildingNames = new String[ba.listBuildings().size()];
        ba.listBuildings().toArray(buildingNames);
        for ( String s : buildingNames) {
            Log.d("buildings view", "array: " + s);
            adapter.add(s);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BuildingsActivity.this, PaintRoomActivity.class);
                startActivity(i);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = (String) parent.getItemAtPosition(position);
                BuildingTopology r = ba.getBuidling(name);
                Log.d("buildings view", "Clicked " + name);
                Intent intent = new Intent(BuildingsActivity.this, BuildingActivity.class);
                intent.putExtra("building", name);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("buildings view", "Resumed");
        adapter.clear();

        String [] buildingNames = new String[ba.listBuildings().size()];
        ba.listBuildings().toArray(buildingNames);
        for ( String s : buildingNames) {
            Log.d("buildings view", "array: " + s);
            adapter.add(s);
        }
    }
}
