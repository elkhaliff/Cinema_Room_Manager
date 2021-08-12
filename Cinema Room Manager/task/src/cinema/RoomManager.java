package cinema;

import java.util.Scanner;

/**
 * Основной класс приложения
 *  @author Andrey Zotov aka OldFox
 *  rows - Количество рядов
 *  seats - Количество мест в ряде
 */
public class RoomManager {
    private final int rows;
    private final int seats;
    private int cntCells = 0; // Количество занятых мест
    private double percentage = 0.00; // Процент занятых мест
    private int currIncome = 0; // Текущий доход
    private final String ticket = "B"; // Проданный билет на место
    private final String empty = "S"; // Свободное место

    private final String [][] fieldMap;
    private final int [][] priceMap;

    public RoomManager(int rows, int seats) {
        this.rows = rows;
        this.seats = seats;
        fieldMap = new String[rows][seats];
        priceMap = new int[rows][seats];
        /**
         * Инициализация массива рабочей области кинозала
         */
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < seats; j++) {
                fieldMap[i][j] = empty;
            }
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < seats; j++) {
                priceMap[i][j] = 10;
            }
        }
        setPriceMap();
    }

    public void println(String string) {
        System.out.println(string);
    }

    public static int getInt(String input) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(input);
        return scanner.nextInt();
    }

    private boolean isEmpty(int row, int seat) {
        return fieldMap[row-1][seat-1] == empty;
    }

    private void buyTicket(int row, int seat) {
        fieldMap[row-1][seat-1] = ticket;
    }

    private int getTicketPrice(int row, int seat) {
        return priceMap[row-1][seat-1];
    }

    /**
     * Установка цен на места (снижение цены за место в больших залах >60)
     */
    public void setPriceMap() {
        if ((rows * seats) > 60 ) {
            int lowPrice = rows - rows / 2;
            for (int i = rows - lowPrice; i < rows; i++) {
                for (int j = 0; j < seats; j++) {
                    priceMap[i][j] = 8;
                }
            }
        }
    }

    /**
     * Формирование строки из массива зала кинотеатра
     * (в частности - получаем возможность вывода на печать)
     */
    public String toPrint() {
        String outStr;
        String border = " ";

        for (int i = 1; i < seats+1; i++) {
            border += " " + i;
        }
        border += "\n";
        outStr = "Cinema:\n";
        outStr += border;
        for (int i = 0; i < rows; i++) {
            outStr += i + 1;
            for (int j = 0; j < seats; j++) {
                outStr +=  " " + fieldMap[i][j];
            }
            outStr += "\n";
        }
        return outStr;
    }

    /**
     * Цена всех посадочных мест
     */
    public int getPriceCinema() {
        var price = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < seats; j++) {
                price += priceMap[i][j];
            }
        }
        return price;
    }

    /**
     * Взять билет
     */
    public void takeTicket() {
        int row = getInt("Enter a row number:");
        int seat = getInt("Enter a seat number in that row:");
        if ((row > rows) || (seat > seats)) {
            println("Wrong input!");
            takeTicket();
        } else {
            if (isEmpty(row, seat)) {
                buyTicket(row, seat);
                println("Ticket price: $" + getTicketPrice(row, seat));
            } else {
                println("That ticket has already been purchased!");
                takeTicket();
            }
        }
    }

    /**
     * Расчет статистики по купленным билетам
     */
    private void stat() {
        cntCells = 0;
        currIncome = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < seats; j++) {
                if (fieldMap[i][j] == ticket) {
                cntCells++;
                currIncome += priceMap[i][j];
                }
            }
        }
        double cnt = cntCells;
        percentage = (cnt / (rows * seats)) * 100.0;
    }

    /**
     * Округление до p-знаков после запятой
     */
    public String toRound(Double d, int p) {
        String str = "";
        Double ten = 10.0;
        Long rnd;
        if (d != 0.0) {
            rnd = Math.round(d * Math.pow(ten, p));
            str = rnd.toString();
        } else str = "000";

        int l = str.length();
        str = str.substring(0, l-p)+"."+str.substring(l-p, l);
        return str;
    }

    public void showStatistics() {
        stat();
        println("Number of purchased tickets: " + cntCells);
        println("Percentage: " + toRound(percentage, 2) + "%");
        println("Current income: $" + currIncome);
        println("Total income: $" + getPriceCinema());
    }

    public void showMenu() {
        println("1. Show the seats");
        println("2. Buy a ticket");
        println("3. Statistics");
        println("0. Exit");
    }

    public void menu() {
        Scanner scanner = new Scanner(System.in);
        int oper = -1;

        while(oper != 0) {
            showMenu();
            oper = scanner.nextInt();
            if (oper == 1) println(toPrint());
            else if (oper == 2) takeTicket();
            else if (oper == 3) showStatistics();
        }
    }
}
