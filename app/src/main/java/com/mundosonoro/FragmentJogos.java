package com.mundosonoro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mundosonoro.databinding.ActivityMainBinding;
import com.mundosonoro.databinding.FragmentJogosBinding;

public class FragmentJogos extends Fragment {
    private FragmentJogosBinding binding;
    private Button btn_jg1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentJogosBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnJg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            FragmentJogo1 fragmentJogo1 = new FragmentJogo1();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragmentJogo1)
                    .addToBackStack(null)
                    .commit();
            }
        });
    }
}
