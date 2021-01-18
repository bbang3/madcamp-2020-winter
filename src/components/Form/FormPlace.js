import { Button, TextField } from "@material-ui/core";
import { GoogleApiWrapper, Map, Marker } from "google-maps-react";
import React, { useState } from "react";

const FormPlace = ({ google, location, setLocation }) => {
  const defaultLocation = { lat: 36.37374155241465, lng: 127.35836653738268 }; // KAIST dormitory

  const onClick = (t, map, coord) => {
    const { latLng } = coord;
    const currentLat = latLng.lat();
    const currentLng = latLng.lng();

    setLocation({ lat: currentLat, lng: currentLng });
  };

  return (
    <div>
      오른쪽 클릭하여 지도에서 선택
      <Map
        google={google}
        zoom={15}
        style={{ width: "70%", height: "70%" }}
        initialCenter={location || defaultLocation}
        disableDefaultUI={true}
        onRightclick={onClick}
      >
        {location != null && <Marker position={location} />}
      </Map>
    </div>
  );
};

export default GoogleApiWrapper({
  apiKey: `${process.env.REACT_APP_GOOGLE_MAP_API}`,
})(FormPlace);
