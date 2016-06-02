package ca.sfu.cmpt373.alpha.vrctextui;

/**
 * Created by Hassan Fahad on 2016-06-01.
 */
public class UI_Driver {
    private static final int NULL_OPTION = -1;

    public static void main(String[] args) {
        final int OPTION1 = 1;
        final int EXIT_OPTION = 2;
        String[] options = {"option 1: do something", "exit"};

        Decorator.printInBox("Menu Class Test", '*');
        System.out.println();

        Menu testMenu = new Menu("MAIN MENU", options);
        int userChoice = NULL_OPTION;
        while (userChoice != EXIT_OPTION) {
            Decorator.clearScreen();
            testMenu.display();
            userChoice = testMenu.getMenuChoice();
            switch (userChoice) {
                case OPTION1: showOption1();
                    break;
                case EXIT_OPTION:
                    break;
                default:
            }
            System.out.println();
        }
    }

    public static void showOption1() {
        final int OPTION1 = 1;
        final int OPTION2 = 2;
        final int RETURN_OPTION = 3;
        String[] options = {"take the blue pill", "take the red pill", "return to main menu"};
        Menu option1menu = new Menu("OPTION1", options);
        int userChoice = NULL_OPTION;
        while (userChoice != RETURN_OPTION) {
            Decorator.clearScreen();
            option1menu.display();
            userChoice = option1menu.getMenuChoice();
            switch (userChoice) {
                case OPTION1:
                    break;
                case OPTION2:
                    break;
                case RETURN_OPTION:
                    break;
                default:
            }
            System.out.println();
        }
    }
}
