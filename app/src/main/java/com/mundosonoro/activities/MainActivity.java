package com.mundosonoro.activities;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.mundosonoro.fragments.jogos.rimas.RimasJogo;
import com.mundosonoro.fragments.menu.HomeFragment;
import com.mundosonoro.fragments.jogos.traduzir.TraduzirJogo; // Adicionado import
import com.mundosonoro.R;
import com.mundosonoro.databinding.ActivityMainBinding;
import com.mundosonoro.fragments.menu.TutorialFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private Button botao_jogar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configuração da barra de status
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        WindowInsetsControllerCompat insetsController = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
        insetsController.setAppearanceLightStatusBars(false);

        // Carrega o fragmento inicial se ainda não foi carregado
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }
    }

    // Método para navegar para o jogo de tradução - Adicionado
    public void navegarParaTraduzirJogo() {
        TraduzirJogo traduzirJogo = new TraduzirJogo();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, traduzirJogo)
                .addToBackStack(null)
                .commit();
    }

    //Metodo para navegar para o jogo de rimas
    public void navegarParaRimasJogo(){
        RimasJogo rimasJogo = new RimasJogo();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, rimasJogo)
                .addToBackStack(null)
                .commit();
    }

    //Metodo para navegar para o tutorial
    public void navegarParaTutorial(){
        TutorialFragment tutorialFragment = new TutorialFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, tutorialFragment)
                .addToBackStack(null)
                .commit();
    }

    // Método para voltar ao menu principal - Adicionado
    public void voltarParaMenu() {
        HomeFragment homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, homeFragment)
                .commit();

        // Limpa a pilha de fragments
        getSupportFragmentManager().popBackStack(null, getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
    }
}