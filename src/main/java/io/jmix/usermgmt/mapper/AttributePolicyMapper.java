package io.jmix.usermgmt.mapper;

import com.google.common.base.Strings;
import io.jmix.core.Metadata;
import io.jmix.security.model.ResourcePolicy;
import io.jmix.usermgmt.entity.AttributePolicyType;
import io.jmix.usermgmt.entity.EntityAttributePolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AttributePolicyMapper extends BasePolicyMapper {
    @Autowired
    private Metadata metadata;

    public EntityAttributePolicy mapToDto(ResourcePolicy src) {
        EntityAttributePolicy dst = metadata.create(EntityAttributePolicy.class);

        String entityName = null;
        String attribute = null;
        if (!Strings.isNullOrEmpty(src.getResource())) {
            String resource = src.getResource();
            entityName = resource.substring(0, resource.lastIndexOf("."));
            attribute = resource.substring(resource.lastIndexOf(".") + 1);
        }

        dst.setId(getPolicyIdForDto(src));
        dst.setEntityName(entityName);
        dst.setAttribute(attribute);
        dst.setAction(getAttributePolicyType(src.getAction()));

        //TODO: set version

        return dst;
    }

    private AttributePolicyType getAttributePolicyType(String action) {
        return AttributePolicyType.fromId(action);
    }


}
