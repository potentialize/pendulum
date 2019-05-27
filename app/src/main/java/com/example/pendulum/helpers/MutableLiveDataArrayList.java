package com.example.pendulum.helpers;

import java.util.ArrayList;
import java.util.function.Predicate;

import androidx.lifecycle.LiveData;

// NOTE: postValue and setValue not exposed to control data
public class MutableLiveDataArrayList<T> extends LiveData<ArrayList<T>> {
    // super.postValue(value);
    // super.setValue(value);

    public void add(T item) {
        ArrayList<T> list = super.getValue();

        if (list == null) {
            list = new ArrayList<>();
        }

        list.add(item);

        super.setValue(list);
    }

    public void removeIf(Predicate<T> filter) {
        ArrayList<T> list = super.getValue();

        if (list == null) {
            return;
        }

        list.removeIf(filter);

        super.setValue(list);
    }

    public void clear() {
        super.setValue(new ArrayList<>());
    }

    public boolean contains(T item) {
        ArrayList<T> list = super.getValue();

        if (list == null) {
            // no items => cannot contain item
            return false;
        }

        return list.contains(item);
    }
}
