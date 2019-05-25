package name.kuraya.masaki.bbs.ui.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import name.kuraya.masaki.bbs.ui.config.ApiConfig;
import name.kuraya.masaki.bbs.ui.model.input.Signup;
import name.kuraya.masaki.bbs.ui.model.output.Error;
import name.kuraya.masaki.bbs.ui.model.output.UserInfo;
import reactor.core.publisher.Mono;

@Service
public class AccountService {

    private final WebClient webClient;

    public AccountService(WebClient.Builder webClientBuilder, ApiConfig apiConfig) {
        String authApiUri = apiConfig.getAuthUri().toASCIIString();
        this.webClient = webClientBuilder.baseUrl(authApiUri).build();
    }

    public Mono<UserInfo> signin(String email, String password) {
        return webClient.get()
            .uri("auth")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .header("Authorization", "Basic " +  Base64.getEncoder().encodeToString((email + ":" + password).getBytes(StandardCharsets.UTF_8)))
            .exchange()
            .flatMap(r -> {
                if (r.statusCode() == HttpStatus.OK) {
                    return r.bodyToMono(UserInfo.class);
                } else {
                    return r.bodyToMono(Error.class).map(e -> {
                        throw new ServiceException(e);
                    });
                }
            });
    }

    public Mono<Boolean> signup(Signup signup) {
        return webClient.post()
            .uri("users")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .body(BodyInserters.fromObject(signup))
            .exchange()
            .flatMap(r -> {
                if (r.statusCode() == HttpStatus.CREATED) {
                    return r.bodyToMono(String.class).map(body -> Boolean.TRUE);
                } else {
                    return r.bodyToMono(Error.class).map(e -> {
                        throw new ServiceException(e);
                    });
                }
            });
    }

}