package crm.logopedia.data.document.repository;

import crm.logopedia.data.document.model.entity.Document;
import org.springframework.data.repository.CrudRepository;

public interface DocumentRepository extends CrudRepository<Document, Long> {
}
