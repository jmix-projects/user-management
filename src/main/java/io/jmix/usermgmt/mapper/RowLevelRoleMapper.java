package io.jmix.usermgmt.mapper;

import io.jmix.core.Metadata;
import io.jmix.core.UuidProvider;
import io.jmix.securitydata.entity.RowLevelRoleEntity;
import io.jmix.usermgmt.entity.RowLevelRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class RowLevelRoleMapper {
    @Autowired
    private Metadata metadata;

    public RowLevelRole mapToDto(io.jmix.security.model.RowLevelRole src) {
        RowLevelRole dst = metadata.create(RowLevelRole.class);
        dst.setId(getRoleIdForDto(src));
        dst.setCode(src.getCode());
        dst.setName(src.getName());
        dst.setDescription(src.getDescription());

        return dst;
    }

    private String getRoleIdForDto(io.jmix.security.model.RowLevelRole src) {
        Map<String, String> customProperties = src.getCustomProperties();
        String roleId = null;
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

    public RowLevelRoleEntity mapFromDto(RowLevelRole src) {
        RowLevelRoleEntity dst = metadata.create(RowLevelRoleEntity.class);
        if (src.getId() != null) {
            dst.setId(UUID.fromString(src.getId()));
        }
        dst.setCode(src.getCode());
        dst.setName(src.getName());
        dst.setDescription(src.getDescription());

        return dst;
    }
}
