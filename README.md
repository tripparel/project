# Clothing Store Database Project

이 프로젝트는 **옷가게 더미 데이터**를 포함한 데이터베이스로, 주어진 카테고리에 따라 매장 정보를 제공합니다. 이 데이터베이스는 `SPA`, `명품`, `빈티지`, `캐주얼`, `스트릿`, `아메카지`, `고프코어`, `미니멀` 등 8가지 카테고리로 매장을 분류하여 제공합니다.

## 📂 파일 설명

- `ClothingStoresDB.sql`: 옷가게 더미 데이터베이스의 스키마와 샘플 데이터를 포함한 SQL 파일입니다. 이 파일을 사용하여 데이터베이스를 직접 생성할 수 있습니다.

## 🛠️ 설치 방법

1. **MySQL Workbench 또는 CLI로 데이터베이스 생성**:
   - MySQL Workbench를 열고 `ClothingStoresDB.sql` 파일을 실행하여 데이터베이스를 생성합니다.
   - 또는 CLI에서 아래 명령어를 사용하여 데이터베이스를 생성합니다:
     ```bash
     mysql -u [username] -p [database_name] < ClothingStoresDB.sql
     ```
   - `[username]`에는 MySQL 사용자 이름을, `[database_name]`에는 생성할 데이터베이스 이름을 입력합니다.

2. **테이블과 데이터 확인**:
   - MySQL Workbench에서 `SELECT * FROM ClothingStores;` 명령어를 실행하여 데이터베이스와 테이블이 올바르게 생성되었는지 확인합니다.

## 📋 데이터베이스 구조

`ClothingStores` 테이블의 주요 필드는 다음과 같습니다:
- `store_id` - 매장의 고유 ID
- `store_name` - 매장 이름
- `location` - 매장 위치
- `category` - 매장 카테고리 (예: SPA, 빈티지 등)

## 📌 사용 예시

- 이 데이터베이스는 특정 카테고리별로 매장을 조회하거나 경로 알고리즘에 사용하기에 적합합니다. 예를 들어, `스트릿` 카테고리의 모든 매장을 조회하려면 다음과 같이 쿼리를 사용할 수 있습니다:
  ```sql
  SELECT * FROM ClothingStores WHERE category = '스트릿';


파이썬 코드 예제입니다,

```python
import mysql.connector # 모듈 설치 (필수)

# MySQL 데이터베이스에 연결
def connect_to_db():
    try:
        connection = mysql.connector.connect(
            host='localhost',         # 데이터베이스 호스트 (로컬 환경에서는 'localhost')
            user='your_username',     # MySQL 사용자 이름
            password='your_password', # MySQL 비밀번호
            database='ClothingStoresDB' # 생성한 데이터베이스 이름
        )
        if connection.is_connected():
            print("Database connection successful!")
            return connection
    except mysql.connector.Error as err:
        print(f"Error: {err}")
        return None

# 카테고리별 매장 목록 조회 함수
def get_stores_by_category(connection, category):
    try:
        cursor = connection.cursor(dictionary=True)
        query = "SELECT * FROM ClothingStores WHERE category = %s"
        cursor.execute(query, (category,))
        stores = cursor.fetchall()
        return stores
    except mysql.connector.Error as err:
        print(f"Error: {err}")
        return []
    finally:
        cursor.close()

# 데이터베이스 연결 종료 함수
def close_connection(connection):
    if connection.is_connected():
        connection.close()
        print("Database connection closed.")

# 실행 예제
if __name__ == "__main__":
    # 데이터베이스 연결
    db_connection = connect_to_db()
    
    if db_connection:
        # 예: '스트릿' 카테고리의 매장을 조회합니다
        category = '스트릿'
        stores = get_stores_by_category(db_connection, category)
        
        print(f"Stores in category '{category}':")
        for store in stores:
            print(f"Store ID: {store['store_id']}, Name: {store['store_name']}, Location: {store['location']}")
        
        # 데이터베이스 연결 종료
        close_connection(db_connection)
