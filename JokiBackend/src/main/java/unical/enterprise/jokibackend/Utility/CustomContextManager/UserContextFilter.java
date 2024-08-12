// package unical.enterprise.jokibackend.Utility.CustomContextManager;

// import com.nimbusds.jwt.JWTClaimsSet;
// import com.nimbusds.jwt.SignedJWT;
// import jakarta.servlet.Filter;
// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.ServletRequest;
// import jakarta.servlet.ServletResponse;
// import jakarta.servlet.http.HttpServletRequest;
// import java.io.IOException;

// public class UserContextFilter implements Filter {

// //    @Override
// //    public void init(FilterConfig filterConfig) throws ServletException {}

//     @Override
//     public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//         try {
// //            TODO: GESTIRE BENE ECCEZIONE

//             String token = ((HttpServletRequest) request).getHeader("Authorization");
//             assert token != null;

//             token = token.replace("Bearer ", "");

//             UserContext context = new UserContext();
//             SignedJWT signedJWT = SignedJWT.parse(token);
//             JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

//             context.setScope(claims.getStringClaim("scope"));
//             context.setSid(claims.getStringClaim("sid"));
//             context.setEmailVerified(claims.getBooleanClaim("email_verified"));
//             context.setName(claims.getStringClaim("name"));
//             context.setPreferredUsername(claims.getStringClaim("preferred_username"));
//             context.setGivenName(claims.getStringClaim("given_name"));
//             context.setFamilyName(claims.getStringClaim("family_name"));
//             context.setEmail(claims.getStringClaim("email"));

//             UserContextHolder.setContext(context);

//             chain.doFilter(request, response);
//         }catch (Exception e) {
//             System.out.println("Errore: " + e.getMessage());
//         } finally {
//             UserContextHolder.clearContext();
//         }
//     }

// //    @Override
// //    public void destroy() {}
// }