package io.jmix.usermgmt.graphql;

import com.google.common.base.Preconditions;
import io.jmix.core.*;
import io.jmix.graphql.loader.GraphQLEntityDataFetcher;
import io.jmix.graphql.loader.GraphQLEntityDataFetcherContext;
import io.jmix.graphql.loader.GraphQLEntityListDataFetcher;
import io.jmix.graphql.loader.GraphQLEntityListDataFetcherContext;
import io.jmix.security.model.ResourcePolicy;
import io.jmix.security.model.ResourcePolicyType;
import io.jmix.security.role.ResourceRoleRepository;
import io.jmix.securitydata.entity.ResourceRoleEntity;
import io.jmix.usermgmt.entity.ResourceRole;
import io.jmix.usermgmt.mapper.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ResourceRoleController implements GraphQLEntityListDataFetcher<ResourceRole>,
        GraphQLEntityDataFetcher<ResourceRole> {

    @Autowired
    protected ResourceRoleRepository roleRepository;
    @Autowired
    protected ResourceRoleMapper resourceRoleMapper;
    @Autowired
    protected EntityPolicyMapper entityPolicyMapper;
    @Autowired
    protected AttributePolicyMapper attributePolicyMapper;
    @Autowired
    protected SpecificPolicyMapper specificPolicyMapper;
    @Autowired
    protected ScreenPolicyMapper screenPolicyMapper;
    @Autowired
    protected MenuPolicyMapper menuPolicyMapper;
    @Autowired
    protected DataManager dataManager;

    @Override
    public ResourceRole loadEntity(GraphQLEntityDataFetcherContext<ResourceRole> context) {
        String id = (String) context.getLoadContext().getId();
        Preconditions.checkNotNull(id);
        UUID uuid = getUUID(id);

        String roleCode = null;
        if (uuid != null) {
            ResourceRoleEntity resourceRoleEntity = dataManager.load(ResourceRoleEntity.class).id(uuid)
                    .fetchPlan(FetchPlan.LOCAL)
                    .optional()
                    .orElse(null);

            if (resourceRoleEntity == null) {
                return null;
            }

            roleCode = resourceRoleEntity.getCode();
        } else {
            roleCode = id;
        }

        io.jmix.security.model.ResourceRole src = roleRepository.findRoleByCode(roleCode);
        if (src != null) {
            return mapRoleToDtoForLoadingById(src);
        }

        return null;
    }

    @Override
    public List<ResourceRole> loadEntityList(GraphQLEntityListDataFetcherContext<ResourceRole> context) {
        Stream<ResourceRole> stream = roleRepository.getAllRoles().stream()
                .map(resourceRoleMapper::mapToDto);

        Comparator<ResourceRole> comparator = getComparator(context.getLoadContext());
        if (comparator != null) {
            stream = stream.sorted(comparator);
        }

        return stream.collect(Collectors.toList());
    }

    @Nullable
    private UUID getUUID(String value) {
        try {
            return UuidProvider.fromString(value);
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    private Comparator<ResourceRole> getComparator(LoadContext<ResourceRole> loadContext) {
        Comparator<ResourceRole> comparator = null;
        if (loadContext.getQuery() != null) {
            Sort sort = loadContext.getQuery().getSort();
            //noinspection ConstantConditions
            if (sort != null && !sort.getOrders().isEmpty()) {
                Sort.Order order = sort.getOrders().get(0);
                if ("code".equals(order.getProperty())) {
                    comparator = Comparator.comparing(ResourceRole::getCode);
                } else if ("name".equals(order.getProperty())) {
                    comparator = Comparator.comparing(ResourceRole::getName);
                }

                if (comparator != null && order.getDirection() == Sort.Direction.DESC) {
                    comparator = comparator.reversed();
                }
            }
        }
        return comparator;
    }

    private ResourceRole mapRoleToDtoForLoadingById(io.jmix.security.model.ResourceRole src) {
        ResourceRole dst = resourceRoleMapper.mapToDto(src);

        dst.setEntityPolicies(getEntityResourcePolicies(src.getResourcePolicies())
                .map(entityPolicyMapper::mapToDto)
                .collect(Collectors.toList()));

        dst.setEntityAttributePolicies(getEntityAttributeResourcePolicies(src.getResourcePolicies())
                .map(attributePolicyMapper::mapToDto)
                .collect(Collectors.toList()));

        dst.setSpecificPolicies(getSpecificResourcePolicies(src.getResourcePolicies())
                .map(specificPolicyMapper::mapToDto)
                .collect(Collectors.toList()));

        dst.setScreenPolicies(getScreenResourcePolicies(src.getResourcePolicies())
                .map(screenPolicyMapper::mapToDto)
                .collect(Collectors.toList()));

        dst.setMenuPolicies(getMenuResourcePolicies(src.getResourcePolicies())
                .map(menuPolicyMapper::mapToDto)
                .collect(Collectors.toList()));

        return dst;
    }

    private Stream<ResourcePolicy> getEntityResourcePolicies(Collection<ResourcePolicy> policies) {
        return policies.stream().
                filter(resourcePolicy -> ResourcePolicyType.ENTITY.equals(resourcePolicy.getType()));
    }

    private Stream<ResourcePolicy> getEntityAttributeResourcePolicies(Collection<ResourcePolicy> policies) {
        return policies.stream().
                filter(resourcePolicy -> ResourcePolicyType.ENTITY_ATTRIBUTE.equals(resourcePolicy.getType()));
    }

    private Stream<ResourcePolicy> getSpecificResourcePolicies(Collection<ResourcePolicy> policies) {
        return policies.stream().
                filter(resourcePolicy -> ResourcePolicyType.SPECIFIC.equals(resourcePolicy.getType()));
    }

    private Stream<ResourcePolicy> getScreenResourcePolicies(Collection<ResourcePolicy> policies) {
        return policies.stream().
                filter(resourcePolicy -> StringUtils.startsWith(resourcePolicy.getResource(), "frontend_screen:"));
    }

    private Stream<ResourcePolicy> getMenuResourcePolicies(Collection<ResourcePolicy> policies) {
        return policies.stream().
                filter(resourcePolicy -> StringUtils.startsWith(resourcePolicy.getResource(), "frontend_menu:"));
    }
}
