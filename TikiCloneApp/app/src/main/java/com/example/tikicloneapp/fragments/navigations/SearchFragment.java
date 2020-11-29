package com.example.tikicloneapp.fragments.navigations;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tikicloneapp.R;
import com.example.tikicloneapp.activities.ListProductActivity;
import com.example.tikicloneapp.activities.MainActivity;
import com.example.tikicloneapp.activities.SearchActivity;
import com.example.tikicloneapp.adapters.SearchesAdapter;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.tikicloneapp.activities.MainActivity.dbVolley;

public class SearchFragment extends Fragment {
    private ImageButton ibBack, ibClear, ibSearch;
    private EditText edtSearch;
    private RecyclerView rvSearches;
    private LinearLayout llSearches;

    private SearchesAdapter searchesAdapter;
    private ArrayList<String> searchKeys = new ArrayList<>();

    private final int REQUEST_CODE_REFRESH = 1234;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        initWidget(view);

        setEachView();

        return view;
    }

    private void initWidget(View view) {
        ibBack = view.findViewById(R.id.imageButton_back);
        ibClear = view.findViewById(R.id.imageButton_clear);
        edtSearch = view.findViewById(R.id.editText_search);
        rvSearches = view.findViewById(R.id.recyclerView_searches);
        llSearches = view.findViewById(R.id.linearLayout_searches);
        ibSearch = view.findViewById(R.id.imageButton_search);
    }

    private void setEachView() {
        edtSearch.requestFocus();
        showKeyboard();

        setSearchesRV();

        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    String searchKey = edtSearch.getText().toString().trim();
                    if (!searchKey.isEmpty()) {

                        Intent intent = new Intent(getContext(), ListProductActivity.class);
                        intent.putExtra("keySearch", searchKey);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                        } else startActivity(intent);

                        insertSearchKey(searchKey);
                    }
                    return true;
                }
                return false;
            }
        });

        ibSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchKey = edtSearch.getText().toString().trim();
                if (!searchKey.isEmpty()) {

                    Intent intent = new Intent(getContext(), ListProductActivity.class);
                    intent.putExtra("keySearch", searchKey);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        startActivityForResult(intent, REQUEST_CODE_REFRESH, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                    } else startActivityForResult(intent, REQUEST_CODE_REFRESH);
                    insertSearchKey(searchKey);
                }

            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = edtSearch.getText().toString();
                if (text.isEmpty()) {
                    ibClear.setVisibility(View.INVISIBLE);
                } else ibClear.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ibClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSearch.setText("");
            }
        });

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getContext() instanceof MainActivity) {
                    ((MainActivity) getActivity()).LoadFragment(((MainActivity) getActivity()).previous_Of_SearchFragment);
                } else if (getContext() instanceof SearchActivity) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getActivity().finishAfterTransition();
                    } else getActivity().finish();
                }
            }
        });

    }

    private void showKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public void closeKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        closeKeyboard();
    }


    @Override
    public void onPause() {
        super.onPause();
        closeKeyboard();
    }

    private void setSearchesRV() {
        if (searchesAdapter == null) {
            searchesAdapter = new SearchesAdapter(searchKeys, getActivity());
            rvSearches.setAdapter(searchesAdapter);
            rvSearches.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        getSearches();
    }

    public void insertSearchKey(final String searchKey) {
        if (MainActivity.idUser == 0)
            return;

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.dbVolley.URL_INSERT_SEARCH_KEY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("thang", "onResponse: " + response);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("thang", "Insert order: " + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("idUser", String.valueOf(MainActivity.idUser));
                params.put("searchKey", searchKey);

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void getSearches() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, dbVolley.URL_GET_SEARCHES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() != 0) {
                        llSearches.setVisibility(View.VISIBLE);
                        searchKeys.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                String searchKey = jsonArray.getString(i);

                                searchKeys.add(searchKey);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        searchesAdapter.notifyDataSetChanged();
                    } else {
                        llSearches.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("thang", "Insert order: " + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("idUser", String.valueOf(MainActivity.idUser));

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_REFRESH) {

            getSearches();
        }
    }

}
