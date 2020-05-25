package de.domjos.mycache.services.caching.opencaching;

public class CacheList {
    private String[] results;
    private boolean more;

    public CacheList(String[] results, boolean more) {
        this.results = results;
        this.more = more;
    }

    public String[] getResults() {
        return this.results;
    }

    public void setResults(String[] results) {
        this.results = results;
    }

    public boolean isMore() {
        return this.more;
    }

    public void setMore(boolean more) {
        this.more = more;
    }
}
