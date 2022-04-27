package banking;

import org.sqlite.SQLiteDataSource;
import java.sql.*;

/**
 * Класс базы данных со свойствами <b>dataSource</b> и <b>url</b>.
 * @autor Petr Fateyev
 * @version 1.0
 */
public class DataBase {

    /** Поле источник данных */
    private final SQLiteDataSource dataSource = new SQLiteDataSource();

    /** Поле URL */
    private final String url;

    /**
     * Конструктор - создание нового объекта
     */
    public DataBase(String fileName){
        this.url = "jdbc:sqlite:" + fileName;
    }

    /**
     * Метод для создания новой таблицы, если её не существует
     */
    public void createTable() {
        dataSource.setUrl(url);
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                // Statement execution
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS card (\n" +
                        "id INTEGER PRIMARY KEY\n," +
                        "number TEXT NOT NULL\n," +
                        "pin TEXT NOT NULL,\n" +
                        "balance INTEGER DEFAULT 0)");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод для добавления новой карты в таблицу
     */
    public void addCard(Card card) {
        String sqlInsert = "INSERT INTO card (id, number, pin, balance) VALUES (?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert)) {
                preparedStatement.setInt(1,card.getId());
                preparedStatement.setString(2,card.getCardNumber());
                preparedStatement.setInt(3,card.getPin());
                preparedStatement.setLong(4,card.getBalance());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод для выбора карты из таблицы по заданному номеру карты и пину
     * @return возвращает выбранную карту
     */
    public Card selectCard(String cardNumber, int pin) {
        String sqlSelect = "SELECT * FROM card WHERE number = " + cardNumber + " AND pin = " + pin;
        Card card = new Card();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlSelect);
                    ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int resultId = resultSet.getInt("id");
                    String resultCardNumber = resultSet.getString("number");
                    int resultPin = resultSet.getInt("pin");
                    long resultBalance = resultSet.getLong("balance");
                    card.setId(resultId);
                    card.setCardNumber(resultCardNumber);
                    card.setPin(resultPin);
                    card.setBalance(resultBalance);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return card;
    }

    /**
     * Метод для проверки заданного номера карты и пину
     * @return возвращает номер карты, если такой номер карты есть в таблице
     * если его нет то возвращает пустую строку
     */
    public String selectCardNumber(String cardNumber) {
        String sqlSelect = "SELECT number, pin, balance FROM card WHERE number = " + cardNumber;
        String resultCardNumber = "";
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlSelect);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    resultCardNumber = resultSet.getString("number");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultCardNumber;
    }

    /**
     * Метод для выбора карты из таблицы по заданному номеру карты
     * @return возвращает выбранную карту
     */
    public Card selectCardByCardNumber(String cardNumber) {
        String sqlSelect = "SELECT * FROM card WHERE number = " + cardNumber;
        Card card = new Card();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlSelect);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int resultId = resultSet.getInt("id");
                    String resultCardNumber = resultSet.getString("number");
                    int resultPin = resultSet.getInt("pin");
                    long resultBalance = resultSet.getLong("balance");
                    card.setId(resultId);
                    card.setCardNumber(resultCardNumber);
                    card.setPin(resultPin);
                    card.setBalance(resultBalance);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return card;
    }

    /**
     * Метод для получения баланса из таблицы по заданному id
     * @return баланс
     */
    public long getCardBalance(int id) {
        String sqlSelect = "SELECT balance FROM card WHERE id = " + id;
        long balance = 0;
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlSelect);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    balance = resultSet.getLong("balance");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balance;
    }

    /**
     * Метод для изменения баланса в базе данных,
     * если sign = "+", то баланс будет увеличен,
     * если sign = "-", то баланс будет уменьшен
     */
    public void updateBalance(int id, long income, String sign) {
        String sqlUpdate = sign.equals("+") ? "UPDATE card SET balance = balance + ? WHERE id = ?" :
                                              "UPDATE card SET balance = balance - ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate)) {
                preparedStatement.setLong(1,income);
                preparedStatement.setInt(2,id);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод для удаления карты из базы данных
     */
    public void deleteCard(int id) {
        String sqlDelete = "DELETE FROM card WHERE id = ?";
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlDelete)) {
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
