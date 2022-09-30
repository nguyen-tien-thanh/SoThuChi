package com.example.cw_1.ui.trip;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TripViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public TripViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is trip fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}