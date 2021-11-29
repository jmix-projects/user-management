import { AttributePolicyType } from "../enums/enums";
import { ResourceRole } from "./umgmt_ResourceRole";
export class EntityAttributePolicy {
  static NAME = "umgmt_EntityAttributePolicy";
  id?: string;
  version?: number | null;
  entityName?: string | null;
  attribute?: string | null;
  action?: AttributePolicyType | null;
  readOnly?: boolean | null;
  role?: ResourceRole | null;
}
export type EntityAttributePolicyViewName =
  | "_base"
  | "_instance_name"
  | "_local";
export type EntityAttributePolicyView<
  V extends EntityAttributePolicyViewName
> = V extends "_base"
  ? Pick<
      EntityAttributePolicy,
      "id" | "version" | "entityName" | "attribute" | "action" | "readOnly"
    >
  : V extends "_local"
  ? Pick<
      EntityAttributePolicy,
      "id" | "version" | "entityName" | "attribute" | "action" | "readOnly"
    >
  : never;
