package com.webrdaniel.collectmydata;

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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class DataListFragment extends Fragment {

    private ArrayList<Record> mRecords;
    private DataCollDetailActivity parent;
    private BasicListAdapter mBasicListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent = (DataCollDetailActivity) getContext();
        mRecords = parent.mDatabaseHelper.getValues(parent.mDataCollItem.getId());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_view, container, false);
        rootView.setTag("DataListFragment");
        RecyclerView mRecyclerView =  rootView.findViewById(R.id.recycler_view);
        mBasicListAdapter = new BasicListAdapter(mRecords);
        mRecyclerView.setAdapter(mBasicListAdapter);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(parent,  DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getContext().getDrawable(R.drawable.divider));
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        return mRecyclerView;
    }
    class BasicListAdapter extends RecyclerView.Adapter<BasicListAdapter.ViewHolder>{
        private ArrayList<Record> items;

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_value, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder,int position) {
            holder.mValue.setText(parent.formatNoLastZero.format(items.get(position).getValue()));
            holder.mValueDate.setText(Utils.dateToString(items.get(position).getDate(),Utils.DATE_FORMAT_DAY_MONTH));
        }
        @Override
        public int getItemCount() {
            return items.size();
        }

        BasicListAdapter(ArrayList<Record> items){
            this.items = items;

        }

        @SuppressWarnings("deprecation")
        public class ViewHolder extends RecyclerView.ViewHolder{
            View mView;
            TextView mValueDate;
            TextView mValue;
            ImageButton mIbMenu;
            Record record;

            public ViewHolder(View v){
                super(v);
                mView = v;
                mValue = v.findViewById(R.id.tv_value);
                mValueDate = v.findViewById(R.id.tv_value_date);
                mIbMenu = v.findViewById(R.id.ib_edit);
                mIbMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialogEditValue();
                    }
                });
            }

            private void showDialogEditValue() {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(parent);
                View dialog = layoutInflaterAndroid.inflate(R.layout.input_dialog_edit_value, null);
                final TextInputEditText et_name = dialog.findViewById(R.id.ti_et);
                double value = mRecords.get(ViewHolder.this.getAdapterPosition()).getValue();
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
                Utils.OnEnterConfirm(et_name,alertDialog);
                Utils.showKeyboard(parent);
            }

            private void editValue(Double value) {
                int position = ViewHolder.this.getAdapterPosition();
                record = mRecords.get(position);
                parent.mDatabaseHelper.editValue(record.getId(), value);
                record.setValue(value);
                mBasicListAdapter.notifyItemChanged(position);
                parent.dataOverviewFragment.updateData();
                parent.dataOverviewFragment.updateLayout();
            }
        }
    }
}


