package com.webrdaniel.collectmydata;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class DataListFragment extends Fragment {

    private DataCollDetailActivity parent;
    protected BasicListAdapter mBasicListAdapter;
    protected RecyclerView mRecyclerView;
    private TextView tv_emptryRV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent = (DataCollDetailActivity) getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.records_recycler_view, container, false);
        rootView.setTag("DataListFragment");
        FrameLayout frame = rootView.findViewById(R.id.frame);
        mRecyclerView =  rootView.findViewById(R.id.recycler_view_values_list);
        mBasicListAdapter = new BasicListAdapter(parent.mRecords);
        mRecyclerView.setAdapter(mBasicListAdapter);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(parent,  DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getContext().getDrawable(R.drawable.divider));
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        tv_emptryRV = rootView.findViewById(R.id.empty_view);
        messageIfEmpty();
        return frame;
    }

    public void messageIfEmpty()
    {
        if (parent.mRecords.size()==0) {
            mRecyclerView.setVisibility(View.GONE);
            tv_emptryRV.setVisibility(View.VISIBLE);
        }
        else {
            mRecyclerView.setVisibility(View.VISIBLE);
            tv_emptryRV.setVisibility(View.GONE);
        }
    }

    public BasicListAdapter getBasicListAdapter()
    {
        return new BasicListAdapter(parent.mRecords);
    }

    public class BasicListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private ArrayList<Record> items;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType == 0) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.records_recyclre_view_header, parent, false);
                return new HeaderViewHolder(v);
            }
            else
            {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_records, parent, false);
                return new ListViewHolder(v,getActivity());
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
             if(position!=0) {
                BasicListAdapter.ListViewHolder listViewHolder = (BasicListAdapter.ListViewHolder) holder;
                 listViewHolder.mValue.setText(parent.formatNoLastZero.format(items.get(position-1).getValue()));
                 listViewHolder.mValueDate.setText(Utils.dateToString(items.get(position-1).getDate(), Utils.DATE_FORMAT_DM));
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
        public class ListViewHolder extends RecyclerView.ViewHolder{
            View mView;
            TextView mValueDate;
            TextView mValue;
            ImageButton mIbEdit,bIbDelete;
            Record record;

            public ListViewHolder(View v, Context record){
                super(v);
                mView = v;
                mValue = v.findViewById(R.id.tv_value);
                mValueDate = v.findViewById(R.id.tv_value_date);
                mIbEdit = v.findViewById(R.id.ib_edit);
                mIbEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialogEditValue();
                    }
                });
                bIbDelete = v.findViewById(R.id.ib_delete);
                bIbDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialogDeleteRecord();
                    }
                });
            }

            private void showDialogDeleteRecord() {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(parent);
                View dialog = layoutInflaterAndroid.inflate(R.layout.input_dialog_delete, null);
                TextView tv_delete = (TextView) dialog.findViewById(R.id.tv_delete);
                tv_delete.setText(parent.getString(R.string.really_delete_record));
                Callable methodEditValue = new Callable() {
                    @Override
                    public Object call() throws Exception {
                        deleteRecord();
                        return null;
                    }
                };
                Utils.getDialog(dialog,parent, methodEditValue,R.string.delete);
            }

            private void deleteRecord() {
                int position = ListViewHolder.this.getAdapterPosition();
                record = parent.mRecords.get(position-1);
                parent.mDatabaseHelper.deleteRecord(record.getId());
                parent.mRecords.remove(record);
                mBasicListAdapter.notifyItemRemoved(position);
                parent.dataOverviewFragment.updateData();
                parent.dataOverviewFragment.updateLayout();
                messageIfEmpty();
            }

            private void showDialogEditValue() {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(parent);
                View dialog = layoutInflaterAndroid.inflate(R.layout.input_dialog_edit_value, null);
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
                parent.dataOverviewFragment.updateData();
                parent.dataOverviewFragment.updateLayout();
            }
        }

        private class HeaderViewHolder extends RecyclerView.ViewHolder {
            public HeaderViewHolder(View v) {
                super(v);
            }
        }
    }
}


