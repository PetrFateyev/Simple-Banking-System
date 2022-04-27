package banking;

import java.util.Scanner;

/**
 * Класс банковская система со свойствами <b>card</b>, <b>dataBase</b> и <b>scanner</b>.
 * @autor Petr Fateyev
 * @version 1.0
 */
public class BankingSystem {

    /** Поле карта */
    private Card card;

    /** Поле база данных */
    private DataBase dataBase;

    /** Поле чтения из консоли */
    private Scanner scanner = new Scanner(System.in);

    /**
     * Конструктор - создание нового объекта
     */
    public BankingSystem(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    /**
     * Метод для главного меню
     */
    public void menu() {
        boolean exit = false;
        do {
            System.out.println("1. Create an account");
            System.out.println("2. Log into account");
            System.out.println("0. Exit");
            switch (scanner.nextInt()) {
                case 0:
                    exit = true;
                    exit();
                    break;
                case 1:
                    createAccount();
                    break;
                case 2:
                    logIntoAccount();
                    break;
                default:
                    exit = true;
                    break;
            }
        } while (!exit);
    }

    /**
     * Метод для создания нового аккаунта
     */
    public void createAccount() {
        Card card = new Card();
        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        System.out.println(card.getCardNumber());
        System.out.println("Your card PIN:");
        System.out.println(card.getPin());
        dataBase.addCard(card);
        System.out.println(card.getId());
        System.out.println();
    }

    /**
     * Метод для авторизации
     */
    public void logIntoAccount() {
        System.out.println("Enter your card number:");
        scanner.nextLine();
        String cardNumber = scanner.nextLine();
        System.out.println("Enter your PIN:");
        int pin = Integer.parseInt(scanner.nextLine());
        card = dataBase.selectCard(cardNumber,pin);

        if (pin == card.getPin() && cardNumber.equals(card.getCardNumber())) {
            accountMenu();
        } else {
            System.out.println("Wrong card number or PIN!");
        }
    }

    /**
     * Метод для меню аккаунта
     */
    public void accountMenu() {
        boolean exit = false;
        System.out.println("You have successfully logged in!");
        do {
            System.out.println("1. Balance");
            System.out.println("2. Add income");
            System.out.println("3. Do transfer");
            System.out.println("4. Close account");
            System.out.println("5. Log out");
            System.out.println("0. Exit");
            System.out.println();
            switch (scanner.nextInt()) {
                case 0:
                    exit = true;
                    exit();
                    break;
                case 1:
                    getBalance();
                    break;
                case 2:
                    addIncome();
                    break;
                case 3:
                    doTransfer();
                    break;
                case 4:
                    closeAccount();
                    return;
                case 5:
                    System.out.println("You have successfully logged out!");
                    return;
                default:
                    break;
            }
        } while (!exit);
    }

    /**
     * Метод для получения баланса
     */
    public void getBalance() {
        System.out.println("Balance: " + dataBase.getCardBalance(card.getId()));
    }

    /**
     * Метод для добавления дохода
     */
    public void addIncome() {
        System.out.println("Enter income:");
        long income = scanner.nextLong();
        dataBase.updateBalance(card.getId(),income,"+");
        System.out.println("Income was added!");
    }

    /**
     * Метод для перевода
     */
    public void doTransfer() {
        System.out.println("Transfer");
        System.out.println("Enter card number:");
        scanner.nextLine();
        String cardNumber = scanner.nextLine();
        if(!Card.checkCard(cardNumber)) {
            System.out.println("Probably you made a mistake in the card number. Please try again!");
        } else if (card.getCardNumber().equals(cardNumber)) {
            System.out.println("You can't transfer money to the same account!");
        } else if (!dataBase.selectCardNumber(cardNumber).equals(cardNumber)) {
            System.out.println("Such a card does not exist.");
        } else {
            System.out.println("Enter how much money you want to transfer:");
            long money = scanner.nextLong();
            if(dataBase.getCardBalance(card.getId()) < money) {
                System.out.println("Not enough money!");
            } else {
                Card cardForTransfer = dataBase.selectCardByCardNumber(cardNumber);
                dataBase.updateBalance(cardForTransfer.getId(),money,"+");
                dataBase.updateBalance(card.getId(),money,"-");
                System.out.println("Success!");
            }
        }
    }

    /**
     * Метод для удаления аккаунта
     */
    public void closeAccount() {
        dataBase.deleteCard(card.getId());
        menu();
    }

    /**
     * Метод для выхода из системы
     */
    public void exit() {
        System.out.println("Bye!");
        System.exit(1);
    }

}
