package com.davidkeen.test;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertThat;

public class Hamcrest {

    @Test
    public void assertMapHasEntry() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("foo", "bar");
        map.put("baz", "quux");

        assertThat(map, Matchers.<String, String>hasEntry("foo", "bar"));
        assertThat(map, Matchers.<String, String>hasEntry("baz", "quux"));
    }

    @Test
    public void assertListContains() throws Exception {
        List<String> list = new ArrayList<>();
        list.add("foo");
        list.add("bar");

        assertThat(list, Matchers.containsInAnyOrder("bar", "foo"));
    }
}
