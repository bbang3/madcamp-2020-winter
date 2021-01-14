# madcamp_2_app

### Healthgram

친구들과 운동에 관한 추억을 공유하고 나눌 수 있는 SNS 앱.

##### <로그인 및 회원가입>
   <img width="300" src="https://user-images.githubusercontent.com/29402508/104631232-52190800-56df-11eb-8276-ef8a50f57ef4.png"> <img width="300" src="https://user-images.githubusercontent.com/29402508/104631237-534a3500-56df-11eb-8284-92a0b422c904.png">
로그인은 Facebook 로그인과 앱 자체 로그인 2가지 방법이 있다. Sign up을 눌러 회원가입을 할 수 있다.
##### <메인 페이지>
   <img width="300" src="https://user-images.githubusercontent.com/29402508/104631232-52190800-56df-11eb-8276-ef8a50f57ef4.png"> <img width="300" src="https://user-images.githubusercontent.com/29402508/104631237-534a3500-56df-11eb-8284-92a0b422c904.png">
로그인이 끝나면 세 가지 탭으로 구성된 페이지가 나온다.

1. Home 탭 (뉴스피드)
   <img width="300" src="https://user-images.githubusercontent.com/29402508/104631242-534a3500-56df-11eb-902c-19079b99c7a3.png">
   내가 팔로잉하는 친구들의 게시물 목록을 작성일 순으로 보여준다. DB의 Post 콜렉션을 참조하여 팔로잉 중인 친구의 포스트만 가져온다.
   게시물 목록의 최상단에서 스와이프하면 게시물이 갱신되고, 새로운 게시물이 있다면 다시 로드한다.

2. Following 탭
   <img width="300" src="https://user-images.githubusercontent.com/29402508/104631245-53e2cb80-56df-11eb-94c3-bb30820e6078.png"> <img width="300" src="https://user-images.githubusercontent.com/29402508/104631247-547b6200-56df-11eb-8810-57ad463f357e.png">
   팔로잉할 친구를 선택한다. 기본 화면에서는 현재 팔로잉 중인 친구 목록이 보여지고, 팔로잉 중인 친구들을 언팔로우 할 수 있는 기능을 제공한다.
   또한, 우측 하단의 플로팅액션버튼을 클릭하면, 휴대폰의 연락처를 불러와 해당 번호로 앱에 가입한 유저와 그렇지 않은 유저를 같이 보여준다. 가입한 유저는 팔로우/언팔로우 할 수 있고, 가입하지 않은 유저에게는 앱 초대 문자를 보낼 수 있다.
3. My Page 탭
   <img width="300" src="https://user-images.githubusercontent.com/29402508/104631250-5513f880-56df-11eb-9ecf-1af24c01cf81.png"> <img width="300" src="https://user-images.githubusercontent.com/29402508/104631252-5513f880-56df-11eb-99c2-fc7a3ddaa437.png">
   나의 프로필 사진과 포스트를 꾸밀 수 있는 탭이다. 먼저 프로필 사진을 터치하면 갤러리에서 원하는 이미지로 프로필 사진을 설정할 수 있다. 또한 포스트도 추가할 수 있다. 포스트는 이미지 1장과 텍스트로 구성되며, 서버에 전송하면 DB에 해당 정보가 기록된다. 이때 등록된 포스트들은 나를 팔로잉하는 유저들의 Home 탭에 노출된다.
