import { RowLevelType, RowLevelAction } from "../enums/enums";
import { RowLevelRole } from "./umgmt_RowLevelRole";
export class RowLevelPolicy {
  static NAME = "umgmt_RowLevelPolicy";
  id?: string;
  version?: number | null;
  type?: RowLevelType | null;
  entityName?: string | null;
  action?: RowLevelAction | null;
  whereClause?: string | null;
  joinClause?: string | null;
  script?: string | null;
  role?: RowLevelRole | null;
}
export type RowLevelPolicyViewName = "_base" | "_instance_name" | "_local";
export type RowLevelPolicyView<
  V extends RowLevelPolicyViewName
> = V extends "_base"
  ? Pick<
      RowLevelPolicy,
      | "id"
      | "version"
      | "type"
      | "entityName"
      | "action"
      | "whereClause"
      | "joinClause"
      | "script"
    >
  : V extends "_local"
  ? Pick<
      RowLevelPolicy,
      | "id"
      | "version"
      | "type"
      | "entityName"
      | "action"
      | "whereClause"
      | "joinClause"
      | "script"
    >
  : never;
