package io.jmix.usermgmt.mapper;

import io.jmix.core.UuidProvider;
import io.jmix.security.model.ResourcePolicy;
import io.jmix.security.model.RowLevelPolicy;

import java.util.Map;
import java.util.UUID;

public class BasePolicyMapper {

    protected UUID getPolicyIdForDto(ResourcePolicy model) {
        Map<String, String> customProperties = model.getCustomProperties();
        UUID id = null;
        if (customProperties != null) {
            String databaseId = customProperties.get("databaseId");
            if (databaseId != null) {
                id = UuidProvider.fromString(databaseId);
            }
        }
        if (id == null) {
            id = UUID.randomUUID();
        }
        return id;
    }

    protected UUID getPolicyIdForDto(RowLevelPolicy model) {
        Map<String, String> customProperties = model.getCustomProperties();
        UUID id = null;
        if (customProperties != null) {
            String databaseId = customProperties.get("databaseId");
            if (databaseId != null) {
                id = UuidProvider.fromString(databaseId);
            }
        }
        if (id == null) {
            id = UUID.randomUUID();
        }
        return id;
    }
}
