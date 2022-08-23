package cinema;
import java.util.Scanner;
import java.util.Arrays;

public class CinemaManager {

    final static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        int totalRows = howManyRows();
        int seatsInRow = howManySeatsInRow();
        String[][] cinHall = createHall(totalRows, seatsInRow);
        int currNumOfTickets = 0;
        int currIncome = 0;
        whatToDo(totalRows, seatsInRow, cinHall, currNumOfTickets, currIncome);

    }

    public static void whatToDo(int totalRows, int seatsInRow, String[][] cinHall, int currNumOfTickets, int currIncome) {
        boolean keepDoing = true;
        showOptions();
        while (keepDoing) {
            int chosenNumber = scanner.nextInt();
            switch (chosenNumber) {
                case 0:
                    keepDoing = false;
                    return;
                case 1:
                    displayHall(cinHall); //showing cinema with bought tickets
                    showOptions();
                    break;
                case 2:
                    int rowNumber = chooseRow();
                    int seatNumber = chooseSeat();
                    int ticketPrice = calculateTicketPrice(cinHall, totalRows, seatsInRow, rowNumber, seatNumber); //calculating ticket price
                    choosingSeat(cinHall, totalRows, seatsInRow, rowNumber, seatNumber); //choosing seat
                    currIncome = increaseCurrentIncome(currIncome, ticketPrice); //current income
                    currNumOfTickets = numberOfBoughtTickets(cinHall); //current number of tickets bought
                    showOptions();
                    break;
                case 3:
                    System.out.println("Number of purchased tickets: " + currNumOfTickets);
                    showPercentageOfBoughtTickets(currNumOfTickets, totalRows, seatsInRow); //showing percentage of bought tickets
                    System.out.println("Current income: $" + currIncome);
                    profitIfAllTicketsAreSold(totalRows, seatsInRow); //showing how much can we get when all tickets are sold
                    showOptions();
                    break;
                default:
                    System.out.println("Not correct input! Try again");
                    break;
            }
        }
    }

    public static void choosingSeat(String[][] cinHall, int totalRows, int seatsInRow, int rowNumber, int seatNumber) {
        if (totalRows < rowNumber || seatsInRow < seatNumber) { //if user inputs row number or seat number out of the cinema size
            System.out.println("Wrong input!"); //then there is a signal "wrong input"
            choosingSeat(cinHall, totalRows, seatsInRow, chooseRow(), chooseSeat()); // then user choose his seat again
        } else if (cinHall[rowNumber - 1][seatNumber - 1].equals("B")) { //if user tries to choose a seat that is already purchased
            System.out.println("That ticket has already been purchased!"); // then there is a signal "That ticket has already been purchased"
            choosingSeat(cinHall, totalRows, seatsInRow, chooseRow(), chooseSeat()); //then user choose his seat again
        } else {
            int ticketPrice = calculateTicketPrice(cinHall, totalRows, seatsInRow, rowNumber, seatNumber); // if everything is fine ticket price is calculated
            System.out.println("Ticket price: $" + ticketPrice); // printing price of ticket
            changeSeatToOccupied(cinHall, rowNumber - 1, seatNumber - 1); // marking a purchased seat on map of cinema
        }
    }

    public static int calculateTicketPrice(String[][] cinHall, int totalRows, int seatsInRow, int rowNumber, int seatNumber) {
        int totalSeats = totalRows * seatsInRow;
        int priceInFirstSeats = 10;
        int priceInLastSeats = 8;
        int ticketPrice = 0;
        if (totalRows >= rowNumber && seatsInRow >= seatNumber) {
            if (cinHall[rowNumber - 1][seatNumber - 1].equals("S")) {
                if (totalSeats <= 60) { // if a hall is small ticket price equals price in first seats
                    ticketPrice = priceInFirstSeats;
                } else { // if its bigger it depends on which row does the user choose
                    if (rowNumber > totalRows / 2) { // if user chose further half of map ticket price equals price in last seats
                        ticketPrice = priceInLastSeats;
                    } else {
                        ticketPrice = priceInFirstSeats; // if user chose closer half of map ticket price equals price in first seats
                        //middle row has also price in first seats
                    }
                }
            }
        }
        return ticketPrice;
    }
    public static int numberOfBoughtTickets(String[][] cinHall) {
        int numOfRows = cinHall.length;
        int numOfSeatsInRow = cinHall[0].length;
        int currNumOfTickets = 0;
        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < numOfSeatsInRow; j++) {
                if (cinHall[i][j].equals("B")) { //going through whole map of cinema and if there is a purchased seat
                    currNumOfTickets += 1; // we increase current number of bought tickets
                }
            }
        }
        return currNumOfTickets;
    }

    public static int increaseCurrentIncome(int currIncome, int ticketPrice) {
        return currIncome + ticketPrice;
    } //method increasing current income  with ticket price

    public static void showPercentageOfBoughtTickets(double currNumOfTickets, int totalRows, int seatsInRow) {
        int numOfSeats = totalRows * seatsInRow;
        if (numOfSeats != 0) {
            double result = currNumOfTickets / numOfSeats * 100;
            System.out.printf("Percentage: %.2f%%", result); //showing percentage of sold tickets
            System.out.print("\n");
        } else {
            System.out.println("number of seats equals 0, something is wrong"); // division by 0
        }
    }

    public static String[][] changeSeatToOccupied(String[][] cinHall, int rowChosen, int seatChosen) {
        cinHall[rowChosen][seatChosen] = "B";
        return cinHall;
    } //simple method changing seat mark to B if user buys it

    public static void showOptions() {
        System.out.println("1. Show the seats");
        System.out.println("2. Buy a ticket");
        System.out.println("3. Statistics");
        System.out.println("0. Exit");
    } //showing menu

    public static void profitIfAllTicketsAreSold(int totalRows, int seatsInRow) { //profit cinema gains if all tickets were sold
        int totalSeats = totalRows * seatsInRow;
        int income;
        int priceInFirstSeats = 10;
        int priceInLastSeats = 8;
        if (totalSeats <= 60) {
            income = totalSeats * 10;
        } else {
            int firstRows = totalRows / 2;
            int lastRows = totalRows - firstRows;
            income = firstRows * priceInFirstSeats * seatsInRow + lastRows * priceInLastSeats * seatsInRow;
        }
        System.out.println("Total income: $" + income);
    }

    public static int chooseRow() {
        System.out.println("Enter a row number: ");
        int rowNumber = scanner.nextInt();
        return rowNumber;
    } //choosing row by user

    public static int chooseSeat() {
        System.out.println("Enter a seat number in that row: ");
        int seatNumber = scanner.nextInt();
        return seatNumber;
    } //choosing seat in a row by user

    public static int howManyRows() {
        System.out.println("Enter the number of rows: ");
        int totalRows = scanner.nextInt();
        return totalRows;
    } //input of how many row does cinema have

    public static int howManySeatsInRow() {
        System.out.println("Enter the number of seats in each row: ");
        int seatsInRow = scanner.nextInt();
        return seatsInRow;
    } //input of how many seats in a row does cinema have

    public static String[][] createHall(int rows, int seats) {
        String[][] cinHall = new String[rows][seats];
        for (int i = 0; i < cinHall.length; i++) {
            Arrays.fill(cinHall[i], "S");
        }
        return cinHall;
    } // creating a two-dimensional array that represents cinema

    public static void displayHall(String[][] cinHall) {
        System.out.println("Cinema:");
        System.out.print(" ");
        for (int k = 0; k < cinHall[0].length; k++) {
            System.out.print(" " + (k + 1));
        }
        System.out.println();
        for (int j = 0; j < cinHall.length; j++) {
            System.out.printf("%d", j + 1);
            for (int i = 0; i < cinHall[j].length; i++) {
                System.out.print(" " + cinHall[j][i]);
            }
            System.out.println();
        }
        System.out.println();
    } //displaying map of hall - printing two-dimensional array
}
