package ru.sbtqa.smartly.common.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.Arrays;

/** Матчер, который проверяет вхождение строк по очереди, но игнорируя регистр  */
public class StringContainsInOrderIgnoringCase extends TypeSafeMatcher<String> {
    private final Iterable<String> substrings;

    public StringContainsInOrderIgnoringCase(Iterable<String> substrings) {
        this.substrings = substrings;
    }

    @Override
    public boolean matchesSafely(String s) {
        int fromIndex = 0;

        for (String substring : substrings) {
            fromIndex = s.toUpperCase().indexOf(substring.toUpperCase(), fromIndex);
            if (fromIndex == -1) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void describeMismatchSafely(String item, Description mismatchDescription) {
        mismatchDescription.appendText("was \"").appendText(item).appendText("\"");
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a string containing ")
                .appendValueList("", ", ", "", substrings)
                .appendText(" in order");
    }

    public static Matcher<String> stringContainsInOrderIgnoringCase(Iterable<String> substrings) {
        return new StringContainsInOrderIgnoringCase(substrings);
    }

    public static Matcher<String> stringContainsInOrderIgnoringCase(String... substrings) {
        return new StringContainsInOrderIgnoringCase(Arrays.asList(substrings));
    }
}