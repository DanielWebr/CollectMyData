package com.webrdaniel.collectmydata.fragments;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.webrdaniel.collectmydata.activities.DataCollDetailActivity;
import com.webrdaniel.collectmydata.lists.RecordsListAdapter;
import com.webrdaniel.collectmydata.models.Record;
import com.webrdaniel.collectmydata.R;
import com.webrdaniel.collectmydata.utils.DialogUtils;

import java.util.concurrent.Callable;

public class RecordsListFragment extends Fragment {

    public RecordsListAdapter recordsListAdapter;
    public DataCollDetailActivity dataCollDetailActivity;
    public View mRootView;
    private RecyclerView mRecyclerView;
    private TextView mEmptyRvTv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataCollDetailActivity = (DataCollDetailActivity) getContext();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_records_list_view, container, false);
        mRootView.setTag("RecordsListFragment");
        FrameLayout frame = mRootView.findViewById(R.id.fl_records_list);
        mRecyclerView =  mRootView.findViewById(R.id.rv_records);
        recordsListAdapter = new RecordsListAdapter(this);
        mRecyclerView.setAdapter(recordsListAdapter);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mEmptyRvTv = mRootView.findViewById(R.id.tv_empty_records_list);
        showTvIfRvIsEmpty();

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(dataCollDetailActivity,  DividerItemDecoration.VERTICAL);
        Drawable drawable = ContextCompat.getDrawable(dataCollDetailActivity, R.drawable.divider);
        if(drawable!=null)dividerItemDecoration.setDrawable( drawable);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,  ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                showDialogDeleteRecord(position);
            }
            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                if (viewHolder.getAdapterPosition()!=0) {
                    return super.getSwipeDirs(recyclerView, viewHolder);
                } else {
                    return 0;
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        return frame;
    }

    public void showTvIfRvIsEmpty() {
        if (dataCollDetailActivity.records.size()==0) {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyRvTv.setVisibility(View.VISIBLE);
        }
        else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyRvTv.setVisibility(View.GONE);
        }
    }

    private void showDialogDeleteRecord(final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(dataCollDetailActivity);
        View dialog = layoutInflaterAndroid.inflate(R.layout.dialog_delete, (ViewGroup) mRootView,false);
        TextView deleteTv =  dialog.findViewById(R.id.tv_dialog_delete);
        deleteTv.setText(dataCollDetailActivity.getString(R.string.really_delete_record));
        Callable methodDeleteRecord = new Callable() {
            @Override
            public Object call() throws Exception {
                deleteRecord(position);
                return null;
            }
        };
        AlertDialog alertDialog = DialogUtils.showDialog(dialog, dataCollDetailActivity, methodDeleteRecord,R.string.delete);
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                recordsListAdapter.notifyItemChanged(position);
            }
        });
    }

    private void deleteRecord(int position) {
        Record record = dataCollDetailActivity.records.get(position-1);
        dataCollDetailActivity.databaseHelper.deleteRecord(record.getId());
        dataCollDetailActivity.records.remove(record);
        recordsListAdapter.notifyItemRemoved(position);
        dataCollDetailActivity.recordsOverviewFragment.updateData();
        dataCollDetailActivity.recordsOverviewFragment.updateLayout();
        dataCollDetailActivity.updateDatesList();
        showTvIfRvIsEmpty();
    }

}


