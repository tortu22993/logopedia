package crm.logopedia.data.user.model.entity;

import crm.logopedia.data.role.model.entity.Role;
import crm.logopedia.util.ExtendedStringUtils;
import crm.logopedia.util.abstraction.AbstractAuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad que hace referencia a los usuarios de la aplicación.
 * Se mapea contra la tabla <strong>Usuarios</strong> en la base de datos.
 * 
 * @author Enrique Escalante
 */
@Entity
@Table(name = "Usuarios")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends AbstractAuditableEntity {

    /**
     * El nombre de usuario del usuario (actúa como ID).
     */
    @Id
    @Column(name = "nombreUsuario", length = 50, updatable = false)
    @EqualsAndHashCode.Include
    private String username;

    /**
     * El correo electrónico del usuario.
     */
    @Column(name = "correoElectronico", nullable = false, updatable = false, unique = true)
    private String email;

    /**
     * La contraseña del usuario.
     */
    private String password;

    /**
     * El perfil del usuario.
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "perfilId", nullable = false, unique = true)
    private UserProfile profile;

    /**
     * Indica si el usuario está habilitado o no.
     */
    @Column(name = "habilitado", nullable = false)
    private Boolean enabled;

    /**
     * Indica si la cuenta del usuario está bloqueada o no.
     */
    @Column(name = "cuentaBloqueada", nullable = false)
    private Boolean lockedAccount;

    /**
     * Indica el número de veces que el usuario ha intentado
     * iniciar sesión.
     */
    @Column(name = "intentosInicioSesion", nullable = false)
    private Integer loginAttempts;

    /**
     * La fecha en la que el usuario queda bloqueado por superar
     * el número máximo de intentos de inicio de sesión permitidos.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fechaBloqueo")
    private Date lockedDate;

    /**
     * Indica si la cuenta del usuario ha sido verificada por correo electrónico.
     * Esto significa que la configuración mínima requerida para que el usuario
     * pueda utilizar la aplicación está finalizada.
     */
    @Column(name = "cuentaVerificada", nullable = false)
    private Boolean verifiedAccount;



    /**
     * Los roles del usuario.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "UsuariosRoles",
        joinColumns = @JoinColumn(name = "nombreUsuario", referencedColumnName = "nombreUsuario"),
        inverseJoinColumns = @JoinColumn(name = "rolId", referencedColumnName = "id"),
        uniqueConstraints = {
            @UniqueConstraint(columnNames = { "nombreUsuario", "rolId" })
        }
    )
    @Builder.Default
    private Set<Role> roles = new HashSet<>();


    /**
     * Acción que se ejecuta al crear (persistir por primera vez) el
     * registro en la base de datos.
     */
    @PrePersist
    public void onPrePersist() {
        username = ExtendedStringUtils.substringBefore(email, "@");
        enabled = false;
        lockedAccount = false;
        loginAttempts = 0;
    }

    /**
     * Acción que se ejecuta al guardar el registro existente
     * en la base de datos.
     */
    @PreUpdate
    public void onPreUpdate() {
        if(loginAttempts == null) {
            loginAttempts = 0;
        }
    }

    /**
     * Acción que se ejecuta al eliminar el registro de la base de datos.
     */
    @PreRemove
    public void onPreRemove() {
        roles.clear();
    }

    /**
     * Añade un rol a la colección de roles del usuario.
     *
     * @param rol El rol a añadir
     */
    public void addRole(Role rol) {
        roles.add(rol);
    }

    /**
     * Elimina un rol a la colección de roles del usuario.
     *
     * @param rol El rol a eliminar
     */
    public void removeRole(Role rol) {
        roles.remove(rol);
    }



}
