// Shrey Shah
// ID: 112693183
// Shrey.Shah@stonybrook.edu
// Homework #7
// CSE214
// R.04 James Finn

import java.util.*;

public class URLComparator implements Comparator<WebPage>{
    public int compare(WebPage page1, WebPage page2) {
        return page1.getUrl().compareTo(page2.getUrl());
    }
}
