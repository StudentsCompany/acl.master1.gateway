package acl.master1.gateway.service;

import java.security.Key;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

public static final String SECRET = "JeSuisLeSelDeLaTerreUneVilleSitueeSurUneMontagneLaLumiereDuMonde";
	
	
	public Jws<Claims> validateToken(final String token){
		Jws<Claims> claimsJws = Jwts.parserBuilder()
				.setSigningKey(getSignKey())
				.build().parseClaimsJws(token);
		return claimsJws;		
	}
	
	
	/**
	 * This method sign our secret key
	 * @return
	 */
	private Key getSignKey() {
		byte [] keyBytes = Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(keyBytes); 
	}
}
