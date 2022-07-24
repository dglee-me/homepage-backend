# 홈페이지 백엔드 서버(SpringBoot)

## History
### 프로젝트 생성 및 형상관리 적용
```
# git 초기화
git init
git config --local user.email dglee.dev@gmail.com
git config --local user.name dglee-me
git config --local core.autocrlf true input     # For mac

# 로컬 레포지토리 생성 및 원격 깃 레포지토리 동기화
git remote add origin https://github.com/dglee-me/homepage-backend.git
git branch -M master

# 스테이징
git add .
git commit -m "homepage backend server first commimt"
git push origin master
```