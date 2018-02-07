package com.webrdaniel.collectmydata;

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
import android.widget.Button;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DataCollDetailActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.viewpager) ViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabs;
    @BindView(R.id.fab_records) FloatingActionButton fab;
    protected DataCollItem mDataCollItem;
    public ArrayList<Record> mRecords;
    protected DatabaseHelper mDatabaseHelper;
    protected DecimalFormat formatNoLastZero;
    protected DataOverviewFragment dataOverviewFragment;
    protected DataListFragment dataListFragment;
    protected Adapter adapter;
    private TextInputEditText et_date;
    private TextInputLayout ti_layout_date;
    private Menu menu;
    private Date dateChosen;
    private ArrayList <Date> dates;
    private int filterDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_coll_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        formatNoLastZero = new DecimalFormat("0.##");
        //dateChosen = Utils.dateToDateFormat(Utils.DATE_FORMAT_DMY);

        setupViewPager(viewPager);
        tabs.setupWithViewPager(viewPager);
        mDataCollItem = (DataCollItem)getIntent().getSerializableExtra(MainActivity.DATA_COLL_ITEM);
        mDatabaseHelper = new DatabaseHelper(this);
        dates = mDatabaseHelper.getDates(mDataCollItem.getId());
        mRecords = mDatabaseHelper.getRecords(mDataCollItem.getId());
        getSupportActionBar().setTitle(mDataCollItem.getName());
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
                    if(position == 0)
                    {
                        fab.hide();
                    }
                    else
                    {
                        fab.show();
                    }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new Adapter(getSupportFragmentManager());
        dataOverviewFragment = new DataOverviewFragment();
        adapter.addFragment(dataOverviewFragment,getResources().getString(R.string.tab_overview));
        dataListFragment = new DataListFragment();
        adapter.addFragment(dataListFragment, "Data");
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        public Adapter(FragmentManager manager) {
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

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
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

    protected void onPause() {
        super.onPause();
        Utils.hideKeyboard(this);
    }

    private void showDialogAddValueDate() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View dialog = layoutInflaterAndroid.inflate(R.layout.input_dialog_value_date, null);
        final TextInputEditText etValue = dialog.findViewById(R.id.ti_et);
        et_date = dialog.findViewById(R.id.ti_et_date);
        ti_layout_date = dialog.findViewById(R.id.ti_layout_date);
        et_date.setText(Utils.dateToString(null, Utils.DATE_FORMAT_EDMM));

        Callable methodStoreValue = new Callable() {
            @Override
            public Object call() throws Exception {
                storeRecord(Double.parseDouble(etValue.getText().toString()),Utils.stringToDate(et_date.getText().toString(),Utils.DATE_FORMAT_DMY));
                return null;
            }
        };
        AlertDialog alertDialog = Utils.getDialog(dialog,this, methodStoreValue,R.string.add);

        Utils.onEnterConfirm(etValue,alertDialog);
        Utils.showKeyboard(this);

        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar =Calendar.getInstance();
                calendar.setTime(dateChosen);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(DataCollDetailActivity.this, year, month, day);
                datePickerDialog.show(getFragmentManager(), "DateFragment");
                datePickerDialog.setMaxDate(Calendar.getInstance());
                Utils.hideKeyboard(DataCollDetailActivity.this,et_date);
            }
        });


        final Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setEnabled(false);

        dateChosen = Utils.dateToDateFormat(Utils.DATE_FORMAT_DMY);
        if(dates.contains(dateChosen))ti_layout_date.setError(getResources().getString(R.string.error_date));

        et_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if(dates.contains(dateChosen))
                {
                    positiveButton.setEnabled(false);
                    ti_layout_date.setError(getResources().getString(R.string.error_date));
                }
                else{
                    ti_layout_date.setError(null);
                   if(!TextUtils.isEmpty(etValue.getText())) positiveButton.setEnabled(true);
                }
            }
        });

        etValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s))positiveButton.setEnabled(false);
                else if(!dates.contains(dateChosen))positiveButton.setEnabled(true);
            }
        });
    }

    private void storeRecord(double value, Date date) {
        int id = mDatabaseHelper.insertDataValue(mDataCollItem.getId(),value,Utils.dateToString(date,Utils.DATE_FORMAT_DMY));
        int idList = getIdToList(date);
        Record record = new Record(id,date,value);
        mRecords.add(idList,record);
        filterRecords(filterDates);
        Collections.sort(mRecords, new RecordComparator());
        dataOverviewFragment.updateData();
        dataOverviewFragment.updateLayout();
        dataListFragment.messageIfEmpty();
        dataListFragment.mBasicListAdapter.notifyDataSetChanged();
        updateDatesList();
        Toast.makeText(this, this.getString(R.string.record_added), Toast.LENGTH_SHORT).show();
    }

    private int getIdToList(Date date) {
        for(Record record : mRecords )
        {
            if(date.after(record.getDate()))
            return mRecords.indexOf(record);
        }
        return mRecords.size();
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        setDate(year, month, day);
    }

    public void setDate(int year, int month, int day){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day,0,0,0);
        calendar.clear(Calendar.MILLISECOND);
        dateChosen = calendar.getTime();
        et_date.setText(Utils.dateToString(calendar.getTime(),Utils.DATE_FORMAT_DMY));
        Utils.showKeyboard(this);
    }

    private void showPopupMenuFilter() {
        PopupMenu popup = new PopupMenu(this,findViewById(R.id.tv_filter) );
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_filter, popup.getMenu());
        final MenuItem menuitem = menu.getItem(0);
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

    private int getDaysPast(int field, int value) {
        Calendar c = Calendar.getInstance();
        c.set(field,value);
        Date date = new Date();
        long difference = date.getTime() - c.getTime().getTime();
        float daysBetween = (difference / (1000*60*60*24));
        return (int) daysBetween;
    }

    private void filterRecords(int daysToPast) {
        if (daysToPast==0)
        {
            showAllRecords();
            return;
        }
        mRecords.clear();
        mRecords.addAll(mDatabaseHelper.getRecords(mDataCollItem.getId()));
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -daysToPast-1);
        Date date = cal.getTime();
        List<Record> listToRemove = new ArrayList<>();
        for(Record record : mRecords) {
            if (record.getDate().before(date))
            {
                listToRemove.add(record);
            }
        }
        mRecords.removeAll(listToRemove);
        dataOverviewFragment.updateData();
        dataOverviewFragment.updateLayout();
        dataListFragment.messageIfEmpty();
        dataListFragment.mBasicListAdapter.notifyDataSetChanged();
        filterDates = daysToPast;
    }

    private void showAllRecords() {
        mRecords.clear();
        mRecords.addAll(mDatabaseHelper.getRecords(mDataCollItem.getId()));
        dataOverviewFragment.updateData();
        dataOverviewFragment.updateLayout();
        dataListFragment.messageIfEmpty();
        dataListFragment.mBasicListAdapter.notifyDataSetChanged();
        filterDates = 0;
    }
    protected void updateDatesList()
    {
        dates.clear();
        dates.addAll(mDatabaseHelper.getDates(mDataCollItem.getId()));
    }
}
