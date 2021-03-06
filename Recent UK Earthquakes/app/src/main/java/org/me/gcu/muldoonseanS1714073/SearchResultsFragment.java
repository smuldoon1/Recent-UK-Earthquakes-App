// Code written by Sean Muldoon
// S1714073
// Mobile Platform Development coursework submission

package org.me.gcu.muldoonseanS1714073;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SearchResultsFragment extends Fragment implements RecyclerClickListener
{
    private String startDate;
    private String endDate;

    private TextView searchResultsText;

    private SearchRecyclerAdapter recyclerAdapter;
    private RecyclerView recyclerView;

    public SearchResultsFragment() {

    }

    public SearchResultsFragment(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_results, parent, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // If two dates have aleady been used to search, restore these dates
        if (savedInstanceState != null) {
            startDate = savedInstanceState.getString("startDate");
            endDate = savedInstanceState.getString("endDate");
        }

        searchResultsText = (TextView)view.findViewById(R.id.searchResults);

        recyclerView = (RecyclerView)view.findViewById(R.id.eqRecyclerView);
        recyclerAdapter = new SearchRecyclerAdapter(getContext(), this, EarthquakeData.getEarthquakesInRange(startDate, endDate));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recyclerAdapter);

        updateResultsMessage();
    }

    @Override
    public void onRecyclerItemClicked(View view, int index) {
        EarthquakeActivity activity = (EarthquakeActivity)getActivity();
        activity.startDetailedViewActivity(index);
    }

    // Shows how many earthquakes were found in the given range
    public void updateResultsMessage() {
        List<Earthquake> earthquakes = EarthquakeData.getEarthquakesInRange(startDate, endDate);

        searchResultsText.setText("Search returned " + earthquakes.size() + " earthquake(s) in the range of dates entered.");
    }

    // Saves the start and end dates so they can be reused when the user rotates the device
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("startDate", startDate);
        savedInstanceState.putString("endDate", endDate);

        super.onSaveInstanceState(savedInstanceState);
    }
}
