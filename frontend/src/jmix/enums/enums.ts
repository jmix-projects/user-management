export enum AttributePolicyType {
  VIEW = "VIEW",
  MODIFY = "MODIFY"
}

export enum EntityPolicyType {
  CREATE = "CREATE",
  READ = "READ",
  UPDATE = "UPDATE",
  DELETE = "DELETE",
  ALL = "ALL"
}

export enum RowLevelAction {
  CREATE = "CREATE",
  READ = "READ",
  UPDATE = "UPDATE",
  DELETE = "DELETE"
}

export enum RowLevelType {
  JPQL = "JPQL",
  PREDICATE = "PREDICATE"
}
