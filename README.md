
> 💡 **자락 (JARAK)**: **자락**은 “**옷자락**”을 의미하는 우리말로, 이는 다양한 패션 아이템을 선택할 수 있는 온라인 쇼핑 플랫폼이라는 의미를 담고 있습니다.<br><br>
주제 : 다양한 브랜드의 의류 상품들을 하나의 쇼핑몰에서 모아볼 수 있는 종합형 의류 쇼핑몰. 모든 사람들이 보다 쉽고 편리하게 쇼핑할 수 있는 웹 사이트
> <br><br>
설명 : 이 웹사이트는 다양한 브랜드의 의류와 악세사리를 제공하여 여러 성별과 연령층의 사용자들이 편리하게 옷을 구매할 수 있는 플랫폼입니다.<br>
이 웹사이트는 관리자가 회원, 상품, 카테고리 등을 관리 할 수 있는 확장성과 유연성 높은 기능을 제공합니다.

---

## 사이트 이름 & 로고

- **자락 (JARAK)**

![메인로고 2](https://github.com/user-attachments/assets/db84075d-8405-4e84-8c16-125313931329)

## 서버 주소
- http://34.22.69.4/
---

## 기술 스택
### **Backend**

- Java
- Spring Boot
- Spring Data JPA
- Spring Security
- Java JWT
- Swagger (Springdoc)

### Frontend

- React
- react-bootstrap
- react-router-dom
- react-toastify
- axios

### Database

- MySQL

### Other

- AWS S3

### 배포

- Google Cloud Platform (GCP)

### 협업 도구

- GitLab: 코드 관리 및 버전 관리
- Notion: 문서 공유 및 협업

---
## 테스트 계정

- ***관리자 계정***

|  | **Id** | **Password** |
| --- | --- | --- |
| *관리자* | admin | 1235 |
- ***회원 계정***

|  | **Id** | **Password** |
| --- | --- | --- |
| *회원01* | user01 | 6666 |
| *회원02* | user02 | 6666 |
| *회원03* | user03 | 6666 |
| *회원04* | user04 | 6666 |

---

## ERD
<img width="1309" alt="ERD(2)" src="https://github.com/user-attachments/assets/cddf73d8-776f-425a-8709-4a45f39b5103">

---
## 피그마 (와이어프레임)

<img width="1269" alt="와이어 프레임" src="https://github.com/user-attachments/assets/8ecd00b2-49d4-4a5c-a151-7da0329efbc0">
---
## 서비스 기능
### [회원 도메인]
- 회원 가입
-  로그인
- 로그아웃
- 회원 CRUD
- 주소 CRUD
- 관리자 기능
    - 카테고리 추가
    - 상품추가
    - 회원 탈퇴처리

### [상품 도메인]
- 상품 CRUD
- 상품 디테일 CRUD

### [리뷰 도메인]
- 리뷰 CRUD

### [장바구니 도메인]
- 장바구니 CRUD
- cart의 백엔드 코드는 작성 돼있으나 로컬 스토리지 사용으로 프론트 연결을 하지 않아서
  백엔드 api 동작은 하지 않습니다.

### [주문 도메인]
- 주문 결제
- 주문 완료
- 주문 조회
- 주문 상세
- 배송 정보 수정
- 관리자 주문 관리

### [카테고리 도메인]
- 카테고리 CRUD
- 브랜드 CRUD

---
## 서비스 화면
### - 회원가입, 정보수정, 회원탈퇴
![회원가입,정보수정,회원탈퇴](https://github.com/user-attachments/assets/a401ac99-3a2a-4ebd-a581-860674f3aac6)


### - 상품 등록
<img width="1271" alt="스크린샷 2024-09-19 오후 4 33 05" src="https://github.com/user-attachments/assets/bd51b3f1-5758-4820-981e-27cbd3f29a16">


<img width="1340" alt="스크린샷 2024-09-19 오후 4 33 27" src="https://github.com/user-attachments/assets/d2ea8fea-f4f2-43b5-a298-556338579bdf">

### - 상품 재고 등록
<img width="998" alt="스크린샷 2024-09-19 오후 4 43 41" src="https://github.com/user-attachments/assets/949869c7-5099-45a5-85f0-7759320773d1">
<img width="1089" alt="스크린샷 2024-09-19 오후 4 43 50" src="https://github.com/user-attachments/assets/52cb60f8-428c-42f3-89b5-713708707993">


### - 리뷰 작성
<img width="1102" alt="스크린샷 2024-09-19 오후 4 48 25" src="https://github.com/user-attachments/assets/427a7b13-400f-4e75-9bea-2fadd22b8065">


### - 브랜드 등록
<img width="1042" alt="스크린샷 2024-09-19 오후 4 54 05" src="https://github.com/user-attachments/assets/42af352e-1371-455d-9309-35ed2d13a50f">

### - 메인 카테고리 등록
<img width="1194" alt="스크린샷 2024-09-19 오후 8 38 26" src="https://github.com/user-attachments/assets/319dc9f0-5e1a-4812-bc24-fe159ee80bb5">

### - 서브 카테고리 등록
<img width="1163" alt="스크린샷 2024-09-19 오후 8 39 53" src="https://github.com/user-attachments/assets/8e82b20e-752a-4fdb-b394-152027d567f5">

### - 장바구니
<img width="1061" alt="스크린샷 2024-09-19 오후 4 56 41" src="https://github.com/user-attachments/assets/9a7df081-711a-4977-99e7-936abb3d472b">

---
## 깃 컨벤션

## Branch

---

### 종류

- `main`: 바로 product로 release(배포)할 수 있는 브랜치
- `dev(develop)`: 출시를 위해 개발하는 브랜치
    - `feat/{기능명}`: 새로운 기능 개발하는 브랜치
    - `refactor/{기능명}`: 개발된 기능을 리팩터링하는 브랜치

### 예시

- `dev/feat/login`
- `dev/feat/register`

## Commit

---
### 제목

제목에는 변경 사항에 대한 간결한 설명 포함하기

### `<type>`

- feat : 기능 (새로운 기능)
- fix : 버그 (버그 수정)
- refactor : 리팩토링
- design : CSS 등 사용자 UI 디자인 변경
- *comment : 필요한 주석 추가 및 변경*
- *style : 스타일 (코드 형식, 세미콜론 추가: 비즈니스 로직에 변경 없음)*
- docs : 문서 수정 (문서 추가, 수정, 삭제, README)
- test : 테스트 (테스트 코드 추가, 수정, 삭제: 비즈니스 로직에 변경 없음)
- chore : 기타 변경사항 (빌드 스크립트 수정, assets, 패키지 매니저 등)
- init : 초기 생성
- rename : 파일 혹은 폴더명을 수정하거나 옮기는 작업만 한 경우
- remove : 파일을 삭제하는 작업만 수행한 경우