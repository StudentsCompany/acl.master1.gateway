package acl.master1.gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.google.common.net.HttpHeaders;

import acl.master1.gateway.service.JwtService;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config>{

	@Autowired
	private RouterValidator routerValidator;
	
	//@Autowired
	//private RestTemplate restTemplate;
	
	@Autowired
	private JwtService jwtService;
	
	public AuthenticationFilter() {
		super(Config.class);
	}
	
	@Override
	public GatewayFilter apply(Config config) {
		// <artifactId>spring-boot-starter-webflux</artifactId> : this dependance is very important
		
		return ((exchange, chain)->{
			// Check first the request we are getting
			if(routerValidator.isSecure.test(exchange.getRequest())){
				// Check Second : Is the header contain a token ?
				if( ! exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
					throw new RuntimeException ("Missing authorization in he header");
				}
				
				String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
				
				if(authHeader != null && authHeader.startsWith("Bearer ")) {
					authHeader = authHeader.substring(7); // We remove the 7 first chars and return the rest
					
					System.out.println("TOKEN+++"+authHeader);
				}
				
				try {
					// Now he can check the validation of the token in the header
					//restTemplate.getForObject("http://acl.master1.user//validateToken?token=" + authHeader, String.class);
					// We can notice that we give an incomplete url. It is not important since the application is register in the gateway
					// This method is not secure. 
					/**
					 * Here a another method : 
					 * - Create another class in the gateway : JwtService. Now in this class
					 * - Copy the same secret in user micro service 
					 * - Copy the same getSignKey() method
					 * - Copy the same validateToken() method. 
					 * - Now inject this class here in this class with autowired and class validateToken() method instead of restTemplate.getForObject(...°
					 */ 
					jwtService.validateToken(authHeader);
				} catch (Exception e) {
					System.out.println("Invalide Access");
					throw new RuntimeException("un authorized access to the app");
				}
			}
			
			return chain.filter(exchange);
		});
		
	}
	
	public static class Config{
		
	}

}
