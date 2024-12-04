## 📢 프로젝트 소개
이 서비스는 스트리밍 플랫폼 내에서 **영상 제작자에게 특화된 정산 및 통계 데이터를 제공하는 서비스**입니다.<br>
영상 제작자는 이 서비스를 통해 **업로드한 영상의 일간, 주간, 월간 수익과 통계 파악** 및 **자신의 콘텐츠 성과를 모니터링하고 파악**할 수 있습니다.<br>
<br>

## ✨ 프로젝트 기간 및 주요 기능
### 기간
> **2024.10 ~ 2024.11 (1개월)**

### 주요 기능
<table>
  <tr>
    <th>📡 스트리밍</th>
    <th>🧮 영상 수익 및 통계</th>
  </tr>
  <tr>
    <td>영상 재생</td>
    <td>일일 스트리밍이 발생한 영상에 대해 통계 및 정산</td>
  </tr>
  <tr>
    <td>30초 내 동일 IP, 계정 접속 시 어뷰징 방지</td>
    <td>영상별 일간, 주간, 월간 수익 조회</td>
  </tr>
  <tr>
    <td>영상 재생 종료</td>
    <td>일간, 주간, 월간 Top 5 동영상 조회</td>
  </tr>
</table>
<br>

## 📌 프로젝트 주요 경험

### 1. 대용량 시청 기록 통계 및 정산 배치 성능 개선
> 일일 스트리밍 발생 영상 체크, DB 인덱싱, JDBC Bulk Insert, Spring Batch Partitioning 단계별 개선을 통해 배치 성능 최적화

- 1차 최적화 : 일일 스트리밍 발생 영상만 선별하여 배치 실행 **[2시간 58분 → 1시간 7분 소요]** <br>
  - 유튜브 리서치에 따르면 일일 68% 영상은 조회수 미발생 전체 영상에서 조회수 미발생 영상 68% 제외 후 처리하여 약 62% 시간 단축
    
- 2차 최적화 : 영상 시청 로그 테이블에 복합 인덱스 적용하여 조회 성능 최적화 **[1시간 7분 → 2분 소요]** <br>
  - 시청 일자, 영상 아이디 순으로 복합 인덱스 설정을 통해 시청 로그 1천 건 조회 기준, 복합 인덱스 설정으로 조회 속도 99% 향상

- 3차 최적화 : JpaItemWriter에서 JDBC Bulk Insert로 전환하여 데이터 일괄 처리 **[2분 → 1분 2초 소요]** <br>
  - JDBC Bulk Insert 도입으로 대용량 데이터 일괄 처리 및 쓰기 성능 91% 개선 (1,900ms → 255ms)

- 4차 최적화 : Partitioning 도입으로 배치 병렬 처리 **[1분 2초 → 28초 개선]** <br>
  - Single Thread -> Multi Thread (Grid Size : 4) 전환으로 배치 소요 시간 약 30% 추가 단축
 
<br>

✅ 영상 1만 개, 시청 로그 1천만 건 기준 **98% 성능 개선 (2시간 58분 → 28초)** <br>

| 단계 | 처리 시간 |
|------|-----------|
| 최적화 전 | 2시간 58분 50초 |
| 1차 최적화 | 1시간 7분 28초 |
| 2차 최적화 | 2분 |
| 3차 최적화 | 1분 2초 |
| 4차 최적화 | 28초 |
<br>

✅ 영상 7만개, 시청 로그 1억건 추가 테스트 진행 **35% 성능 개선 (3분 39초 → 2분 23초)** `[DB 인덱스 최적화 적용 후 테스트]` 

| 단계 | 처리 시간 |
|------|-----------|
| 최적화 전 | 3분 39초 |
| 1차 최적화 | - |
| 2차 최적화 | - |
| 3차 최적화 | 2분 39초 |
| 4차 최적화 | 2분 23초 |
<br>

[**🔗 [상세내용] 스트리밍 통계·정산 배치 단계별 최적화**](https://github.com/younghyun-j/streaming-settlement-2024/wiki/%EC%8A%A4%ED%8A%B8%EB%A6%AC%EB%B0%8D-%ED%86%B5%EA%B3%84%C2%B7%EC%A0%95%EC%82%B0-%EB%B0%B0%EC%B9%98-%EB%8B%A8%EA%B3%84%EB%B3%84-%EC%B5%9C%EC%A0%81%ED%99%94)

<br>

### 2. 영상 조회수 업데이트 최적화
> Redis 캐싱과 JDBC Bulk Insert 적용을 통한 DB 부하 감소 및 성능 최적화
<br>

✅ **영상 아이디별 분 단위 조회수 카운트 캐싱**
- Redis 도입으로 조회수 카운팅을 위한 DB 부하 감소
- 성능 최적화를 통해 대규모 트래픽에도 안정적인 조회수 처리 가능
<br>

✅ **스케줄러를 통한 주기적인 조회수 Bulk Insert**
- JDBC Bulk Insert를 활용하여 영상별 조회수를 한 번에 업데이트
- 쿼리 수를 최소화하고 DB 업데이트 성능 최적화 
<br>

[**🔗 [상세내용] Redis 캐싱과 JDBC Bulk Insert를 통한 영상 조회수 업데이트 최적화**](https://github.com/younghyun-j/streaming-settlement-2024/wiki/Redis-%EC%BA%90%EC%8B%B1%EA%B3%BC-JDBC-Bulk-Insert%EB%A5%BC-%ED%86%B5%ED%95%9C-%EC%98%81%EC%83%81-%EC%A1%B0%ED%9A%8C%EC%88%98-%EC%97%85%EB%8D%B0%EC%9D%B4%ED%8A%B8-%EC%B5%9C%EC%A0%81%ED%99%94)

<br>

## ⚖️ 기술적 의사결정

- [**스트리밍 발생 영상 체크 동시성 처리에 Redisson을 선택한 이유**](https://github.com/younghyun-j/streaming-settlement-2024/wiki/%EC%8A%A4%ED%8A%B8%EB%A6%AC%EB%B0%8D-%EB%B0%9C%EC%83%9D-%EC%98%81%EC%83%81-%EC%B2%B4%ED%81%AC-%EB%8F%99%EC%8B%9C%EC%84%B1-%EC%B2%98%EB%A6%AC%EC%97%90-Redisson%EC%9D%84-%EC%84%A0%ED%83%9D%ED%95%9C-%EC%9D%B4%EC%9C%A0) <br>
- [**스트리밍 어뷰징 판단을 위한 Redis 활용**](https://github.com/younghyun-j/streaming-settlement-2024/wiki/%EC%8A%A4%ED%8A%B8%EB%A6%AC%EB%B0%8D-%EC%96%B4%EB%B7%B0%EC%A7%95-%ED%8C%90%EB%8B%A8%EC%9D%84-%EC%9C%84%ED%95%9C-Redis-%ED%99%9C%EC%9A%A9) <br>

<br>

## 🛠️ 기술 스택

- **Language** : ![Java](https://img.shields.io/badge/Java17-%23ED8B00.svg?style=square&logo=openjdk&logoColor=white) <br>
- **Framework** : <img src = "https://img.shields.io/badge/Springboot 3.3.5-6DB33F?&logo=springboot&logoColor=white"> <img src = "https://img.shields.io/badge/Spring Batch 5 -6DB33F?&logo=Spring&logoColor=white"> ![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=square&logo=Spring&logoColor=white) <br>
- **Build** : ![Gradle](https://img.shields.io/badge/Gradle%208-02303A.svg?style=square&logo=Gradle&logoColor=white)
- **Database** : <img src = "https://img.shields.io/badge/MySQL 8-4479A1?&logo=MySQL&logoColor=white"> <img src = "https://img.shields.io/badge/Redis-FF4438?&logo=redis&logoColor=white">
- **DevOps** :<img src = "https://img.shields.io/badge/Docker-2496ED?&logo=docker&logoColor=white">
- **IDE** : <img src = "https://img.shields.io/badge/Intellij Idea-000000?&logo=intellijidea&logoColor=white">
- **Version Control** : <img src = "https://img.shields.io/badge/Git-F05032?&logo=git&logoColor=white"> <img src = "https://img.shields.io/badge/Github-181717?&logo=github&logoColor=white">
<br>

## 📄 ERD
![image](https://github.com/user-attachments/assets/6d36a014-7f49-407a-9ccb-957d691729eb)
