import {openCrudScreen} from "@haulmont/jmix-react-web";
import {Screens} from "@haulmont/jmix-react-core";

export function showRoleAssignmentsComponent(entityId?: string | null, screens?: Screens): void {
  if (entityId && screens) {
    openCrudScreen({
      entityName: 'umgmt_UserRoleAssignments',
      crudScreenType: 'entityEditor',
      screens: screens,
      screenParams: {
        entityId: entityId
      }
    });
  }
}