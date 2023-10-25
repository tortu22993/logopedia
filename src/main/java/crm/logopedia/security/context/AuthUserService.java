package crm.logopedia.security.context;


import crm.logopedia.data.user.model.entity.User;
import crm.logopedia.data.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

/**
 * Clase contenedora de funciones relacionadas con los intentos
 * de inicio de sesión por parte del usuario.
 * 
 * @author Enrique Escalante
 */
@Service
@RequiredArgsConstructor
public class AuthUserService {
    
    /**
     * El número máximo de intentos de inicio de sesión permitidos.
     */
    public static final int MAX_FAILED_ATTEMPTS = 5;

    /**
     * El tiempo en segundos que indica cuánto tiempo está un usuario
     * bloqueado en caso de superar el máximo número de intentos para
     * iniciar sesión.
     */
    public static final long LOCK_TIME_DURATION = 8_640_000; // 24 * 60 * 60 * 1000 = 8.640.000s = 24h

    /**
     * El repositorio de la entidad {@link User}.
     */
    private final UserRepository USER_REPOSITORY;

    /**
     * Obtiene un usuario según su correo electrónico.
     * 
     * @param email El correo electrónico del usuario a obtener
     * @return El usuario con dicho correo electrónico
     */
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return USER_REPOSITORY.findByEmail(email).orElse(null);
    }

    /**
     * Incrementa el número de intentos que ha realizado el usuario
     * para iniciar sesión cuando los datos de autenticación no son
     * correctos.
     * 
     * @param user El usuario que intenta iniciar sesión
     */
    @Transactional
    public void increaseFailedLoginAttempts(User user) {
        USER_REPOSITORY.updateLoginAttempts(
            user.getLoginAttempts() + 1,
            user.getEmail()
        );
    }

    /**
     * Restablece el número de intentos de inicio de sesión
     * del usuario.
     * 
     * @param email El correo electrónico del usuario que ha iniciado sesión
     */
    @Transactional
    public void resetLoginAttempts(String email) {
        USER_REPOSITORY.updateLoginAttempts(0, email);
    }

    /**
     * Bloquea la cuenta de un usuario.
     * 
     * @param user El usuario a bloquear
     */
    @Transactional
    public void lock(User user) {
        user.setLockedAccount(true);
        user.setLockedDate(Calendar.getInstance().getTime());

        USER_REPOSITORY.save(user);
    }

    /**
     * Desbloquea a un usuario cuando ha trascurrido el tiempo necesario
     * para restablecer el estado de su cuenta, es decir, cuando el tiempo
     * actual más el tiempo de bloqueo {@link AuthUserService#LOCK_TIME_DURATION}
     * es menor que el tiempo de la fecha actual.
     * 
     * @param user El usuario que intenta iniciar sesión
     * @return Verdadero si el usuario queda desbloqueado o falso en caso contrario
     */
    public boolean unlockWhenTimeExpires(User user) {
        final Optional<Date> lockedDate = Optional.ofNullable(user.getLockedDate());
        final long lockTimeInMillis = lockedDate.map(Date::getTime).orElse(0L);
        final long currentTimeInMillis = System.currentTimeMillis();

        if(lockTimeInMillis + LOCK_TIME_DURATION < currentTimeInMillis) {
            user.setLockedAccount(false);
            user.setLockedDate(null);
            user.setLoginAttempts(0);

            USER_REPOSITORY.save(user);

            return true;
        }

        return false;
    }

}
