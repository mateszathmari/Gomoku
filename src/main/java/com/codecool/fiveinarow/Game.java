package com.codecool.fiveinarow;

import java.util.Random;
import java.util.Scanner;

public class Game implements GameInterface {
    private int[][] board;
    private boolean player1IsAi;
    private boolean player2IsAi;
    private String player1Symbol;
    private String player2Symbol;

    public Game(int nRows, int nCols) {
        this.board = new int[nRows][nCols];
        this.player1IsAi = false;
        this.player2IsAi = false;
        this.player1Symbol = " x";
        this.player2Symbol = " o";

    }

    public void setSymbol(int player, String symbol) {
        if (player == 1) {
            player1Symbol = symbol;
        } else if (player == 2) {
            player2Symbol = symbol;
        }
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    /**
     * Takes an input, of 2 or 3 length. Format: {letter}{number}.
     *
     * @param player Player's number
     * @return Returns an int array, as the coordinates of a position that is inside the board's range.
     */
    public int[] getMove(int player) {
        int[][] board = getBoard();
        Scanner inputReader = new Scanner(System.in);
        int[] result = new int[2];
        boolean validMove = false;
        while (!validMove) {
            System.out.println("What's your move? (Example: A1, C4):");
            String input = inputReader.nextLine();
            if (input.equals("quit")) { // If input equals "quit" stops the program
                System.out.println("Goodbye! Thank you for playing!");
                System.exit(0);
            }
            try { // if the input is not at least length one, inform user
                int firstCoordinate = Character.toLowerCase(input.charAt(0)) - 'a';
                int secondCoordinate;
                if (0 <= firstCoordinate && firstCoordinate <= board.length - 1) {
                    if (input.length() == 2) // If there is only 1 number after the letter, only add it to second coordinate. Otherwise add both letters
                        secondCoordinate = Character.getNumericValue(input.charAt(1)) - 1;
                    else
                        secondCoordinate = (Character.getNumericValue(input.charAt(1))) * 10 + Character.getNumericValue(input.charAt(2)) - 1;
                    if (!isNumeric(input.charAt(0)) && isNumeric(input.charAt(1))) {
                        // Checks if firstCoordinate is between range of board's first dimension
                        if (0 <= secondCoordinate && secondCoordinate <= board[firstCoordinate].length - 1) { // Checks if SecondCoordinate is between range of board's second dimension
                            if (board[firstCoordinate][secondCoordinate] == 0) {
                                result[0] = firstCoordinate;
                                result[1] = secondCoordinate;
                                validMove = true;
                            } else {
                                System.out.println("Wrong input!");
                            }
                        } else
                            System.out.println("Wrong input!");
                    } else
                        System.out.println("Wrong input!");
                } else
                    System.out.println("Wrong input!");
            } catch (Exception ignored) {
                System.out.println("Wrong input!");
            }
        }
        return result;
    }

    private boolean isNumeric(char character) {
        boolean numeric = true;

        try {
            Double.parseDouble(String.valueOf(character));
        } catch (NumberFormatException e) {
            numeric = false;
        }
        return numeric;
    }

    public int[] getAiMove(int player, int howMany) {
        for (int i = howMany - 1; i > 0; i--) {
            if (!(AiPossibleNextMove(player, i) == null)) {
                int[] pos = AiPossibleNextMove(player, i);//repeat howMany-2 for effective defend
                return pos;
            }
        }
        return getRandomPos();
    }

    private int[] getRandomPos() {
        Random r = new Random();
        int[] pos = new int[2];
        int maxRow = board.length - 1;
        int maxCol = board[0].length - 1;
        while (true) {
            int row = r.nextInt(maxRow);
            int col = r.nextInt(maxCol);
            if (!(isTakenPosition(row, col))) {
                pos[0] = row;
                pos[1] = col;
                return pos;
            }
        }
    }

    private boolean isTakenPosition(int row, int col) {
        if (board[row][col] == 0) {
            return false;
        }
        return true;
    }

    public void mark(int player, int row, int col) {
        board[row][col] = player;
    }

    private boolean checkingHorizontally(int player, int howMany) {
        int counter = 0;
        for (int[] value : board) {
            for (int i : value) {
                if (i == player) {
                    counter++;
                    if (counter == howMany) {
                        return true;
                    }
                } else {
                    counter = 0;
                }
            }
            counter = 0;
        }
        return false;
    }

    private int[] checkingHorizontallyForAi(int player, int howMany) {
        int counter = 0;
        int[] position = new int[2];
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] == player) {
                    counter++;
                    if (counter == howMany) {
                        if (board[row].length > col + 1) {
                            if (board[row][col + 1] == 0) {
                                position[0] = row;
                                position[1] = col + 1;
                                return position;
                            }
                        }
                        if (col - howMany >= 0) {
                            if (board[row][col - howMany] == 0) {
                                position[0] = row;
                                position[1] = col - howMany;
                                return position;
                            }
                        }
                        return null;
                    }
                } else {
                    counter = 0;
                }
            }
            counter = 0;
        }
        return null;
    }

    private int[] checkingVerticallyForAi(int player, int howMany) {
        /*
         * Checking vertically for available position
         * */
        int counter = 0;
        int[] position = new int[2];
        for (int col = 0; col < board[0].length; col++) {
            for (int row = 0; row < board.length; row++) {
                if (board[row][col] == player) {
                    counter++;
                    if (counter == howMany) {
                        if (board.length > row + 1) {
                            if (board[row + 1][col] == 0) {
                                position[0] = row + 1;
                                position[1] = col;
                                return position;
                            }
                        }
                        if (row - howMany >= 0) {
                            if (board[row - howMany][col] == 0) {
                                position[0] = row - howMany;
                                position[1] = col;
                                return position;
                            }
                        }
                        return null;
                    }
                } else {
                    counter = 0;
                }
            }
            counter = 0;
        }
        return null;
    }


    private int[] checkingLeftTopToRightBottomForAi(int player, int howMany) {
        /*
         * Checking left top to right bottom for Ai for next possible position
         * */
        int counter = 0;
        int[] position = new int[2];
        int checkingHeight = board.length - howMany;
        if (checkingHeight >= 0) {
            for (int l = checkingHeight; l >= 0; l--) {
                for (int row = l, col = 0; row < board.length; row++, col++) {
                    if (col >= board[0].length) {
                        continue;
                    }
                    if (board[row][col] == player) {
                        counter++;
                        if (counter == howMany) {
                            if (board.length > row + 1) {
                                if (board[row + 1][col + 1] == 0) {
                                    position[0] = row + 1;
                                    position[1] = col + 1;
                                    return position;
                                }
                            }
                            if (row - howMany >= 0 && col - howMany >= 0) {
                                if (board[row - howMany][col - howMany] == 0) {
                                    position[0] = row - howMany;
                                    position[1] = col - howMany;
                                    return position;
                                }
                            }
                            return null;
                        }
                    } else {
                        counter = 0;
                    }

                }
            }
            for (int k = 0; k <= board[0].length; k++) {
                for (int col = k, row = 0; col < board.length; col++, row++) {
                    if (col >= board[0].length) {
                        continue;
                    }

                    if (board[row][col] == player) {
                        counter++;
                        if (counter == howMany) {
                            if (board.length > row + 1 && board[0].length > col + 1) {
                                if (board[row + 1][col + 1] == 0) {
                                    position[0] = row + 1;
                                    position[1] = col + 1;
                                    return position;
                                }
                            }
                            if (row - howMany >= 0 && col - howMany >= 0) {
                                if (board[row - howMany][col - howMany] == 0) {
                                    position[0] = row - howMany;
                                    position[1] = col - howMany;
                                    return position;
                                }
                            }
                            return null;
                        }
                    } else {
                        counter = 0;
                    }
                }

            }
        }
        return null;
    }


    private int[] checkingRightTopToLeftBottomForAi(int player, int howMany) {
        /*
         * Checking right top to left bottom
         * */
        int counter = 0;
        int[] position = new int[2];
        int checkingHeight = board.length - howMany;
        if (checkingHeight >= 0) {
            for (int l = board.length - 1; l >= 0; l--) {
                for (int row = l, col = board[0].length - 1; row < board.length; row++, col--) {
                    if (col < 0) {
                        continue;
                    }

                    if (board[row][col] == player) {
                        counter++;
                        if (counter == howMany) {
                            if (board.length > row + 1 && col - 1 >= 0) {
                                if (board[row + 1][col - 1] == 0) {
                                    position[0] = row + 1;
                                    position[1] = col - 1;
                                    return position;
                                }
                            }
                            if (row - howMany >= 0 && col + howMany < board[0].length) {
                                if (board[row - howMany][col + howMany] == 0) {
                                    position[0] = row - howMany;
                                    position[1] = col + howMany;
                                    return position;
                                }
                            }
                            return null;
                        }
                    } else {
                        counter = 0;
                    }

                }
            }
            for (int k = board[0].length - 1; k >= 0; k--) {
                for (int row = 0, col = k; col >= 0; row++, col--) {
                    if (row >= board.length) {
                        continue;
                    }

                    if (board[row][col] == player) {
                        counter++;
                        if (counter == howMany) {
                            if (board.length > row + 1 && col - 1 >= 0) {
                                if (board[row + 1][col - 1] == 0) {
                                    position[0] = row + 1;
                                    position[1] = col - 1;
                                    return position;
                                }
                            }
                            if (row - howMany >= 0 && col + howMany < board[0].length) {
                                if (board[row - howMany][col + howMany] == 0) {
                                    position[0] = row - howMany;
                                    position[1] = col + howMany;
                                    return position;
                                }
                            }
                        }
                    } else {
                        counter = 0;
                    }

                }
            }
        }
        return null;
    }

    private int[] AiPossibleNextMove(int player, int howMany) {
        int enemy = 1;

        int[] pos = checkingHorizontallyForAi(player, howMany);
        if (!(pos == null)) {
            return pos;
        }
        pos = checkingVerticallyForAi(player, howMany);
        if (!(pos == null)) {
            return pos;
        }
        pos = checkingLeftTopToRightBottomForAi(player, howMany);
        if (!(pos == null)) {
            return pos;
        }
        pos = checkingRightTopToLeftBottomForAi(player, howMany);
        if (!(pos == null)) {
            return pos;
        } else {
            if (player == 1) {
                enemy++;
            }
            pos = checkingHorizontallyForAi(enemy, howMany);
            if (!(pos == null)) {
                return pos;
            }
            pos = checkingVerticallyForAi(enemy, howMany);
            if (!(pos == null)) {
                return pos;
            }
            pos = checkingLeftTopToRightBottomForAi(enemy, howMany);
            if (!(pos == null)) {
                return pos;
            }
            pos = checkingRightTopToLeftBottomForAi(enemy, howMany);
            if (!(pos == null)) {
                return pos;
            }
        }
        return null;
    }


    private boolean checkingVertically(int player, int howMany) {
        int counter = 0;
        /*
         * Checking vertically
         * */
        for (int i = 0; i < board[0].length; i++) {
            for (int[] ints : board) {
                if (ints[i] == player) {
                    counter++;
                    if (counter == howMany) {
                        return true;
                    }
                } else {
                    counter = 0;
                }
            }
            counter = 0;
        }
        return false;
    }

    private boolean checkingLeftTopToRightBottom(int player, int howMany) {
        /*
         * Checking left top to right bottom
         * */
        int counter = 0;
        int checkingHeight = board.length - howMany;
        if (checkingHeight >= 0) {
            for (int l = checkingHeight; l >= 0; l--) {
                for (int i = l, j = 0; i < board.length; i++, j++) {
                    if (j >= board[j].length) {
                        continue;
                    }

                    if (board[i][j] == player) {
                        counter++;
                        if (counter == howMany) {
                            return true;
                        }
                    } else {
                        counter = 0;
                    }

                }
                counter = 0;
            }
            for (int k = 0; k <= board[0].length; k++) {
                for (int m = k, n = 0; m < board.length; m++, n++) {
                    if (m >= board[0].length) {
                        continue;
                    }

                    if (board[n][m] == player) {
                        counter++;
                        if (counter == howMany) {
                            return true;
                        }
                    } else {
                        counter = 0;
                    }
                }
                counter = 0;

            }
        }
        return false;
    }


    private boolean checkingRightTopToLeftBottom(int player, int howMany) {
        /*
         * Checking right top to left bottom
         * */
        int counter = 0;
        int checkingHeight = board.length - howMany;
        if (checkingHeight >= 0) {
            for (int l = board.length - 1; l >= 0; l--) {
                for (int i = l, j = board[0].length - 1; i < board.length; i++, j--) {
                    if (j < 0) {
                        continue;
                    }

                    if (board[i][j] == player) {
                        counter++;
                        if (counter == howMany) {
                            return true;
                        }
                    } else {
                        counter = 0;
                    }
                }
                counter = 0;
            }
            for (int k = board[0].length - 1; k >= 0; k--) {
                for (int i = 0, j = k; j >= 0; i++, j--) {
                    if (i >= board.length) {
                        continue;
                    }

                    if (board[i][j] == player) {
                        counter++;
                        if (counter == howMany) {
                            return true;
                        }
                    } else {
                        counter = 0;
                    }

                }
                counter = 0;
            }
        }
        return false;
    }


    public boolean hasWon(int player, int howMany) {
        if (checkingHorizontally(player, howMany)) {
            return true;
        } else if (checkingVertically(player, howMany)) {
            return true;
        } else if (checkingLeftTopToRightBottom(player, howMany)) {
            return true;
        } else if (checkingRightTopToLeftBottom(player, howMany)) {
            return true;
        }
        return false;

    }

    public boolean isFull() {
        for (int[] row : board) {
            for (int cell : row) {
                if (cell == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Prints out board. First row is numbers equal number of columns. After that, first element of each row is the next element of alphabet.
     */
    public void printBoard() {
        System.out.print(" ");
        // Prints numbers in first line
        for (int i = 0; i < board[0].length + 1; i++) {
            if (i != 0)
                if (i >= 10)
                    System.out.printf(" %s", i);
                else
                    System.out.printf("  %s", i);
            else
                System.out.print(" ");
        }
        // Prints capital letters each row
        char letter = 'A';
        for (int[] row : board) {
            System.out.printf("\n %s", letter);
            // Prints the row. For each element: if zone is taken: if player 1 -> X, else O (Player 2)
            for (int zone : row) {
                switch (zone) {
                    case (1):
                        System.out.printf(" %s", player1Symbol);
                        break;
                    case (2):
                        System.out.printf(" %s", player2Symbol);
                        break;
                    default:
                        System.out.print("  .");

                }
            }
            letter++;
        }
        System.out.println();
    }

    public void printResult(int player) {
        clearScreen();
        printBoard();
        if (player == 1) {
            System.out.printf("\n %s won!\n", player1Symbol);
        } else if (player == 2) {
            System.out.printf("\n %s won!\n", player2Symbol);
        } else {
            System.out.println("\nIt's a tie!");
        }
        sleep(4000);
    }

    private static void sleep(int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public void enableAi(int player) {
        if (player == 1) {
            player1IsAi = true;
        } else if (player == 2) {
            player2IsAi = true;
        }
    }

    /**
     * @param player  ID of the player who is having the turn
     * @param howMany Defines how many symbols are needed in a turn to win
     * @return Returns value of boolean. Indicates whether player won or not.
     */
    private boolean playerTurn(int player, int howMany) {
        int[] coordinates;
        System.out.printf("Player %s's turn!\n", player);
        printBoard();
        coordinates = getMove(player);
        mark(player, coordinates[0], coordinates[1]);
        if (hasWon(player, howMany)) {
            printResult(player);
            return true;
        }
        if (isFull()) {
            printResult(0);
            return true;
        }
        return false;
    }

    private boolean aiTurn(int player, int howMany) {
        int[] coordinates;
        System.out.printf("AI %s's turn!\n", player);
        printBoard();
        sleep(2000);
        coordinates = getAiMove(player, howMany);
        mark(player, coordinates[0], coordinates[1]);
        if (hasWon(player, howMany)) {
            printResult(player);
            return true;
        }
        if (isFull()) {
            printResult(0);
            return true;
        }
        return false;
    }

    /**
     * Derives an entire match.
     *
     * @param howMany number of matching symbols in a row.
     */
    public void play(int howMany) {
        boolean gameOver = false;
        while (!gameOver) {
            clearScreen();
            if (player1IsAi) {
                gameOver = aiTurn(1, howMany);
            } else {
                gameOver = playerTurn(1, howMany);
            }
            if (!gameOver) {
                clearScreen();
                if (player2IsAi) {
                    gameOver = aiTurn(2, howMany);
                } else {
                    gameOver = playerTurn(2, howMany);
                }
            }
        }
    }

    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
