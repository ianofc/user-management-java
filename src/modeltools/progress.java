package modeltools;

import javax.swing.*;
import java.awt.*;

public class progress {
    private JDialog progressoDialog;
    private JProgressBar barraProgresso;

    public void mostrarProgresso(JFrame parent, String mensagem) {
        progressoDialog = new JDialog(parent, "Progresso", true);
        progressoDialog.setLayout(new BorderLayout());
        progressoDialog.setSize(300, 100);
        progressoDialog.setLocationRelativeTo(parent);

        JLabel labelMensagem = new JLabel(mensagem, JLabel.CENTER);
        barraProgresso = new JProgressBar();
        barraProgresso.setIndeterminate(true); // Para operações sem tempo estimado

        progressoDialog.add(labelMensagem, BorderLayout.NORTH);
        progressoDialog.add(barraProgresso, BorderLayout.CENTER);
        progressoDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        new Thread(() -> progressoDialog.setVisible(true)).start();
    }

    public void esconderProgresso() {
        if (progressoDialog != null) {
            progressoDialog.dispose();
        }
    }
}
