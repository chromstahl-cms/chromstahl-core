package software.kloud.kmscore.config;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import software.kloud.kms.entities.OperationJpaRecord;
import software.kloud.kms.entities.RoleJpaRecord;
import software.kloud.kms.repositories.OperationRepository;
import software.kloud.kms.repositories.RoleRepository;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadyInitialized = false;
    private final OperationRepository operationRepository;
    private final RoleRepository roleRepository;

    public InitialDataLoader(OperationRepository operationRepository, RoleRepository roleRepository) {
        this.operationRepository = operationRepository;
        this.roleRepository = roleRepository;
    }

    @SuppressWarnings("unused")
    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadyInitialized) return;

        var everythingOperation = createOperationIfNotExists("*");

        var adminRole = createRoleIfNotExists("ROLE_ADMIN", List.of(everythingOperation));
        // TODO: Figure out operations
        var userRole = createRoleIfNotExists("ROLE_USER", Collections.emptyList());
        var guestRole = createRoleIfNotExists("ROLE_GUEST", Collections.emptyList());
        alreadyInitialized = true;
    }

    @Transactional
    public OperationJpaRecord createOperationIfNotExists(String id) {
        return operationRepository.findById(id).orElseGet(() -> {
            var op = new OperationJpaRecord(id);
            operationRepository.save(op);
            return op;
        });
    }

    @Transactional
    public RoleJpaRecord createRoleIfNotExists(String id, List<OperationJpaRecord> operations) {
        return roleRepository.findById(id).orElseGet(() -> {
            var role = new RoleJpaRecord(id, operations);
            roleRepository.save(role);
            return role;
        });
    }
}
