package crm.logopedia.data.role.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

/**
 * Entidad que hace referencia a los roles que posee un usuario.
 * Se mapea contra la tabla <strong>Roles</strong> en la base de datos.
 * 
 * @author Enrique Escalante
 */
@Entity
@Table(name = "Roles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role implements Serializable {
    
    /**
     * El ID del rol.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * El nombre del rol.
     */
    @Column(name = "nombre", length = 30, nullable = false, updatable = false, unique = true)
    @NotBlank
    @Size(max = 30)
    private String name;

    /**
     * El nombre plano del rol, es decir, sin el sufijo ROLE_.
     */
    @Transient
    private String plainName;

    /**
     * Ejecuta una serie de instrucciones después de cargar la
     * entidad en una consulta a la base de datos.
     */
    @PostLoad
    public void onPostLoad() {
        plainName = name.replace("ROLE_", "");
    }

}
