package com.andy6804tw.dengueweather.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andy6804tw.dengueweather.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckedDengueFragment extends Fragment {


    public CheckedDengueFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_checked_dengue, container, false);
    }

}
