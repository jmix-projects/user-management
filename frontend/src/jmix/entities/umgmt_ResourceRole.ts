import { EntityPolicy } from "./umgmt_EntityPolicy";
import { EntityAttributePolicy } from "./umgmt_EntityAttributePolicy";
import { ScreenPolicy } from "./umgmt_ScreenPolicy";
import { MenuPolicy } from "./umgmt_MenuPolicy";
import { SpecificPolicy } from "./umgmt_SpecificPolicy";
export class ResourceRole {
  static NAME = "umgmt_ResourceRole";
  id?: string;
  readOnly?: boolean | null;
  version?: number | null;
  name?: string | null;
  code?: string | null;
  description?: string | null;
  entityPolicies?: EntityPolicy[] | null;
  entityAttributePolicies?: EntityAttributePolicy[] | null;
  screenPolicies?: ScreenPolicy[] | null;
  menuPolicies?: MenuPolicy[] | null;
  specificPolicies?: SpecificPolicy[] | null;
  childRoles?: string | null;
  scopes?: string | null;
}
export type ResourceRoleViewName = "_base" | "_instance_name" | "_local";
export type ResourceRoleView<V extends ResourceRoleViewName> = V extends "_base"
  ? Pick<
      ResourceRole,
      | "id"
      | "name"
      | "readOnly"
      | "version"
      | "code"
      | "description"
      | "childRoles"
      | "scopes"
    >
  : V extends "_instance_name"
  ? Pick<ResourceRole, "id" | "name">
  : V extends "_local"
  ? Pick<
      ResourceRole,
      | "id"
      | "readOnly"
      | "version"
      | "name"
      | "code"
      | "description"
      | "childRoles"
      | "scopes"
    >
  : never;
