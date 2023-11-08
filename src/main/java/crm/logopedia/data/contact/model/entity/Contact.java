package crm.logopedia.data.contact.model.entity;

import crm.logopedia.data.patient.model.entity.Patient;
import crm.logopedia.util.ExtendedStringUtils;
import crm.logopedia.util.abstraction.AbstractAuditableEntity;
import jakarta.persistence.*;
import lombok.*;


/**
 * Entidad que hace referencia a los contactos de la aplicación.
 * Se mapea contra la tabla <strong>Contactos</strong> en la base de datos.
 *
 * @author Enrique Escalante
 */
@Entity
@Table(name = "Contactos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Contact extends AbstractAuditableEntity {
    /**
     * El ID del contacto.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;


    /**
     * El paciente al que está asociado el contacto.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pacienteId", nullable = false, updatable = false)
    private Patient patient;

    /**
     * El nombre del contacto.
     */
    @Column(name = "nombre", length = 30, nullable = false)
    private String name;

    /**
     * El primer apellido del contacto.
     */
    @Column(name = "primerApellido", length = 30, nullable = false)
    private String middleName;

    /**
     * El segundo apellido del contacto.
     */
    @Column(name = "segundoApellido", length = 30)
    private String lastName;

    /**
     * El nombre completo del contacto.
     */
    @Transient
    private String fullName;

    /**
     * El parentesco del contacto.
     */
    @Column(name = "parentesco", length = 60)
    private String parentesque;

    /**
     * El teléfono del contacto.
     */
    @Column(name = "telefono", length = 15)
    private String telephone;

    /**
     * El teléfono móvil del contacto.
     */
    @Column(name = "movil", length = 15)
    private String mobilePhone;

    /**
     * El correo electrónico del contacto.
     */
    @Column(name = "correoElectronico")
    private String email;


    /**
     * El municipio o ciudad del contacto.
     */
    @Column(name = "municipio", nullable = false)
    private String municipality;

    /**
     * La provincia del contacto.
     */
    @Column(name = "provincia", nullable = false)
    private String province;

    /**
     * El país del contacto.
     */
    @Column(name = "pais", nullable = false)
    private String country;

    /**
     * La dirección del contacto.
     */
    @Column(name = "direccion", nullable = false)
    private String address;

    /**
     * El código postal de la dirección del contacto.
     */
    @Column(name = "codigoPostal", length = 5, nullable = false)
    private String zipCode;



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
