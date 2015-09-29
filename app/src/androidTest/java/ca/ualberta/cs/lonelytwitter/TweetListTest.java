package ca.ualberta.cs.lonelytwitter;

import android.test.ActivityInstrumentationTestCase2;

import java.util.List;
import java.util.ListIterator;

/**
 * Created by sakaluk on 9/28/15.
 */
public class TweetListTest extends ActivityInstrumentationTestCase2 {

    public TweetListTest() {
        super(ca.ualberta.cs.lonelytwitter.LonelyTwitterActivity.class);
    }

    public void testAddTweet() {
        TweetList list = new TweetList();
        list.addTweet(new NormalTweet("Test"));
    }

    public void testDoubleAdd() {
        TweetList list = new TweetList();
        Tweet tweet = new NormalTweet("Test");
        Boolean exceptionCaught = new Boolean("FALSE");
        list.addTweet(tweet);

        try {
            list.addTweet(tweet);
        } catch (IllegalArgumentException e) {
            exceptionCaught = TRUE;
        } catch (Exception e) {

        }

        assertTrue(exceptionCaught);
    }

    public void testRemoveTweet() {
        TweetList list = new TweetList();
        Tweet tweet = new NormalTweet("Test");
        list.addTweet(tweet);
        list.removeTweet(tweet);
        assertFalse(list.hasTweet(tweet));
    }

    public void testHasTweet() {
        TweetList list = new TweetList();
        Tweet tweet = new NormalTweet("Test");
        Tweet tweet2 = tweet;
        list.addTweet(tweet);
        assertTrue(list.hasTweet(tweet));
        assertTrue(list.hasTweet(tweet2));
    }

    public void testGetTweets() {
        TweetList list = new TweetList();
        Tweet tweet = new NormalTweet("Older");
        Tweet tweet2 = new NormalTweet("Newer");
        boolean inOrder = new boolean[TRUE);

        List<Tweet> orderedTweets = TweetList.getTweets();
        ListIterator<Tweet> it = orderedTweets.listIterator(1); // Gets the second tweet

        while(it.hasNext()) {
            if(it.previous().getDate().after(orderedTweets.get(it.previousIndex() + 1).getDate())) {
                inOrder = FALSE;
                break;
            }
        }

        assertTrue(inOrder);
    }

    public void testGetCount() {
        TweetList list = new TweetList();
        Tweet tweet = new NormalTweet("Test");
        assertTrue(list.getCount() == 0);
        list.addTweet(tweet);
        assertTrue(list.getCount() == 1);
        list.removeTweet(tweet);
        assertTrue(list.getCount() == 0);
    }
}