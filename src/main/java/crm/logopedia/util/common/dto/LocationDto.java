package crm.logopedia.util.common.dto;

import crm.logopedia.util.ExtendedStringUtils;
import crm.logopedia.util.api.GoogleMapsApi;
import crm.logopedia.util.contract.Locatable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Contiene la estructura y métodos útiles relativos a la dirección
 * de una entidad.
 *
 * @author Enrique Escalante
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationDto implements Locatable {

    /**
     * El país en la que está la dirección.
     */
    @NotBlank
    private String country;

    /**
     * La provincia en la que está la dirección.
     */
    @NotBlank
    private String province;
    

    /**
     * El nombre del municipio o ciudad en la que está la dirección.
     */
    @NotBlank
    private String municipality;

    /**
     * La dirección de la dirección.
     */
    @NotBlank
    @Size(max = 255)
    private String address;

    /**
     * El código postal de la dirección de la dirección.
     */
    @NotBlank
    @Size(max = 5)
    private String zipCode;

    @Override
    public final String getLocation() {
        return ExtendedStringUtils.concatWithSpaces(
            address,
            zipCode,
            province,
            municipality,
            country
        );
    }

    @Override
    public final String getFormattedLocation() {
        return ExtendedStringUtils.concatWithBreakLines(
            address,
            ExtendedStringUtils.concatWithSpaces(
                zipCode,
                province,
                municipality
            ),
            country
        );
    }

    @Override
    public final String getGoogleMapsLocationSearchLink() {
        return GoogleMapsApi.search(getLocation());
    }

    @Override
    public final String getGoogleMapsLocationEmbedLink() {
        return GoogleMapsApi.embed(getLocation());
    }

}
