import { Box, Paper } from "@material-ui/core";
import React from "react";
import MultiStepForm from "../components/Form/MultiStepForm";

const MatchMake = ({ user }) => {
  return (
    <Box width={1 / 2}>
      <Paper>
        <MultiStepForm user={user} />
      </Paper>
    </Box>
  );
};

export default MatchMake;
