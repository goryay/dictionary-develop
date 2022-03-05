package view;

import controller.DictController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

//Для добавлдения нового слова, открывается новое окно
public class NewWordWindow extends JFrame {
    DictController dictController = new DictController();

    JLabel wordLabel;
    JTextField wordField;
    JLabel meanLabel;
    JTextArea meanField;
    JButton okButton;
    JButton cancelButton;
    JPanel wordPanel;
    JPanel meanPanel;
    JPanel buttonsPanel;
    JPanel contentPanel;

    public NewWordWindow() {
        super("Добавить новое слово");

        wordLabel = new JLabel("Слово");
        wordField = new JTextField(40);
        meanLabel = new JLabel("Значение");
        meanField = new JTextArea("", 10, 40);
        okButton = new JButton("Добавить");
        cancelButton = new JButton("Отмена");

        okButton.addActionListener(e -> {
            if (!wordField.getText().isEmpty() && !meanField.getText().isEmpty()){
                String res = dictController.addWord(wordField.getText(), meanField.getText()); //Передача слова и значения
                if (isNumeric(res)){
                    JOptionPane.showMessageDialog(this,
                        "Слово успешно добавлено c id=" + res,
                        "Успешно!",
                        JOptionPane.INFORMATION_MESSAGE);
                    MainWindow.setLocked(false);
                    this.dispose();
                }
                else
                    JOptionPane.showMessageDialog(this,
                        "Ошибка добавления: " + res,
                        "Ошибка!",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        cancelButton.addActionListener(e -> {
            MainWindow.setLocked(false);
            this.dispose();
        });

        wordPanel = new JPanel(new BorderLayout());
        wordPanel.add(wordLabel, BorderLayout.WEST);
        wordPanel.add(wordField, BorderLayout.EAST);

        meanPanel = new JPanel(new BorderLayout());
        meanPanel.add(meanLabel, BorderLayout.WEST);
        meanPanel.add(meanField, BorderLayout.EAST);

        buttonsPanel = new JPanel(new BorderLayout());
        buttonsPanel.add(okButton, BorderLayout.WEST);
        buttonsPanel.add(cancelButton, BorderLayout.EAST);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(wordPanel, BorderLayout.NORTH);
        contentPanel.add(meanPanel, BorderLayout.CENTER);
        contentPanel.add(buttonsPanel, BorderLayout.SOUTH);
        setContentPane(contentPanel);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);

        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                MainWindow.setLocked(false);
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
    }

    public static boolean isNumeric(final String str) {
        if (str == null || str.length() == 0) return false;
        return str.chars().allMatch(Character::isDigit);
    }

}
