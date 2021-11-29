package io.jmix.usermgmt.mapper;

import io.jmix.core.Metadata;
import io.jmix.core.UuidProvider;
import io.jmix.securitydata.entity.ResourceRoleEntity;
import io.jmix.securitydata.entity.RowLevelRoleEntity;
import io.jmix.usermgmt.entity.ResourceRole;
import io.jmix.usermgmt.entity.RowLevelRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class ResourceRoleMapper {
    @Autowired
    private Metadata metadata;

    public ResourceRole mapToDto(io.jmix.security.model.ResourceRole src) {
        ResourceRole dst = metadata.create(ResourceRole.class);
        dst.setId(getRoleIdForDto(src));
        dst.setCode(src.getCode());
        dst.setName(src.getName());
        dst.setDescription(src.getDescription());
        dst.setScopes(src.getScopes());
        dst.setReadOnly(isReadOnly(src));

        return dst;
    }

    private String getRoleIdForDto(io.jmix.security.model.ResourceRole src) {
        Map<String, String> customProperties = src.getCustomProperties();
        String roleId = null;
        //noinspection ConstantConditions
        if (customProperties != null) {
            String databaseId = customProperties.get("databaseId");
            if (databaseId != null) {
                roleId = UuidProvider.fromString(databaseId).toString();
            }
        }
        if (roleId == null) {
            roleId = src.getCode();
        }
        return roleId;
    }

    public boolean isReadOnly(io.jmix.security.model.ResourceRole src) {
        Map<String, String> customProperties = src.getCustomProperties();
        //noinspection ConstantConditions
        if (customProperties != null) {
            String databaseId = customProperties.get("databaseId");
            if (databaseId != null) {
                return false;
            }
        }
        return true;
    }

    public ResourceRoleEntity mapFromDto(ResourceRole src) {
        ResourceRoleEntity dst = metadata.create(ResourceRoleEntity.class);
        if (src.getId() != null) {
            dst.setId(UUID.fromString(src.getId()));
        }
        dst.setCode(src.getCode());
        dst.setName(src.getName());
        dst.setDescription(src.getDescription());

        return dst;
    }

}
