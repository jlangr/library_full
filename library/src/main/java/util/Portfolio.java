package util;

import java.util.HashMap;
import java.util.Map;

public class Portfolio {
    private Map<String,Integer> holdings = new HashMap<>();

    public boolean isEmpty() {
        return countOfUniqueSymbols() == 0;
    }

    public void purchase(String symbol, int shares) {
        holdings.put(symbol,
                shares + sharesForSymbol(symbol));
    }

    public int countOfUniqueSymbols() {
        return holdings.size();
    }

    public int sharesForSymbol(String symbol) {
        if (!holdings.containsKey(symbol))
            return 0;
        return holdings.get(symbol);
    }

    public void sell(String symbol, int shares) {
        throwWhenSellingTooManyShares(symbol, shares);
        holdings.put(symbol, sharesForSymbol(symbol) - shares);
    }

    private void throwWhenSellingTooManyShares(String symbol, int shares) {
        if (shares > sharesForSymbol(symbol))
            throw new PortfolioException("selling too much!");
    }
}
