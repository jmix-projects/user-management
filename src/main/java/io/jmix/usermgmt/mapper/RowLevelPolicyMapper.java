package io.jmix.usermgmt.mapper;

import io.jmix.core.Metadata;
import io.jmix.usermgmt.entity.RowLevelAction;
import io.jmix.usermgmt.entity.RowLevelPolicy;
import io.jmix.usermgmt.entity.RowLevelType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RowLevelPolicyMapper extends BasePolicyMapper {
    @Autowired
    private Metadata metadata;

    public RowLevelPolicy mapToDto(io.jmix.security.model.RowLevelPolicy src) {
        RowLevelPolicy dst = metadata.create(RowLevelPolicy.class);

        dst.setId(getPolicyIdForDto(src));
        dst.setEntityName(src.getEntityName());
        dst.setWhereClause(src.getWhereClause());
        dst.setJoinClause(src.getJoinClause());
        dst.setScript(src.getScript());
        //noinspection ConstantConditions
        dst.setType(src.getType() == null ? null : RowLevelType.fromId(src.getType().getId()));
        //noinspection ConstantConditions
        dst.setAction(src.getAction() == null ? null : RowLevelAction.fromId(src.getAction().getId()));

        //TODO: set version

        return dst;
    }
}
