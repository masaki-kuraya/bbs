package name.kuraya.masaki.bbs.ui.config;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("bbs.api")
@Component
public class ApiConfig {

    private URI bbsUri;
    private URI authUri;

    public URI getBbsUri() {
        return bbsUri;
    }

    public void setBbsUri(String bbsUri) {
        this.bbsUri = URI.create(bbsUri);
    }

    public URI getAuthUri() {
        return authUri;
    }

    public void setAuthUri(String authUri) {
        this.authUri = URI.create(authUri);
    }
}