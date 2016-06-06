package ca.sfu.cmpt373.alpha.vrctextui;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {
    private String[] options;
    private String title;

    public Menu(String title, String[] options) {
        this.title = title;
        this.options = options;
    }

    public void display() {
        Decorator.printInBox(title, '*');
        Decorator.printAsNumberedList(options);
        System.out.println();
    }

    public int getMenuChoice() {
        return getNumberInRange(1, options.length);
    }

    public static int getNumberInRange(int start, int end) {
        try {
            if (start > end) {
                throw new IllegalArgumentException("parameter 'start' must be <= 'end'");
            }
            Scanner scanner = new Scanner(System.in);
            System.out.print("> ");
            int userChoice = scanner.nextInt();

            while (!(userChoice >= start && userChoice <= end)) {
                System.out.println("Please enter a number in the given range:");
                System.out.print("> ");
                userChoice = scanner.nextInt();
                scanner.nextLine(); // Strip newline in stream
            }
            return userChoice;
        } catch (InputMismatchException e) {
            System.out.println("Please enter an Integer number in the given range");
            return getNumberInRange(start, end);
        }
    }
}
