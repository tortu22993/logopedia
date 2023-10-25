package crm.logopedia.util.provider.jwt;

import crm.logopedia.util.contract.TokenManagement;
import io.jsonwebtoken.*;
import io.jsonwebtoken.gson.io.GsonSerializer;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

/**
 * Implementa las funcionalidades de un gestor de tokens.
 * Concretamente, para JWT.
 *
 * @author Enrique Escalante
 */
@Component
@RequiredArgsConstructor
public class JwtProvider implements TokenManagement {

	/**
	 * El secreto para tokens JWT.
	 */
	@Value("${jwt.secret}")
	protected String jwtSecret;

	@Override
	public String getSubjectFrom(String token) {
		final var claims = getClaims(token);

		if(claims != null) {
			return claims.getSubject();
		}

		return null;
	}

	@Override
	public Date getExpirationDateFrom(String token) {
		final var claims = getClaims(token);

		if(claims != null) {
			return claims.getExpiration();
		}

		return null;
	}

	@Override
	public Instant getExpirationTimeFrom(String token) {
		final var expirationTime = getExpirationDateFrom(token);

		if(expirationTime != null) {
			return expirationTime.toInstant();
		}

		return null;
	}

	@Override
	public String create(String subject, Instant expirationTime) {
		return Jwts.builder()
			.serializeToJsonWith(new GsonSerializer<>())
			.setSubject(subject)
			.setExpiration(Date.from(expirationTime))
			.signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), SignatureAlgorithm.HS256)
			.compact();
	}

	@Override
	public boolean isValid(String token) {
		final var expirationTime = getExpirationTimeFrom(token);

		if(expirationTime != null) {
			return Instant.now().isBefore(expirationTime);
		}

		return false;
	}

	/**
	 * Obtiene el analizador de un token JWT con base al secreto.
	 *
	 * @return El analizado de un token JWT
	 */
	private JwtParser getParser() {
		return Jwts.parserBuilder()
			.setSigningKeyResolver(new JwtSigningKeyResolver(jwtSecret))
			.build();
	}

	/**
	 * Obtiene las reclamaciones del analizador de un token JWT.
	 *
	 * @param token El token JWT a partir del cual se obtendrán las reclamaciones
	 * @return Las reclamaciones del token JWT
	 */
	private Claims getClaims(String token) {
		Claims claims;

		try {
			claims = getParser().parseClaimsJws(token).getBody();
		} catch (Exception e) {
			claims = null;
		}

		return claims;
	}

	/**
	 * Permite verificar si la firma de un token JWT es válida.
	 *
	 * @author Enrique Escalante
	 */
	@RequiredArgsConstructor
	public static class JwtSigningKeyResolver implements SigningKeyResolver {

		/**
		 * El secreto que se utilizará para resolver la firma de los tokens JWT.
		 */
		private final String SECRET;

		@Override
		public Key resolveSigningKey(JwsHeader header, Claims claims) {
			return Keys.hmacShaKeyFor(SECRET.getBytes());
		}

		@Override
		public Key resolveSigningKey(JwsHeader header, String plaintext) {
			return Keys.hmacShaKeyFor(SECRET.getBytes());
		}

	}

}
