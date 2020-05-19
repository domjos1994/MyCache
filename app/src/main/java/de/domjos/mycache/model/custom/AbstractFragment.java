package de.domjos.mycache.model.custom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.ParameterizedType;

public abstract class AbstractFragment<T extends ViewModel> extends Fragment {
    private int layout_id;
    protected View root;
    protected T model;
    private Class<T> cls;

    public AbstractFragment(int layout_id) {
        this.layout_id = layout_id;

        this.cls = ((Class<T>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.model = new ViewModelProvider(this).get(this.cls);
        this.root = inflater.inflate(this.layout_id, container, false);
        this.initControls();
        this.initActions();
        this.initViewModel();
        this.manageControls(false, false);

        return root;
    }

    protected abstract void initControls();
    protected abstract void initActions();
    protected abstract void initViewModel();

    protected void manageControls(boolean editMode, boolean selected) {

    }
}
