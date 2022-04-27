package banking;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс карты со свойствами <b>id</b>, <b>cardNumber</b>, <b>pin</b> и <b>balance</b>.
 * @autor Petr Fateyev
 * @version 1.0
 */
public class Card {

    /** Поле ид */
    private int id;

    /** Поле номер карты */
    private String cardNumber;

    /** Поле пин */
    private int pin;

    /** Поле баланс */
    private long balance;

    /**
     * Конструктор - создание нового объекта
     */
    public Card() {
        /** Генерация ид */
        id = (int)(Math.random() * (99999999-1) + 1);

        /** Генерация номера карты с помощью константы BIN и 9 случайныч чисел */
        final String BIN = "400000";
        cardNumber = (BIN + (long)(Math.random() * (999999999L-100000000L) + 100000000L));
        /** Добавление контрольной суммы к сгенерированному номеру карты */
        cardNumber += checkSum(cardNumber);

        /** Генерация пин из 4 случайных чисел */
        pin = (int)(Math.random() * (9999-1000) + 1000);
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Метод для генерации 16-го числа в номере карты
     * @return возвращает 16-ое число в номере карты
     * */
    public static int checkSum(String cardNumber) {
        int sum = findSum(cardNumber);
        int number = 0;
        for (int i = 1; i < 10; i++) {
            if ((sum + i) % 10 == 0) {
                number = i;
                break;
            }
        }
        return number;
    }

    /**
     * Метод для проверки карты по алгоритму Луна
     * @return возвращает действительна ли карта
     * */
    public static boolean checkCard(String cardNumber) {
        int sum = findSum(cardNumber.substring(0,cardNumber.length()-1));
        int checkNumber = cardNumber.charAt(cardNumber.length()-1) - '0';
        return (sum + checkNumber) % 10 == 0;
    }

    /**
     * Метод для определения контрольной суммы
     * @return возвращает сумму первых 15-ти чисел карты
     * */
    public static int findSum(String cardNumber) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < cardNumber.length(); i++) {
            int num = cardNumber.charAt(i) - '0';
            if (i % 2 == 0) {
                num *= 2;
            }
            if (num > 9) {
                num -= 9;
            }
            list.add(num);
        }

        return list.stream().mapToInt(a -> a).sum();
    }
}
