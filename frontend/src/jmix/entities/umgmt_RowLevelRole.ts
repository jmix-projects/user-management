import { RowLevelPolicy } from "./umgmt_RowLevelPolicy";
export class RowLevelRole {
  static NAME = "umgmt_RowLevelRole";
  id?: string;
  version?: number | null;
  name?: string | null;
  code?: string | null;
  description?: string | null;
  readOnly?: boolean | null;
  rowLevelPolicies?: RowLevelPolicy[] | null;
  childRoles?: string | null;
}
export type RowLevelRoleViewName = "_base" | "_instance_name" | "_local";
export type RowLevelRoleView<V extends RowLevelRoleViewName> = V extends "_base"
  ? Pick<
      RowLevelRole,
      | "id"
      | "name"
      | "version"
      | "code"
      | "description"
      | "readOnly"
      | "childRoles"
    >
  : V extends "_instance_name"
  ? Pick<RowLevelRole, "id" | "name">
  : V extends "_local"
  ? Pick<
      RowLevelRole,
      | "id"
      | "version"
      | "name"
      | "code"
      | "description"
      | "readOnly"
      | "childRoles"
    >
  : never;
