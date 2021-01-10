package com.conference.backend.conference_and_rooms.entities;

import com.conference.backend.data.utils.base.AbstractEntity;

import java.util.*;

/**
 * {@link AbstractEntity} plus start and end date used for {@link ConferenceEvent} times
 *
 */
public class DateInterval extends AbstractEntity implements Comparable<DateInterval> {
    private static final long serialVersionUID = 3936436254753483L;

    private Date start;
    private Date end;

    /**
     * Constructs a new instance with the given data.
     *
     * @param start The start time of the associated event (inclusive)
     * @param end The end time of the associated event (inclusive)
     *
     */
    public DateInterval(Date start, Date end) {
        this.start = start;
        this.end = end;
        this.setId(this.hashCode() + "");
    }

    /**
     * Copy constructor.
     *
     * @param other
     *            The instance to copy
     */
    public DateInterval(DateInterval other) {
        this(other.getStart(), other.getEnd());
        this.setId(other.getId());
    }

    /**
     * Constructs a new instance without given data. Ensure that the object's id is eventually set
     */
    public DateInterval() {}

    /**
     * Checks if this object is equal to another object
     *
     * @param o other objects
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        DateInterval other = (DateInterval) o;
        return start.equals(other.start)
                && end.equals(other.end);
    }

    /**
     * Returns a unique int based on the start time and end time of the {@link DateInterval}
     *
     * @return a unique int based on the start time and end time of the {@link DateInterval}
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), start.getTime(), end.getTime());
    }

    /**
     * Returns the string representation of this {@link DateInterval}
     *
     * @return the string representation of this {@link DateInterval}
     */
    @Override
    public String toString() {
        return  start.toLocaleString().replace("-" + (start.getYear() + 1900),"")
                + " to "
                + end.toLocaleString().replace("-" + (end.getYear() + 1900), "");
    }

    /**
     * Returns the the start Date
     *
     * @return the the start Date
     */
    public Date getStart() {
        Date d = new Date();
        d.setTime(start.getTime());
        return d;
    }

    /**
     * Returns the the end Date
     *
     * @return the the end Date
     */
    public Date getEnd() {
        Date d = new Date();
        d.setTime(end.getTime());
        return d;
    }

    /**
     * Compares this DateInterval and another DateInterval's start time
     *
     * @param o the other DateInterval object to compare this DateInterval to
     * @return 0 if start times are equal, -1 if this DateInterval's start time is before o's start time,
     * or 1 if this DateInterval's start time is after o's start time
     */
    @Override
    public int compareTo(DateInterval o) {
        int val = this.getStart().before(o.getStart()) ? -1 : 1;
        if (this.getStart() == o.getStart()) {
            return 0;
        }
        return val;
    }

}