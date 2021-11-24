package io.jmix.usermgmt.graphql;

import com.google.common.base.Preconditions;
import io.jmix.core.*;
import io.jmix.graphql.loader.GraphQLEntityDataFetcher;
import io.jmix.graphql.loader.GraphQLEntityDataFetcherContext;
import io.jmix.graphql.loader.GraphQLEntityListDataFetcher;
import io.jmix.graphql.loader.GraphQLEntityListDataFetcherContext;
import io.jmix.security.role.RowLevelRoleRepository;
import io.jmix.securitydata.entity.RowLevelRoleEntity;
import io.jmix.usermgmt.entity.RowLevelRole;
import io.jmix.usermgmt.mapper.RowLevelPolicyMapper;
import io.jmix.usermgmt.mapper.RowLevelRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class RowLevelRoleController implements GraphQLEntityListDataFetcher<RowLevelRole>,
        GraphQLEntityDataFetcher<RowLevelRole> {

    @Autowired
    protected RowLevelRoleRepository roleRepository;
    @Autowired
    protected RowLevelRoleMapper rowLevelRoleMapper;
    @Autowired
    protected RowLevelPolicyMapper rowLevelPolicyMapper;
    @Autowired
    protected DataManager dataManager;

    @Override
    public RowLevelRole loadEntity(GraphQLEntityDataFetcherContext<RowLevelRole> context) {
        String id = (String) context.getLoadContext().getId();
        Preconditions.checkNotNull(id);
        UUID uuid = getUUID(id);

        String roleCode = null;
        if (uuid != null) {
            RowLevelRoleEntity rowLevelRoleEntity = dataManager.load(RowLevelRoleEntity.class).id(uuid)
                    .fetchPlan(FetchPlan.LOCAL)
                    .optional()
                    .orElse(null);

            if (rowLevelRoleEntity == null) {
                return null;
            }

            roleCode = rowLevelRoleEntity.getCode();
        } else {
            roleCode = id;
        }

        io.jmix.security.model.RowLevelRole src = roleRepository.findRoleByCode(roleCode);
        if (src != null) {
            return mapRoleToDtoForLoadingById(src);
        }

        return null;
    }

    @Override
    public List<RowLevelRole> loadEntityList(GraphQLEntityListDataFetcherContext<RowLevelRole> context) {
        Stream<RowLevelRole> stream = roleRepository.getAllRoles().stream()
                .map(rowLevelRoleMapper::mapToDto);

        Comparator<RowLevelRole> comparator = getComparator(context.getLoadContext());
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
    private Comparator<RowLevelRole> getComparator(LoadContext<RowLevelRole> loadContext) {
        Comparator<RowLevelRole> comparator = null;
        if (loadContext.getQuery() != null) {
            Sort sort = loadContext.getQuery().getSort();
            //noinspection ConstantConditions
            if (sort != null && !sort.getOrders().isEmpty()) {
                Sort.Order order = sort.getOrders().get(0);
                if ("code".equals(order.getProperty())) {
                    comparator = Comparator.comparing(RowLevelRole::getCode);
                } else if ("name".equals(order.getProperty())) {
                    comparator = Comparator.comparing(RowLevelRole::getName);
                }

                if (comparator != null && order.getDirection() == Sort.Direction.DESC) {
                    comparator = comparator.reversed();
                }
            }
        }
        return comparator;
    }

    private RowLevelRole mapRoleToDtoForLoadingById(io.jmix.security.model.RowLevelRole src) {
        RowLevelRole dst = rowLevelRoleMapper.mapToDto(src);

        dst.setRowLevelPolicies(src.getRowLevelPolicies().stream()
                .map(rowLevelPolicyMapper::mapToDto)
                .collect(Collectors.toList()));

        return dst;
    }
}
