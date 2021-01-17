import { Box, Button, Grid, TextField } from "@material-ui/core";
import DateFnsUtils from "@date-io/date-fns";
import {
  MuiPickersUtilsProvider,
  KeyboardTimePicker,
  KeyboardDatePicker,
  DatePicker,
  TimePicker,
} from "@material-ui/pickers";
import React, { useState } from "react";

const FormDatePlace = ({
  selectedDate,
  setSelectedDate,
  prevStep,
  nextStep,
}) => {
  const prev = (e) => {
    e.preventDefault();
    prevStep();
  };
  const next = (e) => {
    e.preventDefault();
    nextStep();
  };

  return (
    <>
      <MuiPickersUtilsProvider utils={DateFnsUtils}>
        <Box paddingBottom={2}>
          <DatePicker
            margin="normal"
            id="date-picker-dialog"
            label="경기 일시"
            format="yyyy/MM/dd"
            value={selectedDate}
            onChange={(date) => {
              setSelectedDate(date);
              console.log(date);
            }}
            KeyboardButtonProps={{
              "aria-label": "change date",
            }}
            maxDate={new Date(selectedDate).setMonth(
              selectedDate.getMonth() + 6
            )}
            disableToolbar
            disablePast
            variant="static"
          />
        </Box>
        <Box>
          <KeyboardTimePicker
            margin="normal"
            id="time-picker"
            label="경기 시각"
            value={selectedDate}
            onChange={(date) => setSelectedDate(date)}
            KeyboardButtonProps={{
              "aria-label": "change time",
            }}
            disablePast
            minutesStep={30}
          />
        </Box>
      </MuiPickersUtilsProvider>
      <Box paddingBottom={2}>
        {`선택한 경기 시각: ${selectedDate.toLocaleString("ko-KR")}`}
      </Box>
      <Button color="primary" variant="contained" onClick={next}>
        Continue
      </Button>
      <Button color="secondary" variant="contained" onClick={prev}>
        Back
      </Button>
    </>
  );
};

export default FormDatePlace;
