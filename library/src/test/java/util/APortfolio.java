package util;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class APortfolio {
    final static String ALLSTATE = "ALL";
    final static String IBM = "IBM";

    Portfolio portfolio;

    @Before
    public void create() {
        portfolio = new Portfolio();
    }

    @Test
    public void isEmptyWhenCreated() {
        assertThat(portfolio.isEmpty(), is(true));
    }

    @Test
    public void isNoLongerEmptyAfterPurchase() {
        portfolio.purchase("ALL", 42);

        assertThat(portfolio.isEmpty(), is(false));
    }

    @Test
    public void containsZeroUniqueSymbolsWhenCreated() {
        assertThat(portfolio.countUniqueSymbols(), is(equalTo(0)));
    }

    @Test
    public void incrementsUniqueSymbolCountAfterPurchase() {
        portfolio.purchase(ALLSTATE, 42);
        portfolio.purchase(IBM, 10);

        int count = portfolio.countUniqueSymbols();

        assertThat(count, is(equalTo(2)));
    }

    @Test
    public void doesNotIncrementSymbolCountForDuplicatePurchase() {
        portfolio.purchase(ALLSTATE, 42);
        portfolio.purchase(ALLSTATE, 10);

        int count = portfolio.countUniqueSymbols();

        assertThat(count, is(equalTo(1)));
    }

    @Test
    public void returnsZeroForSharesOfUnpurchasedSymbol() {
        assertThat(portfolio.shares(ALLSTATE), is(equalTo(0)));
    }

    @Test
    public void returnsSharesPurchasedForSymbol() {
        portfolio.purchase(ALLSTATE, 42);

        int shares = portfolio.shares(ALLSTATE);

        assertThat(shares, is(equalTo(42)));
    }

    @Test
    public void separatesSharePurchasesBySymbol() {
        portfolio.purchase(ALLSTATE, 42);
        portfolio.purchase(IBM, 10);

        int shares = portfolio.shares(ALLSTATE);

        assertThat(shares, is(equalTo(42)));
    }

    @Test
    public void returnsSumOfAllSharesPurchasedForSymbol() {
        portfolio.purchase(ALLSTATE, 42);
        portfolio.purchase(ALLSTATE, 10);

        int shares = portfolio.shares(ALLSTATE);

        assertThat(shares, is(equalTo(52)));
    }

    @Test
    public void reducesShareCountOnSell() {
        portfolio.purchase(ALLSTATE, 42);
        portfolio.sell(ALLSTATE, 10);

        int shares = portfolio.shares(ALLSTATE);

        assertThat(shares, is(equalTo(32)));
    }

    @Test
    public void removesSymbolWhenAllSharesSold() {
        portfolio.purchase(ALLSTATE, 42);
        portfolio.sell(ALLSTATE, 42);

        int symbolCount = portfolio.countUniqueSymbols();

        assertThat(symbolCount, is(equalTo(0)));
    }

    @Test(expected = PortfolioTransactionException.class)
    public void throwsOnSellOfTooManyShares() {
        portfolio.purchase(ALLSTATE, 42);
        portfolio.sell(ALLSTATE, 43);
    }
}
