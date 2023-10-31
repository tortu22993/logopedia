package crm.logopedia.data.patient.model.entity;

import crm.logopedia.data.role.model.entity.Role;
import crm.logopedia.data.services.model.entity.Services;
import crm.logopedia.util.ExtendedStringUtils;
import crm.logopedia.util.abstraction.AbstractAuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Pacientes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Patient extends AbstractAuditableEntity {

    /**
     * El ID del paciente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * El nombre del paciente.
     */
    @Column(name = "nombre", length = 30, nullable = false)
    private String name;

    /**
     * El primer apellido del paciente.
     */
    @Column(name = "primerApellido", length = 30, nullable = false)
    private String middleName;

    /**
     * El segundo apellido del paciente.
     */
    @Column(name = "segundoApellido", length = 30)
    private String lastName;

    /**
     * El nombre completo del paciente.
     */
    @Transient
    private String fullName;

    /**
     * La fecha de nacimiento del paciente
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "fechaNacimiento", nullable = false)
    private Date birthdate;

    /**
     * El nombre completo del paciente.
     */
    @Transient
    private Integer age;

    /**
     * El nombre del colegio del paciente
     */
    @Column(name = "colegio", length = 50)
    private String school;

    /**
     * Las observaciones del paciente.
     */
    @Column(name = "observaciones", length = 500)
    private String observations;

    /**
     * Si el paciente se encuentra en activo o no
     */
    @Column(name = "activo", nullable = false)
    private Boolean enabled;

    /**
     * Si el paciente tiene la ley de proteccion de datos firmada o no
     */
    @Column(name = "lopd", nullable = false)
    private Boolean lopd;

    /**
     * Si el paciente tiene el consentimiento de coordinacion con el colegio
     */
    @Column(name = "consentimientoCoordinacionEscuelas", nullable = false)
    private Boolean schoolCordination;

    //TODO CONTACT, SESSIONS, DOCUMENTS, IMAGE


    /**
     * Los servicios del paciente.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "PacienteServicios",
            joinColumns = @JoinColumn(name = "pacienteId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "servicioId", referencedColumnName = "id"),
            uniqueConstraints = {
                    @UniqueConstraint(columnNames = { "pacienteId", "servicioId" })
            }
    )
    @Builder.Default
    private Set<Services> services = new HashSet<>();

    /**
     * Acción que se ejecuta al eliminar el registro de la base de datos.
     */
    @PreRemove
    public void onPreRemove() {
        services.clear();
    }

    /**
     * Añade un rol a la colección de service del paciente.
     *
     * @param service El servicio a añadir
     */
    public void addRole(Services service) {
        services.add(service);
    }

    /**
     * Elimina un rol a la colección de service del paciente.
     *
     * @param service El servicio a eliminar
     */
    public void removeRole(Services service) {
        services.remove(service);
    }

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

        Calendar nacimiento = Calendar.getInstance();
        nacimiento.setTime(birthdate);
        Calendar hoy = Calendar.getInstance();
        age = hoy.get(Calendar.YEAR)- nacimiento.get(Calendar.YEAR);
        if (hoy.get(Calendar.DAY_OF_YEAR) < nacimiento.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
    }


}
