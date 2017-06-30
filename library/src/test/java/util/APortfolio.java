package util;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class APortfolio {
    private static final int ALLSTATE_VALUE = 88;
    private static final int IBM_VALUE =  120;

    @InjectMocks
    private Portfolio portfolio;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private StockService service;

    @Mock
    private SECNotifierAPI sec;

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

        assertThat(portfolio.shares("ALL"),
                is(equalTo(42)));
    }

    @Test
    public void answersNumberOfSharesForNonPurchasedSymbol() {
        assertThat(portfolio.shares("ALL"),
                is(equalTo(0)));
    }

    @Test
    public void sumsSharesOfSameSymbolPurchases() {
        portfolio.purchase("ALL", 42);
        portfolio.purchase("ALL", 22);

        assertThat(portfolio.shares("ALL"),
                is(equalTo(42 + 22)));
    }

    @Test
    public void reducesSharesOnSale() {
        portfolio.purchase("ALL", 42);

        portfolio.sell("ALL", 20);

        assertThat(portfolio.shares("ALL"),
                is(equalTo(42 - 20)));
    }

    @Test
    public void notifySECOnLargeSale() {
        portfolio.purchase("ALL", 10000);

        portfolio.sell("ALL", Portfolio.LARGE_SALE_MINIMUM + 1);

        verify(sec).notifyOfLargePurchase(
            eq("jeff"),
            eq("ALL"),
            eq(Portfolio.LARGE_SALE_MINIMUM + 1));
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

    @Test
    public void multipliesSharesByPriceToDetermineValue() {
        when(service.price("ALL")).thenReturn(ALLSTATE_VALUE);

        portfolio.purchase("ALL", 10);

        assertThat(portfolio.value(), is(equalTo(10 * ALLSTATE_VALUE)));
    }

    @Test
    public void accumulatesValuesForMultiplePurchases() {
        when(service.price("ALL")).thenReturn(ALLSTATE_VALUE);
        when(service.price("IBM")).thenReturn(IBM_VALUE);

        portfolio.purchase("ALL", 10);
        portfolio.purchase("IBM", 20);

        assertThat(portfolio.value(),
                is(equalTo(10 * ALLSTATE_VALUE +
                           20 * IBM_VALUE)));
    }
}
