import { Button, TextField } from "@material-ui/core";
import { GoogleApiWrapper, Map, Marker } from "google-maps-react";
import React, { useState } from "react";
import Geocode from "react-geocode";

Geocode.setApiKey(process.env.REACT_APP_GOOGLE_MAP_API);
Geocode.setLanguage("ko");

const FormPlace = ({ google, location, setLocation }) => {
  const onClick = (t, map, coord) => {
    const { latLng } = coord;
    const currentLat = latLng.lat();
    const currentLng = latLng.lng();

    Geocode.fromLatLng(currentLat, currentLng).then(
      (res) => {
        const address = res.results[0].formatted_address;
        setLocation({ lat: currentLat, lng: currentLng, address: address });
      },
      (err) => {
        setLocation({ lat: currentLat, lng: currentLng, address: "" });
        console.log(err);
      }
    );
  };

  return (
    <div>
      오른쪽 클릭하여 지도에서 선택 <br />
      주소: {location.address}
      <Map
        google={google}
        zoom={15}
        style={{ width: "70%", height: "70%" }}
        initialCenter={location}
        disableDefaultUI={true}
        onRightclick={onClick}
      >
        {location != null && (
          <Marker position={{ lat: location.lat, lng: location.lng }} />
        )}
      </Map>
    </div>
  );
};

export default GoogleApiWrapper({
  apiKey: `${process.env.REACT_APP_GOOGLE_MAP_API}`,
})(FormPlace);
