package com.mundosonoro.fragments.menu;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mundosonoro.R;
import com.mundosonoro.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ação do botão "JOGAR"
        binding.botaoJogar.setOnClickListener(v -> {
            SeletorJogosFragment jogosFragment = new SeletorJogosFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, jogosFragment)
                    .addToBackStack(null)
                    .commit();
        });

        // Ação do botão "CONFIGURAÇÕES"
        binding.botaoConfig.setOnClickListener(v -> {
            // TODO: Criar e abrir o fragmento de configurações se necessário
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}