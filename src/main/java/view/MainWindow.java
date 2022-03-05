package view;

import controller.DictController;
import utils.XmlConfig;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;

//Описываем поведение окна программы
public class MainWindow extends JFrame {
    DictController dictController = new DictController();
    private static JFrame frame = null;
    JMenuBar menuBar;
    JTextField searchField;
    JButton searchButton;
    JList<String> listPane;
    JTextArea textArea;
    JPanel contentPanel;
    JPanel leftPanel;
    JPanel rightPanel;
    JPanel searchPanel;
    JPanel listPanel;
    JPanel meanPanel;

    //Создаем параметры и конструируем окно
    public MainWindow () {
        super("Толковый словарь");
        frame = this;

        menuBar = new JMenuBar();
        searchButton = new JButton();
        searchField = new JTextField(25);
        listPane = new JList<>();
        textArea = new JTextArea("", 10, 40);

        menuBar.add(createFileMenu());
        menuBar.add(createEditMenu());
        menuBar.add(createAboutMenu(frame));
        setJMenuBar(menuBar);

        //Провекрка на ввода
        searchField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    listPane.setListData(dictController.getWordsList(searchField.getText())); //Вывода списка
            }
        });
        searchButton.addActionListener(e -> listPane.setListData(dictController.getWordsList(searchField.getText()))); //Поиск по кнопке
        listPane.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if ( e.getClickCount() == 1 ) {
                    ArrayList<String> list = (ArrayList<String>) listPane.getSelectedValuesList();
                    String mean = list.get(list.size() - 1);
                    textArea.setText(mean + ": " + dictController.getMean(mean)); //Вывод текста определения
                }
            }
        });

        //Установка картинки
        try {
            Image img = ImageIO.read(new File(new XmlConfig().getByKey("media_folder") + "magnifier.png"));
            Image new_img = img.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
            searchButton.setIcon(new ImageIcon(new_img));
            searchButton.setPreferredSize(new Dimension(20,20));
        } catch (IOException e) {//Установка текста, если не открылась картинка
            e.printStackTrace();
            searchButton.setText("Поиск");
            searchButton.setPreferredSize(new Dimension(80,20));
        }
        searchButton.setBorderPainted(false);
        searchButton.setFocusPainted(false);
        searchButton.setContentAreaFilled(false);

        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        contentPanel = new JPanel(new BorderLayout());
        leftPanel = new JPanel(new BorderLayout());
        rightPanel = new JPanel(new BorderLayout());
        searchPanel = new JPanel(new BorderLayout());
        listPanel = new JPanel(new BorderLayout());
        meanPanel = new JPanel(new BorderLayout());

        searchPanel.add(searchField, BorderLayout.WEST);
        searchPanel.add(searchButton, BorderLayout.EAST);
        leftPanel.add(searchPanel, BorderLayout.NORTH);

        listPanel.add(new JScrollPane(listPane), BorderLayout.CENTER);
        leftPanel.add(listPanel, BorderLayout.SOUTH);

        meanPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        rightPanel.add(meanPanel, BorderLayout.CENTER);

        contentPanel.add(leftPanel, BorderLayout.WEST);
        contentPanel.add(rightPanel, BorderLayout.EAST);
        setContentPane(contentPanel);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setMinimumSize(new Dimension(740, 200));
        setVisible(true);
    }

    public static void setLocked(boolean isLock){
        frame.setEnabled(!isLock);
    }

    //Строка explorer в окне file
    private JMenu createFileMenu()
    {
        JMenu file = new JMenu("Файл");

        JMenuItem clear = new JMenuItem("Очистить");
        clear.addActionListener(e -> {
            searchField.setText("");
            listPane.setListData(new String[] {});
            textArea.setText("");
        });

        JMenuItem search = new JMenuItem("Найти");
        search.addActionListener(e -> listPane.setListData(dictController.getWordsList(searchField.getText())));

        JMenuItem exit = new JMenuItem(new ExitAction());

        file.add(clear);
        file.add(search);
        file.addSeparator();
        file.add(exit);

        return file;
    }

    //Строка explorer в окне edit
    private JMenu createEditMenu()
    {
        JMenu edit = new JMenu("Изменить слово");

        JMenuItem new_word = new JMenuItem("Добавить слово");

        new_word.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new NewWordWindow();
                setLocked(true);
            }
        });

        edit.add(new_word);
        edit.addSeparator();

        return edit;
    }

    //Строка explorer в окне справка
    private JMenu createAboutMenu(JFrame frame)
    {
        JMenu about = new JMenu("Справка");
        JMenuItem help = new JMenuItem("Справка");
        help.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame,
                        """
                                Программа "Толковый словарь" реализована в рамках выполнения 
                                практического задания по дисцеплине Технологии разработки 
                                качественного программного обеспечения\s
                                Горяйнов И.Р.\s
                                2022""",
                        "О программе",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        about.add(help);
        return about;
    }

    //Очистка поля/списка
    static class ClearAction extends AbstractAction
    {
        @Serial
        private static final long serialVersionUID = 1L;
        ClearAction() {
            putValue(NAME, "Новый поиск");
        }
        public void actionPerformed(ActionEvent e) {
            System.out.println("Нажатие на кнопку Новый поиск");
        }
    }

    //Выход
    static class ExitAction extends AbstractAction
    {
        @Serial
        private static final long serialVersionUID = 1L;
        ExitAction() {
            putValue(NAME, "Выход");
        }
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }
}
