package name.kuraya.masaki.bbs.ui.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import name.kuraya.masaki.bbs.ui.config.ApiConfig;
import name.kuraya.masaki.bbs.ui.model.input.InputPost;
import name.kuraya.masaki.bbs.ui.model.output.Error;
import name.kuraya.masaki.bbs.ui.model.output.OutputPost;
import name.kuraya.masaki.bbs.ui.model.output.OutputPosts;
import reactor.core.publisher.Mono;

@Service
public class BbsService {

    private final WebClient webClient;

    public BbsService(WebClient.Builder webClientBuilder, ApiConfig apiConfig) {
        String bbsApiUri = apiConfig.getBbsUri().toASCIIString();
        this.webClient = webClientBuilder.baseUrl(bbsApiUri).build();
    }

    public Mono<OutputPost> post(String token, InputPost post) {
        return webClient.post()
            .uri("posts")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .header("Authorization", "Bearer " + token)
            .body(BodyInserters.fromObject(post))
            .exchange()
            .flatMap(r -> {
                if (r.statusCode() == HttpStatus.CREATED) {
                    return r.bodyToMono(PostResponse.class).map(p -> {
                        return new OutputPost(p.getPoster().getName(),p.getModified(),p.getComment());
                    });
                } else {
                    return r.bodyToMono(Error.class).map(e -> {
                        throw new ServiceException(e);
                    });
                }
            });
    }

    public Mono<OutputPosts> findOfRange(String token, int limit, int page) {
        return webClient.get()
            .uri(b -> b.path("posts").queryParam("limit", limit).queryParam("offset", (page - 1) * limit).build())
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .header("Authorization", "Bearer " + token)
            .exchange()
            .flatMap(r -> {
                if (r.statusCode() == HttpStatus.OK) {
                    return r.bodyToMono(PostsResponse.class).map(ps -> {
                        List<OutputPost> posts = ps.getPosts().stream().map(p -> {
                            return new OutputPost(p.getPoster().getName(),p.getModified(),p.getComment());
                        }).collect(Collectors.toList());
                        int total = ps.getMeta().getTotal();
                        return new OutputPosts(posts, total);
                    });
                } else {
                    return r.bodyToMono(Error.class).map(e -> {
                        throw new ServiceException(e);
                    });
                }
            });
    }

    public static class PostsResponse {
        private List<PostResponse> posts;
        private Meta meta;
        public static class Meta {
            private int total;

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }
        }

        public List<PostResponse> getPosts() {
            return posts;
        }

        public void setPosts(List<PostResponse> posts) {
            this.posts = posts;
        }

        public Meta getMeta() {
            return meta;
        }

        public void setMeta(Meta meta) {
            this.meta = meta;
        }
    }

    public static class PostResponse {
        private User poster;
        private String comment;
        private Instant modified;

        public User getPoster() {
            return poster;
        }

        public void setPoster(User poster) {
            this.poster = poster;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public Instant getModified() {
            return modified;
        }

        public void setModified(long modified) {
            this.modified = Instant.ofEpochMilli(modified);
        }

        public static class User {
            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }

}