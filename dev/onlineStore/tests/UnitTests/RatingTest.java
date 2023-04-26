package UnitTests;

import DomainLayer.Stores.Rating;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RatingTest {
    Rating rate;
    @Test
    void CreateRate_Good() {
        rate = new Rating(3);
        assertEquals(3,rate.getRate());
    }
    @Test
    void CreateRate_BadRating() {
        assertThrows(IllegalArgumentException.class,()->rate = new Rating(8));
    }
    @Test
    void CreateRate_NeagiveRating() {
        assertThrows(IllegalArgumentException.class,()->rate = new Rating(-8));
    }
    @Test
    void CreateRateWithComment_Good() {
        String comment = "Great product";
        rate = new Rating(5,comment);
        assertEquals(5,rate.getRate());
        assertEquals(comment,rate.getComment());
    }
    @Test
    void CreateRateWithComment_nullComment() {
        String comment =null;
        assertThrows(NullPointerException.class,()->rate = new Rating(5,comment));
    }
    @Test
    void addComment_Good() {
        String comment = "Great product";
        rate = new Rating(3);
        rate.addComment(comment);
        assertEquals(comment,rate.getComment());
    }
    @Test
    void addComment_null() {
        String comment = null;
        rate = new Rating(3);
        assertThrows(NullPointerException.class,()->rate.addComment(comment));
    }
    @Test
    void addRate_good() {
        int newRating = 5;
        rate = new Rating(3);
        rate.addRate(newRating);
        assertEquals(newRating,rate.getRate());
    }
    @Test
    void addRate_BadVal() {
        int newRating = 8;
        rate = new Rating(3);
        assertThrows(IllegalArgumentException.class,()->rate.addRate(newRating));
    }
    @Test
    void addRate_NegVal() {
        int newRating = -8;
        rate = new Rating(3);
        assertThrows(IllegalArgumentException.class,()->rate.addRate(newRating));
    }
}