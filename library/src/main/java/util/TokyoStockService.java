package util;

public class TokyoStockService implements StockLookupService {

	@Override
	public int price(String symbol) {
		throw new RuntimeException("sorry please come back later, we are down!");
	}

}
