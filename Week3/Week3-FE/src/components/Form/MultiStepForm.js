import { Button } from "@material-ui/core";
import axios from "axios";
import React, { Component, useState } from "react";
import { Link, NavLink, Redirect } from "react-router-dom";
import Confirm from "./Confirm";
import FormDate from "./FormDate";
import FormGroupDetails from "./FormGroupDetails";
import FormPlace from "./FormPlace";

const MultiStepForm = ({ user }) => {
  const [step, setStep] = useState(0);
  const [state, setState] = useState({
    groupSize: 1,
    skills: 5,
    intensity: 5,
    age: 20,
  });
  const [location, setLocation] = useState(
    {
      lat: 36.37374155241465,
      lng: 127.35836653738268,
      address: "대한민국 대전광역시 유성구 어은동 44",
    } // KAIST dormitory
  );
  const [date, setDate] = useState(
    new Date(Math.ceil(Date.now() / 1800000) * 1800000) // ceil to nearest 30 minutes
  );

  // Proceed to next step
  const nextStep = () => {
    console.log(step);
    setStep(step + 1);
  };

  // Go back to prev step
  const prevStep = () => {
    setStep(step - 1);
  };

  const onSubmit = async (values) => {
    try {
      console.log(values);
      const response = await axios.post(
        `${process.env.REACT_APP_BASE_URL}/match`,
        {
          ...values,
          userId: user.userId,
          category: "football",
        },
        { headers: { token: user.token } }
      );
      if (response.data.success) {
        setStep(step + 1);
      } else {
        alert(response.data.message);
      }
    } catch (error) {
      console.log(error);
      alert("매치 신청에 실패했습니다.");
      <Redirect to="/" />;
    }
  };

  const { groupSize, skills, intensity, age } = state;
  const values = {
    groupSize,
    skills,
    intensity,
    age,
    location,
    date,
  };

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
            date={date}
            setDate={setDate}
            prevStep={prevStep}
            nextStep={nextStep}
          />
          <FormPlace location={location} setLocation={setLocation} />
          <br />
          <Button
            color="primary"
            variant="contained"
            onClick={() => nextStep()}
            style={{ marginTop: 650 }}
          >
            Continue
          </Button>
          <Button
            color="secondary"
            variant="contained"
            onClick={() => prevStep()}
            style={{ marginTop: 650 }}
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
            style={{ marginTop: 650 }}
          >
            Submit
          </Button>
          <Button
            color="secondary"
            variant="contained"
            onClick={() => prevStep()}
            style={{ marginTop: 650 }}
          >
            Back
          </Button>
        </>
      );
    case 3:
      return (
        <>
          매치 신청이 성공적으로 완료되었습니다. 신청한 매치 정보는 마이
          페이지에서 확인해주세요.
          <Button color="primary">
            <Link to="/" style={{ textDecoration: "none" }}>
              Home
            </Link>
          </Button>
          <Button color="primary">
            <NavLink to="/mypage" style={{ textDecoration: "none" }}>
              My Page
            </NavLink>
          </Button>
        </>
      );
    default:
      return <>Error page</>;
  }
};

export default MultiStepForm;
