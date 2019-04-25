package software.kloud.kmscore.util;

import org.springframework.stereotype.Component;
import software.kloud.ChromPluginSDK.NavBarEntity;
import software.kloud.ChromPluginSDK.NavBarLinkRegister;

import java.util.Collections;
import java.util.List;

@Component
public class NoOpNavbarRegister implements NavBarLinkRegister {
    @Override
    public List<NavBarEntity> register() {
        return Collections.emptyList();
    }
}
