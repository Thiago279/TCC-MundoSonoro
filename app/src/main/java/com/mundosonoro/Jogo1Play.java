package com.mundosonoro;

import android.os.Bundle;
import android.media.MediaPlayer;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mundosonoro.databinding.ActivityMainBinding;
import com.mundosonoro.databinding.FragmentJogo1Binding;
import com.mundosonoro.databinding.FragmentJogo1PlayBinding;
import com.mundosonoro.databinding.FragmentJogosBinding;


public class Jogo1Play extends Fragment {
    private MediaPlayer mediaPlayer;
    private Button btnRepetir;
    private FragmentJogo1PlayBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentJogo1PlayBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        //tocar som
        tocarSom(R.raw.fazenda);

        //repetir som
        binding.btnRepetir.setOnClickListener(v -> tocarSom(R.raw.fazenda));

        return view;
    }

    private void tocarSom(int somResource){
        if (mediaPlayer != null){
            mediaPlayer.release(); //libera qualquer recurso de audio anterior
        }

        mediaPlayer = MediaPlayer.create(getContext(), somResource);
        mediaPlayer.start();

        mediaPlayer.setOnCompletionListener(mp -> {
            mp.release(); //libera recursos quando o som terminar
        });
    }





    @Override
    public void onDestroy(){
        super.onDestroy();
        if(mediaPlayer != null){
            mediaPlayer.release(); //libera o player ao sair do fragment
            mediaPlayer = null;
        }
    }

}