package crm.logopedia.data.services.service;


import crm.logopedia.data.services.model.dto.ServicesDetailDto;
import crm.logopedia.data.services.model.dto.ServicesListDto;
import crm.logopedia.data.services.model.entity.Services;
import crm.logopedia.data.services.repository.ServicesRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServicesServiceImpl implements ServicesService{

    /**
     * El repositorio de la entidad {@link Services}.
     */
    private final ServicesRepository SERVICE_REPOSITORY;

    /**
     * El gestor de mapeado de objetos.
     */
    private final ModelMapper MODEL_MAPPER;

    /**
     * Ejecuta una serie de instrucciones justo después de construir el componente.
     * TODO: entidad a DetailDto y viceversa
     */
    @PostConstruct
    private void onPostConstruct() {
        // Entidad a ListDTO
        MODEL_MAPPER.map(Services.class, ServicesListDto.class);

        // Entidad a DetailDTO
        MODEL_MAPPER.typeMap(Services.class, ServicesDetailDto.class).addMappings(mapper -> {
            mapper.map(source -> source.getCreatedBy().getUsername(), ServicesDetailDto::setCreatedByUsername);
            mapper.map(source -> source.getCreatedBy().getProfile().getFullName(), ServicesDetailDto::setCreatedByFullName);
            mapper.map(source -> source.getLastModifiedBy().getUsername(), ServicesDetailDto::setLastModifiedByUsername);
            mapper.map(source -> source.getLastModifiedBy().getProfile().getFullName(), ServicesDetailDto::setLastModifiedByFullName);
        });

        //DetailDTO a Entidad
        MODEL_MAPPER.map(ServicesDetailDto.class, Services.class);
    }

    @Override
    @Transactional(readOnly = true)
    public ServicesDetailDto findById(Long id) {
        return SERVICE_REPOSITORY.findById(id)
                .map(this::convertToDetailDto)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServicesListDto> findAll() {
        return SERVICE_REPOSITORY.findAll().stream()
                .map(this::convertToListDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ServicesListDto> findByFilter(ServicesListDto servicesListDto, Pageable pageable) {
        final var example = Example.of(
                convertToEntity(servicesListDto),
                ExampleMatcher.matching()
                        .withIgnoreCase()
                        .withIgnoreNullValues()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
        );

        final var pagination = SERVICE_REPOSITORY.findAll(example, pageable);
        final var services = pagination.getContent().stream()
                .map(this::convertToListDto)
                .collect(Collectors.toList());

        return new PageImpl<>(services, pageable, pagination.getTotalElements());
    }

    @Override
    public ServicesDetailDto save(ServicesDetailDto servicesDetailDto) {
        final var services = convertToEntity(servicesDetailDto);
        final var savedServices = SERVICE_REPOSITORY.save(services);

        return convertToDetailDto(savedServices);
    }

    /**
     * Transforma una instancia de la entidad {@link Services}
     * en el DTO {@link ServicesListDto}.
     *
     * @param services La entidad a convertir
     * @return Una nueva instancia del DTO con los datos de la entidad
     */
    private ServicesListDto convertToListDto(final Services services) {
        return MODEL_MAPPER.map(services, ServicesListDto.class);
    }

    /**
     * Transforma una instancia del DTO {@link ServicesListDto}
     * en la entidad {@link Services}.
     *
     * @param servicesListDto El DTO a convertir
     * @return Una instancia de la entidad con los datos del DTO
     */
    private Services convertToEntity(final ServicesListDto servicesListDto) {
        return MODEL_MAPPER.map(servicesListDto, Services.class);
    }

    /**
     * Transforma una instancia de la entidad {@link Services}
     * en el DTO {@link ServicesDetailDto}.
     *
     * @param services La entidad a convertir
     * @return Una nueva instancia del DTO con los datos de la entidad
     */
    private ServicesDetailDto convertToDetailDto(final Services services) {
        return MODEL_MAPPER.map(services, ServicesDetailDto.class);
    }

    /**
     * Transforma una instancia del DTO {@link ServicesDetailDto}
     * en la entidad {@link Services}.
     *
     * @param servicesDetailDto El DTO a convertir
     * @return Una instancia de la entidad con los datos del DTO
     */
    private Services convertToEntity(final ServicesDetailDto servicesDetailDto) {
        return MODEL_MAPPER.map(servicesDetailDto, Services.class);
    }
}
