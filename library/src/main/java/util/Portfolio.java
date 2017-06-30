package util;

import java.util.HashMap;
import java.util.Map;

public class Portfolio {
    public static final int LARGE_SALE_MINIMUM = 1000;
    private Map<String, Integer> holdings = new HashMap<>();
    private StockService stockService = new NASDAQService();
    private SECNotifierAPI secNotifier;

    public boolean isEmpty() {
        return countOfUniqueSymbols() == 0;
    }

    public void purchase(String symbol, int shares) {
        holdings.put(symbol,
                shares + shares(symbol));
    }

    public int countOfUniqueSymbols() {
        return holdings.size();
    }

    public int shares(String symbol) {
        if (!holdings.containsKey(symbol))
            return 0;
        return holdings.get(symbol);
    }

    public void sell(String symbol, int shares) {
        throwWhenSellingTooManyShares(symbol, shares);
        notifyIfLargePurchase(symbol, shares);
        holdings.put(symbol, shares(symbol) - shares);
        removeSymbolIfNoSharesExist(symbol);
    }

    private void notifyIfLargePurchase(String symbol, int shares) {
        if (secNotifier != null && shares >= LARGE_SALE_MINIMUM)
           secNotifier.notifyOfLargePurchase("jeff", symbol, shares);
    }

    private void removeSymbolIfNoSharesExist(String symbol) {
        if (shares(symbol) == 0) holdings.remove(symbol);
    }

    private void throwWhenSellingTooManyShares(String symbol, int shares) {
        if (shares > shares(symbol)) throw new PortfolioException("selling too much!");
    }

    public int value() {
        return holdings
                .keySet()
                .stream()
                .mapToInt(symbol ->
                        stockService.price(symbol) * shares(symbol))
                .sum();
    }

    public void setStockService(StockService stockService) {
        this.stockService = stockService;
    }

    public void setSecNotifier(SECNotifierAPI secNotifier) {
        this.secNotifier = secNotifier;
    }
}
