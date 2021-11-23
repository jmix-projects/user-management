import React from "react";
import ResourceRoleList from "../app/resource-role/ResourceRoleList";
import ResourceRoleEditor from "../app/resource-role/ResourceRoleEditor";
import SpecificPolicyList from "../app/specific-policy/SpecificPolicyList";
import SpecificPolicyEditor from "../app/specific-policy/SpecificPolicyEditor";
import ScreenPolicyList from "../app/screen-policy/ScreenPolicyList";
import ScreenPolicyEditor from "../app/screen-policy/ScreenPolicyEditor";
import MenuPolicyList from "../app/menu-policy/MenuPolicyList";
import MenuPolicyEditor from "../app/menu-policy/MenuPolicyEditor";
import EntityAttributePolicyList from "../app/entity-attribute-policy/EntityAttributePolicyList";
import EntityAttributePolicyEditor from "../app/entity-attribute-policy/EntityAttributePolicyEditor";
import EntityPolicyList from "../app/entity-policy/EntityPolicyList";
import EntityPolicyEditor from "../app/entity-policy/EntityPolicyEditor";
import RowLevelPolicyList from "../app/row-level-policy/RowLevelPolicyList";
import RowLevelPolicyEditor from "../app/row-level-policy/RowLevelPolicyEditor";
import RowLevelRoleList from "../app/row-level-role/RowLevelRoleList";
import RowLevelRoleEditor from "../app/row-level-role/RowLevelRoleEditor";
import { Previews, ComponentPreview } from "@haulmont/react-ide-toolbox";

export const ComponentPreviews = () => {
  return (
    <Previews>
      <ComponentPreview path="/RowLevelRoleEditor">
        <RowLevelRoleEditor />
      </ComponentPreview>
      <ComponentPreview path="/RowLevelRoleList">
        <RowLevelRoleList />
      </ComponentPreview>
      <ComponentPreview path="/RowLevelPolicyEditor">
        <RowLevelPolicyEditor />
      </ComponentPreview>
      <ComponentPreview path="/RowLevelPolicyList">
        <RowLevelPolicyList />
      </ComponentPreview>
      <ComponentPreview path="/EntityPolicyEditor">
        <EntityPolicyEditor />
      </ComponentPreview>
      <ComponentPreview path="/EntityPolicyList">
        <EntityPolicyList />
      </ComponentPreview>
      <ComponentPreview path="/EntityAttributePolicyEditor">
        <EntityAttributePolicyEditor />
      </ComponentPreview>
      <ComponentPreview path="/EntityAttributePolicyList">
        <EntityAttributePolicyList />
      </ComponentPreview>
      <ComponentPreview path="/MenuPolicyEditor">
        <MenuPolicyEditor />
      </ComponentPreview>
      <ComponentPreview path="/MenuPolicyList">
        <MenuPolicyList />
      </ComponentPreview>
      <ComponentPreview path="/ScreenPolicyEditor">
        <ScreenPolicyEditor />
      </ComponentPreview>
      <ComponentPreview path="/ScreenPolicyList">
        <ScreenPolicyList />
      </ComponentPreview>
      <ComponentPreview path="/SpecificPolicyEditor">
        <SpecificPolicyEditor />
      </ComponentPreview>
      <ComponentPreview path="/SpecificPolicyList">
        <SpecificPolicyList />
      </ComponentPreview>
      <ComponentPreview path="/ResourceRoleEditor">
        <ResourceRoleEditor />
      </ComponentPreview>
      <ComponentPreview path="/ResourceRoleList">
        <ResourceRoleList />
      </ComponentPreview>
    </Previews>
  );
};
