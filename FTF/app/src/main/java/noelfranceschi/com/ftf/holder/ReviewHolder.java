package noelfranceschi.com.ftf.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import noelfranceschi.com.ftf.R;
import noelfranceschi.com.ftf.model.FoodTruckReview;

/**
 * Created by noelfranceschi on 10/13/17.
 */

public class ReviewHolder extends RecyclerView.ViewHolder {

    //: Vars
    private TextView title;
    private TextView text;

    public ReviewHolder(View itemView) {
        super(itemView);

        this.title = (TextView) itemView.findViewById(R.id.review_title);
        this.text = (TextView) itemView.findViewById(R.id.review_text);
    }

    public void updateUI(FoodTruckReview review) {
        title.setText(review.getTitle());
        text.setText(review.getText());
    }
}
