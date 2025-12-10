# 🐘Dongguk University Group Buying Platform
> **신뢰할 수 있는 동국대 학생 전용 공동구매 중개 플랫폼** > 먹튀 없는 안전한 거래를 위한 거래 상태 관리 및 인증 시스템

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?logo=springboot&logoColor=white)](https://spring.io/)
[![React](https://img.shields.io/badge/React-18.x-61DAFB?logo=react&logoColor=black)](https://reactjs.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql&logoColor=white)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

---

## 🎥 Project Demo
시스템의 실제 구동 영상을 확인해보세요. 이미지를 클릭하면 유튜브로 이동합니다.

[![Demo Video](https://img.youtube.com/vi/YOUTUBE_VIDEO_ID_HERE/maxresdefault.jpg)](https://www.youtube.com/watch?v=YOUTUBE_VIDEO_ID_HERE)

---

## 📝 Introduction
**"익명 거래의 불안함, 학교 인증으로 해결하다."**

본 프로젝트는 기존 대학가 공동구매에서 발생하는 **'먹튀(Fraud)'** 문제를 해결하기 위해 개발되었습니다. 동국대학교 웹메일 인증을 통과한 재학생만 이용할 수 있는 폐쇄형 플랫폼으로, 판매자와 구매자 간의 거래 프로세스(입금-확인-배송-수령)를 시스템적으로 제어하여 안전한 거래 환경을 제공합니다.

### 💡 Key Features
* **🔐 확실한 신원 인증**: `@dgu.ac.kr` 도메인 이메일을 통해서만 회원가입이 가능합니다.
* **🔄 투명한 거래 흐름 (State Machine)**: `입금대기` → `입금확인` → `배송중` → `거래완료`의 5단계 상태 관리로 거래 과정을 추적합니다.
* **🛡️ 먹튀 방지 시스템 (Anti-Fraud)**: 진행 중인 거래가 하나라도 남아있을 경우, 시스템적으로 **회원 탈퇴가 불가능**하도록 설계되었습니다.
* **✅ 구매자 중심 확정**: 물건을 수령한 구매자가 직접 '거래 확정'을 눌러야 거래가 최종 종료됩니다.
* **🤖 자동화 관리**: 마감 기한이 지난 공구 폼은 스케줄러(Scheduler)에 의해 매일 자정 자동으로 마감 처리됩니다.

---

## 🛠 Tech Stack

### Frontend
| Tech | Description |
| --- | --- |
| <img src="https://skillicons.dev/icons?i=react" width="40"/> **React** | 사용자 인터페이스(UI) 구축 및 SPA 구현 |
| <img src="https://skillicons.dev/icons?i=vite" width="40"/> **Vite** | 빠른 빌드 및 개발 환경 구성 |
| <img src="https://skillicons.dev/icons?i=css" width="40"/> **CSS Modules** | 컴포넌트별 스타일링 및 반응형 디자인 |
| **Axios** | REST API 비동기 통신 및 Interceptor 설정 |

### Backend
| Tech | Description |
| --- | --- |
| <img src="https://skillicons.dev/icons?i=java" width="40"/> **Java 17** | 주요 개발 언어 |
| <img src="https://skillicons.dev/icons?i=spring" width="40"/> **Spring Boot** | 백엔드 프레임워크 및 비즈니스 로직 구현 |
| **Spring Security** | JWT 기반 인증/인가 및 비밀번호 암호화(BCrypt) |
| **Spring Data JPA** | ORM 기반의 데이터베이스 접근 및 조작 |

### Database & Tools
* **Database:** MySQL 8.0 (AWS RDS / Local)
* **API Test:** Postman
* **Collaboration:** Git, GitHub, Miro

---

## 📊 System Architecture & ERD
본 프로젝트는 **3단계 정규화(3NF)**를 적용하여 데이터 중복을 최소화하고 무결성을 강화하였습니다.

[ERD Diagram]
<img width="400" alt="ERD Diagram" src="https://github.com/user-attachments/assets/151c3869-d1ab-4d20-b5b8-0ca801221779" />

* **Users:** 사용자 정보 및 학교 인증 관리
* **Forms:** 공동구매 게시글 및 판매자 정보 (인덱싱 적용)
* **Submissions:** 구매 참여 내역 및 결제 상태 (역정규화 적용)
* **Categories:** 상품 카테고리 관리

