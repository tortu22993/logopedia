package crm.logopedia.data.document.model.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentDto {

    /**
     * El ID del documento.
     */
    private Long id;

    private Long patient;

    /**
     * El nombre único del documento.
     */
    private String uniqueName;

    /**
     * El nombre original del documento.
     */
    private String originalName;

    /**
     * El usuario creador del documento (nombre de usuario).
     */
    private String createdBy;

    /**
     * El último usuario modificador del documento (nombre de usuario).
     */
    private String lastModifiedBy;

    /**
     * La fecha de creación del documento.
     */
    private Date createdAt;

    /**
     * La fecha de última modificación del documento.
     */
    private Date lastModifiedAt;

}
