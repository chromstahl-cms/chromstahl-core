package software.kloud.kmscore.util;

import org.springframework.stereotype.Service;
import software.kloud.ChromPluginSDK.RoleService;
import software.kloud.kms.entities.RoleJpaRecord;
import software.kloud.kms.repositories.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public RoleJpaRecord getAdminRole() {
        return this.getRoleFor("ADMIN");
    }

    @Override
    public RoleJpaRecord getUserRole() {
        return this.getRoleFor("USER");
    }

    @Override
    public RoleJpaRecord getGuestRole() {
        return this.getRoleFor("GUEST");
    }

    @Override
    public RoleJpaRecord getRoleFor(String s) {
        var sanitizedRole = s.toUpperCase();
        var fullRoleName = String.format("ROLE_%s", sanitizedRole);

        return roleRepository.findById(fullRoleName).orElse(null);
    }
}
