package crm.logopedia.data.contact.model.dto;

import crm.logopedia.util.abstraction.AbstractListDto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactListDto extends AbstractListDto {

    /**
     * El ID del contacto.
     */
    private Long id;

    /**
     * El nombre del contacto.
     */
    private String fullName;

    /**
     * El parentesco del contacto.
     */
    private String parentesque;


    /**
     * El correo electrónico del contacto.
     */
    private String email;

    /**
     * El teléfono del contacto.
     */
    private String telephone;

    /**
     * El teléfono móvil del contacto.
     */
    private String mobilePhone;

    @Override
    public ContactListDto convertBlankToNull() {
        return (ContactListDto) super.convertBlankToNull();
    }

}
