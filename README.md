# 요즘 (Yozzm)

전화번호부, 갤러리, 날씨 등 일상에서 필수적인 기능들을 가진 앱.   

## 탭 1 - 전화번호부
<img width="300" src="https://user-images.githubusercontent.com/29402508/107553599-49e9b500-6c18-11eb-8c12-f7a1c1796af4.png">   
 
전화번호부 상에 등록된 정보를 가져와 보여준다.    
* Asc/desc 버튼으로 정렬 순서 조정이 가능하고, 상단의 검색 창에서 이름으로 전화번호를 검색할 수 있다.   
* 각 아이템을 누르면 상세 정보를 확인할 수 있으며, 전화 버튼을 누르면 전화를 걸 수 있다.   
* 우측 하단의 추가 버튼을 누르면 새로운 전화번호를 추가할 수 있다.   

위의 모든 동작은 휴대폰 내부 전화번호부와 연동된다.   

<img width="300" src="https://user-images.githubusercontent.com/29402508/107553625-4f46ff80-6c18-11eb-9c9a-b6effb70397b.png"> <img width="300" src="https://user-images.githubusercontent.com/29402508/107553621-4eae6900-6c18-11eb-9bfd-a50dba4bdc42.png">

1. 상세 정보 화면이다. 삭제 버튼을 누르면 해당 전화번호를 삭제할 수 있다.   
2. 전화번호 추가시의 화면이다. 이름과 전화번호를 입력하면 전화번호부에 새로운 번호가 등록된다.


## 탭 2 - 갤러리
<img width="300" src="https://user-images.githubusercontent.com/29402508/107553617-4e15d280-6c18-11eb-8193-d71e3fd92857.png">

사전에 등록된 이미지들을 그리드 형태로 보여주는 탭이다. 각 이미지를 클릭하면 상세 보기 페이지로 이동한다.

<img width="300" src="https://user-images.githubusercontent.com/29402508/107553615-4e15d280-6c18-11eb-8272-b722998795aa.png">

한 이미지만 크게 볼 수 있는 페이지이다. 좌우로 스크롤하면 이전/다음 이미지로 넘어갈 수 있다.   
좌측 상단의 버튼을 누르면 이미지 자르기 및 공유하기를 할 수 있다.

<img width="300" src="https://user-images.githubusercontent.com/29402508/107553613-4d7d3c00-6c18-11eb-99fe-4b3e1da5273e.png">

공유 버튼을 클릭하면 이미지를 Gmail, 문자 메시지 등으로 공유할 수 있다.   

<img width="300" src="https://user-images.githubusercontent.com/29402508/107553611-4ce4a580-6c18-11eb-8f0a-760935cc37a0.png"> <img width="300" src="https://user-images.githubusercontent.com/29402508/107564382-ad2e1400-6c25-11eb-9637-9a708357468d.png">

자르기 버튼을 클릭하면 이미지를 직사각형 형태로 잘라낼 수 있다. 잘라낸 이미지는 Share 버튼을 눌러 공유할 수 있다.    
(잘라낸 이미지가 갤러리 내에 따로 저장되지는 않는다.)

## 탭 3 - 날씨

<img width="300" src="https://user-images.githubusercontent.com/29402508/107553604-4a824b80-6c18-11eb-818e-668d036c1e73.png">

오늘의 날씨와 7일 간의 날씨 예보를 확인할 수 있다. 날씨 정보는 [OpenWeatherMap API](https://openweathermap.org/)에서 제공하는 데이터를 활용한다.

* 날씨 정보는 카드뷰로 나타나며, 각 카드뷰는 **색깔 책갈피, 일자, 날씨, 기온** 등의 정보로 구성되어 있다.
* 색깔 책갈피는 현재 기온에 따라 색깔을 다르게 하여 날씨를 직관적으로 확인할 수 있도록 한 기능이다.   
-15도 ~ 15도 범위 내의 기온에 대해, 기온이 높으면 빨간색, 낮으면 파란색을 띠도록 했다.
* 오늘 날씨에서는 날씨를 시간별로 상세하게 확인할 수 있다.

<img width="300" src="https://user-images.githubusercontent.com/29402508/107553608-4ce4a580-6c18-11eb-9585-9c95c355bed9.png">

날씨 예보 카드를 클릭하면 특정 날짜의 예보를 상세하게 볼 수 있는 페이지로 이동한다.   
날씨에 따라 배경이 달라지며, 일출/일몰시간, 강수확률, 습도 등의 상세 정보를 확인할 수 있다.
