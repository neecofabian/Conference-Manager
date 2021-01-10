package com.conference.backend.data.utils.base;

import java.io.Serializable;
import java.util.Objects;

/**
 * Abstract with id and version property. Serves as a base
 * class for entities.
 *
 * <p>
 *      Provides {@code hashCode()}, {@code equals(...)}
 * </p>
 */
public abstract class AbstractEntity implements Serializable {
    private String id;

    private int version;

    /**
     * Get the id of this {@link AbstractEntity}
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Set the id for this {@link AbstractEntity}
     *
     * @param id the string used to set the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get the version of this {@link AbstractEntity}
     *
     * @return the version
     */
    public int getVersion() {
        return version;
    }

    /**
     * Set the version for this {@link AbstractEntity}
     *
     * @param version the version to set
     */
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * Gets the hashcode value for the id of this {@link AbstractEntity}
     *
     * @return the hashcode value
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Compares this {@link AbstractEntity} with another
     *
     * @param other the other entity to be compared
     * @return {@code true} if both {@link AbstractEntity}s are equal
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }

        AbstractEntity that = (AbstractEntity) other;
        return this.version == that.version && this.id.equals(that.id);
    }
}
