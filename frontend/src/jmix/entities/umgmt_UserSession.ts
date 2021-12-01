export class UserSession {
  static NAME = "umgmt_UserSession";
  id?: string;
  username?: string | null;
  lastRequest?: any | null;
}
export type UserSessionViewName = "_base" | "_instance_name" | "_local";
export type UserSessionView<V extends UserSessionViewName> = V extends "_base"
  ? Pick<UserSession, "id" | "username" | "lastRequest">
  : V extends "_local"
  ? Pick<UserSession, "id" | "username" | "lastRequest">
  : never;
