package com.sudo.campusambassdor.model;



public class Task {
    private String mTitle;
    private String mDescription;

    private int uploadButton;

    public Task(String mTitle, String mDescription, int uploadButton) {
        this.mTitle = mTitle;
        this.mDescription = mDescription;
        this.uploadButton = uploadButton;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public int getUploadButton() {
        return uploadButton;
    }

    public void setUploadButton(int uploadButton) {
        this.uploadButton = uploadButton;
    }

}
