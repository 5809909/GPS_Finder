package com.github.a5809909.gps_finder.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.a5809909.gps_finder.Loaders.IAsyncTaskListener;
import com.github.a5809909.gps_finder.Model.LocationModel;
import com.github.a5809909.gps_finder.R;
import com.github.a5809909.gps_finder.Sql.DatabaseHelper;

public class LocationFragment extends Fragment implements IAsyncTaskListener {

    private static final String TAG = LocationFragment.class.getSimpleName();
    Context mContext;
    LocationModel mLocationModel;
    TextView textViewDateAndTime, textViewCellID, textViewLAC, textViewMNC, textViewMCC, textViewLatitude, textViewLongitude, textViewAccuracy,
            textViewAddress;

    public LocationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_location, container, false);
        mLocationModel = new LocationModel();
        initFragment(view);
        return view;
    }

    @Override
    public void finishedAsyncTask() {
        Log.i(TAG, "finishedAsyncTask: ");
    }

    private void initFragment(View pView) {
        Log.i(TAG, "initFragment: ");

        initFragmentViews(pView);
        setFragmentViews(mContext);
    }

    public void setFragmentViews(Context pContext) {
        try {
            DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
            mLocationModel = databaseHelper.getAllLocationModels();
            databaseHelper.close();

            textViewDateAndTime.setText(mLocationModel.getDateAndTime());
            textViewCellID.setText(mLocationModel.getCellId());
            textViewLAC.setText(mLocationModel.getLac());
            textViewMCC.setText(mLocationModel.getMcc());
            textViewMNC.setText(mLocationModel.getMnc());
            textViewLatitude.setText(mLocationModel.getLat());
            textViewLongitude.setText(mLocationModel.getLng());
            textViewAccuracy.setText(mLocationModel.getAcc());
            textViewAddress.setText(mLocationModel.getAddress());
            Log.i(TAG, "setFragmentViews: " + mLocationModel.getDateAndTime());
        } catch (Exception pE) {

        }
    }

    private void initFragmentViews(View pView) {
        Log.i(TAG, "initFragmentViews: ");
        textViewDateAndTime = pView.findViewById(R.id.text_date_and_time);
        textViewCellID = pView.findViewById(R.id.text_cell_id);
        textViewLAC = pView.findViewById(R.id.text_lac);
        textViewMNC = pView.findViewById(R.id.text_mnc);
        textViewMCC = pView.findViewById(R.id.text_mcc);
        textViewLatitude = pView.findViewById(R.id.text_latitude);
        textViewLongitude = pView.findViewById(R.id.text_longitude);
        textViewAccuracy = pView.findViewById(R.id.text_accuracy);
        textViewAddress = pView.findViewById(R.id.text_address);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
    }
}
