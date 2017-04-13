package bryce.familymap;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;


/**
 * Created by Bryce on 12/1/16.
 */

public class ListFragment extends Fragment {
    ListAdapter mAdapter;

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    public void updateUI() {
        //generate list

        if (mAdapter == null) { //Xadapter extends recyclerView.Adapter<>
            mAdapter = new ListAdapter();
        }
    }

    private class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //this will response to a click (onclicklistener) and extend the recycler view
        private TextView mTitleTextView;
        private CheckBox mTitleCheckBox;

        public Holder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {

        }
    }

    private class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }

}
