package com.epam.ht1.app;

import com.epam.ht1.util.DBWorker;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class PhoneBook {

    // Указатель на экземпляр класса.
    private static PhoneBook instance = null;
    // Хранилище записей о людях.
    private HashMap<String, Person> persons = new HashMap<>();
    // Объект для работы с БД.
    private DBWorker db = DBWorker.getInstance();

    // При создании экземпляра класса из БД извлекаются все записи.
    protected PhoneBook() throws ClassNotFoundException, SQLException {
        ResultSet db_data = this.db.getDBData("SELECT * FROM `person` ORDER BY `surname` ASC");
        while (db_data.next()) {
            this.persons.put(db_data.getString("id"), new Person(db_data.getString("id"),
                    db_data.getString("name"),
                    db_data.getString("surname"),
                    db_data.getString("middlename")));
        }
    }

    // Метод для получения экземпляра класса (реализован Singleton).
    public static PhoneBook getInstance() throws ClassNotFoundException, SQLException {
        if (instance == null) {
            instance = new PhoneBook();
        }

        return instance;
    }

    // +++++++++++++++++++++++++++++++++++++++++
    // Геттеры и сеттеры
    public HashMap<String, Person> getContents() {
        return persons;
    }

    public Person getPerson(String id) {
        return this.persons.get(id);
    }

    // Геттеры и сеттеры
    // -----------------------------------------

    // Добавление записи о человеке.
    public boolean addPerson(Person person) {
        String query;

        // У человека может не быть отчества.
        if (!person.getMiddleName().equals("")) {
            query = "INSERT INTO `person` (`name`, `surname`, `middlename`) VALUES ('" + person.getName() + "', '" + person.getSurname() + "', '" + person.getMiddleName() + "')";
        } else {
            query = "INSERT INTO `person` (`name`, `surname`) VALUES ('" + person.getName() + "', '" + person.getSurname() + "')";
        }

        Integer affected_rows = this.db.changeDBData(query);

        // Если добавление прошло успешно...
        if (affected_rows > 0) {
            person.setId(this.db.getLastInsertId().toString());

            // Добавляем запись о человеке в общий список.
            this.persons.put(person.getId(), person);

            return true;
        } else {
            return false;
        }
    }


    // Обновление записи о человеке.
    public boolean updatePerson(Person person) {
        int id_filtered = Integer.parseInt(person.getId());
        String query;

        // У человека может не быть отчества.
        if (!person.getSurname().equals("")) {
            query = "UPDATE `person` SET `name` = '" + person.getName() + "', `surname` = '" + person.getSurname() + "', `middlename` = '" + person.getMiddleName() + "' WHERE `id` = " + id_filtered;
        } else {
            query = "UPDATE `person` SET `name` = '" + person.getName() + "', `surname` = '" + person.getSurname() + "' WHERE `id` = " + id_filtered;
        }

        Integer affected_rows = this.db.changeDBData(query);

        // Если обновление прошло успешно...
        if (affected_rows > 0) {
            // Обновляем запись о человеке в общем списке.
            this.persons.put(person.getId(), person);
            return true;
        } else {
            return false;
        }
    }


    // Удаление записи о человеке.
    public boolean deletePerson(String id) {
        if ((id != null) && (!id.equals("null"))) {
            int filtered_id = Integer.parseInt(id);

            Integer affected_rows = this.db.changeDBData("DELETE FROM `person` WHERE `id`=" + filtered_id);

            // Если удаление прошло успешно...
            if (affected_rows > 0) {
                // Удаляем запись о человеке из общего списка.
                this.persons.remove(id);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean addPhone(String personId, String phone) {
        int id_filtered = Integer.parseInt(personId);
        String query = "INSERT INTO `phone` (`owner`, `number`) VALUES ('" + id_filtered + "', '" + phone + "')";
        Integer affected_rows = this.db.changeDBData(query);
        if (affected_rows > 0) {
            for (String id : persons.keySet()) {
                if (id.equals(personId)) {
                    this.persons.get(id).setPhone(this.db.getLastInsertId().toString(), phone);
                }
            }
        }
        return affected_rows > 0;
    }


    public boolean updatePhone(String phoneId, String ownerId, String phone) {
        int id_filtered = Integer.parseInt(phoneId);
        String query = "UPDATE `phone` SET `number` = '" + phone + "' WHERE `id` = " + id_filtered;
        Integer affected_rows = this.db.changeDBData(query);
        if (affected_rows > 0) {
            for (String id : persons.keySet()) {
                if (id.equals(ownerId)) {
                    this.persons.get(id).setPhone(phoneId, phone);
                }
            }
        }
        return affected_rows > 0;
    }

    public boolean deletePhone(String personId, String phoneId) {
        if ((phoneId != null) && (!phoneId.equals("null"))) {
            int filtered_id = Integer.parseInt(phoneId);

            Integer affected_rows = this.db.changeDBData("DELETE FROM `phone` WHERE `id`=" + filtered_id);

            // Если удаление прошло успешно...
            if (affected_rows > 0) {
                // Удаляем запись о человеке из общего списка.
                this.persons.get(personId).getPhones().remove(phoneId);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
