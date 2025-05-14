package com.example.androidpicowifiap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.androidpicowifiap.databinding.FragmentLoadingBinding;


public class LoadingFragment extends Fragment {

    private FragmentLoadingBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentLoadingBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity)this.getActivity()).connectWifi();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}