package crm.logopedia.data.document.model.entity;

import crm.logopedia.data.document.model.dto.DocumentDto;
import crm.logopedia.data.patient.model.entity.Patient;
import crm.logopedia.data.user.model.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "Documentos")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Document implements Serializable {

    /**
     * El ID del documento.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * El nombre único del documento.
     */
    @Column(name = "nombreUnico", nullable = false, updatable = false, unique = true)
    private String uniqueName;

    /**
     * El paciente al que está asociado el contacto.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pacienteId", nullable = false)
    private Patient patient;

    /**
     * El nombre original del documento.
     */
    @Column(name = "nombreOriginal", nullable = false, updatable = false)
    @NotBlank
    private String originalName;

    /**
     * La ruta física en la que se guarda el documento.
     */
    @Column(name = "rutaFisica", nullable = false, updatable = false)
    private String path;

    /**
     * El usuario creador del documento.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creadorId", updatable = false, nullable = false)
    @CreatedBy
    private User createdBy;

    /**
     * El último usuario modificador del documento.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ultimoModificadorId", nullable = false)
    @LastModifiedBy
    private User lastModifiedBy;

    /**
     * La fecha de creación del documento.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    @Column(name = "fechaCreacion", nullable = false, updatable = false)
    private Date createdAt;

    /**
     * La última fecha de modificación del documento.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    @Column(name = "fechaUltimaModificacion", nullable = false)
    private Date lastModifiedDate;

    /**
     * Transforma una instancia de la entidad {@link Document}
     * en el DTO {@link DocumentDto}.
     *
     * @return Una nueva instancia del DTO con los datos de la entidad
     */
    public final DocumentDto convertToDto() {
        return DocumentDto.builder()
                .id(id)
                .patient(patient.getId())
                .uniqueName(uniqueName)
                .originalName(originalName)
                .createdBy(createdBy.getUsername())
                .lastModifiedBy(lastModifiedBy.getUsername())
                .createdAt(createdAt)
                .lastModifiedAt(lastModifiedDate)
                .build();
    }

}