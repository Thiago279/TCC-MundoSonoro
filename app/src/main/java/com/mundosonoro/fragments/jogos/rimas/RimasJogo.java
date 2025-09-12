package com.mundosonoro.fragments.jogos.rimas;

import android.os.Bundle;
import android.media.MediaPlayer;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mundosonoro.R;
import com.mundosonoro.activities.MainActivity;
import com.mundosonoro.databinding.FragmentRimasJogoBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import android.speech.tts.TextToSpeech;
import java.util.Locale;

public class RimasJogo extends Fragment {
    private MediaPlayer mediaPlayer;
    private TextToSpeech textToSpeech;
    private FragmentRimasJogoBinding binding;
    private List<ParRimas> paresRimas;
    private ParRimas parAtual;
    private int pontos = 0;
    private int rodadaAtual = 1;
    private final int MAX_RODADAS = 10;

    // Variáveis de seleção de palavras
    private String primeiraPalavra = "";
    private String segundaPalavra = "";
    private int primeiraSelecao = -1;
    private int segundaSelecao = -1;

    // Lista para armazenar as opções embaralhadas
    private List<String> opcoesEmbaralhadas;

    // Flag para controlar se o jogo está processando resposta
    private boolean processandoResposta = false;

    // Classe para representar um par de rimas + distratores
    private static class ParRimas {
        String palavra1;
        String palavra2;
        String distrator1;
        String distrator2;
        String categoria;
        int dificuldade;

        public ParRimas(String palavra1, String palavra2, String distrator1, String distrator2, String categoria, int dificuldade) {
            this.palavra1 = palavra1;
            this.palavra2 = palavra2;
            this.distrator1 = distrator1;
            this.distrator2 = distrator2;
            this.categoria = categoria;
            this.dificuldade = dificuldade;
        }

        public String[] getTodasPalavras() {
            return new String[]{palavra1, palavra2, distrator1, distrator2};
        }

        public boolean ehParCorreto(String p1, String p2) {
            return (p1.equalsIgnoreCase(palavra1) && p2.equalsIgnoreCase(palavra2)) ||
                    (p1.equalsIgnoreCase(palavra2) && p2.equalsIgnoreCase(palavra1));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRimasJogoBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        inicializarParesRimas();

        // Inicializa TTS
        textToSpeech = new TextToSpeech(getContext(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(new Locale("pt", "BR"));
                sortearPar();
            }
        });

        // Botão para repetir a instrução
        binding.btnRepetirPergunta.setOnClickListener(v -> falarInstrucao());

        // Clique dos botões de opções
        binding.opcao1.setOnClickListener(v -> selecionarPalavra(0));
        binding.opcao2.setOnClickListener(v -> selecionarPalavra(1));
        binding.opcao3.setOnClickListener(v -> selecionarPalavra(2));
        binding.opcao4.setOnClickListener(v -> selecionarPalavra(3));

        return view;
    }

    private void inicializarParesRimas() {
        paresRimas = new ArrayList<>();

        // RIMAS FÁCEIS
        paresRimas.add(new ParRimas("GATO", "RATO", "carro", "casa", "Animais", 1));
        paresRimas.add(new ParRimas("PATO", "SAPATO", "livro", "mesa", "Objetos", 1));
        paresRimas.add(new ParRimas("CORAÇÃO", "PAIXÃO", "festa", "janela", "Sentimentos", 1));
        paresRimas.add(new ParRimas("CAMELO", "MARTELO", "cadeira", "papel", "Ferramentas", 1));
        paresRimas.add(new ParRimas("PANELA", "JANELA", "porta", "fogão", "Casa", 1));
        paresRimas.add(new ParRimas("VIOLÃO", "SABÃO", "guitarra", "toalha", "Música", 1));
        paresRimas.add(new ParRimas("BALEIA", "SEREIA", "peixe", "golfinho", "Mar", 1));
        paresRimas.add(new ParRimas("CADEIRA", "POEIRA", "mesa", "sofá", "Casa", 1));
        paresRimas.add(new ParRimas("FLORES", "CORES", "jardim", "plantas", "Natureza", 1));
        paresRimas.add(new ParRimas("BOLO", "ROLO", "doce", "festa", "Comida", 1));
        paresRimas.add(new ParRimas("MESA", "PRINCESA", "livro", "porta", "Objetos", 1));
        paresRimas.add(new ParRimas("SAPO", "CAPO", "bola", "nuvem", "Animais", 1));
        paresRimas.add(new ParRimas("CÃO", "PÃO", "cadeira", "vento", "Animais", 1));
        paresRimas.add(new ParRimas("PEIXE", "QUEIXE", "tigre", "pato", "Animais", 1));
        paresRimas.add(new ParRimas("BOLA", "ESCOLA", "árvore", "pente", "Objetos", 1));
        paresRimas.add(new ParRimas("FADA", "ESPADA", "sol", "livro", "Fantasia", 1));
        paresRimas.add(new ParRimas("SAPATO", "GATO", "mão", "flor", "Objetos", 1));
        paresRimas.add(new ParRimas("CAMA", "DAMA", "rio", "vento", "Casa", 1));
        paresRimas.add(new ParRimas("LUZ", "CRUZ", "pato", "gelo", "Religião", 1));

        // RIMAS MÉDIAS
        paresRimas.add(new ParRimas("TELEFONE", "MICROFONE", "televisão", "computador", "Tecnologia", 2));
        paresRimas.add(new ParRimas("FOGUETE", "SORVETE", "avião", "chocolate", "Diversos", 2));
        paresRimas.add(new ParRimas("PINGUIM", "JARDIM", "urso", "floresta", "Natureza", 2));
        paresRimas.add(new ParRimas("CAVALO", "RALO", "vaca", "banheiro", "Diversos", 2));
        paresRimas.add(new ParRimas("BONECA", "BIBLIOTECA", "brinquedo", "escola", "Objetos", 2));
        paresRimas.add(new ParRimas("VENTILADOR", "COMPUTADOR", "televisão", "geladeira", "Eletrônicos", 2));
        paresRimas.add(new ParRimas("CAMINHO", "NINHO", "janela", "relógio", "Natureza", 2));
        paresRimas.add(new ParRimas("MAR", "LAR", "tigre", "gelo", "Natureza", 2));
        paresRimas.add(new ParRimas("FLORESTA", "FESTA", "pedra", "avião", "Natureza", 2));
        paresRimas.add(new ParRimas("SINO", "MENINO", "porta", "chave", "Objetos", 2));
        paresRimas.add(new ParRimas("FOGO", "JOGO", "mão", "janela", "Natureza", 2));
        paresRimas.add(new ParRimas("PONTE", "FRONTE", "mesa", "anel", "Construções", 2));
        paresRimas.add(new ParRimas("LUA", "RUA", "flor", "copo", "Natureza", 2));
        paresRimas.add(new ParRimas("SORTE", "NORTE", "pato", "navio", "Conceitos", 2));
        paresRimas.add(new ParRimas("TERRA", "GUERRA", "nuvem", "vidro", "Natureza", 2));
        paresRimas.add(new ParRimas("CHÃO", "MÃO", "ninho", "peixe", "Natureza", 2));

        // RIMAS DIFÍCEIS
        paresRimas.add(new ParRimas("ELEFANTE", "DIAMANTE", "rinoceronte", "esmeralda", "Diversos", 3));
        paresRimas.add(new ParRimas("IMPORTANTE", "INTERESSANTE", "necessário", "fundamental", "Qualidades", 3));
        paresRimas.add(new ParRimas("CHOCOLATE", "ABACATE", "morango", "banana", "Alimentos", 3));
        paresRimas.add(new ParRimas("VERDE", "PERDE", "bola", "vento", "Cores", 3));
        paresRimas.add(new ParRimas("ESPERANÇA", "DANÇA", "janela", "relógio", "Conceitos", 3));
        paresRimas.add(new ParRimas("HORIZONTE", "FONTE", "rio", "carro", "Natureza", 3));
        paresRimas.add(new ParRimas("LAMENTO", "VENTO", "chuva", "fogo", "Conceitos", 3));
        paresRimas.add(new ParRimas("CAMINHÃO", "CANÇÃO", "porta", "estrela", "Objetos", 3));
        paresRimas.add(new ParRimas("DESTINO", "MENINO", "copo", "árvore", "Conceitos", 3));
        paresRimas.add(new ParRimas("MEMÓRIA", "GLÓRIA", "fada", "peixe", "Conceitos", 3));
        paresRimas.add(new ParRimas("PAZ", "JAZ", "navio", "flor", "Conceitos", 3));
        paresRimas.add(new ParRimas("INSPIRAÇÃO", "CANÇÃO", "ponte", "estrela", "Conceitos", 3));
        paresRimas.add(new ParRimas("SABEDORIA", "ALEGRIA", "chuva", "vento", "Conceitos", 3));
        paresRimas.add(new ParRimas("ILUSÃO", "PAIXÃO", "copo", "janela", "Conceitos", 3));
        paresRimas.add(new ParRimas("CORAGEM", "VIAGEM", "floresta", "sol", "Conceitos", 3));
        paresRimas.add(new ParRimas("TRISTEZA", "BELEZA", "lua", "mar", "Conceitos", 3));

    }

    private void sortearPar() {
        Random random = new Random();
        int index = random.nextInt(paresRimas.size());
        parAtual = paresRimas.get(index);

        // Cria uma lista com todas as palavras e embaralha
        opcoesEmbaralhadas = new ArrayList<>();
        String[] todasPalavras = parAtual.getTodasPalavras();
        Collections.addAll(opcoesEmbaralhadas, todasPalavras);
        Collections.shuffle(opcoesEmbaralhadas);

        // Atualiza texto dos botões com opções embaralhadas
        binding.opcao1.setText(opcoesEmbaralhadas.get(0).toUpperCase());
        binding.opcao2.setText(opcoesEmbaralhadas.get(1).toUpperCase());
        binding.opcao3.setText(opcoesEmbaralhadas.get(2).toUpperCase());
        binding.opcao4.setText(opcoesEmbaralhadas.get(3).toUpperCase());

        // Atualiza informações da rodada
        binding.rodada.setText("Rodada: " + rodadaAtual + "/" + MAX_RODADAS);

        // Resetar seleções no início da rodada
        resetarSelecoes();
        processandoResposta = false;

        //Fala o número da rodada antes da instrução
        if (textToSpeech != null) {
            String anuncioRodada = "Rodada " + rodadaAtual;
            textToSpeech.speak(anuncioRodada, TextToSpeech.QUEUE_FLUSH, null, null);

            //Fala a instrução após um delay
            new Handler().postDelayed(this::falarInstrucao, 1500);
        }
    }

    private void falarInstrucao() {
        if (textToSpeech != null) {
            String instrucao = "Encontre o par de palavras que rimam entre as quatro opções.";
            textToSpeech.speak(instrucao, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    private void selecionarPalavra(int indiceEscolhido) {
        // Evita seleções durante processamento
        if (processandoResposta) {
            return;
        }

        // Obtém a palavra do índice embaralhado
        String palavraEscolhida = opcoesEmbaralhadas.get(indiceEscolhido);

        // Fala a palavra selecionada
        if (textToSpeech != null) {
            textToSpeech.speak(palavraEscolhida.toLowerCase(), TextToSpeech.QUEUE_FLUSH, null, null);
        }

        // Lógica de seleção
        if (primeiraPalavra.isEmpty()) {
            // Primeira seleção
            primeiraPalavra = palavraEscolhida;
            primeiraSelecao = indiceEscolhido;
            destacarBotao(indiceEscolhido, true);

            new Handler().postDelayed(() -> {
                if (textToSpeech != null) {
                    textToSpeech.speak("Primeira palavra selecionada: " + palavraEscolhida.toLowerCase() +
                            ". Agora escolha a segunda palavra!", TextToSpeech.QUEUE_FLUSH, null, null);
                }
            }, 800);

        } else if (primeiraSelecao == indiceEscolhido) {
            // Clicou na mesma palavra já selecionada - deseleciona
            resetarSelecoes();
            new Handler().postDelayed(() -> {
                if (textToSpeech != null) {
                    textToSpeech.speak("Palavra desmarcada. Escolha duas palavras diferentes.",
                            TextToSpeech.QUEUE_FLUSH, null, null);
                }
            }, 500);

        } else if (segundaPalavra.isEmpty()) {
            // Segunda seleção (palavra diferente)
            segundaPalavra = palavraEscolhida;
            segundaSelecao = indiceEscolhido;
            destacarBotao(indiceEscolhido, true);
            processandoResposta = true;

            new Handler().postDelayed(() -> {
                if (textToSpeech != null) {
                    textToSpeech.speak("Segunda palavra selecionada: " + palavraEscolhida.toLowerCase(),
                            TextToSpeech.QUEUE_FLUSH, null, null);
                }
                // Checa a resposta após o TTS
                new Handler().postDelayed(this::checarResposta, 3000);
            }, 800);

        } else {
            // Já tem duas palavras selecionadas - permite nova seleção
            resetarSelecoes();
            primeiraPalavra = palavraEscolhida;
            primeiraSelecao = indiceEscolhido;
            destacarBotao(indiceEscolhido, true);

            new Handler().postDelayed(() -> {
                if (textToSpeech != null) {
                    textToSpeech.speak("Nova seleção iniciada. Primeira palavra: " +
                            palavraEscolhida.toLowerCase(), TextToSpeech.QUEUE_FLUSH, null, null);
                }
            }, 800);
        }
    }

    private void destacarBotao(int indice, boolean destacar) {
        int cor = destacar ? R.color.verde_cinzento : R.color.verde_claro;
        int corTexto = destacar ? R.color.verde_claro : R.color.verde_cinzento;

        switch (indice) {
            case 0:
                binding.opcao1.setBackgroundTintList(getResources().getColorStateList(cor));
                binding.opcao1.setTextColor(getResources().getColor(corTexto));
                break;
            case 1:
                binding.opcao2.setBackgroundTintList(getResources().getColorStateList(cor));
                binding.opcao2.setTextColor(getResources().getColor(corTexto));
                break;
            case 2:
                binding.opcao3.setBackgroundTintList(getResources().getColorStateList(cor));
                binding.opcao3.setTextColor(getResources().getColor(corTexto));
                break;
            case 3:
                binding.opcao4.setBackgroundTintList(getResources().getColorStateList(cor));
                binding.opcao4.setTextColor(getResources().getColor(corTexto));
                break;
        }
    }

    private void resetarCoresBotoes() {
        int corPadrao = R.color.verde_claro;
        int corTextoPadrao = R.color.verde_cinzento;

        binding.opcao1.setBackgroundTintList(getResources().getColorStateList(corPadrao));
        binding.opcao1.setTextColor(getResources().getColor(corTextoPadrao));
        binding.opcao2.setBackgroundTintList(getResources().getColorStateList(corPadrao));
        binding.opcao2.setTextColor(getResources().getColor(corTextoPadrao));
        binding.opcao3.setBackgroundTintList(getResources().getColorStateList(corPadrao));
        binding.opcao3.setTextColor(getResources().getColor(corTextoPadrao));
        binding.opcao4.setBackgroundTintList(getResources().getColorStateList(corPadrao));
        binding.opcao4.setTextColor(getResources().getColor(corTextoPadrao));
    }

    private void resetarSelecoes() {
        resetarCoresBotoes();
        primeiraPalavra = "";
        segundaPalavra = "";
        primeiraSelecao = -1;
        segundaSelecao = -1;
        processandoResposta = false;
    }

    private void checarResposta() {
        boolean acertou = parAtual.ehParCorreto(primeiraPalavra, segundaPalavra);

        if (acertou) {
            pontos += 10;
            binding.pontos.setText("Pontos: " + pontos);

            tocarFeedback(R.raw.correct_sfx);

            new Handler().postDelayed(() -> {
                if (textToSpeech != null) {
                    textToSpeech.speak("Correto! " + primeiraPalavra.toLowerCase() + " rima com " +
                            segundaPalavra.toLowerCase() + "! Mais 10 pontos!", TextToSpeech.QUEUE_FLUSH, null, null);
                }
            }, 700);

        } else {
            tocarFeedback(R.raw.wrong_sfx);

            new Handler().postDelayed(() -> {
                if (textToSpeech != null) {
                    textToSpeech.speak("Incorreto. " + ". O par correto era " +
                                    parAtual.palavra1.toLowerCase() + " e " + parAtual.palavra2.toLowerCase(),
                            TextToSpeech.QUEUE_FLUSH, null, null);
                }
            }, 700);
        }

        rodadaAtual++;

        if (rodadaAtual > MAX_RODADAS) {
            finalizarJogo();
        } else {
            new Handler().postDelayed(this::sortearPar, 5700);
        }
    }

    private void finalizarJogo() {
        new Handler().postDelayed(() -> {
            String resultado = "Jogo finalizado! Você fez " + pontos + " pontos!";
            if (pontos >= 80) {
                resultado += " Excelente! Você é um mestre das rimas!";
            } else if (pontos >= 60) {
                resultado += " Muito bom! Continue praticando as rimas!";
            } else if (pontos >= 40) {
                resultado += " Bom trabalho! As rimas ficam mais fáceis com a prática!";
            } else {
                resultado += " Continue praticando! As rimas são divertidas!";
            }
            textToSpeech.speak(resultado, TextToSpeech.QUEUE_FLUSH, null, null);

            new Handler().postDelayed(() -> {
                ((MainActivity) getActivity()).voltarParaMenu();
            }, 8000);

        }, 5700);
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