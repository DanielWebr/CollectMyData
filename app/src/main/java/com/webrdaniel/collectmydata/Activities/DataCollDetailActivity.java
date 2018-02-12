package com.webrdaniel.collectmydata.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.webrdaniel.collectmydata.DatabaseHelper;
import com.webrdaniel.collectmydata.Fragments.RecordsListFragment;
import com.webrdaniel.collectmydata.Fragments.RecordsOverviewFragment;
import com.webrdaniel.collectmydata.Models.DataCollItem;
import com.webrdaniel.collectmydata.Models.Record;
import com.webrdaniel.collectmydata.Models.RecordComparator;
import com.webrdaniel.collectmydata.R;
import com.webrdaniel.collectmydata.Utils.DateUtils;
import com.webrdaniel.collectmydata.Utils.DialogUtils;
import com.webrdaniel.collectmydata.Utils.KeyboardUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DataCollDetailActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    @BindView(R.id.fab_records) FloatingActionButton fab;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.viewpager_data_coll_detail) ViewPager viewPager;
    @BindView(R.id.tabs_data_coll_detail) TabLayout tabLayout;
    public DatabaseHelper databaseHelper;
    public RecordsOverviewFragment recordsOverviewFragment;
    public ArrayList<Record> records;
    private HashSet<Date> mStoredDates;
    private int mDataCollItemId;
    private int mFilterDatesCount;
    private RecordsListFragment mRecordsListFragment;
    private TextInputEditText mDialogDateTiet;
    private TextInputLayout mDialogDateTil;
    private Menu mMenu;
    private Date mDialogDateToStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_coll_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        DataCollItem dataCollItem = (DataCollItem)getIntent().getSerializableExtra(MainActivity.DATA_COLL_ITEM);
        mDataCollItemId = dataCollItem.getId();
        databaseHelper = new DatabaseHelper(this);
        mStoredDates = databaseHelper.getDates(mDataCollItemId);
        records = databaseHelper.getRecords(mDataCollItemId);
        if(getSupportActionBar()!=null)getSupportActionBar().setTitle(dataCollItem.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAddValueDate();
            }
        });
        fab.hide();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    if(position == 0) fab.hide();
                    else fab.show();
            }
            @Override
            public void onPageSelected(int position) {

            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.mMenu = menu;
        getMenuInflater().inflate(R.menu.menu_data_coll_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.tv_filter:
                showPopupMenuFilter();return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        KeyboardUtils.hideKeyboard(this);
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        setDate(year, month, day);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter viewPagerAdapter = new Adapter(getSupportFragmentManager());
        recordsOverviewFragment = new RecordsOverviewFragment();
        mRecordsListFragment = new RecordsListFragment();
        viewPagerAdapter.addFragment(recordsOverviewFragment,getResources().getString(R.string.tab_overview));
        viewPagerAdapter.addFragment(mRecordsListFragment, getResources().getString(R.string.records));
        viewPager.setAdapter(viewPagerAdapter);
    }
    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        private final List<String> mFragmentTitleList = new ArrayList<>();

        Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }
        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }

    public void updateDatesList() {
        mStoredDates.clear();
        mStoredDates.addAll(databaseHelper.getDates(mDataCollItemId));
    }

    private void showDialogAddValueDate() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View dialog = layoutInflater.inflate(R.layout.input_dialog_record_value_date, (ViewGroup) getWindow().getDecorView().getRootView(),false);
        final TextInputEditText dialogValueTiet = dialog.findViewById(R.id.tiet_edit_record_date_value);
        mDialogDateTiet = dialog.findViewById(R.id.tiet_edit_record_value_date);
        mDialogDateTil = dialog.findViewById(R.id.til_edit_record_value_date);
        mDialogDateTiet.setText(DateUtils.dateToString(null, DateUtils.DATE_FORMAT_EDMM));

        Callable methodStoreValue = new Callable() {
            @Override
            public Object call() throws Exception {
                storeRecord(Double.parseDouble(dialogValueTiet.getText().toString()), mDialogDateToStore);
                return null;
            }
        };

        AlertDialog alertDialog = DialogUtils.showDialog(dialog,this, methodStoreValue,R.string.add);

        DialogUtils.onEnterConfirm(dialogValueTiet,alertDialog);
        KeyboardUtils.showKeyboard(this);

        mDialogDateTiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        final Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setEnabled(false);

        mDialogDateToStore = DateUtils.dateToDateFormat(DateUtils.DATE_FORMAT_DMY);
        if(mStoredDates.contains(mDialogDateToStore)) mDialogDateTil.setError(getResources().getString(R.string.error_date));

        mDialogDateTiet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if(mStoredDates.contains(mDialogDateToStore)) {
                    positiveButton.setEnabled(false);
                    mDialogDateTil.setError(getResources().getString(R.string.error_date));
                }
                else{
                    mDialogDateTil.setError(null);
                   if(!TextUtils.isEmpty(dialogValueTiet.getText())) positiveButton.setEnabled(true);
                }
            }
        });

        dialogValueTiet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s))positiveButton.setEnabled(false);
                else if(!mStoredDates.contains(mDialogDateToStore))positiveButton.setEnabled(true);
            }
        });
    }

    private void showDatePickerDialog(){
        Calendar calendar =Calendar.getInstance();
        calendar.setTime(mDialogDateToStore);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(DataCollDetailActivity.this, year, month, day);
        datePickerDialog.show(getFragmentManager(), "DateFragment");
        datePickerDialog.setMaxDate(Calendar.getInstance());
        KeyboardUtils.hideKeyboard(DataCollDetailActivity.this, mDialogDateTiet);
    }

    private void storeRecord(double value, Date date) {
        int id = databaseHelper.insertRecord(mDataCollItemId,value, DateUtils.dateToString(date,DateUtils.DATE_FORMAT_DMY));
        Record record = new Record(id,date,value);
        records.add(record);
        filterRecords(mFilterDatesCount);
        Collections.sort(records, new RecordComparator());
        updateFragments();
        updateDatesList();
        Toast.makeText(this, this.getString(R.string.record_added), Toast.LENGTH_SHORT).show();
    }

    private void setDate(int year, int month, int day){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day,0,0,0);
        calendar.clear(Calendar.MILLISECOND);
        mDialogDateToStore = calendar.getTime();
        mDialogDateTiet.setText(DateUtils.dateToString(calendar.getTime(),DateUtils.DATE_FORMAT_EDM));
        KeyboardUtils.showKeyboard(this);
    }

    private void showPopupMenuFilter() {
        PopupMenu popup = new PopupMenu(this,findViewById(R.id.tv_filter) );
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_filter, popup.getMenu());
        final MenuItem menuitem = mMenu.getItem(0);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_filter_last_week:
                        menuitem.setTitle(R.string.last_week);
                        filterRecords(7);
                        return true;
                    case R.id.menu_filter_last_month:
                        menuitem.setTitle(R.string.last_month);
                        filterRecords(31);
                        return true;
                    case R.id.menu_filter_last_year:
                        menuitem.setTitle(R.string.last_year);
                        filterRecords(356);
                        return true;
                    case R.id.menu_filter_this_week:
                        menuitem.setTitle(R.string.this_week);
                        filterRecords(getDaysPast(Calendar.DAY_OF_WEEK,Calendar.MONDAY));
                        return true;
                    case R.id.menu_filter_this_month:
                        menuitem.setTitle(R.string.this_month);
                        filterRecords(getDaysPast(Calendar.DAY_OF_MONTH,1));
                        return true;
                    case R.id.menu_filter_this_year:
                        menuitem.setTitle(R.string.this_year);
                        filterRecords(getDaysPast(Calendar.DAY_OF_YEAR,1));
                        return true;
                    case R.id.menu_filter_all:
                        menuitem.setTitle(R.string.all);
                        showAllRecords();
                        return true;
                   default:
                       return false;
                }
            }
        });
        popup.show();
    }

    private int getDaysPast(int period, int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(period,value);
        Date date = new Date();
        long difference = date.getTime() - calendar.getTime().getTime();
        float daysBetween = (difference / (1000*60*60*24));
        return (int) daysBetween+1;
    }

    private void filterRecords(int daysToPast) {
        if (daysToPast==0) {
            showAllRecords();
            return;
        }
        records.clear();
        List<Record> listToAdd = databaseHelper.getRecords(mDataCollItemId);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -daysToPast);
        Date date = calendar.getTime();
        for(Record record : listToAdd) {
            if (record.getDate().after(date)) {
                records.add(record);
            }
        }
        updateFragments();
        mFilterDatesCount = daysToPast;
    }

    private void showAllRecords() {
        records.clear();
        records.addAll(databaseHelper.getRecords(mDataCollItemId));
        updateFragments();
        mFilterDatesCount = 0;
    }

    private void updateFragments()
    {
        recordsOverviewFragment.updateData();
        recordsOverviewFragment.updateLayout();
        mRecordsListFragment.showTvIfRvIsEmpty();
        mRecordsListFragment.recordsListAdapter.notifyDataSetChanged();
    }


}
