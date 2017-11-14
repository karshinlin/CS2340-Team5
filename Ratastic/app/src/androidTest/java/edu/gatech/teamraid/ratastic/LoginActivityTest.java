package edu.gatech.teamraid.ratastic;

import android.support.v7.app.AppCompatActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest extends AppCompatActivity {
    @Rule
    public ActivityTestRule<LoginActivity> barActivityRule = new ActivityTestRule<>(
            LoginActivity.class);

    @Test
    public void verifyLoginActivity() {
        String email1 = "Email1@gmail.com";
        String password1 = "Pass1";
        String email2 = "";
        String password2 = "Pass2";
        String email3 = "mengle7@gatech.edu";
        String password3 = "";
        String email4 = "mengle7@gatech.edu";
        String password4 = "dunkin72";


        onView(withId(R.id.emailEdit)).perform(typeText(email1), closeSoftKeyboard());
        onView(withId(R.id.passwordEdit)).perform(typeText(password1), closeSoftKeyboard());
        onView(withId(R.id.signInBtn)).perform(click());
        int visibilityVal1 = findViewById(R.id.failedLoginText).getVisibility();
        boolean visibility1 = true;
        if (visibilityVal1 > 0) {
            visibility1 = false;
        }
        Assert.assertEquals(visibility1, true);

        onView(withId(R.id.emailEdit)).perform(typeText(email2), closeSoftKeyboard());
        onView(withId(R.id.passwordEdit)).perform(typeText(password2), closeSoftKeyboard());
        onView(withId(R.id.signInBtn)).perform(click());
        int visibilityVal2 = findViewById(R.id.failedLoginText).getVisibility();
        boolean visibility2 = true;
        if (visibilityVal2 > 0) {
            visibility2 = false;
        }
        Assert.assertEquals(visibility2, true);

        onView(withId(R.id.emailEdit)).perform(typeText(email3), closeSoftKeyboard());
        onView(withId(R.id.passwordEdit)).perform(typeText(password3), closeSoftKeyboard());
        onView(withId(R.id.signInBtn)).perform(click());
        int visibilityVal3 = findViewById(R.id.failedLoginText).getVisibility();
        boolean visibility3 = true;
        if (visibilityVal3 > 0) {
            visibility3 = false;
        }
        Assert.assertEquals(visibility3, true);

        onView(withId(R.id.emailEdit)).perform(typeText(email4), closeSoftKeyboard());
        onView(withId(R.id.passwordEdit)).perform(typeText(password4), closeSoftKeyboard());
        onView(withId(R.id.signInBtn)).perform(click());
        int visibilityVal4 = findViewById(R.id.failedLoginText).getVisibility();
        boolean visibility4 = true;
        if (visibilityVal4 > 0) {
            visibility4 = false;
        }
        Assert.assertEquals(visibility4, false);
    }
}
