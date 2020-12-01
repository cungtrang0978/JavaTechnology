package com.example.tikicloneapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tikicloneapp.R;
import com.example.tikicloneapp.adapters.ProductListAdapter;
import com.example.tikicloneapp.models.Product;
import com.example.tikicloneapp.models.Rate;

import java.sql.Timestamp;

public class WriteReviewActivity extends AppCompatActivity {

    private ImageView ivImageProduct;
    private ImageButton ibBack;
    private EditText edtReview;
    private TextView tvName, tvTitleReview;
    private ImageView ivStar1, ivStar2, ivStar3, ivStar4, ivStar5;
    private Button btnSend;
    private Product product;
    private int ratePoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review_activity);

        initWidget();

        product = (Product) getIntent().getSerializableExtra("product");
        ratePoint = getIntent().getIntExtra("ratePoint", 0);

        setViews();
    }

    private void initWidget() {
        ibBack = findViewById(R.id.imageButton_back);
        ivImageProduct = findViewById(R.id.imageView_product);
        tvName = findViewById(R.id.textView_nameProduct);
        ivStar1 = findViewById(R.id.imageView_star1);
        ivStar2 = findViewById(R.id.imageView_star2);
        ivStar3 = findViewById(R.id.imageView_star3);
        ivStar4 = findViewById(R.id.imageView_star4);
        ivStar5 = findViewById(R.id.imageView_star5);
        tvTitleReview = findViewById(R.id.textView_titleReview);
        edtReview = findViewById(R.id.editText_review);
        btnSend = findViewById(R.id.button_send);
    }

    private void setViews() {
        ProductListAdapter.loadImageFromUrl(product.getImageUrl(), ivImageProduct);
        tvName.setText(product.getName());
        setTitleName();
        setStars();

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else finish();
            }
        });

        ivStar1.setOnClickListener(starOnClickListener(1));
        ivStar2.setOnClickListener(starOnClickListener(2));
        ivStar3.setOnClickListener(starOnClickListener(3));
        ivStar4.setOnClickListener(starOnClickListener(4));
        ivStar5.setOnClickListener(starOnClickListener(5));

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( ratePoint != 0) {
                    Timestamp createdAt = new Timestamp(System.currentTimeMillis());
                    Rate rate = new Rate(ratePoint, edtReview.getText().toString(), createdAt,product.getId(), MainActivity.idUser);
                    MainActivity.dbVolley.insertRate(rate);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        finishAfterTransition();
                    } else finish();
                    setResult(RESULT_OK);
                }
            }
        });
    }

    final View.OnClickListener starOnClickListener(final int _ratePoint) {

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("thang", "onClick: " + _ratePoint);
                ratePoint = _ratePoint;
                setStars();
                setTitleName();
            }
        };
    }

    private void setStars() {
        if (ratePoint == 1) {
            ivStar1.setImageResource(R.drawable.star_on);
            ivStar2.setImageResource(R.drawable.star_no_review);
            ivStar3.setImageResource(R.drawable.star_no_review);
            ivStar4.setImageResource(R.drawable.star_no_review);
            ivStar5.setImageResource(R.drawable.star_no_review);
        } else if (ratePoint == 2) {
            ivStar1.setImageResource(R.drawable.star_on);
            ivStar2.setImageResource(R.drawable.star_on);
            ivStar3.setImageResource(R.drawable.star_no_review);
            ivStar4.setImageResource(R.drawable.star_no_review);
            ivStar5.setImageResource(R.drawable.star_no_review);
        } else if (ratePoint == 3) {
            ivStar1.setImageResource(R.drawable.star_on);
            ivStar2.setImageResource(R.drawable.star_on);
            ivStar3.setImageResource(R.drawable.star_on);
            ivStar4.setImageResource(R.drawable.star_no_review);
            ivStar5.setImageResource(R.drawable.star_no_review);
        } else if (ratePoint == 4) {
            ivStar1.setImageResource(R.drawable.star_on);
            ivStar2.setImageResource(R.drawable.star_on);
            ivStar3.setImageResource(R.drawable.star_on);
            ivStar4.setImageResource(R.drawable.star_on);
            ivStar5.setImageResource(R.drawable.star_no_review);
        } else if (ratePoint == 5) {
            ivStar1.setImageResource(R.drawable.star_on);
            ivStar2.setImageResource(R.drawable.star_on);
            ivStar3.setImageResource(R.drawable.star_on);
            ivStar4.setImageResource(R.drawable.star_on);
            ivStar5.setImageResource(R.drawable.star_on);
        }
    }

    private void setTitleName() {
        if (ratePoint == 1) {
            tvTitleReview.setText("Rất không hài lòng");
        } else if (ratePoint == 2) {
            tvTitleReview.setText("Không hài lòng");
        } else if (ratePoint == 3) {
            tvTitleReview.setText("Bình thường");
        } else if (ratePoint == 4) {
            tvTitleReview.setText("Hài lòng");
        } else if (ratePoint == 5) {
            tvTitleReview.setText("Rất hài lòng");
        }
    }
}