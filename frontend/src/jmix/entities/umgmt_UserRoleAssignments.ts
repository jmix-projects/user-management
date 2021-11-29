import { ResourceRole } from "./umgmt_ResourceRole";
import { RowLevelRole } from "./umgmt_RowLevelRole";
export class UserRoleAssignments {
  static NAME = "umgmt_UserRoleAssignments";
  id?: string;
  resourceRoles?: ResourceRole[] | null;
  rowLevelRoles?: RowLevelRole[] | null;
  uuid?: any | null;
}
export type UserRoleAssignmentsViewName = "_base" | "_instance_name" | "_local";
export type UserRoleAssignmentsView<
  V extends UserRoleAssignmentsViewName
> = V extends "_base"
  ? Pick<UserRoleAssignments, "id" | "uuid">
  : V extends "_local"
  ? Pick<UserRoleAssignments, "id" | "uuid">
  : never;
