package software.kloud.kmscore.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import software.kloud.KMSPluginSDK.NavBarLinkRegister;
import software.kloud.KMSPluginSDK.RoleService;
import software.kloud.kms.entities.RoleJpaRecord;
import software.kloud.kms.repositories.UserRepository;
import software.kloud.kmscore.dto.NavBarLinkDTO;

import java.util.List;
import java.util.stream.Collectors;

@Controller()
@RequestMapping("/navbar")
public class NavBarController extends software.kloud.KMSPluginSDK.AbsController {
    private final List<NavBarLinkRegister> navBarLinkRegisterList;
    private final UserRepository userRepository;
    private final RoleService roleService;

    public NavBarController(
            List<NavBarLinkRegister> navBarLinkRegisterList,
            UserRepository userRepository,
            RoleService roleService
    ) {
        this.navBarLinkRegisterList = navBarLinkRegisterList;
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @GetMapping("/links")
    public ResponseEntity<List<NavBarLinkDTO>> getNavBarLinks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (null == authentication) {
            List<NavBarLinkDTO> links = getLinksForRole(roleService.getGuestRole());
            return ResponseEntity.ok(links);
        }

        var user = userRepository.findByUserName(getUsernameFromPrincipal(authentication)).orElseThrow(() ->
                new SecurityException(String.format("not able to map user: %s to database user",
                        getUsernameFromPrincipal(authentication))));

        var userRoles = user.getRoleJpaRecords();

        List<NavBarLinkDTO> userLinks = userRoles
                .stream()
                .flatMap(role -> getLinksForRole(role).stream())
                .collect(Collectors.toList());

        return ResponseEntity.ok(userLinks);
    }

    private List<NavBarLinkDTO> getLinksForRole(RoleJpaRecord role) {
        return navBarLinkRegisterList.stream()
                .flatMap(n -> n.register().stream())
                .filter(link -> link.getRole().getId().equals(role.getId()))
                .map(NavBarLinkDTO::new)
                .collect(Collectors.toList());
    }
}
