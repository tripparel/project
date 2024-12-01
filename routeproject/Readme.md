
# 쇼핑 루트 추천 Python 서버

이 프로젝트는 사용자의 선호 조건(성별, 카테고리 등)에 따라 매장을 필터링하고, 
필터링된 결과를 안드로이드 스튜디오에서 활용할 수 있도록 JSON으로 반환하는 Flask 서버입니다.

## 주요 기능
1. 사용자의 조건(성별, 카테고리)에 따른 매장 필터링.
2. 안드로이드와 통합하여 TMap API와 함께 사용할 데이터를 제공.


```bash
project/ 
│ 
├── server.py # Flask 서버 메인 파일
├── store.json # 매장 데이터 (샘플) 
├── requirements.txt # Python 의존성 목록 
└── utils/ # 공용 모듈 폴더 
 ├── filtering.py # 매장 필터링 로직 
 └── routing.py # 경로 알고리즘 (필요 시)
```

- `server.py`: Flask 서버를 실행하여 안드로이드에서 HTTP 요청을 처리.
- `store.json`: 매장 정보 샘플 데이터.
- `utils/filtering.py`: 사용자의 조건에 따라 매장을 필터링하는 로직.

---

#### **c. 실행 방법**
```markdown
## 실행 방법

### 1. Python 환경 설정
1. Python 3.7 이상이 필요합니다.
2. 필요한 라이브러리를 설치합니다:
   ```bash
   pip install -r requirements.txt
   ```
### 2. 서버실행
1. Flask 서버를 실행합니다:

 ```bash
    python server.py
 ```

2. 서버는 기본적으로 http://127.0.0.1:5000에서 실행됩니다.
```
