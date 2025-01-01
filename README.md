BackTracking
---
### ⭐️ 2인 사이드 프로젝트
- 여행 경로 계획과 여행 기록 작성 기능을 제외한 모든 기능의 프론트엔드와 백엔드를 맡았고, 팀원과 함께 아키텍쳐 설계, 코드 구현까지 직접 수행했습니다.

[서비스 시연 클릭](https://www.youtube.com/watch?v=e_P6OvmqpAY)

---

> **전체 기능**
> 
- 회원 관리(회원가입, 로그인, 로그아웃, 회원탈퇴, 회원 정보 수정)
- 여행 경로 계획
- 여행 기록 작성
- 게시물 공유
- 팔로우, 팔로잉
- 좋아요, 쪽지
- 실시간 AI 관광지 추천

**저는 회원관리, 팔로우, 팔로잉, 좋아요, 쪽지, 실시간 AI 관광지 추천 기능을 맡았습니다.**

---

### **✓ 백엔드 구현**

> **로그인, 로그아웃 기능**
> 
- Access Token과 Refresh Token을 활용한 JWT 기반 인증 시스템을 구현했습니다.

> **이미지 저장 기능**
> 
- 회원 프로필 이미지와 여행 기록의 이미지 저장을 위해 NCP(Naver Cloud Platform) Object Storage와 AWS(Amazon Web Service) S3 API를 연동했습니다.

> **실시간 AI 관광지 추천 기능**
> 
- Spring Boot의 ChatClient를 사용하여 Prompt 객체를 통해 AI 모델에 메시지를 전송하고, 응답을 body에 담아 프론트엔드로 전송했습니다. (`gpt-4o-mini` 모델 사용)



### **✓ 프론트엔드 구현**

> **로그인 유지 기능**
> 
- Pinia를 활용하여 데이터를 전역적으로 관리하고 localStorage와 동기화하여 페이지 새로고침 시에도 로그인 상태를 유지하도록 구현했습니다.

> axios를 활용하여 async/await 기반의 비동기 통신을 구현했습니다.



### **✓ 사용한 기술들**
<img width="454" alt="image" src="https://github.com/user-attachments/assets/a06fffda-5043-47d3-88d1-fbcef6ac0e04" />
