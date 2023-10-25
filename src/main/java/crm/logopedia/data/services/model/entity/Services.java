package crm.logopedia.data.services.model.entity;

import crm.logopedia.util.abstraction.AbstractAuditableEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name= "Servicios")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Services extends AbstractAuditableEntity{

    /**
     * El ID del equipo.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * El nombre del servicio médico.
     */
    @Column(name = "nombre", nullable = false, unique = true)
    private String name;
}
