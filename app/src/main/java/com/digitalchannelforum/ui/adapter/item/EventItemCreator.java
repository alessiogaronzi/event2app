package com.digitalchannelforum.ui.adapter.item;

import com.digitalchannelforum.drupalcon.model.data.Event;

public interface EventItemCreator {

    EventListItem getItem(Event event);

}
