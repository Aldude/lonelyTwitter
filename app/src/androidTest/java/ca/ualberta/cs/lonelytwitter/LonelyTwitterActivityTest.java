package ca.ualberta.cs.lonelytwitter;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import junit.framework.TestCase;

/**
 * Created by wz on 14/09/15.
 */
public class LonelyTwitterActivityTest extends ActivityInstrumentationTestCase2 {

    private String tweetText;
    private String tweet2Text;
    private EditText bodyText;
    private Button saveButton;

    public LonelyTwitterActivityTest() {
        super(ca.ualberta.cs.lonelytwitter.LonelyTwitterActivity.class);
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    public void testEditTweet() {
        LonelyTwitterActivity activity = (LonelyTwitterActivity) getActivity();

        activity.getTweets().clear();

        tweetText = "Hello!";
        tweet2Text = "Unhello!";
        bodyText = activity.getBodyText();

        activity.runOnUiThread(new Runnable() {
            public void run() {
                bodyText.setText(tweetText);
            }
        });
        getInstrumentation().waitForIdleSync();

        saveButton = activity.getSaveButton();
        activity.runOnUiThread(new Runnable() {
            public void run() {
                saveButton.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();

        // get the list of tweets from the view!
        final ListView oldTweetsList = activity.getOldTweetsList();
        Tweet newestTweet = (Tweet) oldTweetsList.getItemAtPosition(0);
        assertEquals(tweetText, newestTweet.getText());

        // Code from https://developer.android.com/training/activity-testing/activity-functional-testing.html
        // Date: 2015-10-15
        // Set up an ActivityMonitor
        Instrumentation.ActivityMonitor receiverActivityMonitor =
                getInstrumentation().addMonitor(EditTweetActivity.class.getName(),
                        null, false);

        activity.runOnUiThread(new Runnable() {
            public void run() {
                View v = oldTweetsList.getChildAt(0);
                oldTweetsList.performItemClick(v, 0, v.getId());
            }
        });
        getInstrumentation().waitForIdleSync();

        // Validate that ReceiverActivity is started
        final EditTweetActivity receiverActivity = (EditTweetActivity)
                receiverActivityMonitor.waitForActivityWithTimeout(1000);
        assertNotNull("ReceiverActivity is null", receiverActivity);
        assertEquals("Monitor for ReceiverActivity has not been called",
                1, receiverActivityMonitor.getHits());
        assertEquals("Activity is of wrong type",
                EditTweetActivity.class, receiverActivity.getClass());

        // Remove the ActivityMonitor
        getInstrumentation().removeMonitor(receiverActivityMonitor);

        // test that the tweet editor starts up with the correct tweet
        receiverActivityBodyText = receiverActivity.getBodyText();
        assertEquals(receiverActivityBodyText.getText(), newestTweet.getText());
        receiverActivitySaveButton = receiverActivity.getSaveButton();

        // test that we can edit a tweet
        // test that we can push a save button for the edited tweet
        receiverActivity.runOnUiThread(new Runnable() {
            public void run() {
                receiverActivityBodyText.setText(tweet2Text);
                receiverActivitySaveButton.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();

        // test that the modified tweet was saved
        newestTweet = (Tweet) oldTweetsList.getItemAtPosition(0);
        assertEquals(tweet2Text, newestTweet.getText());

        // test that the modified tweet is in the tweet list

        receiverActivity.finish(); // close activity test is good!
    }



}