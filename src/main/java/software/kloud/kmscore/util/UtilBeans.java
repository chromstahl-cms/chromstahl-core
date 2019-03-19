package software.kloud.kmscore.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import software.kloud.silver.client.CommunicationClient;

@Component
public class UtilBeans {
    @Value("${silver.key}")
    private String key;
    @Value("${silver.url}")
    private String url;

    @Bean
    public CommunicationClient getSilverClient() {
        return new CommunicationClient(url, key);
    }
}
