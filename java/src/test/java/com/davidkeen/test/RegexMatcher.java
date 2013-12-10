package com.davidkeen.test;

/*
 * Adapted from http://piotrga.wordpress.com/2009/03/27/hamcrest-regex-matcher/
 * and http://www.planetgeek.ch/2012/03/07/create-your-own-matcher/
 *
 * Use it like this: assertThat(myString, RegexMatcher.matches(myRegex));
 */

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * A hamcrest matcher to compare a string to a regex.
 */
public class RegexMatcher extends TypeSafeMatcher<String> {
    private final String regex;

    private RegexMatcher(String regex){
        this.regex = regex;
    }

    public static RegexMatcher matches(String regex){
        return new RegexMatcher(regex);
    }

    @Override
    public boolean matchesSafely(String s){
        return s.matches(regex);
    }

    @Override
    public void describeTo(Description description){
        description.appendText("should match regex " + regex);
    }

    @Override
    protected void describeMismatchSafely(String s, Description mismatchDescription) {
        mismatchDescription.appendText("was ").appendValue(s);
    }
}
