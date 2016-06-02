package ca.sfu.cmpt373.alpha.vrctextui;

/**
 * Created by Hassan Fahad on 2016-06-01.
 */
public class uimain {
    public static void main(String[] args) {
        String[] lines = {"demo of decorator", "class functionality", "for use in UI"};

        Decorator.printInBox(lines, '*');

        System.out.println();

        Decorator.printAsNumberedList(lines);
    }
}
