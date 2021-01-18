import { Button } from "@material-ui/core";
import React, { Component, useState } from "react";
import Confirm from "./Confirm";
import FormDate from "./FormDate";
import FormGroupDetails from "./FormGroupDetails";
import FormPlace from "./FormPlace";

const MultiStepForm = () => {
  const [step, setStep] = useState(0);
  const [state, setState] = useState({
    // step: 1,
    groupSize: 1,
    skills: 5,
    intensity: 5,
    age: 20,
  });
  const [location, setLocation] = useState(
    { lat: 36.37374155241465, lng: 127.35836653738268 } // KAIST dormitory
  );

  const [selectedDate, setSelectedDate] = useState(
    new Date(Math.ceil(Date.now() / 1800000) * 1800000) // ceil to nearest 30 minutes
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

  const onSubmit = (values) => {
    console.log(JSON.stringify(values));
  };

  const { groupSize, skills, intensity, age } = state;
  const values = { groupSize, skills, intensity, age, location, selectedDate };

  switch (step) {
    case 0:
      return (
        <>
          <FormGroupDetails
            setState={setState}
            nextStep={nextStep}
            values={state}
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
    case 2:
      return (
        <>
          <Confirm values={values} />
          <Button
            color="primary"
            variant="contained"
            onClick={() => onSubmit(values)}
          >
            Submit
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
