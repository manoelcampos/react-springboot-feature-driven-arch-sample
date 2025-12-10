package sample.application.api.feature.city;

import io.github.manoelcampos.dtogen.DTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import sample.application.api.config.ConstraintKeys;
import sample.application.api.feature.state.State;
import sample.application.api.shared.model.AbstractBaseModel;

import java.util.Objects;

/// A City somewhere in the world.
/// See [AbstractBaseModel] for the reason why all atributes are public.
/// @author Manoel Campos
@Entity
@Table (uniqueConstraints = {
    @UniqueConstraint(name = ConstraintKeys.UC_CITY_NAME, columnNames = "name"),
})
@DTO
public class City extends AbstractBaseModel {
    @NotNull @NotBlank
    public String name;

    /**
     * The country state where the city is.
     */
    @NotNull
    @JoinColumn(foreignKey = @ForeignKey(name = ConstraintKeys.FK_CITY__STATE))
    @ManyToOne
    @DTO.MapToId
    public State state;

    public City() {}

    public City(final long id) {
        super(id);
    }

    public City(final String name) {
        this.name = name;
    }

    public City(final String name, final State state) {
        this.name = name;
        this.state = state;
    }

    public City(final long id, final String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        System.out.println("Getting city.name: " + name);
        return name;
    }

    public void setName(final String descricao) {
        System.out.println("Setting city.name: " + descricao);

        if(Objects.requireNonNullElse(descricao, "").isBlank())
            throw new IllegalArgumentException("Descrição da cidade não pode ser vazia");

        this.name = descricao;
    }
}
