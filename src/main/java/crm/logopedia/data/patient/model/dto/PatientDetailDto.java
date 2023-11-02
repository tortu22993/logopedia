package crm.logopedia.data.patient.model.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientDetailDto {

    private Long id;

    /**
     * El nombre del paciente.
     */
    @NotBlank
    @Size(max = 30)
    private String name;

    /**
     * El primer apellido del paciente.
     */
    @NotBlank
    @Size(max = 30)
    private String middleName;

    /**
     * El segundo apellido del paciente.
     */
    @Size(max = 30)
    private String lastName;

    /**
     * El nombre completo del paciente.
     */
    private String fullName;

    private String school;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthdate;

    private String age;

    private String observations;

    private boolean enabled;

    private boolean lopd;

    private boolean schoolCordination;

    /**
     * Los ID de los roles del paciente.
     */
    @NotEmpty
    @Builder.Default
    private Long[] servicesId = new Long[] {};

    /**
     * Los servicios (en texto plano) del paciente, es decir, todos los nombres
     * de los servicios del usuario contenidos en una cadena de texto.
     */
    private String plainServices;

    /**
     * El nombre de usuario del usuario creador del paciente.
     */
    private String createdByUsername;

    /**
     * El nombre completo del usuario creador del paciente.
     */
    private String createdByFullName;

    /**
     * El nombre de usuario del último usuario modificador del paciente.
     */
    private String lastModifiedByUsername;

    /**
     * El nombre completo del último usuario modificador del paciente.
     */
    private String lastModifiedByFullName;

    /**
     * La fecha de creación del paciente.
     */
    private Date createdAt;

    /**
     * La última fecha de modificación del paciente.
     */
    private Date lastModifiedAt;

}
