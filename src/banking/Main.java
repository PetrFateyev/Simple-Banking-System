package banking;

public class Main {

    public static void main(String[] args) {
        String fileName = "";

        if (args.length != 0){
            if ("-fileName".equals(args[0])) {
                fileName = args[1];
            }
        } else {
            fileName = "card.s3db";
        }

        /** Создаём новую базу данных */
        DataBase dataBase = new DataBase(fileName);

        /** Создаём новую таблицу в базе данных */
        dataBase.createTable();

        /** Создаём новый объект банковской системы и заходи в главное меню */
        BankingSystem bank = new BankingSystem(dataBase);
        bank.menu();
    }
}

