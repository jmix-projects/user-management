import { ResourceRole } from "./umgmt_ResourceRole";
export class SpecificPolicy {
  static NAME = "umgmt_SpecificPolicy";
  id?: string;
  version?: number | null;
  resource?: string | null;
  readOnly?: boolean | null;
  role?: ResourceRole | null;
}
export type SpecificPolicyViewName = "_base" | "_instance_name" | "_local";
export type SpecificPolicyView<
  V extends SpecificPolicyViewName
> = V extends "_base"
  ? Pick<SpecificPolicy, "id" | "version" | "resource" | "readOnly">
  : V extends "_local"
  ? Pick<SpecificPolicy, "id" | "version" | "resource" | "readOnly">
  : never;
