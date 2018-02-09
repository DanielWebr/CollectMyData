package com.webrdaniel.collectmydata.Fragments;

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

import com.webrdaniel.collectmydata.Activities.DataCollDetailActivity;
import com.webrdaniel.collectmydata.Lists.RecordsListAdapter;
import com.webrdaniel.collectmydata.Models.Record;
import com.webrdaniel.collectmydata.R;
import com.webrdaniel.collectmydata.Utils.DialogUtils;

import java.util.concurrent.Callable;

public class RecordsListFragment extends Fragment {

    public RecordsListAdapter mRecordsListAdapter;
    protected RecyclerView mRecyclerView;
    private TextView tv_emptryRV;
    public DataCollDetailActivity parent;
    public View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent = (DataCollDetailActivity) getContext();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_records_list_view, container, false);
        rootView.setTag("RecordsListFragment");
        FrameLayout frame = rootView.findViewById(R.id.frame);
        mRecyclerView =  rootView.findViewById(R.id.recycler_view_values_list);
        mRecordsListAdapter = new RecordsListAdapter(this, parent.mRecords);
        mRecyclerView.setAdapter(mRecordsListAdapter);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(parent,  DividerItemDecoration.VERTICAL);
        Drawable drawable = ContextCompat.getDrawable(parent, R.drawable.divider);
        if(drawable!=null)dividerItemDecoration.setDrawable( drawable);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        tv_emptryRV = rootView.findViewById(R.id.empty_records_list);
        messageIfEmpty();

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

    public void messageIfEmpty() {
        if (parent.mRecords.size()==0) {
            mRecyclerView.setVisibility(View.GONE);
            tv_emptryRV.setVisibility(View.VISIBLE);
        }
        else {
            mRecyclerView.setVisibility(View.VISIBLE);
            tv_emptryRV.setVisibility(View.GONE);
        }
    }

    private void showDialogDeleteRecord(final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(parent);
        View dialog = layoutInflaterAndroid.inflate(R.layout.input_dialog_delete, (ViewGroup) rootView,false);
        TextView tv_delete =  dialog.findViewById(R.id.tv_delete);
        tv_delete.setText(parent.getString(R.string.really_delete_record));
        Callable methodEditValue = new Callable() {
            @Override
            public Object call() throws Exception {
                deleteRecord(position);
                return null;
            }
        };
        AlertDialog alertDialog = DialogUtils.getDialog(dialog,parent, methodEditValue,R.string.delete);
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mRecordsListAdapter.notifyItemChanged(position);
            }
        });
    }

    private void deleteRecord(int position) {
        Record record = parent.mRecords.get(position-1);
        parent.mDatabaseHelper.deleteRecord(record.getId());
        parent.mRecords.remove(record);
        mRecordsListAdapter.notifyItemRemoved(position);
        parent.recordsOverviewFragment.updateData();
        parent.recordsOverviewFragment.updateLayout();
        parent.updateDatesList();
        messageIfEmpty();

    }

}


