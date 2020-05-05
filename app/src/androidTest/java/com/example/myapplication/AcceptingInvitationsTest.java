package com.example.myapplication;

import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.myapplication.activity.MainActivity;
import com.example.myapplication.data.Group;
import com.example.myapplication.data.Group_Place_User;
import com.example.myapplication.utility.EspressoIdlingResource;
import com.example.myapplication.utility.TestApplication;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class AcceptingInvitationsTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class,true, false);

    @Before
    public void sendInvitation () throws InterruptedException {
        Backendless.UserService.login("test2@test2.it", "test2", new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                TestApplication.user = response;
                TestApplication.groups = new ArrayList<>();
                TestApplication.group_place_users = new ArrayList<>();

            }

            @Override
            public void handleFault(BackendlessFault fault) {
            }
        });
        Thread.sleep(5000);
        activityRule.launchActivity(null);
        Thread.sleep(5000);
    }


    @Test
    public void acceptingInvitation () throws InterruptedException {
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.etNameGroup)).perform(typeText("TestGroup"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btnNewGroup)).perform(click());

        Thread.sleep(5000);
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open());

        // Start the screen of your activity.
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_groups));
        Thread.sleep(3000);

        onData(allOf())
                .inAdapterView(withId(R.id.lvList))
                .atPosition(0)
                .perform(click());


        Thread.sleep(4000);

        onView(withId(R.id.ivInvite)).perform(click());

        closeSoftKeyboard();
        Thread.sleep(4000);

        onView(withId(R.id.searchView)).perform(click());

        onView(isAssignableFrom(EditText.class)).perform(typeText("test"), 	closeSoftKeyboard());

        onView(withId(R.id.btnInvite)).perform(click());

        Thread.sleep(3000);

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open());

        // Start the screen of your activity.
        onView(withId(R.id.ivLogout))
                .perform(click());

        Thread.sleep(2500);

        //verifico che ci sia l'invito

        onView(withId(R.id.etMail)).perform(typeText("test@test.it"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.etPassword)).perform(typeText("test"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btnLogin)).perform(click());
        Thread.sleep(3000);

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open());

        // Start the screen of your activity.
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_invitations));

        Thread.sleep(2000);

        //check the invitation
        onView(withId(R.id.lvList))
                .check(matches(hasDescendant(withText("TestGroup"))));

        onView(withId(R.id.btnConfirm)).perform(click());

        Thread.sleep(2500);

        onView(withId(R.id.lvList))
                .check(matches(not(hasDescendant(withText("TestGroup")))));

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open());

        // Start the screen of your activity.
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_groups));
        Thread.sleep(3000);

        onView(withId(R.id.lvList))
                .check(matches(hasDescendant(withText("TestGroup"))));



    }

    @After
    public void deleteGroups() throws InterruptedException {


        IdlingRegistry.getInstance().register(EspressoIdlingResource.getIdlingResource());
        EspressoIdlingResource.increment();
        EspressoIdlingResource.increment();

        Backendless.Data.of(Group.class).remove(TestApplication.groups.get(0), new AsyncCallback<Long>() {
            @Override
            public void handleResponse(Long response) {
                Log.i("Group", response.toString());
                EspressoIdlingResource.decrement();
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
        Backendless.Data.of(Group_Place_User.class).remove(TestApplication.group_place_users.get(0), new AsyncCallback<Long>() {
            @Override
            public void handleResponse(Long response) {
                Log.i("Group_place_user", response.toString());
                EspressoIdlingResource.decrement();
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });

        Thread.sleep(3000);
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getIdlingResource());
    }

}
