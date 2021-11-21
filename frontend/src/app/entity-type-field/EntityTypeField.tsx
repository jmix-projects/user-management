import {JmixFormFieldProps, JmixFormFieldWrapper} from "@haulmont/jmix-react-antd";
import {EntityTypeSelect} from "./EntityTypeSelect";
import React from "react";
import {EntityTypeInfo} from "../metadata";


export type EntityTypeFieldProps = { entityTypeOptions: EntityTypeInfo[] }

export function EntityTypeField({
                                  entityName,
                                  propertyName,
                                  formItemProps,
                                  value,
                                  onChange,
                                  entityTypeOptions,
                                  ...rest
                                }: JmixFormFieldProps & EntityTypeFieldProps) {
  const passedValue = value ? value : "";

  return (
    <JmixFormFieldWrapper
      entityName={entityName}
      propertyName={propertyName}
      formItemProps={formItemProps}
      renderField={isReadOnly =>
        <EntityTypeSelect
          value={passedValue}
          entityTypeOptions={entityTypeOptions}
          onChange={onChange}
        />
      }
    />
  );
}