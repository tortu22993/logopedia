package crm.logopedia.security.service;

import crm.logopedia.data.user.model.entity.User;
import crm.logopedia.data.user.repository.UserRepository;
import crm.logopedia.security.context.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del servicio de autenticación de usuarios.
 * 
 * @author Enrique Escalante
 */
@Component
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {
    
    /**
     * El repositorio de la entidad {@link User}.
     */
    private final UserRepository USER_REPOSITORY;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final var user = USER_REPOSITORY.findByEmailWithProfile(email).orElse(null);
        final List<GrantedAuthority> authorities = new ArrayList<>();

        if(user == null) {
            throw new UsernameNotFoundException("El usuario con el correo electrónico '" + email + "' no existe.");
        }

        if(!user.getEnabled()) {
            throw new UsernameNotFoundException("El usuario con el correo electrónico '" + email + "' tiene la cuenta deshabilitada.");
        }

        for(final var role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        if(authorities.isEmpty()) {
            throw new UsernameNotFoundException("El usuario no tiene roles asignados.");
        }

        return new AuthenticatedUser(
            user.getEmail(),
            user.getPassword(),
            user.getEnabled(),
            true,
            true,
            !user.getLockedAccount(),
            authorities,
            user
        );
    }
    
}
