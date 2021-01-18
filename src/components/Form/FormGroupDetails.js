import React from "react";
import Dialog from "@material-ui/core/Dialog";
import AppBar from "@material-ui/core/AppBar";
import { ThemeProvider } from "@material-ui/core/styles";
import TextField from "@material-ui/core/TextField";
import Button from "@material-ui/core/Button";
import { Box, InputLabel, MenuItem, Select, Slider } from "@material-ui/core";

const FormGroupDetails = ({ values, setState }) => {
  return (
    <div>
      <InputLabel id="group-size-label"> 팀원 수 </InputLabel>
      <Select
        labelId="group-size-label"
        value={values.groupSize}
        onChange={(e) => {
          setState({ ...values, groupSize: e.target.value });
        }}
      >
        <MenuItem value={1}>1명</MenuItem>
        <MenuItem value={2}>2명</MenuItem>
        <MenuItem value={3}>3명</MenuItem>
        <MenuItem value={4}>4명</MenuItem>
        <MenuItem value={5}>5명</MenuItem>
      </Select>
      <Box>실력</Box>
      <Slider
        style={{ width: "10rem" }}
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
      <Box>플레이스타일</Box>
      <Slider
        style={{ width: "10rem" }}
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
