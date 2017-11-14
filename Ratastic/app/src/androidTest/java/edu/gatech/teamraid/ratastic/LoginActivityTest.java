package edu.gatech.teamraid.ratastic;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    @Rule
    public ActivityTestRule<LoginActivity> barActivityRule = new ActivityTestRule<>(
            LoginActivity.class);


    @Test
    public void invalidLoginTest() {
        String invalidEmail = "Email1@gmail.com";
        String invalidPassword = "Pass4";

        onView(withId(R.id.emailEdit)).perform(typeText(invalidEmail), closeSoftKeyboard());
        onView(withId(R.id.passwordEdit)).perform(typeText(invalidPassword), closeSoftKeyboard());
        onView(withId(R.id.signInBtn)).perform(click());
        onView(withId(R.id.failedLoginText)).check(matches(withText("Unsuccessful login. Please try again.")));
    }
    @Test
    public void emptyEmailTest() {
        String emptyEmail = "";
        String validPassword2 = "dunkin72";

        onView(withId(R.id.emailEdit)).perform(typeText(emptyEmail), closeSoftKeyboard());
        onView(withId(R.id.passwordEdit)).perform(typeText(validPassword2), closeSoftKeyboard());
        onView(withId(R.id.signInBtn)).perform(click());
        onView(withId(R.id.failedLoginText)).check(matches(withText("Unsuccessful login. Please try again.")));
    }
    @Test
    public void emptyPasswordTest() {
        String validEmail2 = "mengle7@gatech.edu";
        String emptyPassword = "";

        onView(withId(R.id.emailEdit)).perform(typeText(validEmail2), closeSoftKeyboard());
        onView(withId(R.id.passwordEdit)).perform(typeText(emptyPassword), closeSoftKeyboard());
        onView(withId(R.id.signInBtn)).perform(click());
        onView(withId(R.id.failedLoginText)).check(matches(withText("Unsuccessful login. Please try again.")));
    }

    @Test
    public void emptyUsernameAndPasswordTest() {
        String emptyEmail2 = "";
        String emptyPassword2 = "";

        onView(withId(R.id.emailEdit)).perform(typeText(emptyEmail2), closeSoftKeyboard());
        onView(withId(R.id.passwordEdit)).perform(typeText(emptyPassword2), closeSoftKeyboard());
        onView(withId(R.id.signInBtn)).perform(click());
        onView(withId(R.id.failedLoginText)).check(matches(withText("Unsuccessful login. Please try again.")));
    }

    @Test
    public void validLoginTest() {
        String validEmail = "mengle7@gatech.edu";
        String validPassword = "dunkin72";

        onView(withId(R.id.emailEdit)).perform(typeText(validEmail), closeSoftKeyboard());
        onView(withId(R.id.passwordEdit)).perform(typeText(validPassword), closeSoftKeyboard());
        onView(withId(R.id.signInBtn)).perform(click());
        onView(withId(R.id.failedLoginText)).check(matches(withText("")));
    }
}
