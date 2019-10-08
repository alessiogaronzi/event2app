package com.digitalchannelforum.drupalcon.model;

public abstract class UpdateCallback {

    abstract public void onDownloadSuccess();

    abstract public void onDownloadError();
}
