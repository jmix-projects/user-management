import { EntityPolicyType } from "../enums/enums";
import { ResourceRole } from "./umgmt_ResourceRole";
export class EntityPolicy {
  static NAME = "umgmt_EntityPolicy";
  id?: string;
  version?: number | null;
  entityName?: string | null;
  action?: EntityPolicyType | null;
  readOnly?: boolean | null;
  role?: ResourceRole | null;
}
export type EntityPolicyViewName = "_base" | "_instance_name" | "_local";
export type EntityPolicyView<V extends EntityPolicyViewName> = V extends "_base"
  ? Pick<EntityPolicy, "id" | "version" | "entityName" | "action" | "readOnly">
  : V extends "_local"
  ? Pick<EntityPolicy, "id" | "version" | "entityName" | "action" | "readOnly">
  : never;
