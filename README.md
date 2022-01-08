# SpringBoot-Board-Practice

``` docker 상 Mysql Image 생성 및 root password 설정
 - 다른 docker 컨테이너와 충돌을 피하기 위해 3308Port 설정
docker run --name springboot-mysql-container -e MYSQL_ROOT_PASSWORD="password" -d -p 3308:3306 mysql:latest
