//package br.com.onetalk.infrastucture.config;
//
//import io.quarkus.security.identity.IdentityProviderManager;
//import io.quarkus.security.identity.SecurityIdentity;
//import io.quarkus.security.runtime.QuarkusSecurityIdentity;
//import io.quarkus.vertx.http.runtime.security.ChallengeData;
//import io.quarkus.vertx.http.runtime.security.HttpAuthenticationMechanism;
//import io.smallrye.jwt.auth.principal.JWTParser;
//import io.smallrye.mutiny.Uni;
//import io.vertx.core.http.HttpHeaders;
//import io.vertx.core.http.HttpServerResponse;
//import io.vertx.core.json.JsonObject;
//import io.vertx.ext.web.RoutingContext;
//import jakarta.enterprise.context.ApplicationScoped;
//import jakarta.inject.Inject;
//import lombok.SneakyThrows;
//import org.eclipse.microprofile.jwt.JsonWebToken;
//
//import java.util.*;
//
//@ApplicationScoped
//public class CookieAuthMechanism implements HttpAuthenticationMechanism {
//
//    @Inject
//    JWTParser jwtParser;
//
//    @Override
//    public Uni<SecurityIdentity> authenticate(RoutingContext context, IdentityProviderManager identityProviderManager) {
//        if (!context.request().path().equals("/auth/login") &&
//                !context.request().path().equals("/auth/register") &&
//                !context.request().path().equals("/auth/logout")) {
//
//            String cookieValue = null;
//            if (context.request().getCookie("access_token") != null) {
//                cookieValue = context.request().getCookie("access_token").getValue();
//            }
//
//            if (cookieValue != null && isValidToken(cookieValue)) {
//                JsonObject decodedToken = decodeJwt(cookieValue);
//
//                String email = decodedToken.getString("email");
//                List<String> roles = List.copyOf(decodedToken.getJsonArray("roles").getList());
//
//                QuarkusSecurityIdentity identity = QuarkusSecurityIdentity.builder()
//                        .setPrincipal(() -> email)
//                        .addRoles(new TreeSet<>(roles))
//                        .build();
//                return Uni.createFrom().item(identity);
//            }
//            return Uni.createFrom().nullItem();
//        }
//        return Uni.createFrom().nullItem();
//    }
//
//
//    @Override
//    public Uni<ChallengeData> getChallenge(RoutingContext context) {
//        HttpServerResponse response = context.response();
//        response.setStatusCode(401);
//        response.putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
//        response.end("{\"message\": \"Autenticação falhou. Cookie inválido ou ausente.\"}");
//
//        ChallengeData challengeData = new ChallengeData(
//                401,
//                "WWW-Authenticate",
//                "Bearer realm=\"OneTalk\""
//        );
//
//        return Uni.createFrom().item(challengeData);
//    }
//
//    private boolean isValidToken(String token) {
//        try {
//            jwtParser.parse(token);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    @SneakyThrows
//    public JsonObject decodeJwt(String token) {
//        try {
//            JsonWebToken jwt = jwtParser.parse(token);
//
//            String email = jwt.getClaim("email") != null ? jwt.getClaim("email").toString() : null;
//            List<String> roles = jwt.getGroups() != null ? new ArrayList<>(jwt.getGroups()) : new ArrayList<>();
//
//            return JsonObject.of("email", email, "roles", roles);
//        } catch (Exception e) {
//            return JsonObject.of("email", null, "roles", new HashSet<>());
//        }
//    }
//
//}