package util;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class APortfolio {
    private static final int ALLSTATE_VALUE = 88;
    private static final int IBM_VALUE =  125;
    private Portfolio portfolio;

    @Rule
    public ExpectedException thrown =
            ExpectedException.none();

    @Before
    public void create() {
        portfolio = new Portfolio();
    }

    @Test
    public void isEmptyBeforeAnyPurchase() {
        assertThat(portfolio.isEmpty(), is(true));
    }

    @Test
    public void isNoLongerEmptyAfterPurchase() {
        portfolio.purchase("ALL", 1);

        assertThat(portfolio.isEmpty(), is(false));
    }

    @Test
    public void hasCountZeroBeforeAnyPurchase() {
        assertThat(portfolio.countOfUniqueSymbols(), is(equalTo(0)));
    }

    @Test
    public void incrementsCountWithPurchaseOfNewSymbol() {
        portfolio.purchase("IBM", 1);
        portfolio.purchase("ALL", 1);

        assertThat(portfolio.countOfUniqueSymbols(), is(equalTo(2)));
    }

    @Test
    public void doesNotIncrementCountWithPurchaseOfSameSymbol() {
        portfolio.purchase("ALL", 1);
        portfolio.purchase("ALL", 1);

        assertThat(portfolio.countOfUniqueSymbols(),
                is(equalTo(1)));
    }

    @Test
    public void answersNumberOfSharesForPurchasedSymbol() {
        portfolio.purchase("ALL", 42);

        assertThat(portfolio.sharesForSymbol("ALL"),
                is(equalTo(42)));
    }

    @Test
    public void answersNumberOfSharesForNonPurchasedSymbol() {
        assertThat(portfolio.sharesForSymbol("ALL"),
                is(equalTo(0)));
    }

    @Test
    public void sumsSharesOfSameSymbolPurchases() {
        portfolio.purchase("ALL", 42);
        portfolio.purchase("ALL", 22);

        assertThat(portfolio.sharesForSymbol("ALL"),
                is(equalTo(42 + 22)));
    }

    @Test
    public void reducesSharesOnSale() {
        portfolio.purchase("ALL", 42);

        portfolio.sell("ALL", 20);

        assertThat(portfolio.sharesForSymbol("ALL"),
                is(equalTo(42 - 20)));
    }

    @Test
    public void reducesCountOnSaleOfAllShares() {
        portfolio.purchase("ALL", 42);

        portfolio.sell("ALL", 42);

        assertThat(portfolio.countOfUniqueSymbols(),
                is(equalTo(0)));
    }

    @Test
    public void exceptionHasAppropriateMessageWhenSellingTooMuch() {
        portfolio.purchase("ALL", 10);
        thrown.expect(PortfolioException.class);
        thrown.expectMessage("selling too much!");

        portfolio.sell("ALL", 10 + 1);
    }

    @Test
    public void isWorthlessWhenNothingPurchased() {
        assertThat(portfolio.value(), is(equalTo(0)));
    }
}
