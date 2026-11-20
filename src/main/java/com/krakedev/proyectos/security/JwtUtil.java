package com.krakedev.proyectos.security;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

public class JwtUtil {

	private static final String SECRET = "ClaveSecretaProyectosKrakedev2026";
	private static final String ISSUER = "api-proyectos-krakedev";
	private static final long EXPIRATION_TIME = 1000L * 60 * 60 * 2; // 2 horas

	public static String generarToken(String username, String rol) {
		Algorithm algorithm = Algorithm.HMAC256(SECRET);

		return JWT.create()
				.withIssuer(ISSUER)
				.withSubject(username)
				.withClaim("rol", rol)
				.withIssuedAt(new Date())
				.withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.sign(algorithm);
	}

	public static DecodedJWT validarToken(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(SECRET);
			JWTVerifier verifier = JWT.require(algorithm)
					.withIssuer(ISSUER)
					.build();

			return verifier.verify(token);
		} catch (JWTVerificationException e) {
			return null;
		}
	}

}