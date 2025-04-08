package choose.your.team;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.File;

public class ChooseYourTeam extends JFrame implements KeyListener {
    private JPanel painelImagens;
    private JLabel labelRodada;
    private JLabel labelInstrucoes;
    private ArrayList<Integer> numerosAtuais;
    private ArrayList<Integer> selecionados;
    private int indiceAtual = 0;
    private int fase = 0;
    private final int[] tamanhosFase = {162, 54, 18, 6};
    private final String caminhoImagens = "../imagens/";

    public ChooseYourTeam() {
        setTitle("Choose Your Team");
        setSize(1000, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel painelTopo = new JPanel(new BorderLayout());
        labelRodada = new JLabel();
        labelRodada.setFont(new Font("Arial", Font.BOLD, 16));
        labelRodada.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 0));
        painelTopo.add(labelRodada, BorderLayout.WEST);

        labelInstrucoes = new JLabel("Aperte de 1 a 6, conforme a ordem de imagens da esquerda para a direita. Não é possível a mesma escolha 2 vezes.");
        labelInstrucoes.setFont(new Font("Arial", Font.PLAIN, 14));
        labelInstrucoes.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 10));
        painelTopo.add(labelInstrucoes, BorderLayout.EAST);

        add(painelTopo, BorderLayout.NORTH);

        painelImagens = new JPanel(new GridLayout(1, 6));
        add(painelImagens, BorderLayout.CENTER);

        addKeyListener(this);
        setFocusable(true);
        iniciarJogo();

        setVisible(true);
    }

    private void iniciarJogo() {
        numerosAtuais = new ArrayList<>();
        for (int i = 1; i <= 162; i++) numerosAtuais.add(i);
        Collections.shuffle(numerosAtuais);
        selecionados = new ArrayList<>();
        mostrarGrupoAtual();
    }

    private void atualizarLabelRodada() {
        if (fase == tamanhosFase.length - 1) {
            labelRodada.setText("Final Team:");
        } else {
            int escolha = (indiceAtual / 6) + 1;
            labelRodada.setText("Rodada " + (fase + 1) + " - Escolha " + escolha);
        }
    }

    private void mostrarGrupoAtual() {
        atualizarLabelRodada();
        painelImagens.removeAll();
        int fim = Math.min(indiceAtual + 6, numerosAtuais.size());

        for (int i = indiceAtual; i < fim; i++) {
            int numero = numerosAtuais.get(i);
            JPanel painel = new JPanel(new BorderLayout());

            JLabel imgLabel = new JLabel();
            String caminho = caminhoImagens + numero + ".jpeg";
            if (new File(caminho).exists()) {
                ImageIcon icone = new ImageIcon(caminho);
                imgLabel.setIcon(new ImageIcon(icone.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH)));
            } else {
                imgLabel.setText("Sem imagem");
                imgLabel.setHorizontalAlignment(JLabel.CENTER);
            }

            JLabel numLabel = new JLabel(String.valueOf(numero), JLabel.CENTER);
            painel.add(imgLabel, BorderLayout.CENTER);
            painel.add(numLabel, BorderLayout.SOUTH);
            painelImagens.add(painel);
        }

        painelImagens.revalidate();
        painelImagens.repaint();
    }

    private void proximoGrupo() {
        indiceAtual += 6;

        if (indiceAtual >= numerosAtuais.size()) {
            if (fase == tamanhosFase.length - 2) { 
                numerosAtuais = new ArrayList<>(selecionados);
                selecionados.clear();
                indiceAtual = 0;
                fase++;
                mostrarGrupoAtual(); 
                return;
            }

            if (fase == tamanhosFase.length - 1) {
                indiceAtual = 0;
                mostrarGrupoAtual();
                return;
            }

            numerosAtuais = new ArrayList<>(selecionados);
            selecionados.clear();
            indiceAtual = 0;
            fase++;
        }

        mostrarGrupoAtual();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int tecla = e.getKeyCode();
        if (tecla >= KeyEvent.VK_1 && tecla <= KeyEvent.VK_6) {
            int escolha = tecla - KeyEvent.VK_1;
            int indexReal = indiceAtual + escolha;
            if (indexReal < numerosAtuais.size() && !selecionados.contains(numerosAtuais.get(indexReal))) {
                selecionados.add(numerosAtuais.get(indexReal));
                if (selecionados.size() % 2 == 0) {
                    proximoGrupo();
                }
            }
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChooseYourTeam::new);
    }
}
