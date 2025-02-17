# 카프카, 주피커 설치
- 프로젝트 루트 내에 docker-compose.yml 존재
- 터미널
  ```
  docker-compose up -d
  ```

# Docker - cli
- Docker 터미널
- 토픽 목록 확인  
    ```
    kafka-topics --list --bootstrap-server localhost:9092
    ```
- 결과 : 없음 (아직 아무것도 생성 안해서 첫 실행 후 결과 없음)
  

- 아래는 실행 안함
- 특정 토픽의 상세 정보
  ```
  kafka-topics --describe --topic test-topic --bootstrap-server localhost:9092
  ```
- 결과 :  

- consumer의 메세지 수신 대기 -> 메세지 송신되면 출력
  ```
  kafka-console-consumer --topic test-topic --from-beginning --bootstrap-server localhost:9092
  ```