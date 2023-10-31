package crm.logopedia.data.patient.repository.specification;

import crm.logopedia.data.patient.model.entity.Patient;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class PatientSpecification implements Specification<Patient> {

    private final String name;
    private final String school;

    public PatientSpecification(String name, String school) {
        this.school = school;
        this.name = name;
    }
    @Override
    public Predicate toPredicate(Root<Patient> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Predicate predicate = criteriaBuilder.disjunction();

        if (name != null && !name.isEmpty()) {
            predicate = criteriaBuilder.or(
                    predicate,
                    criteriaBuilder.like(root.get("name"), "%" + name + "%"),
                    criteriaBuilder.like(root.get("school"), "%" + name + "%")
            );
        } else {
            predicate = criteriaBuilder.or(
                    predicate,
                    criteriaBuilder.equal(root.get("name"), root.get("name"))
            );

        }

        return predicate;
    }
}
