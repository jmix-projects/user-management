import {findEntityMetadata, MetaClassInfo, Metadata} from "@haulmont/jmix-react-core";
import {AttributeInfo, EntityTypeInfo} from "./EntityTypes.types";
import {IntlShape} from "react-intl";


// export function getEntityNamesInfo(allEnttityNames: EntityNamesInfo[], entityName: string): EntityTypeInfo {
//   return allEnttityNames.find(entityNamesItem => entityNamesItem.entityName === entityName)!;
// }

export function getAllPersistentEntityTypes(metadata: Metadata): EntityTypeInfo[] {
  return metadata.entities
    .filter(({persistentEntity}) => persistentEntity)
    .map(({entityName, className}: MetaClassInfo) => {
      return {entityName, className};
    })
}

export function getAllPersistentEntityTypesIncludeAll(metadata: Metadata, intl: IntlShape): EntityTypeInfo[] {
  const result = metadata.entities
    .filter(({persistentEntity}) => persistentEntity)
    .map(({entityName, className}: MetaClassInfo) => {
      return {entityName, className};
    })
  result.push({entityName: "*", className: intl.formatMessage({id: "jmix.usermgmt.allEntities"})})
  return result
}

export function getEntityPropertyNames(entityName: string, metadata: Metadata): string[] | undefined {
  return findEntityMetadata(entityName, metadata)?.properties.map(({name}) => name);
}

export function getEntityPropertyNamesIncludeAll(entityName: string, metadata: Metadata): AttributeInfo[] {
  const entityMetadata = findEntityMetadata(entityName, metadata)
  if (entityMetadata !== undefined ) {
    return entityMetadata.properties.map(({name}) => {
      return {attribute: name, entityName: entityName}
    })
  }
  return []
}