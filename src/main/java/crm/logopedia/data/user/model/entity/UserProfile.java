package crm.logopedia.data.user.model.entity;



import crm.logopedia.data.jobposition.model.entity.JobPosition;
import crm.logopedia.util.ExtendedStringUtils;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Entidad que hace referencia a los perfiles de los usuarios de la aplicación.
 * Se mapea contra la tabla <strong>PerfilesUsuarios</strong> en la base de datos.
 *
 * @author Enrique Escalante
 */
@Entity
@Table(name = "PerfilesUsuarios")
@Data
@NoArgsConstructor
public class UserProfile implements Serializable {

    /**
     * El ID del perfil del usuario.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * El nombre del usuario.
     */
    @Column(name = "nombre", length = 30, nullable = false)
    private String name;

    /**
     * El primer apellido del usuario.
     */
    @Column(name = "primerApellido", length = 30, nullable = false)
    private String middleName;

    /**
     * El segundo apellido del usuario.
     */
    @Column(name = "segundoApellido", length = 30)
    private String lastName;

    /**
     * El nombre completo del usuario.
     */
    @Transient
    private String fullName;

    /**
     * Las funciones del usuario.
     */
    @Column(name = "funciones", length = 200)
    private String duties;

    /**
     * El número de teléfono móvil (trabajo) del usuario.
     */
    @Column(name = "movil", length = 15)
    private String phoneNumber;

    /**
     * El N.A.F. del usuario.
     */
    @Column(length = 12)
    private String naf;

    /**
     * El D.N.I. del usuario.
     */
    @Column(length = 9)
    private String dni;

    // TODO: campo 'image' (imagen)


    /**
     * El puesto de trabajo del usuario.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "puestoTrabajoId")
    private JobPosition jobPosition;

    /**
     * Ejecuta una serie de instrucciones después de cargar la
     * entidad en una consulta a la base de datos.
     */
    @PostLoad
    public void onPostLoad() {
        fullName = ExtendedStringUtils.concatWithSpaces(
            name,
            middleName,
            lastName
        );
    }

}
