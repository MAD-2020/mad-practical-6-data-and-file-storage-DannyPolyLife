package sg.edu.np.week_6_whackamole_3_0;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class CustomScoreViewHolder extends RecyclerView.ViewHolder {
    /* Hint:
        1. This is a customised view holder for the recyclerView list @ levels selection page
     */
    private static final String FILENAME = "CustomScoreViewHolder.java";
    private static final String TAG = "Whack-A-Mole3.0!";

    TextView levelNumTextView;
    TextView highestScoreTextView;
    ConstraintLayout parentLayout;

    public CustomScoreViewHolder(final View itemView){
        super(itemView);

        /* Hint:
        This method dictates the viewholder contents and links the widget to the objects for manipulation.
         */
        levelNumTextView = itemView.findViewById(R.id.levelNumTextView);
        highestScoreTextView = itemView.findViewById(R.id.highestScoreTextView);
        parentLayout = itemView.findViewById(R.id.parentLayout);
    }
}
