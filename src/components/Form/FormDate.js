import { Box, Button } from "@material-ui/core";
import DateFnsUtils from "@date-io/date-fns";
import {
  MuiPickersUtilsProvider,
  KeyboardTimePicker,
  DatePicker,
} from "@material-ui/pickers";
import React from "react";

const FormDate = ({ date, setDate, prevStep, nextStep }) => {
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
            value={date}
            onChange={(date) => {
              setDate(date);
              console.log(date);
            }}
            KeyboardButtonProps={{
              "aria-label": "change date",
            }}
            maxDate={new Date(date).setMonth(date.getMonth() + 1)}
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
            value={date}
            onChange={(date) => setDate(date)}
            KeyboardButtonProps={{
              "aria-label": "change time",
            }}
            disablePast
            minutesStep={30}
          />
        </Box>
      </MuiPickersUtilsProvider>
      <Box paddingBottom={2}>
        {`선택한 경기 시각: ${date.toLocaleString("ko-KR")}`}
      </Box>
      {/* <Button color="primary" variant="contained" onClick={next}>
        Continue
      </Button>
      <Button color="secondary" variant="contained" onClick={prev}>
        Back
      </Button> */}
    </>
  );
};

export default FormDate;
