package com.example.android.popularmoviesstage1;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@LargeTest
@RunWith(AndroidJUnit4.class)

public class GridViewTitleTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    public CountingIdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource(){
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResourceInTest();
        IdlingRegistry.getInstance().register(mIdlingResource);
        //Espresso.registerIdlingResources(mIdlingResource);
    }


    @Test
    public void clickRecyclerViewItem_PassesRecipeNameToNextActivity() {
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions
                .actionOnItemAtPosition(0, click()));
        onView(withId(R.id.selectedTitle)).check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResource(){
        IdlingRegistry.getInstance().unregister(mIdlingResource);
        //Espresso.unregisterIdlingResources(mIdlingResource);
    }
}
