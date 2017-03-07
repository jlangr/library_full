package util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Portfolio {
    private Map<String, Integer> holdings = new HashMap<>();

    public boolean isEmpty() {
        return holdings.isEmpty();
    }

    public void purchase(String symbol, int shares) {
        holdings.put(symbol, shares(symbol) + shares);
    }

    public int countUniqueSymbols() {
        return holdings.size();
    }

    public int shares(String symbol) {
        if (!holdings.containsKey(symbol))
            return 0;
        return holdings.get(symbol);
    }

    public void sell(String symbol, int shares) {
        throwWhenSellingTooManyShares(symbol, shares);
        holdings.put(symbol, shares(symbol) - shares);
        removeIfNoSharesExist(symbol);
    }

    private void removeIfNoSharesExist(String symbol) {
        if (shares(symbol) == 0)
            holdings.remove(symbol);
    }

    private void throwWhenSellingTooManyShares(String symbol, int shares) {
        if (shares > shares(symbol))
            throw new PortfolioTransactionException();
    }
}
