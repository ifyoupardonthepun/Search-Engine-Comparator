// Shrey Shah
// ID: 112693183
// Shrey.Shah@stonybrook.edu
// Homework #7
// CSE214
// R.04 James Finn

import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class SearchEngine {
    public static final String PAGES_FILE = "pages.txt";
    public static final String LINKS_FILE = "links.txt";
    private static final AtomicReference<WebGraph> web = new AtomicReference<>();

    /**
     * Beginning of program.
     */
    public static void main(String[] args) throws FileNotFoundException {
        Scanner stdin = new Scanner(System.in);

        System.out.println("Loading WebGraph data...");
        web.set(WebGraph.buildFromFiles(PAGES_FILE, LINKS_FILE));
        System.out.print("Success!");

            while (true) {
                System.out.print("\nMenu: \n     (AP) - Add a new page to the graph. \n     (RP) - Remove a page from the graph." +
                        "\n     (AL) - Add a link between pages in the graph. \n     (RL) - Remove a link between pages in the graph.\n     (P)  - Print the graph. \n " +
                        "    (S)  - Search for pages with a keyword. \n     (Q)  - Quit. \nPlease make a selection from the above menu choices: ");
                String[] sel = stdin.nextLine().trim().replaceAll("\\s+", " ").split(" ");

                switch (sel[0].toUpperCase()) {
                    case "AP" -> {
                        System.out.print("\nEnter a url: ");
                        String apURL = stdin.nextLine().trim();
                        System.out.print("Enter keywords (space-separated): ");
                        ArrayList<String> keywords = new ArrayList<>(Arrays.asList(stdin.nextLine().trim().split(" ")));
                        try {
                            web.get().addPage(apURL, keywords);
                            System.out.printf("%n%s successfully added to the WebGraph!", apURL);
                        } catch (IllegalArgumentException e) {
                            System.out.printf("\nError: %s already exists in the WebGraph. Could not add new WepPage.\n", apURL);
                        }
                    }
                    case "RP" -> {
                        System.out.print("\nEnter a url: ");
                        String rpURL = stdin.nextLine().trim();
                        web.get().removePage(rpURL);
                        System.out.printf("%n%s successfully removed from the WebGraph!", rpURL);
                    }
                    case "AL" -> {
                        System.out.print("\nEnter a source url: ");
                        String asrURL = stdin.nextLine();
                        System.out.print("Enter a destination url: ");
                        String adesURL = stdin.nextLine();
                        web.get().addLink(asrURL, adesURL);
                    }
                    case "RL" -> {
                        System.out.print("\nEnter a source url: ");
                        AtomicReference<String> rsrURL = new AtomicReference<>(stdin.nextLine());
                        System.out.print("Enter a destination url: ");
                        AtomicReference<String> rdesURL = new AtomicReference<>(stdin.nextLine());
                        web.get().removeLink(rsrURL, rdesURL);
                    }
                    case "P" -> {
                        System.out.print("\n(I) Sort based on index (ASC)\n(U) Sort based on URL (ASC)\n(R) Sort based on rank (DSC)\n");
                        System.out.print("Please select an option: ");
                        web.get().printTable(stdin.nextLine().toLowerCase());
                    }
                    case "S" -> {
                        System.out.print("\nSearch keyword: ");
                        String searchKeywords = stdin.nextLine();
                        web.get().searchKeyword(searchKeywords);
                    }
                    case "Q" -> {
                        System.out.println("\nAuf Wiedersehen!"); //Means goodbye in German
                        System.exit(0);
                    }
                    default -> System.out.print("\nThis is not a valid menu option, please try again!");
                }
            }
    }
}