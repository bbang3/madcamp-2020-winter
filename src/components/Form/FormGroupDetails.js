import React from "react";
import Dialog from "@material-ui/core/Dialog";
import AppBar from "@material-ui/core/AppBar";
import { ThemeProvider } from "@material-ui/core/styles";
import TextField from "@material-ui/core/TextField";
import Button from "@material-ui/core/Button";
import { Slider } from "@material-ui/core";

const FormGroupDetails = ({ values, setState }) => {
  return (
    <div>
      <AppBar title="Enter Personal Details" />
      <Slider
        value={values.skills}
        onChange={(e, value) => {
          console.log(value);
          setState({ ...values, skills: value });
        }}
        valueLabelDisplay="auto"
        min={0}
        max={10}
        step={1}
      />
      <br />
      <Slider
        value={values.intensity}
        onChange={(e, value) => setState({ ...values, intensity: value })}
        valueLabelDisplay="auto"
        min={0}
        max={10}
        step={1}
      />
      <br />
      <TextField
        value={values.age}
        type="number"
        onChange={(e) => {
          setState({ ...values, age: parseInt(e.target.value) });
        }}
        placeholder="그룹원의 평균 나이를 입력해주세요"
        label="Age"
        margin="normal"
        fullWidth
      />
      <br />
    </div>
  );
};

export default FormGroupDetails;
