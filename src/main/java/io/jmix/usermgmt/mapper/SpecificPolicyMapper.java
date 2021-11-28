package io.jmix.usermgmt.mapper;

import io.jmix.core.Metadata;
import io.jmix.security.model.ResourcePolicy;
import io.jmix.security.model.ResourcePolicyEffect;
import io.jmix.security.model.ResourcePolicyType;
import io.jmix.securitydata.entity.ResourcePolicyEntity;
import io.jmix.usermgmt.entity.SpecificPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SpecificPolicyMapper extends BasePolicyMapper {
    @Autowired
    private Metadata metadata;

    public SpecificPolicy mapToDto(ResourcePolicy src) {
        SpecificPolicy dst = metadata.create(SpecificPolicy.class);

        dst.setId(getPolicyIdForDto(src));
        dst.setResource(src.getResource());

        //TODO: set version

        return dst;
    }

    public ResourcePolicyEntity mapFromDto(SpecificPolicy src) {
        ResourcePolicyEntity dst = metadata.create(ResourcePolicyEntity.class);

        if (src.getId() != null) {
            dst.setId(src.getId());
        }

        dst.setResource(src.getResource());
        dst.setType(ResourcePolicyType.SPECIFIC);
        dst.setAction(ResourcePolicy.DEFAULT_ACTION);
        dst.setEffect(ResourcePolicyEffect.ALLOW);

        return dst;
    }
}
