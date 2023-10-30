package crm.logopedia.data.patient.repository;

import crm.logopedia.data.patient.model.entity.Patient;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface PatientRepository extends JpaRepository<Patient, String> {

    @Override
    @NonNull
    <S extends Patient> Page<S> findAll(@NonNull Example<S> example, @NonNull Pageable pageable);
}
