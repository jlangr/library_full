package util;

public class NASDAQService implements StockService {
    @Override
    public int price(String symbol) {
        throw new RuntimeException("we are down for the night, sorry!");
    }
}
