package io.jmix.usermgmt.mapper;

import io.jmix.core.Metadata;
import io.jmix.security.model.ResourcePolicy;
import io.jmix.usermgmt.entity.MenuPolicy;
import io.jmix.usermgmt.entity.ScreenPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MenuPolicyMapper extends BasePolicyMapper {
    @Autowired
    private Metadata metadata;

    public MenuPolicy mapToDto(ResourcePolicy src) {
        MenuPolicy dst = metadata.create(MenuPolicy.class);

        dst.setId(getPolicyIdForDto(src));
        dst.setResource(src.getResource());

        //TODO: set version

        return dst;
    }
}
