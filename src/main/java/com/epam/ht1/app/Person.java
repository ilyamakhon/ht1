package com.epam.ht1.app;

import com.epam.ht1.util.DBWorker;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Person {

    // Данные записи о человеке.
    private String id;
    private String name;
    private String surname;
    private String middleName;
    private HashMap<String, String> phones = new HashMap<>();

    // Конструктор для создания записи о человеке на основе данных из БД.
    public Person(String id, String name, String surname, String middleName) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.middleName = middleName;

        // Извлечение телефонов человека из БД.
        ResultSet db_data = DBWorker.getInstance().getDBData("SELECT * FROM `phone` WHERE `owner`=" + id);

        try {
            // Если у человека нет телефонов, ResultSet будет == null.
            if (db_data != null) {
                while (db_data.next()) {
                    this.phones.put(db_data.getString("id"), db_data.getString("number"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Конструктор для создания пустой записи о человеке.
    public Person() {
        this.id = "0";
        this.name = "";
        this.surname = "";
        this.middleName = "";
    }

    // Конструктор для создания записи, предназначенной для добавления в БД.
    public Person(String name, String surname, String middleName) {
        this.id = "0";
        this.name = name;
        this.surname = surname;
        this.middleName = middleName;
    }

    // ++++++++++++++++++++++++++++++++++++++
    // Геттеры и сеттеры
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMiddleName() {
        if ((this.middleName != null) && (!this.middleName.equals("null"))) {
            return this.middleName;
        } else {
            return "";
        }
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setPhone(String ownerId, String phone) {
        this.phones.put(ownerId, phone);
    }

    public HashMap<String, String> getPhones() {
        return this.phones;
    }

    public void setPhones(HashMap<String, String> phones) {
        this.phones = phones;
    }

    public String getPhone(String phoneId) {
        return this.getPhones().get(phoneId);
    }
    // Геттеры и сеттеры
    // --------------------------------------

    // Валидация частей ФИО. Для отчества можно передать второй параетр == true,
    // тогда допускается пустое значение.
    public boolean validateFMLNamePart(String fml_name_part, boolean empty_allowed) {
        if (empty_allowed) {
            Matcher matcher = Pattern.compile("[[A-Я][а-я]+\\s[A-Я][а-я]+\\s[A-Я][а-я]+\\w+]{0,150}").matcher(fml_name_part);
            return matcher.matches();
        } else {
            Matcher matcher = Pattern.compile("[[A-Я][а-я]+\\s[A-Я][а-я]+\\s[A-Я][а-я]+\\w+]{1,150}").matcher(fml_name_part);
            return matcher.matches();
        }
    }
}
