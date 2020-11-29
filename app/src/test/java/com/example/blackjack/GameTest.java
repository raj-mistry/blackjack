package com.example.blackjack;

import junit.framework.TestCase;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class GameTest extends TestCase {
    private Game mGame;

    public void setUp() throws Exception {
        super.setUp();

        mGame = new Game();

        mGame.dealerTotal=22;


    }

    public void tearDown() throws Exception {
    }

    public void testSetDealerBust() {
        mGame.setDealerBust();
        assertThat(mGame.dealerBust,is(equalTo(true)));
    }
}