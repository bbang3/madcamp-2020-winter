import { Button } from "@material-ui/core";
import React, { Component, useState } from "react";
import FormDate from "./FormDate";
import FormGroupDetails from "./FormGroupDetails";
import FormPlace from "./FormPlace";

const MultiStepForm = () => {
  const [step, setStep] = useState(0);
  const [state, setState] = useState({
    step: 1,
    skills: 5,
    intensity: 5,
    age: 20,
  });
  const [location, setLocation] = useState(null);

  const [selectedDate, setSelectedDate] = useState(
    new Date(parseInt(Date.now() / 1800000) * 1800000)
  );
  console.log(selectedDate);

  // Proceed to next step
  const nextStep = () => {
    console.log(step);
    setStep(step + 1);
  };

  // Go back to prev step
  const prevStep = () => {
    setStep(step - 1);
  };

  const handleDateChange = (date) => {
    setSelectedDate(date);
    console.log(date);
  };

  const { skills, intensity, age } = state;
  const values = { skills, intensity, age };

  switch (step) {
    case 0:
      return (
        <>
          <FormGroupDetails
            setState={setState}
            nextStep={nextStep}
            values={values}
          />
          <Button
            color="primary"
            variant="contained"
            onClick={() => nextStep()}
          >
            Continue
          </Button>
        </>
      );
    case 1:
      return (
        <>
          <FormDate
            selectedDate={selectedDate}
            setSelectedDate={setSelectedDate}
            prevStep={prevStep}
            nextStep={nextStep}
          />
          <FormPlace location={location} setLocation={setLocation} />
          <br />
          <Button
            color="primary"
            variant="contained"
            onClick={() => nextStep()}
          >
            Continue
          </Button>
          <Button
            color="secondary"
            variant="contained"
            onClick={() => prevStep()}
          >
            Back
          </Button>
        </>
      );
    default:
      return <>!!!</>;
  }
};

export default MultiStepForm;
