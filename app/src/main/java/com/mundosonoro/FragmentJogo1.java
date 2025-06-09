package com.mundosonoro;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mundosonoro.databinding.FragmentJogo1Binding;
import com.mundosonoro.databinding.FragmentJogosBinding;


public class FragmentJogo1 extends Fragment {

    private FragmentJogo1Binding binding;
    private Button btn_play;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentJogo1Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnPlay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Jogo1Play jogo1play = new Jogo1Play();
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, jogo1play)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}