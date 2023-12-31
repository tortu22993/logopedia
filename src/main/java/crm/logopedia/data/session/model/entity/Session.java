package crm.logopedia.data.session.model.entity;

import crm.logopedia.data.patient.model.entity.Patient;
import crm.logopedia.data.services.model.entity.Services;
import crm.logopedia.data.user.model.entity.User;
import crm.logopedia.util.abstraction.AbstractAuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.Date;

@Entity
@Table(name = "Sesiones")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Session extends AbstractAuditableEntity {
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
    @JoinColumn(name = "pacienteId", nullable = false)
    private Patient patient;

    /**
     * El servicio al que está asociado a la sesion.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servicioId", nullable = false)
    private Services service;

    /**
     * El usuario propietario de la sesion.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nombreUsuarioPropietario")
    private User owner;

    /**
     * Asunto de la sesion
     */
    @JoinColumn(name = "asunto", nullable = false)
    private String subject;

    /**
     * Detalles de la sesion
     */
    @JoinColumn(name = "detalles", nullable = false)
    private String details;

    /**
     * Fecha de la sesion
     */
    @JoinColumn(name = "fechaSesion", nullable = false)
    private Date sessionDate;

    /**
     * La cantidad de tiempo
     */
    @Column(name = "hora", nullable = false)
    private Date hour;

    //TODO estadoSession, facturacion, tipoSession

}
