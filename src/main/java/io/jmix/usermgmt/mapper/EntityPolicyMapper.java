package io.jmix.usermgmt.mapper;

import io.jmix.core.Metadata;
import io.jmix.security.model.EntityPolicyAction;
import io.jmix.security.model.ResourcePolicy;
import io.jmix.security.model.ResourcePolicyEffect;
import io.jmix.security.model.ResourcePolicyType;
import io.jmix.securitydata.entity.ResourcePolicyEntity;
import io.jmix.usermgmt.entity.EntityPolicy;
import io.jmix.usermgmt.entity.EntityPolicyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityPolicyMapper extends BasePolicyMapper {
    @Autowired
    private Metadata metadata;

    public EntityPolicy mapToDto(ResourcePolicy src) {
        EntityPolicy dst = metadata.create(EntityPolicy.class);

        dst.setId(getPolicyIdForDto(src));
        dst.setEntityName(src.getResource());
        dst.setAction(getEntityPolicyType(src.getAction()));

        //TODO: set version

        return dst;
    }

    private EntityPolicyType getEntityPolicyType(String action) {
        return EntityPolicyType.fromId(action);
    }

    public ResourcePolicyEntity mapFromDto(EntityPolicy src) {
        ResourcePolicyEntity dst = metadata.create(ResourcePolicyEntity.class);

        if (src.getId() != null) {
            dst.setId(src.getId());
        }

        dst.setResource(src.getEntityName());
        dst.setType(ResourcePolicyType.ENTITY);
        dst.setAction(src.getAction().getId());
        dst.setEffect(ResourcePolicyEffect.ALLOW);

        return dst;
    }
}
