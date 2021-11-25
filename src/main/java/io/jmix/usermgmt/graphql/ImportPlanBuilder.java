package io.jmix.usermgmt.graphql;

import io.jmix.core.CollectionImportPolicy;
import io.jmix.core.EntityImportPlan;
import io.jmix.core.EntityImportPlanProperty;
import io.jmix.securitydata.entity.RowLevelPolicyEntity;
import io.jmix.securitydata.entity.RowLevelRoleEntity;
import org.springframework.stereotype.Component;

@Component
public class ImportPlanBuilder {

    public EntityImportPlan buildRowLevelImportPlan(EntityImportPlan srcImportPlan) {
        EntityImportPlan dstImportPlan = new EntityImportPlan(RowLevelRoleEntity.class);

        //noinspection ConstantConditions
        if (srcImportPlan.getProperty("id") != null) {
            dstImportPlan.addProperty(new EntityImportPlanProperty("id"));
        }

        dstImportPlan.addProperty(new EntityImportPlanProperty("code"));
        dstImportPlan.addProperty(new EntityImportPlanProperty("name"));
        dstImportPlan.addProperty(new EntityImportPlanProperty("description"));

        EntityImportPlanProperty srcPoliciesProperty = srcImportPlan.getProperty("rowLevelPolicies");
        //noinspection ConstantConditions
        if (srcPoliciesProperty != null) {
            EntityImportPlanProperty dstPoliciesProperty =
                    new EntityImportPlanProperty("rowLevelPolicies");
            dstPoliciesProperty.setCollectionImportPolicy(CollectionImportPolicy.REMOVE_ABSENT_ITEMS);
            //noinspection ConstantConditions
            if (srcPoliciesProperty.getPlan() != null) {
                EntityImportPlan dstPoliciesPlan = new EntityImportPlan(RowLevelPolicyEntity.class);
                if (!srcPoliciesProperty.getPlan().getProperties().isEmpty()) {
                    dstPoliciesPlan.addProperty(new EntityImportPlanProperty("whereClause"));
                    dstPoliciesPlan.addProperty(new EntityImportPlanProperty("entityName"));
                    dstPoliciesPlan.addProperty(new EntityImportPlanProperty("action"));
                    dstPoliciesPlan.addProperty(new EntityImportPlanProperty("joinClause"));
                    dstPoliciesPlan.addProperty(new EntityImportPlanProperty("type"));
                    dstPoliciesPlan.addProperty(new EntityImportPlanProperty("script"));
                }
                dstPoliciesProperty.setPlan(dstPoliciesPlan);
            }
            dstImportPlan.addProperty(dstPoliciesProperty);
        }
        return dstImportPlan;
    }
}
