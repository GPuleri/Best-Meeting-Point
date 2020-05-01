package com.example.myapplication;


import androidx.test.espresso.Espresso;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;


import com.example.myapplication.activity.CreateGroup;

import com.example.myapplication.utility.DataLoaderHelperTest;
import com.example.myapplication.utility.EspressoIdlingResource;
import com.example.myapplication.utility.TestApplication;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;



import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
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

//    public final static CountingIdlingResource idlingResource = new CountingIdlingResource("data_loaded");
    private DataLoaderHelperTest test = new DataLoaderHelperTest();

    @Rule
    public ActivityTestRule<CreateGroup> activityRule =
            new ActivityTestRule<>(CreateGroup.class);


    @Before
    public void loadData() {
        EspressoIdlingResource.increment();
        test.loadUserData();
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
        EspressoIdlingResource.increment();
        EspressoIdlingResource.increment();
        test.deleteGroups();

    }
}