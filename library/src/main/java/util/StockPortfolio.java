package util;

import java.util.*;

public class StockPortfolio {
	private Map<String, Integer> sharesForSymbol =
			new HashMap<>();

	public boolean isEmpty() {
		return numberOfSymbols() == 0;
	}

	public void purchase(String symbol, int shares) {
		sharesForSymbol.put(symbol, shareCount(symbol) + shares);
	}

	public int numberOfSymbols() {
		return sharesForSymbol.keySet().size();
	}

	public int shareCount(String symbol) {
		if(sharesForSymbol.containsKey(symbol))
			return sharesForSymbol.get(symbol);
		return 0;
	}

	public void sell(String symbol, int shares) {
		if(shareCount(symbol) < shares)
			throw new InsufficientShareSale("selling too many shares of " + symbol);
		sharesForSymbol.put(symbol, shareCount(symbol) - shares);
	}

}
