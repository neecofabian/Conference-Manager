package backend.data.entity;

import com.conference.backend.conference_and_rooms.entities.DateInterval;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link DateInterval}
 *
 */
public class DateIntervalIT {

    @Test
    public void equalsTest() {
        Date start1 = new Date(2020, 11, 3, 18, 0);
        Date end1 = new Date(2020, 11, 3, 19, 0);
        DateInterval i1 = new DateInterval(start1, end1);

        Date start2 = new Date(2020, 11, 3, 18, 0);
        Date end2 = new Date(2020, 11, 3, 19, 0);
        DateInterval i2 = new DateInterval(start2, end2);

        Date start3 = new Date(2020, 11, 3, 18, 0);
        Date end3 = new Date(2020, 11, 3, 20, 0);
        DateInterval i3 = new DateInterval(start3, end3);

        assertTrue(i1.equals(i1));
        assertFalse(i1.equals(start1));
        assertTrue(i1.equals(i2));
        assertFalse(i1.equals(i3));
        assertTrue(i1.equals(new DateInterval(i2)));
    }

    @Test
    public void getStartTest() {
        Date start1 = new Date(2020, 11, 3, 18, 0);
        Date end1 = new Date(2020, 11, 3, 19, 0);
        DateInterval i1 = new DateInterval(start1, end1);

        Date start1copy = new Date(2020, 11, 3, 18, 0);
        assertTrue(i1.getStart().equals(start1));
        assertTrue(i1.getStart().equals(start1copy));
        assertFalse(i1.getStart().equals(end1));
    }

    @Test
    public void getEndTest() {
        Date start1 = new Date(2020, 11, 3, 18, 0);
        Date end1 = new Date(2020, 11, 3, 19, 0);
        DateInterval i1 = new DateInterval(start1, end1);

        Date end1copy = new Date(2020, 11, 3, 19, 0);
        assertTrue(i1.getEnd().equals(end1));
        assertTrue(i1.getEnd().equals(end1copy));
        assertFalse(i1.getEnd().equals(start1));
    }

    @Test
    public void toStringTest() {
        Date start1 = new Date(2020, 11, 3, 18, 0);
        Date end1 = new Date(2020, 11, 3, 19, 0);
        DateInterval i1 = new DateInterval(start1, end1);

        assertEquals(i1.toString(), "3-Dec 6:00:00 PM to 3-Dec 7:00:00 PM");
    }


}
