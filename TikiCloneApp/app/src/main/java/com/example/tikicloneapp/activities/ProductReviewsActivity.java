package com.example.tikicloneapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tikicloneapp.R;
import com.example.tikicloneapp.adapters.ReviewAdapter;
import com.example.tikicloneapp.models.Rate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProductReviewsActivity extends AppCompatActivity {
    private ImageButton ibBack;
    private TextView tvRatePoint, tvReviewQuantity, tvFiveStars, tvFourStars, tvThreeStars, tvTwoStars, tvOneStar;
    private ImageView ivStarDetail_1, ivStarDetail_2, ivStarDetail_3, ivStarDetail_4, ivStarDetail_5;
    private RecyclerView rvReviews;

    private ArrayList<Rate> rates = new ArrayList<>();
    ArrayList<Rate>
            oneStarRates = new ArrayList<>(),
            twoStarsRates = new ArrayList<>(),
            threeStarRates = new ArrayList<>(),
            fourStarsRates = new ArrayList<>(),
            fiveStarRates = new ArrayList<>();

    private ReviewAdapter reviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_reviews);

        Bundle extra = getIntent().getBundleExtra("extra");
        rates = (ArrayList<Rate>) extra.getSerializable("rates");
        initWidget();

        setViews();
    }

    private void initWidget() {
        ibBack = findViewById(R.id.imageButton_back);
        tvRatePoint = findViewById(R.id.textView_ratePoint);
        tvReviewQuantity = findViewById(R.id.textView_reviewQuantity);
        tvFiveStars = findViewById(R.id.textView_fiveStars);
        tvFourStars = findViewById(R.id.textView_fourStars);
        tvThreeStars = findViewById(R.id.textView_threeStars);
        tvTwoStars = findViewById(R.id.textView_twoStars);
        tvOneStar = findViewById(R.id.textView_oneStar);
        ivStarDetail_1 = findViewById(R.id.imageView_starDetail_1);
        ivStarDetail_2 = findViewById(R.id.imageView_starDetail_2);
        ivStarDetail_3 = findViewById(R.id.imageView_starDetail_3);
        ivStarDetail_4 = findViewById(R.id.imageView_starDetail_4);
        ivStarDetail_5 = findViewById(R.id.imageView_starDetail_5);
        rvReviews = findViewById(R.id.recyclerView_reviews);
    }

    private void setViews() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvReviewQuantity.setText(rates.size() + " nhận xét");

        tvRatePoint.setText(getAverageRating());

        initSubReviews();
        setRvReviews();
    }

    String getAverageRating(){
        int sum = 0;
        for(int i=0; i<rates.size(); i++){
            sum+= rates.get(i).getRatePoint();
        }
        DecimalFormat df = new DecimalFormat("#.0");
        df.setRoundingMode(RoundingMode.CEILING);

        return  df.format((double) sum/rates.size());
    }

    // init arrayList of OneStars, arrayList of TwoStars, arrayList of ThreeStars,arrayList of FourStars
    //  arrayList of FiveStars
    @SuppressLint("SetTextI18n")
    private void initSubReviews() {
        for (int i = 0; i < rates.size(); i++){
            Rate rate = rates.get(i);
            switch (rate.getRatePoint()) {
                case 1:
                    oneStarRates.add(rate);
                    break;
                case 2:
                    twoStarsRates.add(rate);
                    break;
                case 3:
                    threeStarRates.add(rate);
                    break;
                case 4:
                    fourStarsRates.add(rate);
                    break;
                case 5:
                    fiveStarRates.add(rate);
                    break;
            }

        }
        tvFiveStars.setText(fiveStarRates.size() + "");
        tvFourStars.setText(fourStarsRates.size() + "");
        tvThreeStars.setText(threeStarRates.size() + "");
        tvTwoStars.setText(twoStarsRates.size() + "");
        tvOneStar.setText(oneStarRates.size() + "");
    }

    public void setRvReviews() {
//        callLoadingPanel_parent();
        if (reviewAdapter == null) {
            DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
            itemDecoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(rvReviews.getContext(), R.drawable.divider_product_cart)));

            reviewAdapter = new ReviewAdapter(this, rates);
            rvReviews.setLayoutManager(new LinearLayoutManager(this));
            rvReviews.addItemDecoration(itemDecoration);
            rvReviews.setAdapter(reviewAdapter);
        }


    }


}