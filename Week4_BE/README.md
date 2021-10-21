# madcamp4_back

### Category

| Method | URI       | Action               |
| ------ | --------- | -------------------- |
| GET    | /category | 카테고리 리스트 읽기 |

### Restaurant

| Method | URI                              | Action                                | 인증 | Status Code                               |
| ------ | -------------------------------- | ------------------------------------- | ---- | ----------------------------------------- |
| GET    | /restaurant                      | 모든 식당 리스트 읽기                 | X    |                                           |
| GET    | /restaurant?category=category_id | 특정 카테고리에 속한 식당 리스트 읽기 | X    | 400: category_id로 검색된 카테고리가 없음 |
| GET    | /restaurant/:restr_id            | 특정 식당 정보 읽기                   | X    | 400: restr_id로 검색된 식당이 없음        |
| POST   | /restaurant                      | 특정 식당 정보 추가                   | O    | 403: 관리자 또는 식당 주인 계정이 아님    |
| PUT    | /restaurant/:restr_id            | 특정 식당 정보 업데이트               | O    |                                           |
| DELETE | /restaurant/:restr_id            | 특정 식당 정보 삭제                   | O    |                                           |

### Authorization

| Method | URI                   | Action                                     | 인증 | Status code                                                                                                                              |
| ------ | --------------------- | ------------------------------------------ | ---- | ---------------------------------------------------------------------------------------------------------------------------------------- |
| POST   | /auth/register        | 회원가입                                   |      | 400: <br />- 관리자로 회원가입<br />- 정의되지 않은 role로 회원가입<br />- 이미 존재하는 ID<br />- Request가 User schema를 만족하지 않음 |
| POST   | /auth/login           | 로그인                                     |      | 400:<br />- ID 또는 PW가 틀림<br />- 일반 유저 계정인데 이메일 인증을 하지 않음                                                          |
| POST   | /auth/logout          | 로그아웃                                   |      |                                                                                                                                          |
| POST   | /auth/update-password | 초기 비밀번호 변경 (Restaurant Owner 한정) | O    | 500:<br />- Request와 관련 없는 서버 상의 오류                                                                                           |
| POST   | /auth/refresh-token   | Access token 재발급 (Refresh token 필요)   |      | 401:<br />- Request에 refresh token이 들어 있지 않음<br />- Refresh token이 유효하지 않음                                                |

### User

| Method | URI            | Action                                          | 인증 | Status code                                                                                                                                           |
| ------ | -------------- | ----------------------------------------------- | ---- | ----------------------------------------------------------------------------------------------------------------------------------------------------- |
| GET    | /user/:user_id | 특정 유저 정보 읽기                             | O    | 400:<br />- Request가 User schema를 만족하지 않음<br />403: <br />- 토큰에 든 ID와 user_id가 일치하지 않음 (자신의 정보에만 접근 가능, 관리자는 예외) |
| PUT    | /user/:user_id | 특정 유저 정보 업데이트<br />(user에 한해 가능) | O    | 400:<br />- Request가 User schema를 만족하지 않음<br />403: <br />- 토큰에 든 ID와 user_id가 일치하지 않음 (자신의 정보에만 접근 가능, 관리자는 예외) |
| DELETE | /user/:user_id | 특정 유저 정보 삭제                             | O    | 400:<br />- Request가 User schema를 만족하지 않음<br />403: <br />- 토큰에 든 ID와 user_id가 일치하지 않음 (자신의 정보에만 접근 가능, 관리자는 예외) |

### Comment

| Method | URI                                             | Action                | 인증 | Status code                                                                                                                                                                                                                    |
| ------ | ----------------------------------------------- | --------------------- | ---- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| GET    | /restaurant/:restr_id/comment                   | 모든 댓글 리스트 읽기 | O    | 400:<br />- restr_id로 검색된 식당이 없음<br />- Request가 Comment schema를 만족하지 않음                                                                                                                                      |
| POST   | /restaurant/:restr_id/comment                   | 댓글 추가             | O    | 400:<br />- restr_id로 검색된 식당이 없음<br />- 요청에 body 항목이 없음                                                                                                                                                       |
| PUT    | /restaurant/:restr_id/comment/:comment_id       | 특정 댓글 수정        | O    | 400:<br />- restr_id로 검색된 식당이 없음<br />- 검색된 식당에 comment_id를 가진 comment가 없음<br />- 요청에 body 항목이 없음<br />403:<br />- 토큰에 든 ID와 댓글 작성자 ID가 일치하지 않음 (자신이 작성한 댓글만 수정 가능) |
| DELETE | /restaurant/:restr_id<br />/comment/:comment_id | 특정 댓글 삭제        | O    | 400:<br />- restr_id로 검색된 식당이 없음<br />403: <br />- 토큰에 든 ID와 댓글 작성자 ID가 일치하지 않음 (자신이 작성한 댓글만 삭제 가능)                                                                                     |

#### 인증 실패시 - 401 (Unauthorized)

Access token이 유효하지 않아 인증에 실패한 경우 일괄적으로 401 응답 코드를 반환함.
아래의 이유 중 하나일 수 있음.

- 토큰이 만료됨
- 해당 토큰으로 유저를 식별할 수 없음
