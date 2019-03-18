package software.kloud.kmscore.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.kloud.kmscore.persistence.security.entities.TokenJpaRecord;
import software.kloud.kmscore.persistence.security.entities.UserJpaRecord;
import software.kloud.kmscore.persistence.security.repositories.TokenRepository;
import software.kloud.kmscore.persistence.security.repositories.UserRepository;
import software.kloud.silver.client.CommunicationClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@Service
public class TokenFactory {
    private static final Logger logger = LoggerFactory.getLogger(TokenFactory.class);

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final CommunicationClient silver;

    @Autowired
    public TokenFactory(TokenRepository tokenRepository, UserRepository userRepository, CommunicationClient silver) {
        this.silver = silver;
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    public TokenJpaRecord generateToken(UserJpaRecord user) {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_MONTH, 1);

        return this.generateToken(user, calendar.getTime());
    }

    @SuppressWarnings("WeakerAccess")
    public TokenJpaRecord generateToken(UserJpaRecord userJpaRecord, Date expiry) {
        TokenJpaRecord token = new TokenJpaRecord();
        token.setUser(userJpaRecord);
        token.setExpiryDate(expiry);
        token.setToken(generateToken());

        if (userJpaRecord.getTokens() == null) {
            userJpaRecord.setTokens(new ArrayList<>());
        }

        userJpaRecord.getTokens().add(token);

        // TODO reliably handle this.
        try {
            silver.save(token);
            silver.save(userJpaRecord);
        } catch (IOException e) {
            logger.error("Could not save to silver! Check connection and Silver status");
            tokenRepository.save(token);
            userRepository.save(userJpaRecord);
            e.printStackTrace();
        }


        return token;
    }

    private String generateToken() {
        return RandomStringUtils.randomAlphanumeric(512, 512);
    }
}

