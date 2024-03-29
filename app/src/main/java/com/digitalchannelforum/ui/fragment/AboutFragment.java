package com.digitalchannelforum.ui.fragment;

import com.digitalchannelforum.drupalcon.R;
import com.digitalchannelforum.drupalcon.model.Model;
import com.digitalchannelforum.drupalcon.model.UpdateRequest;
import com.digitalchannelforum.drupalcon.model.UpdatesManager;
import com.digitalchannelforum.drupalcon.model.data.InfoItem;
import com.digitalchannelforum.drupalcon.model.managers.InfoManager;
import com.digitalchannelforum.ui.activity.AboutDetailsActivity;
import com.digitalchannelforum.utils.L;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 24.06.2016.
 */
public class AboutFragment extends Fragment {
    private ListView mListMenu;
    private View mLayoutPlaceholder;

    private AboutListAdapter adapter;
    private List<InfoItem> infoItems;

    private UpdatesManager.DataUpdatedListener updateListener = new UpdatesManager.DataUpdatedListener() {
        @Override
        public void onDataUpdated(List<UpdateRequest> requests) {
            L.d("AboutFragment");
            reloadData();
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Model.instance().getUpdatesManager().registerUpdateListener(updateListener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.ac_about, container, false);
        initViews(result);
        return result;
    }

    @Override
    public void onDestroy() {
        Model.instance().getUpdatesManager().unregisterUpdateListener(updateListener);
        super.onDestroy();
    }

    private void initViews(View root) {

        mLayoutPlaceholder = root.findViewById(R.id.layout_placeholder);
        mListMenu = (ListView) root.findViewById(R.id.listView);
        mListMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                onItemClicked(position);
            }
        });

        if (adapter == null) {
            adapter = new AboutListAdapter(null, root.getContext());
            mListMenu.setAdapter(adapter);
        }

        reloadData();

    }

    private void reloadData() {
        InfoManager infoManager = Model.instance().getInfoManager();
        infoItems = infoManager.getInfo();

        if (infoItems == null || infoItems.isEmpty()) {
            mListMenu.setVisibility(View.GONE);
            mLayoutPlaceholder.setVisibility(View.VISIBLE);
        } else {
            mListMenu.setVisibility(View.VISIBLE);
            mLayoutPlaceholder.setVisibility(View.GONE);
            adapter.setData(infoItems);
            adapter.notifyDataSetChanged();
        }
    }

    private void onItemClicked(int position) {
        Activity root = getActivity();
        if (root == null) {
            return;
        }

        InfoItem item = infoItems.get(position);
        Intent intent = new Intent(root, AboutDetailsActivity.class);
        intent.putExtra(AboutDetailsActivity.EXTRA_DETAILS_TITLE, item.getTitle());
        intent.putExtra(AboutDetailsActivity.EXTRA_DETAILS_ID, item.getId());
        intent.putExtra(AboutDetailsActivity.EXTRA_DETAILS_CONTENT, item.getContent());
        startActivity(intent);
    }

    private class AboutListAdapter extends BaseAdapter {

        List<InfoItem> mItems = new ArrayList<>();
        LayoutInflater inflatter;

        public AboutListAdapter(List<InfoItem> items, Context context) {
            inflatter = LayoutInflater.from(context);
            setData(items);
        }

        public void setData(List<InfoItem> items) {
            if (items != null && !items.isEmpty()) {
                mItems = new ArrayList<>(items);
            } else {
                mItems = new ArrayList<>();
            }
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Object getItem(int i) {
            return mItems.get(i);
        }

        @Override
        public long getItemId(int i) {
            return mItems.get(i).getId();
        }

        @Override
        public View getView(int i, View view, ViewGroup parent) {
            View resultView;

            if (view == null) {
                resultView = inflatter.inflate(R.layout.item_about, parent, false);
            } else {
                resultView = view;
            }

            InfoItem item = mItems.get(i);
            ((TextView) resultView.findViewById(R.id.txtTitle)).setText(item.getTitle());

            return resultView;
        }
    }
}
