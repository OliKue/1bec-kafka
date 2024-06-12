package com.example.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventTest {

    @Test
    public void testEventToString() {

        Event e1 = new Event("test", 7.5f);
        String expected = "Event{station='test', weather=7.5}";
        assertEquals(expected,e1.toString());
    }

    @Test
    public void testEventFromString() {

        String testString = "Event{station='test', weather=7.5}";

        Event e1 = new Event(testString);

        assertEquals("test", e1.getStation());
        assertEquals(7.5f, e1.getWeather());
    }
}
