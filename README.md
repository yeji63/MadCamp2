# MadCamp2: 오늘뭐먹니?
> 매 끼니 같이 먹을 사람들을 모으고, 무엇을 먹을지 정할때 유용한 앱
### 팀원소개
> 윤예지, 정승안
### Development Environment
>Android Studio 4.1.1
>
>Node.js 
>>Mongo DB, Express, Socket io
---
## 프로젝트 설명
### APP LOGO
<img src = "https://user-images.githubusercontent.com/77380770/125474252-82f50cbd-eed6-40a3-b1ff-c7462f8a5977.png" width="200" height="200">
>앱 시작 화면으로 설정.

### 로그인 화면
<img src = "https://user-images.githubusercontent.com/77380770/125489523-80f93212-0450-4f45-913b-ca067037f5de.gif" width="200">
- 설명
> 카카오톡 로그인 sdk를 활용하여 사용자 가입, 로그인을 처리하였다. 
>
> 첫 사용자는 User database에 프로필 이미지와 카카오톡 닉네임이 등록된다.

### 메인 화면
<img src = "https://user-images.githubusercontent.com/77380770/125489536-72a46eed-ef61-401b-bfdc-fe4ab6c3fee0.gif" width="200">
- 설명
> 왼쪽 상단: 사용자 프로필, 로그아웃
>
> 오른쪽 상단: 새로운 밥팟 생성
>
> 리스트뷰 : 현재 생성되어있는 밥팟 리스트

- 기능
> 밥팟 리스트는 ListView로 구현했다.
>
> 리스트의 각 아이템을 길게 클릭하면, 모임 인원을 확인 기능과 모임 삭제 기능을 사용할 수 있다.
>> 모임 삭제는 밥팟을 생성한 사용자만 할 수 있도록 설정하였다.
>
> 리스트에는 모임의 대표 사진, 모집 인원수, 현재 참여 인원 수, 모임 날짜와 시간, 장소를 표기한다.
>
> ENTER 버튼을 통해 모임에 참여할 수 있다. 모집 인원이 모두 차면, 밥팟 참여가 불가능 하다.

### 모임 추가

<img src = "https://user-images.githubusercontent.com/77380770/125490165-69e43ec2-a7b4-4a3d-9ff8-f07546dc48a6.gif" width="200">

- 설명
>메인화면에서 NEW 버튼을 누르면 창이 뜬다.
>
>사진은 갤러리에서 추가할 수 있다.
>
>장소는 구글 지도에 등록된 장소를 검색하여 선택할 수 있다.
>
>모으는 최대 인원수와 만나는 시각도 정한다.


- 기능
> Google Places API 사용했다.
>> 구글 지도 자동완성 기능
>

### 모임 상세
<img src = "https://user-images.githubusercontent.com/77380770/125490253-c253a8a6-dab9-4a41-bc27-9d4c86092c11.gif" width="200"><img src = "https://user-images.githubusercontent.com/77380770/125489978-289fbf50-4955-4d57-beea-03ef9a0203bf.gif" width="200"> <img src = "https://user-images.githubusercontent.com/77380770/125490066-55c0e538-d6d4-4f13-8c9c-1df26a3cba91.gif" width="200">
- 설명
> 화면 상단: 대표 사진, 날짜, 시간, 장소 표시
>
> 화면 중단: 모임 장소 위치 (구글지도 자동완성 기능)
>
> 화면 하단: 참여자끼리 이용 가능한 **채팅**, **정산** 기능
>> 채팅: 각 밥팟마다 방이 만들어져 채팅이 이루어진다.
>>
>> 정산: 총 금액과 인원 수가 입력되고 화살표를 누르면, 정산되어 1인 당 금액이 계산된다.
>
> 화면 맨 아래에 모임 나가기 버튼 구현

- 기능
> Google Maps API 사용했다.
>
> 채팅이미지 누르면 채팅창으로 이동한다.
>> Socket.io와 Mongo DB를 이용하여 구현하였다.
>
> 정산이미지 누르면 정산창으로 이동한다.
