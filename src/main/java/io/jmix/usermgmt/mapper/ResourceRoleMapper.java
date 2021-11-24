package io.jmix.usermgmt.mapper;

import io.jmix.core.Metadata;
import io.jmix.core.UuidProvider;
import io.jmix.usermgmt.entity.ResourceRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

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

        return dst;
    }

    private String getRoleIdForDto(io.jmix.security.model.ResourceRole src) {
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
}
