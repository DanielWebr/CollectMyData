package com.webrdaniel.collectmydata;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
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

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class RecordsRecyclerViewFragment extends Fragment {

    protected BasicListAdapter mBasicListAdapter;
    protected RecyclerView mRecyclerView;
    private TextView tv_emptryRV;
    private DataCollDetailActivity parent;
    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent = (DataCollDetailActivity) getContext();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_records_recycler_view, container, false);
        rootView.setTag("RecordsRecyclerViewFragment");
        FrameLayout frame = rootView.findViewById(R.id.frame);
        mRecyclerView =  rootView.findViewById(R.id.recycler_view_values_list);
        mBasicListAdapter = new BasicListAdapter(parent.mRecords);
        mRecyclerView.setAdapter(mBasicListAdapter);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(parent,  DividerItemDecoration.VERTICAL);
        Drawable drawable = ContextCompat.getDrawable(parent, R.drawable.divider);
        if(drawable!=null)dividerItemDecoration.setDrawable( drawable);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        tv_emptryRV = rootView.findViewById(R.id.empty_view);
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
        AlertDialog alertDialog = Utils.getDialog(dialog,parent, methodEditValue,R.string.delete);
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mBasicListAdapter.notifyItemChanged(position);
            }
        });
    }

    private void deleteRecord(int position) {
        Record record = parent.mRecords.get(position-1);
        parent.mDatabaseHelper.deleteRecord(record.getId());
        parent.mRecords.remove(record);
        mBasicListAdapter.notifyItemRemoved(position);
        parent.recordsOverviewFragment.updateData();
        parent.recordsOverviewFragment.updateLayout();
        parent.updateDatesList();
        messageIfEmpty();

    }

    public class BasicListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private ArrayList<Record> items;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType == 0) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_records_recyclre_view, parent, false);
                return new HeaderViewHolder(v);
            }
            else {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_records_recycler_view, parent, false);
                return new ListViewHolder(v);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
             if(position!=0) {
                BasicListAdapter.ListViewHolder listViewHolder = (BasicListAdapter.ListViewHolder) holder;
                 listViewHolder.mValue.setText(parent.formatNoLastZero.format(items.get(position-1).getValue()));
                 listViewHolder.mValueDate.setText(Utils.dateToString(items.get(position-1).getDate(), Utils.DATE_FORMAT_EDM));
            }
        }

        @Override
        public int getItemCount() {
            return items.size()+1;
        }

        @Override
        public int getItemViewType (int position) {
            if(position == 0) {
                return 0;
            } else{
                return 1;
            }
        }

        BasicListAdapter(ArrayList<Record> items){
            this.items = items;
        }

        @SuppressWarnings("deprecation")
        class ListViewHolder extends RecyclerView.ViewHolder{
            View mView;
            TextView mValueDate;
            TextView mValue;
            Record record;

            ListViewHolder(View v){
                super(v);
                mView = v;
                mValue = v.findViewById(R.id.tv_value);
                mValueDate = v.findViewById(R.id.tv_value_date);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialogEditValue();
                    }
                });

            }

            private void showDialogEditValue() {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(parent);
                View dialog = layoutInflaterAndroid.inflate(R.layout.input_dialog_record_value_edit, (ViewGroup) rootView,false);
                final TextInputEditText et_name = dialog.findViewById(R.id.ti_et);
                double value = parent.mRecords.get(ListViewHolder.this.getAdapterPosition()-1).getValue();
                et_name.setText(parent.formatNoLastZero.format(value));
                Callable methodEditValue = new Callable() {
                    @Override
                    public Object call() throws Exception {
                        editValue(Double.parseDouble(et_name.getText().toString()));
                        return null;
                    }
                };
                AlertDialog alertDialog = Utils.getDialog(dialog,parent, methodEditValue,R.string.edit);
                Utils.lockPositiveButtonOnEmptyText(alertDialog, et_name, String.valueOf(parent.formatNoLastZero.format(value)));
                et_name.requestFocus();
                Utils.onEnterConfirm(et_name,alertDialog);
                Utils.showKeyboard(parent);
            }

            private void editValue(Double value) {
                int position = ListViewHolder.this.getAdapterPosition();
                record = parent.mRecords.get(position-1);
                parent.mDatabaseHelper.editValue(record.getId(), value);
                record.setValue(value);
                mBasicListAdapter.notifyItemChanged(position);
                parent.recordsOverviewFragment.updateData();
                parent.recordsOverviewFragment.updateLayout();

            }
        }

        class HeaderViewHolder extends RecyclerView.ViewHolder {
            HeaderViewHolder(View v) {
                super(v);
            }
        }
    }
}


