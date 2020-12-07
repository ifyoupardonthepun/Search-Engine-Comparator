// Shrey Shah
// ID: 112693183
// Shrey.Shah@stonybrook.edu
// Homework #7
// CSE214
// R.04 James Finn

import java.util.*;

public class WebPage {
    private final String url;
    private final int index;
    private int rank;
    private final Collection<String> keywords;

    /**
     * A constructor containing the URL, Index, and Keywords.
     * @param u
     * The webpage's URL.
     *
     * @param i
     * The webpage's index.
     *
     * @param k
     * The webpage's keywords.
     */
    public WebPage(String u, int i, ArrayList<String>k) {
        url = u;
        index = i;
        keywords = k;
    }

    /**
     * Returns the URL of the WebPage.
     *
     * @return
     *  Returns the URL of the webpage.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Returns the index of the WebPage.
     *
     * @return
     * Returns the index of the webpage.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Returns the rank of the WebPage.
     *
     * @return
     *  Returns the rank of the webpage.
     */
    public int getRank() {
        return rank;
    }

    /**
     * Sets the rank for the WebPage.
     * @param r
     * The new rank of the webpage.
     */
    public void setRank(int r) {
        rank = r;
    }

    /**
     * Returns the keywords from the WebPage.
     * @return
     * Returns the keywords that associate with the webpage.
     */
    public Collection<String> getKeywords(){
        return keywords;
    }

    /**
     * Prints out the webpage in a tabular form.
     *
     * @return
     * Returns a String of the this WebPage's data members in tabular form.
     */
    public String toString() {
        StringBuilder temp = new StringBuilder();
        for(String str : keywords) {
            temp.append(str).append(", ");
        }
        temp = new StringBuilder(temp.substring(0, temp.length() - 2));
        return String.format("%3d   | %-25s | %3d   | * | %s", index, url, rank, temp);
    }
}
