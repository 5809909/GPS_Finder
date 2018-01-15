package com.github.a5809909.gps_finder.Fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.github.a5809909.gps_finder.R;
import com.github.a5809909.gps_finder.Sql.DatabaseHelper;

public class DatabaseFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemSelectedListener, OnItemLongClickListener {

    private static final String TAG = DatabaseFragment.class.getSimpleName();
    SwipeRefreshLayout mSwipeRefreshLayout;
    ListView listView;
    Cursor mCursor;
    SimpleCursorAdapter mSimpleCursorAdapter;
    DatabaseHelper databaseHelper;
    String[] from = new String[]{databaseHelper.COLUMN_DAY_AND_TIME, databaseHelper.COLUMN_LAT, databaseHelper.COLUMN_LNG,
            databaseHelper.COLUMN_ACCURACY, databaseHelper.COLUMN_ADDRESS};
    int[] to = new int[]{R.id.text_view_date_and_time, R.id.text_view_latitude, R.id.text_view_longitude, R.id.text_view_accurancy, R.id.text_view_address};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_database, container, false);
        listView = view.findViewById(R.id.listView1);
        registerForContextMenu(listView);

        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        Log.i(TAG, "onCreateView: ");
        onRefresh();

        listView.setOnItemLongClickListener(this);
        listView.setOnItemSelectedListener(this);

        return view;
    }

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.context_menu, menu);
//    }
//
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//        switch (item.getItemId()) {
//            case R.id.edit:
//                editItem(info.position); // метод, выполняющий действие при редактировании пункта меню
//                return true;
//            case R.id.delete:
//                deleteItem(info.position); //метод, выполняющий действие при удалении пункта меню
//                return true;
//            default:
//                return super.onContextItemSelected(item);
//        }
//    }

    @Override
    public void onRefresh() {
        databaseHelper = new DatabaseHelper(getActivity());
        mCursor = databaseHelper.getLastItem();
        mSimpleCursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.item_list_view, mCursor, from, to, 0);
        listView.setAdapter(mSimpleCursorAdapter);
        mSwipeRefreshLayout.setRefreshing(false);
        databaseHelper.close();
        Log.i(TAG, "onRefresh: ");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> pAdapterView, View pView, int pI, long pL) {
        Cursor m = ((Cursor) mSimpleCursorAdapter.getItem(pI));
        String mmm = m.getString(0);
        Log.i(TAG, "onItemLongClick: " + mmm + ", PI:" + pI + ", PL:" + pL);
        databaseHelper = new DatabaseHelper(getActivity());
        databaseHelper.deleteIditem(mmm);
        databaseHelper.close();
        onRefresh();
        return false;

    }

    @Override
    public void onItemSelected(AdapterView<?> pAdapterView, View pView, int pI, long pL) {
        //  Log.i(TAG, "onItemSelected: " + ((LocationModel) mSimpleCursorAdapter.getItem(pI)).getDateAndTime());

    }

    @Override
    public void onNothingSelected(AdapterView<?> pAdapterView) {

    }
}

