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
import com.backendless.persistence.LoadRelationsQueryBuilder;
import com.example.myapplication.activity.MainActivity;
import com.example.myapplication.data.Group;
import com.example.myapplication.data.Group_Place_User;
import com.example.myapplication.utility.EspressoIdlingResource;
import com.example.myapplication.utility.TestApplication;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.hamcrest.core.IsNot.not;


@RunWith(AndroidJUnit4.class)
public class ForwardingInvitationTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class,true, false);


    @Before
    public void loadData() throws InterruptedException {
        Backendless.UserService.login("test@test.it", "test", new AsyncCallback<BackendlessUser>() {
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
    public void ForwardingInvitation() throws InterruptedException {
        //onView(withId(R.id.btnGroups)).perform(click());
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

        // check that the group has been created
        Assert.assertEquals(TestApplication.groups.get(0).getName(), "TestGroup");

        onView(withId(R.id.lvList))
                .check(matches(isDisplayed()));


        onData(allOf())
                .inAdapterView(withId(R.id.lvList))
                .atPosition(0)
                .perform(click());


        Thread.sleep(4000);

        onView(withId(R.id.ivInvite)).perform(click());

        closeSoftKeyboard();
        Thread.sleep(4000);

        onView(withId(R.id.searchView)).perform(click());

        // Type the text in the search field and submit the query
        onView(isAssignableFrom(EditText.class)).perform(typeText("cimmo"), 	closeSoftKeyboard());

        Thread.sleep(2000);

        // check that the user you are looking for is in the list
        onData(anything())
                .inAdapterView(withId(R.id.lv1))
                .atPosition(0)
                .onChildView(withId(R.id.tvUsername))
                .check(matches(withText(containsString("cimmo"))));

        onView(withId(R.id.lv1))
                .check(matches(hasDescendant(withText("cimmo"))));


        // I invite the user
        onView(withId(R.id.btnInvite)).perform(click());

        Thread.sleep(3000);

        // check that the searched user has been deleted from the list
        onView(withId(R.id.lv1))
                .check(matches(not(hasDescendant(withText("cimmo")))));

        LoadRelationsQueryBuilder<Group> loadRelationsQueryBuilder;
        loadRelationsQueryBuilder = LoadRelationsQueryBuilder.of( Group.class );
        loadRelationsQueryBuilder.setRelationName( "myInvitation" );

        Backendless.Data.of("Users").loadRelations("FD8C5190-FB82-75B5-FF48-E5D285306F00", loadRelationsQueryBuilder,
                new AsyncCallback<List<Group>>() {
                    @Override
                    public void handleResponse(List<Group> response) {

                        Assert.assertEquals("TestGroup",response.get(0).getName());

                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Log.e( "MYAPP", "server reported an error - " + fault.getMessage() );
                    }
                });

            Thread.sleep(3000);



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
