package filtros;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

/**
 * Servlet Filter implementation class LoginFilter
 */

@WebFilter(urlPatterns = "*")
public class LoginFilter implements Filter {

	/**
	 * Default constructor.
	 */
	String key = "mi_clave";

	public LoginFilter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		if (req.getMethod().equalsIgnoreCase("OPTIONS")) { // Para atajar la request prefligth de cors
			res.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
			res.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS, HEAD, PATCH ");
			res.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
			res.setStatus(javax.servlet.http.HttpServletResponse.SC_ACCEPTED);
		} else {
			String[] splitPath = req.getRequestURI().split("/");

			String path = "";

			if (splitPath.length > 0) {
				path = splitPath[splitPath.length - 1];
			}
			if ("autenticacion".equals(path) || "users".equals(path)) {
				
				chain.doFilter(req, response);	// sigue la cadena de ejecucion hacia el login o register
				
			} else {
				// valido token

				String token = req.getHeader("Authorization");
	
				try {
					Jwts.parser().setSigningKey("mi_clave").parseClaimsJws(token);
					chain.doFilter(req, response);
				} catch (ExpiredJwtException e) {
					System.out.println(
							"El token ya no es valido, expiro su tiempo de validez: " + token + " " + e.getMessage());
					((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				} // esto implica desloguearme, el deslogueo solo lo tengo implementado en el
					// frontend (borrar de localstorage token)
				catch (SignatureException e) {
					System.out.println("No es un token valido, token: " + token + " " + e.getMessage());
					((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				} catch (Exception e) {
					System.out.println("No es un token valido, token: " + token + " " + e.getMessage());
					((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				}
			}
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
