package com.example.myapplication;

import android.view.Gravity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.backendless.BackendlessUser;
import com.example.myapplication.activity.MainActivity;
import com.example.myapplication.utility.TestApplication;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule
            = new ActivityTestRule<>(MainActivity.class, true, false);

    @Before
    public void setUp() {
        TestApplication.user = new BackendlessUser();
        TestApplication.user.setProperty("objectId", "ID");
        TestApplication.user.setProperty("username", "mariorossi");
        TestApplication.user.setProperty("name", "Mario");
        TestApplication.user.setProperty("surname", "Rossi");
        TestApplication.user.setProperty("email", "mariorossi@gmail.com");
        activityRule.launchActivity(null);
    }

    @Test
    public void testNavigateInvitations() {
        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer

        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_invitations));
    }

    @Test
    public void testNavigateInvitationsAndGroup() {

        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer

        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_invitations));

        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer

        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_groups));

    }

    @Test
    public void testUserDetails() {
        onView(withId(R.id.tvUsername))
                .check(matches(withText(TestApplication.user.getProperty("username").toString())));
        onView(withId(R.id.tvName))
                .check(matches(withText(TestApplication.user.getProperty("name").toString())));
        onView(withId(R.id.tvSurname))
                .check(matches(withText(TestApplication.user.getProperty("surname").toString())));
        onView(withId(R.id.tvEmail))
                .check(matches(withText(TestApplication.user.getProperty("email").toString())));
    }

    @Test
    public void clickSettings () {
        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer
        onView(withId(R.id.ivSettings))
                .perform(ViewActions.click());
    }

    @Test
    public void clickLogOut () {
        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer
        onView(withId(R.id.ivLogout))
                .perform(click());
    }

}