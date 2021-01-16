import { TextField } from "@material-ui/core";
import React from "react";
import { Controller, useForm } from "react-hook-form";

const MatchForm = () => {
  const { register, handleSubmit, watch, errors, control } = useForm();
  const onSubmit = (data) => console.log(data);

  console.log(watch("example"));

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <Controller as={TextField} control={control} name="text" />
      <input name="example" defaultValue="test" ref={register} />
      <input name="exampleRequired" ref={register({ required: true })} />
      {errors.exampleRequired && <span>This field is required</span>}
      <input type="submit" />
    </form>
  );
};

export default MatchForm;
