package org.me.gcu.equakestartercode;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private static RecyclerClickListener clickListener;

    private Context context;

    private boolean darkMode;

    public RecyclerAdapter(Context context, RecyclerClickListener clickListener)
    {
        this.context = context;
        this.clickListener = clickListener;
        darkMode = EarthquakeActivity.isDarkMode(context);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private FrameLayout eqFrameLayout;
        private TextView eqLocation;
        private TextView eqMagnitude;
        private TextView eqDateTime;
        private TextView eqDepth;

        public ViewHolder(View view, Context context) {
            super(view);

            // Define click listener for the ViewHolder's View
            eqFrameLayout = (FrameLayout)view.findViewById(R.id.eqFrameLayout);
            eqLocation = (TextView) view.findViewById(R.id.eqLocation);
            eqMagnitude = (TextView) view.findViewById(R.id.eqMagnitude);
            eqDateTime = (TextView) view.findViewById(R.id.eqDateTime);

            // Depth text view only exists when device is in landscape mode
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                eqDepth = (TextView) view.findViewById(R.id.eqDepth);
            else
                eqDepth = null;

            view.setOnClickListener(this);
        }

        public FrameLayout getEqFrameLayout() {
            return eqFrameLayout;
        }

        public TextView getEqLocation() {
            return eqLocation;
        }

        public TextView getEqMagnitude() {
            return eqMagnitude;
        }

        public TextView getEqDateTime() { return eqDateTime; }

        public TextView getEqDepth() { return eqDepth; }

        @Override
        public void onClick(View view) {
            clickListener.onRecyclerItemClicked(view, this.getAdapterPosition());
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup,false);

        return new ViewHolder(view, context);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Earthquake earthquake = MainActivity.earthquakes.get(position);
        viewHolder.getEqLocation().setText(earthquake.getLocation());
        viewHolder.getEqDateTime().setText(earthquake.getDate());
        viewHolder.getEqMagnitude().setText(Float.toString(earthquake.getMagnitude()));

        // Only set the depth text view if the device is in landscape mode
        // Currently seems to occasionally cause a crash due a null reference exception
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            viewHolder.getEqDepth().setText(earthquake.getDepth() + "km");

        if (darkMode)
            viewHolder.getEqFrameLayout().setBackgroundColor(context.getColor(position % 2 == 0 ? R.color.item_dark_1 : R.color.item_dark_2));
        else
            viewHolder.getEqFrameLayout().setBackgroundColor(context.getColor(position % 2 == 0 ? R.color.item_light_1 : R.color.item_light_2));
        viewHolder.getEqMagnitude().setBackgroundColor(Color.parseColor(MagnitudeColourCoding.getColour(earthquake.getMagnitude(), context)));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return MainActivity.earthquakes.size();
    }
}