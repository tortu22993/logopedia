package crm.logopedia.util.abstraction;

import crm.logopedia.data.user.model.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;

/**
 * Abstracción de una entidad que contiene todos los campos
 * comunes a todas las demás entidades, pero sin mapearse
 * contra una tabla de la base de datos.
 * 
 * @author Enrique Escalante
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class AbstractAuditableEntity implements Serializable {
    
    /**
     * El usuario creador de la entidad.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nombreUsuarioCreador", updatable = false, nullable = false)
    @CreatedBy
    protected User createdBy;

    /**
     * El último usuario modificador de la entidad.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nombreUsuarioUltimoModificador", nullable = false)
    @LastModifiedBy
    protected User lastModifiedBy;

    /**
     * La fecha de creación de la entidad.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    @Column(name = "fechaCreacion", nullable = false, updatable = false)
    protected Date createdAt;

    /**
     * La última fecha de modificación de la entidad.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    @Column(name = "fechaUltimaModificacion", nullable = false)
    protected Date lastModifiedAt;

}
