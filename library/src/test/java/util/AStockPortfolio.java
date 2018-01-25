package util;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.*;
import org.junit.rules.*;

public class AStockPortfolio {
	StockPortfolio stockPortfolio = new StockPortfolio();

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void isEmptyWhenCreated() {
		assertThat(stockPortfolio.isEmpty(), is(true));
	}

	@Test
	public void isNotEmptyAfterPurchase() {
		stockPortfolio.purchase("AAPL", 10);

		assertThat(stockPortfolio.isEmpty(), is(false));
	}

	@Test
	public void hasZeroSymbolsWhenCreated() {
		assertThat(stockPortfolio.numberOfSymbols(), 
				is(equalTo(0)));
	}

	@Test
	public void incrementSymbolAfterFirstPurchase() {
		stockPortfolio.purchase("AAPL", 10);

		assertThat(stockPortfolio.numberOfSymbols(), 
				is(equalTo(1)));
	}

	@Test
	public void incrementSymbolAfterDifferentSymbolPurchase() {
		stockPortfolio.purchase("AAPL", 10);
		stockPortfolio.purchase("GOOG", 10);

		assertThat(stockPortfolio.numberOfSymbols(), 
				is(equalTo(2)));
	}

	@Test
	public void doesNotIncrementSymbolAfterSameSymbolPurchase() {
		stockPortfolio.purchase("AAPL", 10);
		stockPortfolio.purchase("AAPL", 10);

		assertThat(stockPortfolio.numberOfSymbols(), 
				is(equalTo(1)));
	}

	@Test
	public void hasZeroSharesForUnpurchasedSymbol() {
		assertThat(stockPortfolio.shareCount("BLAH"), 
				is(equalTo(0)));
	}
	
	@Test
	public void returnsSharesForPurchasedSymbol() {
		stockPortfolio.purchase("IBM", 42);
		
		assertThat(stockPortfolio.shareCount("IBM"), 
				is(equalTo(42)));
	}
	
	@Test
	public void returnsSharesForMultiplePurchasedSymbols() {
		stockPortfolio.purchase("AAPL", 10);
		stockPortfolio.purchase("GOOG", 20);
		
		assertThat(stockPortfolio.shareCount("AAPL"), 
				is(equalTo(10)));
	}
	
	@Test
	public void returnsSumOfSharesForSamePurchasedSymbols() {
		stockPortfolio.purchase("AAPL", 10);
		stockPortfolio.purchase("AAPL", 32);
		
		assertThat(stockPortfolio.shareCount("AAPL"), 
				is(equalTo(42)));
	}
	
	@Test
	public void reduceSharesOnSale() {
		stockPortfolio.purchase("AAPL", 10);
		stockPortfolio.sell("AAPL", 1);
		
		assertThat(stockPortfolio.shareCount("AAPL"), 
				is(equalTo(9)));
	}
	
	@Test(expected = InsufficientShareSale.class)
	public void throwExceptionOnSellingInsufficientShares() {
		stockPortfolio.purchase("AAPL", 10);
		stockPortfolio.sell("AAPL", 11);
	}
	
	@Test
	public void throwWhenSellingInsufficientShares() {
		stockPortfolio.purchase("AAPL", 10);
		try {
			stockPortfolio.sell("AAPL", 11);
			fail("should've thrown an exception but did not");
		}
		catch (InsufficientShareSale expected) {
			// fall thru
		}
		
	}
	
	@Test
	public void sonOfThrowWhenSellingTooManyShares() {
		stockPortfolio.purchase("AAPL", 10);
		thrown.expect(InsufficientShareSale.class);
		thrown.expectMessage("selling too many shares of AAPL");
		stockPortfolio.sell("AAPL", 11);
	}
}