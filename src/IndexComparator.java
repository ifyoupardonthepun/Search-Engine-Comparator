// Shrey Shah
// ID: 112693183
// Shrey.Shah@stonybrook.edu
// Homework #7
// CSE214
// R.04 James Finn

import java.util.*;

public class IndexComparator implements Comparator<WebPage>{
    public int compare(WebPage page1, WebPage page2) {
        return Integer.compare(page1.getIndex(), page2.getIndex());
    }
}
