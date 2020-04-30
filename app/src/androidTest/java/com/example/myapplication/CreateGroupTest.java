package com.example.myapplication;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.myapplication.activity.CreateGroup;
import com.example.myapplication.data.Group;
import com.example.myapplication.data.Group_Place_User;
import com.example.myapplication.data.Place;
import com.example.myapplication.utility.TestApplication;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.atomic.LongAdder;

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

    private final CountingIdlingResource idlingResource = new CountingIdlingResource("data_loaded");
    private DataLoaderHelperTest test = new DataLoaderHelperTest();

    @Rule
    public ActivityTestRule<CreateGroup> activityRule =
            new ActivityTestRule<>(CreateGroup.class);


    @Before
    public void loadData() {
        idlingResource.increment();
        test.loadCreateGroupData();
        idlingResource.decrement();

    }


    @Test
    public void CreationOfGroup() throws InterruptedException {
        onView(withId(R.id.etNameGroup)).perform(typeText("TestGroup"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.spnGroupType)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Restaurant")))
                .perform(click());
        onView(withId(R.id.spnGroupType))
                .check(matches(withSpinnerText(containsString("Restaurant"))));

        onView(withId(R.id.btnNewGroup)).perform(click());

        Thread.sleep(6000);

        Assert.assertEquals(TestApplication.groups.get(0).getName(), "TestGroup");
        Assert.assertEquals(TestApplication.groups.get(0).getType(), "restaurant");
    }

    @After
    public void deleteData() {
        idlingResource.increment();
        test.deleteGroups();
        idlingResource.decrement();
    }
}