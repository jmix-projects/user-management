package io.jmix.usermgmt.graphql;

import com.google.common.base.Preconditions;
import io.jmix.core.*;
import io.jmix.graphql.loader.GraphQLEntityDataFetcher;
import io.jmix.graphql.loader.GraphQLEntityDataFetcherContext;
import io.jmix.graphql.loader.GraphQLEntityListDataFetcher;
import io.jmix.graphql.loader.GraphQLEntityListDataFetcherContext;
import io.jmix.graphql.modifier.GraphQLUpsertEntityDataFetcher;
import io.jmix.graphql.modifier.GraphQLUpsertEntityDataFetcherContext;
import io.jmix.security.role.ResourceRoleRepository;
import io.jmix.security.role.RowLevelRoleRepository;
import io.jmix.security.role.assignment.RoleAssignment;
import io.jmix.security.role.assignment.RoleAssignmentRepository;
import io.jmix.security.role.assignment.RoleAssignmentRoleType;
import io.jmix.securitydata.entity.ResourceRoleEntity;
import io.jmix.securitydata.entity.RoleAssignmentEntity;
import io.jmix.securitydata.entity.RowLevelRoleEntity;
import io.jmix.usermgmt.entity.ResourceRole;
import io.jmix.usermgmt.entity.RowLevelRole;
import io.jmix.usermgmt.entity.UserRoleAssignments;
import io.jmix.usermgmt.mapper.ResourceRoleMapper;
import io.jmix.usermgmt.mapper.RowLevelRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.*;

@Component
public class UserAssignmentsController implements GraphQLEntityListDataFetcher<UserRoleAssignments>,
        GraphQLEntityDataFetcher<UserRoleAssignments>,
        GraphQLUpsertEntityDataFetcher<UserRoleAssignments> {

    @Autowired
    private RoleAssignmentRepository roleAssignmentRepository;
    @Autowired
    protected ResourceRoleRepository resourceRoleRepository;
    @Autowired
    protected RowLevelRoleRepository rowLevelRoleRepository;
    @Autowired
    protected ResourceRoleMapper resourceRoleMapper;
    @Autowired
    protected RowLevelRoleMapper rowLevelRoleMapper;
    @Autowired
    protected DataManager dataManager;
    @Autowired
    protected Metadata metadata;

    @Override
    public UserRoleAssignments loadEntity(GraphQLEntityDataFetcherContext<UserRoleAssignments> context) {
        String username = (String) context.getLoadContext().getId();
        return loadAssignmentsByUsername(username);
    }

    @Override
    public List<UserRoleAssignments> loadEntityList(GraphQLEntityListDataFetcherContext<UserRoleAssignments> context) {
        return Collections.emptyList();
    }

    @Override
    public UserRoleAssignments importEntities(GraphQLUpsertEntityDataFetcherContext<UserRoleAssignments> context) {
        UserRoleAssignments userAssignments = context.getEntities().get(0);

        String username = userAssignments.getId();

        List<RoleAssignmentEntity> assignments = dataManager.load(RoleAssignmentEntity.class)
                .query("e.username = :username")
                .parameter("username", username)
                .list();

        SaveContext saveContext = new SaveContext();

        initResourceRoles(userAssignments);
        initRowLevelRoles(userAssignments);

        createResourceRoleAssignments(userAssignments, assignments, saveContext);
        createRowLevelRoleAssignments(userAssignments, assignments, saveContext);

        removeAssignments(userAssignments, assignments, saveContext);

        dataManager.save(saveContext);

        return loadAssignmentsByUsername(username);
    }

    private void initResourceRoles(UserRoleAssignments userAssignments) {
        for (ResourceRole resourceRole : userAssignments.getResourceRoles()) {
            UUID uuid = getUUID(resourceRole.getId());
            if (uuid != null) {
                ResourceRoleEntity resourceRoleEntity = dataManager.load(ResourceRoleEntity.class).id(uuid)
                        .fetchPlan(FetchPlan.LOCAL)
                        .optional()
                        .orElse(null);
                if (resourceRoleEntity != null) {
                    resourceRole.setCode(resourceRoleEntity.getCode());
                } else {
                    continue;
                }
            } else {
                resourceRole.setCode(resourceRole.getId());
            }
        }
    }

    private void initRowLevelRoles(UserRoleAssignments userAssignments) {
        for (RowLevelRole rowLevelRole : userAssignments.getRowLevelRoles()) {
            UUID uuid = getUUID(rowLevelRole.getId());
            if (uuid != null) {
                RowLevelRoleEntity rowLevelRoleEntity = dataManager.load(RowLevelRoleEntity.class).id(uuid)
                        .fetchPlan(FetchPlan.LOCAL)
                        .optional()
                        .orElse(null);
                if (rowLevelRoleEntity != null) {
                    rowLevelRole.setCode(rowLevelRoleEntity.getCode());
                } else {
                    continue;
                }
            } else {
                rowLevelRole.setCode(rowLevelRole.getId());
            }
        }
    }

    private void createResourceRoleAssignments(UserRoleAssignments userAssignments, List<RoleAssignmentEntity> assignments, SaveContext saveContext) {
        for (ResourceRole resourceRole : userAssignments.getResourceRoles()) {
            createAssignmentIfNotExists(userAssignments.getId(), assignments, saveContext, RoleAssignmentRoleType.RESOURCE, resourceRole.getCode());
        }
    }

    private void createRowLevelRoleAssignments(UserRoleAssignments userAssignments, List<RoleAssignmentEntity> assignments, SaveContext saveContext) {
        for (RowLevelRole rowLevelRole : userAssignments.getRowLevelRoles()) {
            createAssignmentIfNotExists(userAssignments.getId(), assignments, saveContext, RoleAssignmentRoleType.ROW_LEVEL, rowLevelRole.getCode());
        }
    }

    private void removeAssignments(UserRoleAssignments userAssignments, List<RoleAssignmentEntity> assignments, SaveContext saveContext) {
        for (RoleAssignmentEntity assignment : assignments) {
            if (RoleAssignmentRoleType.RESOURCE.equals(assignment.getRoleType())) {
                boolean hasRole = userAssignments.getResourceRoles().stream()
                        .anyMatch(role -> Objects.equals(assignment.getRoleCode(), role.getCode()));
                if (!hasRole) {
                    saveContext.removing(assignment);
                }
            } else if (RoleAssignmentRoleType.ROW_LEVEL.equals(assignment.getRoleType())) {
                boolean hasRole = userAssignments.getRowLevelRoles().stream()
                        .anyMatch(role -> Objects.equals(assignment.getRoleCode(), role.getCode()));
                if (!hasRole) {
                    saveContext.removing(assignment);
                }
            }
        }
    }

    private void createAssignmentIfNotExists(String username,
                                             List<RoleAssignmentEntity> assignments, SaveContext saveContext, String type, String roleCode) {
        boolean hasRole = assignments.stream()
                .anyMatch(assignment -> type.equals(assignment.getRoleType())
                        && Objects.equals(assignment.getRoleCode(), roleCode));

        if (!hasRole) {
            RoleAssignmentEntity assignment = metadata.create(RoleAssignmentEntity.class);
            assignment.setRoleType(type);
            assignment.setRoleCode(roleCode);
            assignment.setUsername(username);

            saveContext.saving(assignment);
        }
    }


    private UserRoleAssignments loadAssignmentsByUsername(String username) {
        Preconditions.checkNotNull(username);

        List<ResourceRole> resourceRoles = new ArrayList<>();
        List<RowLevelRole> rowLevelRoles = new ArrayList<>();

        for (RoleAssignment assignment : roleAssignmentRepository.getAssignmentsByUsername(username)) {
            if (RoleAssignmentRoleType.RESOURCE.equals(assignment.getRoleType())) {
                io.jmix.security.model.ResourceRole role = resourceRoleRepository.findRoleByCode(assignment.getRoleCode());
                if (role != null) {
                    resourceRoles.add(resourceRoleMapper.mapToDto(role));
                }
            } else if (RoleAssignmentRoleType.ROW_LEVEL.equals(assignment.getRoleType())) {
                io.jmix.security.model.RowLevelRole role = rowLevelRoleRepository.findRoleByCode(assignment.getRoleCode());
                if (role != null) {
                    rowLevelRoles.add(rowLevelRoleMapper.mapToDto(role));
                }
            }
        }

        UserRoleAssignments userRoleAssignments = metadata.create(UserRoleAssignments.class);
        userRoleAssignments.setId(username);
        userRoleAssignments.setResourceRoles(resourceRoles);
        userRoleAssignments.setRowLevelRoles(rowLevelRoles);

        return userRoleAssignments;
    }

    @Nullable
    private UUID getUUID(String value) {
        try {
            return UuidProvider.fromString(value);
        } catch (Exception e) {
            return null;
        }
    }
}
