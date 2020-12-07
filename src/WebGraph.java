// Shrey Shah
// ID: 112693183
// Shrey.Shah@stonybrook.edu
// Homework #7
// CSE214
// R.04 James Finn

import java.util.*;
import java.io.*;
import java.util.concurrent.atomic.AtomicReference;

public class WebGraph {
    public static final int MAX_PAGES = 40;
    public static ArrayList<WebPage> pages;
    private static int[][] edges = new int[MAX_PAGES][MAX_PAGES];
    static ArrayList<String> graphs = new ArrayList<>();

    /**
     * Constructor for the pages and edges.
     *
     * @param page
     * An arraylist for pages.
     *
     * @param edge
     * A 2-D Array fpr edges.
     */
    public WebGraph(ArrayList<WebPage> page, int[][] edge) {
        pages = page;
        edges = edge;
    }

    /**
     * Constructs a WebGraph object using the indicated files as the source for pages and edges.
     *
     * @param pagesFile
     * String of the relative path to the file containing the page information.
     *
     * @param linksFile
     * String of the relative path to the file containing the link information.
     *
     * @return
     * The WebGraph constructed from the text files.
     *
     * @throws IllegalArgumentException
     * Thrown if either of the files does not reference a valid text file, or if the files are not formatted correctly.
     *
     * @throws FileNotFoundException
     * Thrown if the file is not found in the directory.
     */
    public static WebGraph buildFromFiles(String pagesFile, String linksFile) throws IllegalArgumentException, FileNotFoundException {
        ArrayList<WebPage> pages = new ArrayList<>();

        if (pagesFile == null && linksFile == null) {
            throw new IllegalArgumentException();
        }
        try {
            Scanner fileReader = new Scanner(new File(Objects.requireNonNull(pagesFile)));
            int count = 0;
            while(fileReader.hasNextLine()) {
                String[] page = fileReader.nextLine().trim().split(" ");

                for (int i = 5; i < page.length; i++) {
                    if (page[i].contains(".")) {
                        throw new IllegalArgumentException("The file pages.txt does not reference a valid text file or is incorrectly formatted!");
                    }
                }
                ArrayList<String> keywords = new ArrayList<>(Arrays.asList(page).subList(1, page.length));
                pages.add(new WebPage(page[0], count++, keywords));
                graphs.add(page[0]);
            }

            fileReader = new Scanner(new File(linksFile));
            while(fileReader.hasNextLine()) {
                String[] link = fileReader.nextLine().trim().split(" ");

                if(link.length != 2) {
                    throw new IllegalArgumentException("The file links.txt does not reference a valid text file or is incorrectly formatted!");
                }
                edges[graphs.indexOf(link[0])][graphs.indexOf(link[1])] = 1;
            }
            return new WebGraph(pages, edges);
        }catch(FileNotFoundException e) {
            throw new FileNotFoundException("Error: File not found.");
        }
    }

    /**
     * Adds a page to the WebGraph.
     *
     * @param url
     * The URL of the webpage
     *
     * @param keywords
     * The keywords that are used in each of the WebPage.
     *
     * @throws IllegalArgumentException
     * If url is not unique and already exists in the graph, or if either argument is null.
     */
    public void addPage(String url, ArrayList<String> keywords) throws IllegalArgumentException{
        if(!url.contains(".")) {
            throw new IllegalArgumentException("Error: This is an invalid URL.");
        }
        else if(pages.size() == MAX_PAGES) {
            System.out.println("Error: The graph is full.");
        }
        else if(keywords == null) {
            throw new IllegalArgumentException("Error: URL or keywords cannot be null.");
        }
        for(WebPage page : pages) {
            if(url.equals(page.getUrl())) {
                throw new IllegalArgumentException("Error: URL already exists.");
            }
        }
        pages.add(new WebPage(url, pages.size(), keywords));
    }

    /**
     * Removes the WebPage from the graph with the given URL.
     *
     * @param url
     * The URL of the page to remove from the graph.
     */
    public void removePage(String url) {
        updatePageRanks();
        int sourceIndex = 0;
        pages.removeIf(p -> p.getUrl().equals(url));
        for (int i = 0; i < pages.size(); i++) {
            edges[i] = edges[i + 1];
            System.arraycopy(edges[i], sourceIndex + 1, edges[i], sourceIndex, pages.size() - 1 - sourceIndex);
        }
    }

    /**
     * Adds a link from the WebPage with the URL indicated by source to the WebPage with the URL indicated by destination
     *
     * @param source
     * the URL of the page which contains the hyperlink to destination.
     *
     * @param destination
     * the URL of the page which the hyperlink points to.
     *
     */
    public void addLink(String source, String destination) {
            int sourceIndex = -1;
            int destinationIndex = -1;
            for (WebPage temp : pages) {
                if (temp.getUrl().contains(source)) {
                    sourceIndex = temp.getIndex();
                }
                if (temp.getUrl().contains(destination)) {
                    destinationIndex = temp.getIndex();
                }
            }
            if (source == null || destination == null) {
                throw new IllegalArgumentException("Error: Cannot have null arguments.");
            } else if (sourceIndex == -1) {
                System.out.printf("%nError: The source: %s could not be found be found in the WebGraph.", source);
            } else if (destinationIndex == -1){
                System.out.printf("%nError: The destination: %s could not be found be found in the WebGraph", destination);
            }
            else {
                edges[sourceIndex][destinationIndex] = 1;
                System.out.printf("\nLink successfully added from %s to %s.\n", source, destination);
            }
        updatePageRanks();
    }

    /**
     * Removes the link from WebPage with the URL indicated by source to the WebPage with the URL indicated by destination.
     *
     * @param source
     * The URL of the WebPage to remove the link.
     *
     * @param destination
     * The URL of the link to be removed.
     */
    public void removeLink(AtomicReference<String> source, AtomicReference<String> destination) {
        int sourceIndex = 1;
        int destinationIndex = 1;
        for (WebPage temp : pages) {
            if (temp.getUrl().contains(source.toString())) {
                sourceIndex = temp.getIndex();
            }
            if (temp.getUrl().contains(destination.toString())) {
                destinationIndex = temp.getIndex();
            }
        }
        if (source == null || destination == null) {
            throw new IllegalArgumentException("Error: Cannot have null arguments.");
        } else if (sourceIndex == 1) {
            System.out.printf("%nError: The source: %s could not be found be found in the WebGraph.", source);
        } else if (destinationIndex == 1){
            System.out.printf("%nError: The destination: %s could not be found be found in the WebGraph.", destination);
        } else {
            try {
                edges[sourceIndex][destinationIndex] = 0;
            } catch (IllegalArgumentException e) {
                System.out.print("Link already removed between " + source + " and " + destination);
            }
            System.out.printf("%nLink removed from %s to %s.%n", source, destination);
        }
        updatePageRanks();
    }

    /**
     * Calculates and assigns the PageRank for every page in the WebGraph
     */
    public void updatePageRanks() {
        int i, j;
        for (i = 0; i < pages.size(); i++) {
            int rank = 0;
            for (j = 0; j < pages.size(); j++) {
                rank = rank + edges[j][i];
            }
            pages.get(i).setRank(rank);
        }
    }

    /**
     * Prints the Keyword Search in tabular form.
     *
     * @param keyword
     * the keyword being searched for
     */
    public void searchKeyword(String keyword) {
        updatePageRanks();
        String data = "";
        int rank = 1;
        pages.sort(new RankComparator());
        System.out.printf("  Rank |  PageRank |       URL            %n---------------------------------------------");
        for (WebPage page : pages) {
                if (page.getKeywords().contains(keyword)) {
                    data = String.format("%n  %2d   |    %2d     |  %-28s", rank++, page.getRank(), page.getUrl());
                    System.out.print(data);
                }
            }
        if (data.equals(""))
            System.out.printf("%nNo search results found for the keyword %s%n%n", keyword);
    }

    /**
     * Prints the WebGraph in tabular form.
     *
     * @param order
     * The sorting order. After sorting is completed it is printed.
     */
    public void printTable(String order) {
        if ("i".equals(order)) {
            pages.sort(new IndexComparator());
        } else if ("u".equals(order)) {
            pages.sort(new URLComparator());
        } else if ("r".equals(order)) {
            pages.sort(new RankComparator());
        } else {
            System.out.println("Not a valid option.");
            return;
        }
        System.out.print("Index |            URL            |  Rank |            Links            |         Keywords\r\n" +
                "-----------------------------------------------------------------------------------------------------------------\n");
        for(WebPage p : pages) {
            int i = p.getIndex();
            StringBuilder link = new StringBuilder("  ");
            for(int j = 0; j < pages.size(); j++) {
                link.append((edges[i][j] == 1) ? j + ", " : "");
            }
            link = new StringBuilder(link.substring(0, link.length() - 2));
            String str = p.toString().replace("*", String.format("%-27s", link.toString()));
            System.out.println(str);
            updatePageRanks();
        }
    }
}