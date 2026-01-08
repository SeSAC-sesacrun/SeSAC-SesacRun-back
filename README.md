# 🏃 SeSAC Run - 온라인 강의 플랫폼 백엔드

> **온라인 강의 플랫폼 + 학습 커뮤니티 통합 서비스**  
> 강사와 학생을 연결하고, 스터디 및 프로젝트 팀원 모집을 지원하는 통합 학습 플랫폼

<br/>

## 📋 목차

- [프로젝트 개요](#-프로젝트-개요)
- [기술 스택](#-기술-스택)
- [주요 기능](#-주요-기능)
- [시스템 아키텍처](#-시스템-아키텍처)

<br/>

## 👥 팀원

- **Frontend**: [Your Name]
- **Backend**: [Backend Developer]

## 🎯 프로젝트 개요

기획의도 (프론트 개발 설계? 포함해서 작성)

## 🛠 기술 스택

### Backend

![Java](https://img.shields.io/badge/Java-21-007396?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.9-6DB33F?style=flat-square&logo=spring-boot)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=flat-square&logo=spring-security)
![JPA](https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=flat-square)
![WebSocket](https://img.shields.io/badge/WebSocket-010101?style=flat-square)

### Database

![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=mysql&logoColor=white)

### Security & Auth

![JWT](https://img.shields.io/badge/JWT-000000?style=flat-square&logo=json-web-tokens)

### Payment

![PortOne](https://img.shields.io/badge/PortOne-5B47ED?style=flat-square)

### Build Tool

![Gradle](https://img.shields.io/badge/Gradle-02303A?style=flat-square&logo=gradle)

### Frontend

![Next.js](https://img.shields.io/badge/Next.js-16.1.0-000000?style=for-the-badge&logo=next.js&logoColor=white)
![React](https://img.shields.io/badge/React-19.2.3-61DAFB?style=for-the-badge&logo=react&logoColor=black)
![TypeScript](https://img.shields.io/badge/TypeScript-5.0-3178C6?style=for-the-badge&logo=typescript&logoColor=white)
![Tailwind CSS](https://img.shields.io/badge/Tailwind_CSS-4.0-38B2AC?style=for-the-badge&logo=tailwind-css&logoColor=white)

<br/>

### 핵심 기능

- 📚 **강의 학습**: 다양한 카테고리의 온라인 강의 수강
- 🤝 **러닝메이트 모집**: 함께 공부할 동료 찾기 / WebSocket 기반 실시간 채팅
- 💳 **안전한 결제**: 포트원 연동 결제 시스템

<br/>

## ✨ 주요 기능

### 1. 강의 관리 (Course)

- ✅ 강의 생성/조회/삭제
- ✅ 카테고리별 강의 필터링
- ✅ 키워드 검색 (제목, 설명)
- ✅ 인기 강의 조회 (수강생 기준)
- ✅ 섹션 및 강의 영상 계층 구조
- ✅ 페이징 처리 및 정렬

### 2. 사용자 인증 (Auth)

- ✅ JWT 기반 인증 시스템
- ✅ 회원가입 / 로그인
- ✅ Access Token
- ✅ Spring Security 통합

### 3. 장바구니 & 주문 (Cart & Order)

- ✅ 장바구니 담기/조회/삭제
- ✅ 주문 생성 및 조회
- ✅ 주문 상태 관리

### 4. 결제 & 환불 (Payment & Refund)

- ✅ 포트원 결제 연동
- ✅ 결제 검증 및 완료 처리
- ✅ 환불 요청 및 처리
- ✅ 환불 상태 관리

### 5. 러닝메이트 모집 (Recruitment)

- ✅ 모집 게시글 작성/조회/수정/삭제
- ✅ 카테고리별 필터링
- ✅ 모집 신청 및 승인/거절
- ✅ 모집 상태 관리 (모집중/마감)

### 6. 실시간 채팅 (Chat)

- ✅ WebSocket 기반 실시간 채팅
- ✅ 채팅방 생성 및 관리
- ✅ 메시지 전송 및 조회

<br/>

## 🏗 시스템 아키텍처

```
┌─────────────────────────────────────────────────────────────┐
│                         Client Layer                         │
│                    (React / Next.js)                         │
└─────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│                      API Gateway Layer                       │
│                   (Spring Security Filter)                   │
│                     - JWT Authentication                     │
│                     - CORS Configuration                     │
└─────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│                     Controller Layer                         │
│              - Request Validation                            │
│              - Response Formatting (ApiResponse)             │
└─────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│                      Service Layer                           │
│              - Business Logic                                │
│              - Transaction Management                        │
└─────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│                    Repository Layer                          │
│              - JPA Repository                                │
│              - Custom Queries                                │
└─────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│                      Database Layer                          │
│                        MySQL 8.0                             │
└─────────────────────────────────────────────────────────────┘

        External Services
┌──────────────────────────┐
│   PortOne Payment API    │
└──────────────────────────┘
```

<br/>
