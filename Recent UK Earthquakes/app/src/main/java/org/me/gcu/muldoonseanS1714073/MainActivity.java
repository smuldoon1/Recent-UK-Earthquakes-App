// Code written by Sean Muldoon
// S1714073
// Mobile Platform Development coursework submission

package org.me.gcu.muldoonseanS1714073;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends EarthquakeActivity implements OnClickListener, AdapterView.OnItemSelectedListener, RecyclerClickListener
{
    private ListRecyclerAdapter recyclerAdapter;
    private RecyclerView recyclerView;

    private Spinner sortSpinner;
    private Button refreshButton;

    private TextView lastBuildDateText;

    private static SortMode currentSortMode;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Set up the raw links to the graphical components
        sortSpinner = (Spinner)findViewById(R.id.sortSpinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.sort_modes, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(spinnerAdapter);
        sortSpinner.setOnItemSelectedListener(this);

        refreshButton = (Button)findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(this);

        recyclerView = (RecyclerView)findViewById(R.id.eqRecyclerView);
        recyclerAdapter = new ListRecyclerAdapter(getApplicationContext(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(recyclerAdapter);

        lastBuildDateText = (TextView)findViewById(R.id.buildDate);
    }

    @Override
    public void onAsyncTaskStarted() {
        lastBuildDateText.setText(getResources().getString(R.string.build_date) + getResources().getString(R.string.refreshing_message));
    }

    @Override
    public void onAsyncTaskComplete() {
        EarthquakeData.sort(currentSortMode);

        recyclerAdapter.notifyDataSetChanged();

        lastBuildDateText.setText(getResources().getString(R.string.build_date) + " " +  EarthquakeData.getLastBuildDate());
    }

    public void onClick(View view)
    {
        if (view == refreshButton) {
            startTask();
        }
    }

    // Called when a new sort mode has been clicked in the spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        currentSortMode = SortMode.getSortMode(position);
        startTask();
    }

    public void onNothingSelected(AdapterView<?> parent) { }

    @Override
    public void onRecyclerItemClicked(View view, int position) {
        Log.d("RecyclerView", "Clicked item at position: " + position);
        startDetailedViewActivity(position);
    }
}