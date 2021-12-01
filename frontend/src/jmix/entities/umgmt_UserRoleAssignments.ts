import { ResourceRole } from "./umgmt_ResourceRole";
import { RowLevelRole } from "./umgmt_RowLevelRole";
export class UserRoleAssignments {
  static NAME = "umgmt_UserRoleAssignments";
  id?: string;
  resourceRoles?: ResourceRole[] | null;
  rowLevelRoles?: RowLevelRole[] | null;
}
export type UserRoleAssignmentsViewName = "_base" | "_instance_name" | "_local";
export type UserRoleAssignmentsView = never;
