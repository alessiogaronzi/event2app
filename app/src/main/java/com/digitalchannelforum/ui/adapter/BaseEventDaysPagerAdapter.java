package com.digitalchannelforum.ui.adapter;


import com.digitalchannelforum.ui.drawer.EventHolderFragmentStrategy;
import com.digitalchannelforum.ui.fragment.EventFragment;
import com.digitalchannelforum.utils.DateUtils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class BaseEventDaysPagerAdapter extends FragmentStatePagerAdapter  {

    private EventHolderFragmentStrategy strategy;

    private List<Long> mDays;

    public BaseEventDaysPagerAdapter(FragmentManager fm) {
        super(fm);
        mDays = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        Long date = getDate(position);
        return EventFragment.newInstance(date, strategy.getEventMode());
    }

    public void setData(List<Long> eventDays, EventHolderFragmentStrategy strategy) {
        mDays.clear();
        mDays.addAll(eventDays);
        this.strategy = strategy;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return mDays.size();
    }

    public Long getDate(int position) {
        return mDays.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return DateUtils.getInstance().getWeekNameAndDate(getDate(position));
    }
}
