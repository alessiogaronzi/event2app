package com.digitalchannelforum.ui.fragment;

import com.digitalchannelforum.drupalcon.R;
import com.digitalchannelforum.drupalcon.model.Model;
import com.digitalchannelforum.drupalcon.model.UpdateRequest;
import com.digitalchannelforum.drupalcon.model.UpdatesManager;
import com.digitalchannelforum.drupalcon.model.data.FloorPlan;
import com.digitalchannelforum.ui.adapter.FloorSelectorAdapter;
import com.digitalchannelforum.ui.view.TouchImageView;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 09.06.2016.
 */
public class FloorPlanFragment  extends Fragment
{
    public static int REDCOMMENDED_FLOOR_IMAGE_HEIGHT = Resources.getSystem().getDisplayMetrics().heightPixels * 2;
    public static int REDCOMMENDED_FLOOR_IMAGE_WIDTH = Resources.getSystem().getDisplayMetrics().widthPixels * 2;

    public static final String TAG = "FloorPlanFragment";
    private View mLayoutContent, mLayoutPlaceholder;
    private Spinner floorSelector;
    private List<FloorPlan>plans;
    private TouchImageView floorImage;

    private UpdatesManager.DataUpdatedListener updateListener = new UpdatesManager.DataUpdatedListener() {
        @Override
        public void onDataUpdated( List<UpdateRequest> requests) {

            if (requests.contains(UpdateRequest.FLOOR_PLANS)){
                new LoadPlansTask().execute();
            }

        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Model.instance().getUpdatesManager().registerUpdateListener(updateListener);
    }

    @Override
    public void onDestroy()
    {
        Model.instance().getUpdatesManager().unregisterUpdateListener(updateListener);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View result = inflater.inflate(R.layout.fr_floor_plan, null);
        mLayoutContent = result.findViewById(R.id.layout_content);
        mLayoutPlaceholder = result.findViewById(R.id.layout_placeholder);
        floorSelector = (Spinner)result.findViewById(R.id.spinner);
        floorSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                new LoadPlanImageTask().execute(plans.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        floorImage = (TouchImageView)result.findViewById(R.id.floor_plan_image);

        new LoadPlansTask().execute();

        return result;
    }

    private class LoadPlansTask extends AsyncTask<Void,Void,List<FloorPlan>>{

        @Override
        protected List<FloorPlan> doInBackground(Void... params)
        {
            return Model.instance().getFloorPlansManager().getFloorPlans();
        }

        @Override
        protected void onPostExecute(List<FloorPlan> floorPlans)
        {
            super.onPostExecute(floorPlans);
            plans = floorPlans;

            if (plans == null || plans.isEmpty()) {
                mLayoutContent.setVisibility(View.GONE);
                mLayoutPlaceholder.setVisibility(View.VISIBLE);
            } else {
                mLayoutContent.setVisibility(View.VISIBLE);
                mLayoutPlaceholder.setVisibility(View.GONE);

                List<String> names = new ArrayList<>(floorPlans.size());
                for (FloorPlan plan : plans) {
                    names.add(plan.getName());
                }

                FloorSelectorAdapter floorsAdapter = new FloorSelectorAdapter(floorSelector.getContext(), names);
                floorSelector.setAdapter(floorsAdapter);

                floorSelector.setVisibility(plans.isEmpty() ? View.INVISIBLE : View.VISIBLE);
            }
        }
    }

    private class LoadPlanImageTask extends AsyncTask<FloorPlan,Void,Drawable>{

        @Override
        protected void onPreExecute()
        {
            floorSelector.setEnabled(false);
            floorSelector.setClickable(false);
            floorImage.setImageDrawable(null);
            super.onPreExecute();
        }

        @Override
        protected Drawable doInBackground(FloorPlan... params)
        {
            Bitmap planImage =  Model.instance().getFloorPlansManager().getImageForPlan(params[0],
                    REDCOMMENDED_FLOOR_IMAGE_WIDTH,REDCOMMENDED_FLOOR_IMAGE_HEIGHT);
            if(planImage != null){
                return new BitmapDrawable(null,planImage);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Drawable drawable)
        {
            super.onPostExecute(drawable);
            floorSelector.setEnabled(true);
            floorSelector.setClickable(true);
            floorImage.setZoom(1);
            floorImage.setImageDrawable(drawable);
        }
    }

}
