package com.example.tikicloneapp.fragments.navigations;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tikicloneapp.MyClass;
import com.example.tikicloneapp.R;
import com.example.tikicloneapp.activities.MainActivity;
import com.example.tikicloneapp.adapters.CatalogGrParentsAdapter;
import com.example.tikicloneapp.adapters.CatalogParentAdapter;
import com.example.tikicloneapp.models.CatalogGrandParent;
import com.example.tikicloneapp.models.CatalogParent;

import java.util.ArrayList;

public class CategoryFragment extends Fragment {
    private LinearLayout layoutSearch;
    public static LinearLayout layLoadingParent, layLoadingChild;
    private RecyclerView rvCataParents, rvCataGrParents;
    private ImageButton ibCart;
    public static TextView tvNameCataGrParents;

    public static CatalogParentAdapter parentAdapter;
    private CatalogGrParentsAdapter grParentsAdapter;


    public static ArrayList<CatalogParent> parentArrayList = new ArrayList<>();
    public static ArrayList<CatalogGrandParent> grParentArrayList = new ArrayList<>();

    public static int grandIndex = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        initWidget(view);

        setEachView();
        return view;
    }

    private void initWidget(View view) {
        layoutSearch = view.findViewById(R.id.layout_searchName);
        rvCataParents = view.findViewById(R.id.recyclerView_catalogParents);
        tvNameCataGrParents = view.findViewById(R.id.textView_name_catalogGrParent);
        rvCataGrParents = view.findViewById(R.id.recyclerView_catalogGrParents);
        ibCart = view.findViewById(R.id.imageButton_cart);
        layLoadingChild = view.findViewById(R.id.loadingPanel_children);
        layLoadingParent = view.findViewById(R.id.loadingPanel_parent);
    }

    private void setEachView() {
        MyClass.callPanel(layLoadingParent, 800);
        layoutSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).previous_Of_SearchFragment = R.id.menu_BottomCategory;
                ((MainActivity) getActivity()).LoadFragment(R.id.menu_BottomSearch);
            }
        });

        ibCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.dbVolley.checkTransact_goToCart(MainActivity.idUser);
            }
        });

        setGrParentsAdapter();
        setParentAdapter();

    }

    public static void setOnClickGrand(CatalogGrandParent grand) {
        MyClass.callPanel(layLoadingChild, 800);
        MainActivity.dbVolley.getCatalogParents(parentArrayList, parentAdapter, grand.getmId());

        tvNameCataGrParents.setText(grand.getmName());
    }

    private void setParentAdapter() {
        DividerItemDecoration itemDecoParents = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecoParents.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider_item_parents));
        parentAdapter = new CatalogParentAdapter(getContext(), parentArrayList);
        rvCataParents.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCataParents.addItemDecoration(itemDecoParents);
        rvCataParents.setAdapter(parentAdapter);

        MainActivity.dbVolley.getCatalogParents(parentArrayList, parentAdapter, 0);
        tvNameCataGrParents.setText("Gợi ý cho bạn");
    }

    private void setGrParentsAdapter() {
        DividerItemDecoration itemDecoGrParents = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecoGrParents.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider_product_cart));
        grParentsAdapter = new CatalogGrParentsAdapter(getContext(), grParentArrayList);
        rvCataGrParents.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCataGrParents.addItemDecoration(itemDecoGrParents);
        rvCataGrParents.setAdapter(grParentsAdapter);

        MainActivity.dbVolley.getCatalogGrParents(grParentArrayList, grParentsAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        grandIndex = 0;
    }
}
