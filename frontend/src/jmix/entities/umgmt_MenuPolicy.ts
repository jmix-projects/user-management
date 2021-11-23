import { ResourceRole } from "./umgmt_ResourceRole";
export class MenuPolicy {
  static NAME = "umgmt_MenuPolicy";
  id?: string;
  version?: number | null;
  resource?: string | null;
  role?: ResourceRole | null;
}
export type MenuPolicyViewName = "_base" | "_instance_name" | "_local";
export type MenuPolicyView<V extends MenuPolicyViewName> = V extends "_base"
  ? Pick<MenuPolicy, "id" | "version" | "resource">
  : V extends "_local"
  ? Pick<MenuPolicy, "id" | "version" | "resource">
  : never;
