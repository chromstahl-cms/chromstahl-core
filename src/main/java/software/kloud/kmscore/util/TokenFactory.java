package software.kloud.kmscore.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.kloud.kmscore.persistence.entities.TokenJpaRecord;
import software.kloud.kmscore.persistence.entities.UserJpaRecord;
import software.kloud.kmscore.persistence.repositories.TokenRepository;
import software.kloud.kmscore.persistence.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@Service
public class TokenFactory {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Autowired
    public TokenFactory(TokenRepository tokenRepository, UserRepository userRepository) {
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

        // TODO Use silver here
        tokenRepository.save(token);
        userRepository.save(userJpaRecord);

        return token;
    }

    private String generateToken() {
        return RandomStringUtils.randomAlphanumeric(512, 512);
    }
}

