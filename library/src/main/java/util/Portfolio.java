package util;

import java.util.HashMap;
import java.util.Map;

public class Portfolio {
    private Map<String,Integer> holdings = new HashMap<>();
    private StockService stockService = new NASDAQService();

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
        removeSymbolIfNoSharesExist(symbol);
    }

    private void removeSymbolIfNoSharesExist(String symbol) {
        if (sharesForSymbol(symbol) == 0)
            holdings.remove(symbol);
    }

    private void throwWhenSellingTooManyShares(String symbol, int shares) {
        if (shares > sharesForSymbol(symbol))
            throw new PortfolioException("selling too much!");
    }

    public int value() {
        int total = 0;
        for (String symbol: holdings.keySet())
            total += stockService.price(symbol)
                   * sharesForSymbol(symbol);
        return total;
    }

    public void setStockService(StockService stockService) {
        this.stockService = stockService;
    }
}
