package api.library.reservations;

import org.junit.Test;

public class AHoldService {
    @Test
    public void acceptsAHold() {
        HoldService service = new HoldService();

//        service.requestHold(holdingBarcode, patronId);
    }
}

// HOLDS:

// request hold: patron ID, barcode

// if holding is checked in:
//   -- holding is marked as "requested"
//   -- notification message broadcast to notification listener (?)

// if holding is checked out:
//   -- holding is marked as "requested"
//

// checkout process needs to be updated:
//   -- if patron who requested hold is not the one checking out the book, require librarian override?
//      -- if overridden w/ notify: send message to original patron
//      -- clear hold after checkout
//   -- if patron who requested hold is the one checking out the book:
//      -- clear hold after checkout

// checkin process updates:
//    -- if book is not "requested," do nothing special
//    -- if book is "requested," mark as "held," notify listeners
//    -- if book is already "held," do nothing special

// patron requests lists of holds
// patron cancels hold(s)
// patron
// librarian requests lists of holds for patron
// librarian requests lists of all held books
// librarian requests lists of all hold requests books
// librarian cancels hold for patron
