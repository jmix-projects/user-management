import {findEntityMetadata, MetaClassInfo, Metadata} from "@haulmont/jmix-react-core";
import {EntityTypeInfo} from "./EntityTypes.types";


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

export function getAllEntityPropertyNames(entityName: string, metadata: Metadata): string[] | undefined {
  return findEntityMetadata(entityName, metadata)?.properties.map(({name}) => name);
} 