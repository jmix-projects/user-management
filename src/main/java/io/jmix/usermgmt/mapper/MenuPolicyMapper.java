package io.jmix.usermgmt.mapper;

import com.google.common.base.Strings;
import io.jmix.core.Metadata;
import io.jmix.security.model.ResourcePolicy;
import io.jmix.security.model.ResourcePolicyEffect;
import io.jmix.securitydata.entity.ResourcePolicyEntity;
import io.jmix.usermgmt.entity.MenuPolicy;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MenuPolicyMapper extends BasePolicyMapper {
    @Autowired
    private Metadata metadata;

    public MenuPolicy mapToDto(ResourcePolicy src) {
        MenuPolicy dst = metadata.create(MenuPolicy.class);

        dst.setId(getPolicyIdForDto(src));
        dst.setResource(getMenu(src.getResource()));

        //TODO: set version

        return dst;
    }

    public ResourcePolicyEntity mapFromDto(MenuPolicy src) {
        ResourcePolicyEntity dst = metadata.create(ResourcePolicyEntity.class);

        if (src.getId() != null) {
            dst.setId(src.getId());
        }

        dst.setResource("frontend_menu:" + src.getResource());
        dst.setType("custom");
        dst.setAction(ResourcePolicy.DEFAULT_ACTION);
        dst.setEffect(ResourcePolicyEffect.ALLOW);

        return dst;
    }

    private String getMenu(String resource) {
        if (Strings.isNullOrEmpty(resource)) {
            return resource;
        }
        if (StringUtils.startsWith("frontend_menu:", resource)) {
            return resource.substring(14);
        }
        return resource;
    }
}
