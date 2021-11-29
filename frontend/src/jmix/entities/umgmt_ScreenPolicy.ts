import { ResourceRole } from "./umgmt_ResourceRole";
export class ScreenPolicy {
  static NAME = "umgmt_ScreenPolicy";
  id?: string;
  version?: number | null;
  resource?: string | null;
  readOnly?: boolean | null;
  role?: ResourceRole | null;
}
export type ScreenPolicyViewName = "_base" | "_instance_name" | "_local";
export type ScreenPolicyView<V extends ScreenPolicyViewName> = V extends "_base"
  ? Pick<ScreenPolicy, "id" | "version" | "resource" | "readOnly">
  : V extends "_local"
  ? Pick<ScreenPolicy, "id" | "version" | "resource" | "readOnly">
  : never;
