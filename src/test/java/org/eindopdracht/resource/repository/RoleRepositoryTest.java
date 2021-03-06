package org.eindopdracht.resource.repository;

import org.eindopdracht.resource.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringJUnitWebConfig(classes = org.eindopdracht.configuration.DatabaseConfigTest.class)
@Transactional
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    private Role role;

    @BeforeEach
    void setUp() {
        this.role = new Role();
        role.setRole("test");
    }

    @Test
    @DisplayName("Testing if getRoles() returns a list of users")
    public void testMethodGetRoles() {
        List<Role> roles = roleRepository.getRoles();
        assertNotNull(roles);
    }

    @Test
    @DisplayName("Testing if getRole retrieves the right user")
    public void testMethodGetRole() {
        Role role = roleRepository.getRole(1);
        assertEquals("user", role.getRole());
    }

    @Test
    @DisplayName("Testing if postRole has posted the right user")
    public void testMethodPostRole() {
        Role role = roleRepository.postRole(this.role);
        assertEquals("test", role.getRole());
    }

    @Test
    @DisplayName("Testing if putRole changed the entry")
    public void testMethodPutRole() {
        roleRepository.putRole(this.role, 1);
        Role roletest = roleRepository.getRole(1);
        assertEquals("test", roletest.getRole());
    }

    @Test
    @DisplayName("Testing if deleteRole works")
    public void testMethodDeleteRole() {
        roleRepository.deleteRole(1);
    }
}
