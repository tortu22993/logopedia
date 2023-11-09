package crm.logopedia.data.session.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SessionDetailDto {

    /**
     * El ID de la session
     */
    private Long id;

    /**
     * El id del paciente
     */
    @NotNull
    private Long patientId;

    /**
     * El nombre del paciente
     */
    private String patientName;

    /**
     * El ID del servicio asociado a la sesion.
     */
    @NotBlank
    private String serviceId;

    /**
     * El nombre del servicio asociado a la sesion.
     */
    private String serviceName;

    /**
     * El asunto de la session.
     */
    @NotBlank
    private String subject;

    /**
     * El nombre de usuario del propietario de la session.
     */
    @NotBlank
    private String ownerUsername;

    /**
     * El nombre completo del propietario de la session.
     */
    private String ownerFullName;

    /**
     * Detalles de la session o informe
     */
    @Size(max=1000)
    private String details;

    /**
     * Fecha de la session
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date sessionDate;

    /**
     * Hora de la session
     */
    @DateTimeFormat(pattern = "HH:mm")
    private Date hour;

    /**
     * El nombre de usuario del usuario creador del caso.
     */
    private String createdByUsername;

    /**
     * El nombre completo del usuario creador del caso.
     */
    private String createdByFullName;

    /**
     * El nombre de usuario del último usuario modificador del caso.
     */
    private String lastModifiedByUsername;

    /**
     * El nombre completo del último usuario modificador del caso.
     */
    private String lastModifiedByFullName;

    /**
     * La fecha de creación del caso.
     */
    private Date createdAt;

    /**
     * La última fecha de modificación del caso.
     */
    private Date lastModifiedAt;

}
