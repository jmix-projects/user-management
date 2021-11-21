import {Select} from "antd";
import React from "react";
import {EntityTypeInfo} from "../metadata";

interface Props {
  onChange?: (value: any) => void,
  entityTypeOptions: EntityTypeInfo[]
  value: any
}

export const EntityTypeSelect: React.FC<Props> = ({value, entityTypeOptions, onChange}: Props) => {
  return (
    <Select
      onChange={onChange}
      value={value}>
      {entityTypeOptions.map(({entityName, className}: EntityTypeInfo) => {
        return (
          <Select.Option
            value={entityName}
            key={entityName}>
            {getEntityOptionCaption({entityName, className})}
          </Select.Option>
        )
      })}
    </Select>
  )
}

function getEntityOptionCaption({entityName, className}: EntityTypeInfo): string {
  return `${className} (${entityName})`;
}
