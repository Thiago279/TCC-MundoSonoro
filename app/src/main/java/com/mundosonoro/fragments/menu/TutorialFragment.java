package com.mundosonoro.fragments.menu;

import android.os.Bundle;
import android.media.MediaPlayer;
import androidx.fragment.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.speech.tts.TextToSpeech;
import android.view.accessibility.AccessibilityEvent;
import java.util.Locale;

import com.mundosonoro.R;
import com.mundosonoro.activities.MainActivity;
import com.mundosonoro.databinding.FragmentTutorialBinding;

public class TutorialFragment extends Fragment {
    private MediaPlayer mediaPlayer;
    private TextToSpeech textToSpeech;
    private FragmentTutorialBinding binding;
    private int etapaAtual = 1;
    private final int MAX_ETAPAS = 5;

    // Controle das etapas
    private boolean navegacaoCompleta = false;
    private boolean selecaoCompleta = false;
    private boolean repeticaoCompleta = false;
    private boolean praticaCompleta = false;

    // Contador para verificar navegação
    private int botoesFocalizados = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTutorialBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Inicializa TTS
        textToSpeech = new TextToSpeech(getContext(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(new Locale("pt", "BR"));
                iniciarEtapa1();
            }
        });

        configurarBotoes();
        configurarAcessibilidade();

        return view;
    }

    private void configurarBotoes() {
        // Botão repetir instrução
        binding.btnRepetirInstrucao.setOnClickListener(v -> {
            tocarFeedback(R.raw.correct_sfx);
            processarRepeticaoInstrucao();
        });

        // Botões de prática
        binding.btnPratica1.setOnClickListener(v -> processarCliqueBotao(1));
        binding.btnPratica2.setOnClickListener(v -> processarCliqueBotao(2));
        binding.btnPratica3.setOnClickListener(v -> processarCliqueBotao(3));
        binding.btnPratica4.setOnClickListener(v -> processarCliqueBotao(4));

        // Botão finalizar
        binding.btnFinalizarTutorial.setOnClickListener(v -> {
            tocarFeedback(R.raw.correct_sfx);
            finalizarTutorial();
        });
    }

    private void configurarAcessibilidade() {
        // Detecta quando um botão recebe foco para contar navegação
        View.AccessibilityDelegate accessibilityDelegate = new View.AccessibilityDelegate() {
            @Override
            public void onPopulateAccessibilityEvent(View host, AccessibilityEvent event) {
                super.onPopulateAccessibilityEvent(host, event);

                if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED) {
                    if (etapaAtual == 1) {
                        botoesFocalizados++;
                        if (botoesFocalizados >= 3 && !navegacaoCompleta) {
                            navegacaoCompleta = true;
                            tocarFeedback(R.raw.correct_sfx);
                            new Handler().postDelayed(() -> {
                                falarTexto("Muito bem! Você aprendeu a navegar. Vamos para a próxima etapa.");
                                new Handler().postDelayed(() -> iniciarEtapa2(), 3000);
                            }, 700);
                        }
                    }
                }
            }
        };

        // Aplicar delegate aos botões de prática
        binding.btnPratica1.setAccessibilityDelegate(accessibilityDelegate);
        binding.btnPratica2.setAccessibilityDelegate(accessibilityDelegate);
        binding.btnPratica3.setAccessibilityDelegate(accessibilityDelegate);
        binding.btnPratica4.setAccessibilityDelegate(accessibilityDelegate);
        binding.btnRepetirInstrucao.setAccessibilityDelegate(accessibilityDelegate);
    }

    private void iniciarEtapa1() {
        etapaAtual = 1;
        atualizarInterface();

        String instrucao = "Etapa 1: Navegação. " +
                "Deslize o dedo pela tela para navegar pelos botões. " +
                "Quando o TalkBack falar o nome de um botão, significa que está selecionado. " +
                "Navegue por pelo menos 3 botões diferentes.";

        falarTexto(instrucao);
    }

    private void iniciarEtapa2() {
        etapaAtual = 2;
        atualizarInterface();

        String instrucao = "Etapa 2: Seleção. " +
                "Agora você vai aprender a selecionar um botão. " +
                "Navegue até o botão 1 e toque duas vezes nele para selecioná-lo.";

        falarTexto(instrucao);
    }

    private void iniciarEtapa3() {
        etapaAtual = 3;
        atualizarInterface();

        String instrucao = "Etapa 3: Repetir instruções. " +
                "Se você esquecer uma instrução, pode repetí-la. " +
                "Navegue até o botão 'Repetir Instrução' e toque duas vezes.";

        falarTexto(instrucao);
    }

    private void iniciarEtapa4() {
        etapaAtual = 4;
        atualizarInterface();

        String instrucao = "Etapa 4: Prática livre. " +
                "Agora pratique! Navegue e selecione qualquer botão de prática. " +
                "Lembre-se: um toque seleciona, dois toques confirmam.";

        falarTexto(instrucao);
    }

    private void iniciarEtapa5() {
        etapaAtual = 5;
        atualizarInterface();
        binding.btnFinalizarTutorial.setVisibility(View.VISIBLE);

        String instrucao = "Etapa 5: Parabéns! " +
                "Você aprendeu a usar o aplicativo! " +
                "Agora você pode jogar todos os nossos games. " +
                "Para finalizar o tutorial, toque duas vezes no botão Finalizar.";

        falarTexto(instrucao);
    }

    private void processarCliqueBotao(int numeroBotao) {
        tocarFeedback(R.raw.correct_sfx);

        String feedback = "Você selecionou o botão " + numeroBotao + "!";

        new Handler().postDelayed(() -> {
            falarTexto(feedback);

            // Verifica progresso da etapa atual
            if (etapaAtual == 2 && numeroBotao == 1 && !selecaoCompleta) {
                selecaoCompleta = true;
                new Handler().postDelayed(() -> {
                    falarTexto("Perfeito! Você aprendeu a selecionar. Vamos continuar.");
                    new Handler().postDelayed(() -> iniciarEtapa3(), 3000);
                }, 2000);
            } else if (etapaAtual == 4 && !praticaCompleta) {
                praticaCompleta = true;
                new Handler().postDelayed(() -> {
                    falarTexto("Excelente! Você está dominando a navegação.");
                    new Handler().postDelayed(() -> iniciarEtapa5(), 3000);
                }, 2000);
            }

        }, 700);
    }

    private void processarRepeticaoInstrucao() {
        if (etapaAtual == 3 && !repeticaoCompleta) {
            repeticaoCompleta = true;
            new Handler().postDelayed(() -> {
                falarTexto("Ótimo! Você aprendeu a repetir instruções. Isso é muito útil!");
                new Handler().postDelayed(() -> iniciarEtapa4(), 3000);
            }, 1500);
        } else {
            repetirInstrucaoAtual();
        }
    }

    private void repetirInstrucaoAtual() {
        String instrucao = "";

        switch (etapaAtual) {
            case 1:
                instrucao = "Deslize o dedo pela tela para navegar pelos botões. Navegue por pelo menos 3 botões.";
                break;
            case 2:
                instrucao = "Navegue até o botão 1 e toque duas vezes nele para selecioná-lo.";
                break;
            case 3:
                instrucao = "Navegue até o botão Repetir Instrução e toque duas vezes para testá-lo.";
                break;
            case 4:
                instrucao = "Pratique navegando e selecionando qualquer botão de prática.";
                break;
            case 5:
                instrucao = "Para finalizar o tutorial, navegue até o botão Finalizar e toque duas vezes.";
                break;
        }

        falarTexto(instrucao);
    }

    private void atualizarInterface() {
        binding.etapaTutorial.setText("Etapa: " + etapaAtual + "/" + MAX_ETAPAS);

        String descricaoEtapa = "";
        switch (etapaAtual) {
            case 1:
                descricaoEtapa = "Aprendendo a navegar";
                break;
            case 2:
                descricaoEtapa = "Aprendendo a selecionar";
                break;
            case 3:
                descricaoEtapa = "Usando repetir instrução";
                break;
            case 4:
                descricaoEtapa = "Praticando navegação";
                break;
            case 5:
                descricaoEtapa = "Tutorial concluído!";
                break;
        }

        binding.instrucaoAtual.setText(descricaoEtapa);
        binding.etapaTutorial.setContentDescription("Etapa " + etapaAtual + " de " + MAX_ETAPAS);
    }

    private void finalizarTutorial() {
        String finalizacao = "Parabéns! Você completou o tutorial com sucesso!-";

        falarTexto(finalizacao);

        new Handler().postDelayed(() -> {
            ((MainActivity) getActivity()).voltarParaMenu();
        }, 6000);
    }

    private void falarTexto(String texto) {
        if (textToSpeech != null) {
            textToSpeech.speak(texto, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    private void tocarFeedback(int resourceId) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mediaPlayer = MediaPlayer.create(getContext(), resourceId);
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(0.5f, 0.5f);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(mp -> {
                mp.release();
                mediaPlayer = null;
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}