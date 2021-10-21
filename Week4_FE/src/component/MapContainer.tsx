import React, { useEffect, useState } from 'react';

declare global {
	interface Window {
		kakao: any;
	}
}

interface Props {
	fullAddress: string;
	name: string;
}

const MapContainer = (props: Props) => {
	const { kakao } = window;
	useEffect(() => {
		const container = document.getElementById('map'); //지도를 담을 영역의 DOM 레퍼런스
		const options = {
			//지도를 생성할 때 필요한 기본 옵션
			center: new window.kakao.maps.LatLng(33.450701, 126.570667), //지도의 중심좌표.
			level: 3, //지도의 레벨(확대, 축소 정도)
		};

		const map = new window.kakao.maps.Map(container, options); //지도 생성 및 객체 리턴

		// 주소-좌표 변환 객체를 생성합니다
		var geocoder = new kakao.maps.services.Geocoder();

		// 주소로 좌표를 검색합니다
		geocoder.addressSearch(props.fullAddress, function (result: any, status: any) {
			// 정상적으로 검색이 완료됐으면
			if (status === kakao.maps.services.Status.OK) {
				var coords = new kakao.maps.LatLng(result[0].y, result[0].x);

				// 결과값으로 받은 위치를 마커로 표시합니다
				var marker = new kakao.maps.Marker({
					map: map,
					position: coords,
				});

				// 인포윈도우로 장소에 대한 설명을 표시합니다
				var infowindow = new kakao.maps.InfoWindow({
					content: `<div style="width:150px;text-align:center;padding:6px 0;">${props.name}</div>`,
				});
				infowindow.open(map, marker);

				// 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
				map.setCenter(coords);
			}
		});
	}, [props.fullAddress]);

	return (
		<div className="App">
			<div id="map" style={{ width: '87vh', height: '50vh' }} />
		</div>
	);
};

export default MapContainer;
