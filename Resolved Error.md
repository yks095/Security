# Error log

1. 테스트를 실행하여 DB에 데이터가 추가될 때 마다, `ID`가 `AUTO_INCREMENT`되는 상황
  - `ALTER TABLE [TABLE] AUTO_INCREMENT = 1` 로 해결 [참조](https://amaze9001.tistory.com/28)
  - But, 계속해서 DDL을 적용하거나, 실제 필요한 데이터값이 있을 시 문제가 될 수 있음. -> 보다 더 정확한 해결 방법 필요!

2. 새 프로젝트 생성시 
   `IntelliJ “Initialization failed for 'https://start.spring.io' Please check URL, network and proxy settings” ` 에러 alert 발생
   - 
