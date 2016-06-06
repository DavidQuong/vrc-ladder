package ca.sfu.cmpt373.alpha.vrctextui;

/**
 * Prints text to console with decoration in the form of boxes and lines
 */
public class Decorator {
    public static void printLine(int length, char decoration) {
        while (length > 0) {
            System.out.print(decoration);
            length--;
        }
        System.out.print('\n');
    }

    public static void printUnderlined(final String text) {
        System.out.println(text);
        printLine(text.length(), '-');
    }

    public static void printInBox(final String[] textLines, char decoration) {
        final int LATERAL_OFFSET = 4;
        int maxLineLength = getMaxLineLength(textLines);
        printLine(maxLineLength + LATERAL_OFFSET, decoration);
        for (String line : textLines) {
            String paddedText = "%c %-" + maxLineLength + "s %c\n";
            System.out.printf(paddedText, decoration, line, decoration);
        }
        printLine(maxLineLength + LATERAL_OFFSET, decoration);
    }

    public static void printInBox(final String text, char decoration) {
        printInBox(new String[]{text}, decoration);
    }

    public static void printAsNumberedList(final String[] textLines) {
        for (int i = 0; i < textLines.length; i++) {
            System.out.println((i + 1) + ". " + textLines[i]);
        }
    }

    public static void clearScreen() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    private static int getMaxLineLength(final String[] lines) {
        int maxLineLength = -1;
        for (String line : lines) {
            int currentLineLength = line.length();
            if (currentLineLength > maxLineLength) {
                maxLineLength = currentLineLength;
            }
        }
        return maxLineLength;
    }
}