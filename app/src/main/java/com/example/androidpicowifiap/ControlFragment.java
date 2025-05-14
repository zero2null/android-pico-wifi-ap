package com.example.androidpicowifiap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.androidpicowifiap.databinding.FragmentControlBinding;

public class ControlFragment extends Fragment {

    private FragmentControlBinding binding;

    private Api api;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentControlBinding.inflate(inflater, container, false);

        api = new Api();

        binding.buttonOff.setEnabled(false);

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                api.TurnOn(new Api.IOnProcessedListener() {
                    @Override
                    public void onProcessed(String result) {
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                binding.buttonOn.setEnabled(false);
                                binding.buttonOff.setEnabled(true);
                            }
                        });
                    }
                });
            }
        });

        binding.buttonOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                api.TurnOff(new Api.IOnProcessedListener() {
                    @Override
                    public void onProcessed(String result) {
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                binding.buttonOn.setEnabled(true);
                                binding.buttonOff.setEnabled(false);
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}