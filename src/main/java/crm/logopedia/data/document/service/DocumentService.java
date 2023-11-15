package crm.logopedia.data.document.service;

import crm.logopedia.data.document.model.dto.DocumentDto;

public interface DocumentService {

    /**
     * Obtiene un documento en función de su ID.
     *
     * @param id El ID del documento a obtener
     * @return El DTO del documento con dicho ID
     */
    DocumentDto findById(Long id);

    /**
     * Guarda un documento en base al DTO recibido
     *
     * @param documentDto El DTO a trasformar en entidad para guardar
     * @return El DTO del documento guardado
     */
    DocumentDto save(DocumentDto documentDto);
}
