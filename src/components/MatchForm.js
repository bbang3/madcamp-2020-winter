import {
  Button,
  MenuItem,
  Select,
  Slider,
  TextField,
  Typography,
} from "@material-ui/core";
import React from "react";
import { Controller, useForm } from "react-hook-form";

const MatchForm = ({ sports }) => {
  const initialValues = {
    skills: 5,
    intensity: 5,
    age: 20,
  };

  const { register, handleSubmit, watch, errors, control } = useForm({
    defaultValues: initialValues,
  });
  const onSubmit = (data) => {
    data.age = parseInt(data.age);
    console.log(data);
  };

  return (
    <form autoComplete="off" onSubmit={handleSubmit(onSubmit)}>
      <section>
        <label> 1. 실력 </label>
        <Controller
          name="skills"
          control={control}
          render={(props) => (
            <Slider
              {...props}
              onChange={(_, value) => {
                props.onChange(value);
              }}
              valueLabelDisplay="auto"
              min={0}
              max={10}
              step={1}
            />
          )}
        />
      </section>

      <section>
        <label> 2. 플레이스타일 </label>
        <Controller
          name="intensity"
          control={control}
          render={(props) => (
            <Slider
              {...props}
              onChange={(_, value) => {
                props.onChange(value);
              }}
              valueLabelDisplay="auto"
              min={0}
              max={10}
              step={1}
            />
          )}
        />
      </section>

      <section>
        <label> 3. 평균 나이대 </label>{" "}
        <Controller
          as={TextField}
          name="age"
          control={control}
          inputRef={register({ required: "required" })}
          type="number"
        />
        {errors.age && "required"}
        {/* <Controller
          name="age"
          control={control}
          render={(props) => (
            <TextField
              {...props}
              label="age"
              type="nu"
            />
          )}
        /> */}
      </section>

      <Button type="submit"> Button </Button>
    </form>
  );
};

export default MatchForm;
