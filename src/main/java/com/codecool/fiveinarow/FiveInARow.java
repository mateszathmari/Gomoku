package com.codecool.fiveinarow;

import java.util.Arrays;
import java.util.Scanner;


public class FiveInARow {

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void sleep(int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    static void intro() {
        System.out.println("   _____                       _          ");
        sleep(200);
        System.out.println("  / ____|                     | |         ");
        sleep(200);
        System.out.println(" | |  __  ___  _ __ ___   ___ | | ___   _ ");
        sleep(200);
        System.out.println(" | | |_ |/ _ \\| '_ ` _ \\ / _ \\| |/ / | | |");
        sleep(200);
        System.out.println(" | |__| | (_) | | | | | | (_) |   <| |_| |");
        sleep(200);
        System.out.println("  \\_____|\\___/|_| |_| |_|\\___/|_|\\_\\\\__,_|");
        sleep(1500);
    }

    /**
     * Asks question, from user.
     *
     * @param correctAnswers an array of int type. The list of options user can choose from.
     * @param question       an array of String. The sentences the program have to ask. Order: from left to right.
     * @return returns the choice of user.
     */
    static int askQuestion(int[] correctAnswers, String[] question) {
        Scanner inputReader = new Scanner(System.in);
        while (true) {
            for (String row : question) {
                System.out.println(row);
            }
            String input = inputReader.nextLine();
            if (input.equals("quit"))
                System.exit(0);
            else {
                try {
                    if (Arrays.stream(correctAnswers).anyMatch(i -> i == Integer.parseInt(input))) {
                        return Integer.parseInt(input);
                    }
                } catch (Exception ignored) {
                }
            }
            System.out.println("Wrong input!");
        }
    }

    /**
     * Asks a user to choose between two numbers. Case numbers INCLUDES the range of choices!
     *
     * @param leftSide  Left side of the range of eligible numbers.
     * @param rightSide Right side of the range of eligible numbers.
     * @param message   an array of String. The sentences the program have to ask. Order: from left to right.
     * @return returns the choice of user.
     */
    static int askNumberBetween(int leftSide, int rightSide, String message) {
        Scanner inputReader = new Scanner(System.in);
        while (true) {
            System.out.println(message);
            String input = inputReader.nextLine();
            if (input.equals("quit"))
                System.exit(0);
            else {
                try {
                    if (leftSide <= Integer.parseInt(input) && Integer.parseInt(input) <= rightSide) {
                        return Integer.parseInt(input);
                    }
                } catch (Exception ignored) {
                }
            }
            System.out.println("Wrong input!");
        }
    }

    /**
     * Creates a game board, based on user's preferences.
     */
    static void playGame() {
        Game game;
        int nRows, nCols, howMany;
        clearScreen();
        System.out.println("Welcome, to Gomoku!");
        int input = askQuestion(new int[]{1, 2, 3}, new String[]{"Please choose a board size:", "(1) 15x15   (2) 19x19 (3) Custom size (up to 26x26)"});
        if (input == 1) { // Based on choice, sets size of board.
            nRows = 15;
            nCols = 15;
        } else if (input == 2) {
            nRows = 19;
            nCols = 19;
        } else { // If choice is 3, asks for custom sizes.
            nRows = askNumberBetween(2, 26, "Please choose size of rows(min 2, max 26):");
            nCols = askNumberBetween(2, 26, "Please choose size of columns(min 2, max 26):");
            System.out.println("Size of board: " + nRows + "x" + nCols);
        }
        game = new Game(nRows, nCols);
        input = askQuestion(new int[]{1, 2, 3, 4}, new String[]{"Please choose a game mode:", "(1) Player vs Player    (2) Player vs AI(Player goes first)    (3) AI vs Player(Robot goes first)    (4) AI vs AI"});
        switch (input) { // Based on choice, enables AI for chosen players. If chosen number is 1, default values applies (Player vs Player).
            case 2:
                game.enableAi(2);
                break;
            case 3:
                game.enableAi(1);
                break;
            case 4:
                game.enableAi(1);
                game.enableAi(2);
                break;
        }
        howMany = askNumberBetween(1, 10, "Choose the number of symbols needed in a row, in order to win (Recommended 5. Up to 10. Using too high number can make the game unplayable.");
        input = askQuestion(new int[]{1, 2, 3, 4}, new String[]{"Please choose a theme:", "(1) Default: x-o   (2) Gomoku: ⚪-⚫  (3) Christmas: \uD83C\uDF84-\uD83C\uDF81 (4) Halloween: \uD83D\uDC7B-\uD83C\uDF83"});
        switch (input) { // Based on choice, sets symbol of players. There is no case for number 1, because it's the default value when object is created.
            case 2:
                game.setSymbol(1, "⚪");
                game.setSymbol(2, "⚫");
                break;
            case 3:
                game.setSymbol(1, "\uD83C\uDF84");
                game.setSymbol(2, "\uD83C\uDF81");
                break;
            case 4:
                game.setSymbol(1, "\uD83D\uDC7B");
                game.setSymbol(2, "\uD83C\uDF83");
                break;
        }
        game.play(howMany);
    }

    /**
     * Asks if the user wants to leave the game.
     *
     * @return returns the choice of user.
     */
    static boolean askLeave() {
        Scanner inputReader = new Scanner(System.in);
        while (true) {
            System.out.println("Wanna play again? (Y/n)");
            String input = inputReader.nextLine();
            switch (input) {
                case "quit":
                    System.exit(0);
                case "Y":
                case "y":
                    return true;
                case "N":
                case "n":
                    return false;
            }
            System.out.println("Wrong input!");
        }
    }

    public static void main(String[] args) {
        clearScreen();
        intro();
        do {
            playGame();
        } while (askLeave());
        System.out.println("Goodbye! Thank you for playing.");
    }
}

