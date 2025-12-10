package sample.application.api.shared.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * An interface to be implemented by classes that represent a JPA Entity.
 * @author Manoel Campos
 */
public interface BaseModel extends Serializable {
    @Nullable
    Long getId();

    @JsonIgnore
    default boolean isEditing(){
        return !isInserting();
    }

    /**
     * @return true or false if the entity is being inserted into the database or not.
     */
    @JsonIgnore
    default boolean isInserting(){
        return getId() == null || getId() == 0;
    }
}
