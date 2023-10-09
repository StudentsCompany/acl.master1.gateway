package acl.master1.gateway.filter;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;


@Component
public class RouterValidator {

	/**
	 * With this method, we are telling he gateway that if the request come 
	 * from the routes in the list, then ignore it or bypass it
	 */
	public static final List<String> openApiEndPoints = List.of(
				"/user/register",
				"user/token",
				"/eureka"
			);
	
	public Predicate<ServerHttpRequest> isSecure = 
			request -> openApiEndPoints
				.stream()
				.noneMatch(uri -> request.getURI().getPath().contains(uri));
}
