package com.example.myapplication;

import android.content.Intent;
import android.widget.Toast;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.myapplication.activity.CreateGroup;
import com.example.myapplication.activity.Login;
import com.example.myapplication.activity.MainActivity;
import com.example.myapplication.utility.TestApplication;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.allOf;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */



@RunWith(AndroidJUnit4.class)
@LargeTest
public class CreateGroupTest {

    @Rule
    public ActivityTestRule<CreateGroup> activityRule =
            new ActivityTestRule<>(CreateGroup.class);

    @Test
    public void CreationOfGroup() {

        Backendless.UserService.login("simone@app.it", "simone", new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {

                TestApplication.user = response;
                onView(withId(R.id.etNameGroup)).perform(typeText("TestGroup"));
                Espresso.closeSoftKeyboard();
                onView(withId(R.id.spnGroupType)).perform(click());
                onData(allOf(is(instanceOf(String.class)), is("Restaurant")))
                        .perform(click());
                onView(withId(R.id.spnGroupType))
                        .check(matches(withSpinnerText(containsString("Restaurant"))));
                onView(withId(R.id.btnNewGroup)).perform(click());
                onData(anything()).inAdapterView(withId(R.id.lvList)).atPosition(0).
                        onChildView(withId(R.id.tvName)).
                        check(matches(withText("TestGroup")));
            }
            @Override
            public void handleFault(BackendlessFault fault) {
            }
        }, false);



    }
}