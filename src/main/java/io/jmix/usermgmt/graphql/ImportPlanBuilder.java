package io.jmix.usermgmt.graphql;

import io.jmix.core.CollectionImportPolicy;
import io.jmix.core.EntityImportPlan;
import io.jmix.core.EntityImportPlanProperty;
import io.jmix.securitydata.entity.ResourcePolicyEntity;
import io.jmix.securitydata.entity.ResourceRoleEntity;
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

    public EntityImportPlan buildResourceImportPlan(EntityImportPlan srcImportPlan) {
        EntityImportPlan dstImportPlan = new EntityImportPlan(ResourceRoleEntity.class);

        //noinspection ConstantConditions
        if (srcImportPlan.getProperty("id") != null) {
            dstImportPlan.addProperty(new EntityImportPlanProperty("id"));
        }

        dstImportPlan.addProperty(new EntityImportPlanProperty("code"));
        dstImportPlan.addProperty(new EntityImportPlanProperty("name"));
        dstImportPlan.addProperty(new EntityImportPlanProperty("description"));

        if (hasCompositionInImport(srcImportPlan)) {
            EntityImportPlanProperty dstPoliciesProperty =
                    new EntityImportPlanProperty("resourcePolicies");
            dstPoliciesProperty.setCollectionImportPolicy(CollectionImportPolicy.REMOVE_ABSENT_ITEMS);

            EntityImportPlan dstPoliciesPlan = new EntityImportPlan(ResourcePolicyEntity.class);
            if (hasPropertiesInCompositionPlan(srcImportPlan)) {
                dstPoliciesPlan.addProperty(new EntityImportPlanProperty("type"));
                dstPoliciesPlan.addProperty(new EntityImportPlanProperty("resource"));
                dstPoliciesPlan.addProperty(new EntityImportPlanProperty("action"));
                dstPoliciesPlan.addProperty(new EntityImportPlanProperty("effect"));
                dstPoliciesPlan.addProperty(new EntityImportPlanProperty("policyGroup"));
            }
            dstPoliciesProperty.setPlan(dstPoliciesPlan);

            dstImportPlan.addProperty(dstPoliciesProperty);
        }
        return dstImportPlan;
    }

    private boolean hasCompositionInImport(EntityImportPlan srcImportPlan) {
        //noinspection ConstantConditions
        return srcImportPlan.getProperty("entityPolicies") != null ||
                srcImportPlan.getProperty("entityAttributePolicies") != null ||
                srcImportPlan.getProperty("screenPolicies") != null ||
                srcImportPlan.getProperty("menuPolicies") != null ||
                srcImportPlan.getProperty("specificPolicies") != null;
    }

    private boolean hasPropertiesInCompositionPlan(EntityImportPlan importPlan) {
        EntityImportPlanProperty importProperty = importPlan.getProperty("entityPolicies");
        //noinspection ConstantConditions
        if (importProperty != null) {
            if (hasPropertiesInImportPlan(importProperty.getPlan())) {
                return true;
            }
        }

        importProperty = importPlan.getProperty("entityAttributePolicies");
        //noinspection ConstantConditions
        if (importProperty != null) {
            if (hasPropertiesInImportPlan(importProperty.getPlan())) {
                return true;
            }
        }

        importProperty = importPlan.getProperty("screenPolicies");
        //noinspection ConstantConditions
        if (importProperty != null) {
            if (hasPropertiesInImportPlan(importProperty.getPlan())) {
                return true;
            }
        }

        importProperty = importPlan.getProperty("menuPolicies");
        //noinspection ConstantConditions
        if (importProperty != null) {
            if (hasPropertiesInImportPlan(importProperty.getPlan())) {
                return true;
            }
        }

        importProperty = importPlan.getProperty("specificPolicies");
        //noinspection ConstantConditions
        if (importProperty != null) {
            if (hasPropertiesInImportPlan(importProperty.getPlan())) {
                return true;
            }
        }

        return false;
    }

    private boolean hasPropertiesInImportPlan(EntityImportPlan importPlan) {
        return importPlan != null && !importPlan.getProperties().isEmpty();
    }
}
