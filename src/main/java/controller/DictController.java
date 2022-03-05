package controller;

import utils.XmlConfig;

import java.sql.*;
import java.util.ArrayList;

//Подключение к БД
public class DictController {
    Connection conn;
    Statement statement;

//Создание подключения к БД
    public DictController() {
        String url = String.format("jdbc:postgresql://%s:5432/%s",
        new XmlConfig().getByKey("db_host"),
        new XmlConfig().getByKey("db_name"));

        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url,
                    new XmlConfig().getByKey("db_user"),
                    new XmlConfig().getByKey("db_pass"));
            statement = conn.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Получение списка фильтров со словами
    public String[] getWordsList(String partOfWord) {
        ArrayList<String> tmp_words = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT word FROM dict WHERE word ILIKE ?;");
            preparedStatement.setString(1, "%" + partOfWord + "%");
            try (final ResultSet result = preparedStatement.executeQuery()) {
                while (result.next())
                    tmp_words.add(result.getString(1));
            }
            if (tmp_words.isEmpty())
                tmp_words.add("СЛОВ НЕ НАЙДЕНО");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //получение результатов
        String[] words = new String[tmp_words.size()];
        for (int i = 0; i < tmp_words.size(); i++) {
            words[i] = tmp_words.get(i);
        }
        return words;
    }

    //Получаем определение конкретного слова
    public String getMean(String word) {
        String mean = "";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT mean FROM dict WHERE word ILIKE ?");
            preparedStatement.setString(1, "%" + word + "%");
            try (final ResultSet result = preparedStatement.executeQuery()) {
                while (result.next())
                    mean = result.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mean;
    }

    //Добавляем слово и его значение
    public String addWord(String word, String mean) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO dict(word, mean) VALUES (?, ?) RETURNING id");
            preparedStatement.setString(1, word);
            preparedStatement.setString(2, mean);
            ResultSet result = preparedStatement.executeQuery();
            result.next();
            return String.valueOf(result.getInt(1));
        } catch (Exception e) {
            return e.getMessage();
        }
    }

}
