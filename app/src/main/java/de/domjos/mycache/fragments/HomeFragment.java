package de.domjos.mycache.fragments;

import android.widget.TextView;

import de.domjos.mycache.R;
import de.domjos.mycache.model.custom.AbstractFragment;
import de.domjos.mycache.model.viewModel.HomeViewModel;

public class HomeFragment extends AbstractFragment<HomeViewModel> {
    private TextView textView;

    public HomeFragment() {
        super(R.layout.fragment_main_home);
    }

    @Override
    protected void initControls() {
        this.textView = this.root.findViewById(R.id.text_home);

    }

    @Override
    protected void initActions() {

    }

    @Override
    protected void initViewModel() {
        this.model.getText().observe(getViewLifecycleOwner(), s -> this.textView.setText(s));
    }
}
