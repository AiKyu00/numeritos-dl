import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Interfaz extends JFrame {
    private FileDialog fileDialog;
    private JButton btnFileSelector;
    private JButton btnStart;
    private BorderLayout bl;
    private TextField txtField;

    public Interfaz() {
        this.fileDialog = new FileDialog(this, "Choose a file", FileDialog.LOAD);
        this.fileDialog.setDirectory("C:\\");
        this.fileDialog.setFile("*.txt");
        this.btnFileSelector = new JButton("Seleccionar fichero");
        this.btnStart = new JButton("Empezar");
        this.txtField = new TextField();
        this.txtField.setEditable(false);
        this.bl = new BorderLayout();


        this.setLayout(this.bl);

        this.add(this.btnFileSelector, BorderLayout.NORTH);
        this.add(this.txtField, BorderLayout.CENTER);
        this.add(this.btnStart, BorderLayout.SOUTH);


        this.btnFileSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileDialog.setVisible(true);
                String filename = fileDialog.getFile();
                txtField.setText(fileDialog.getDirectory() + filename);
            }
        });

        this.btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Numeritos numeritos = new Numeritos();
                if (numeritos.obtainLineas(txtField.getText().trim())) {
                    showAlert(numeritos.getLineasNuevas());
                }
            }
        });


        this.setVisible(true);
        this.setResizable(false);
        this.setSize(480, 150);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }


    public void showAlert(int alertas) {
        JOptionPane.showMessageDialog(this,
                "Se han insertado " + alertas + " nuevas entrasdas a la base [basado]",
                "Operacion finalizada.",
                JOptionPane.PLAIN_MESSAGE);
    }
}
