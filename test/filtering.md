# 테스트 (임시용 휘갈긴 느낌)

---

* 안드로이드 스튜디오 내에서 필터링 처리 -> 파이썬 제외 
* 보수및 코드의 간결화 (네트워크 요청필요없음.)
* json 파일이 있으므로 SQLite 또한 사용 불필요

---

## 1. assets 디렉토리에 stores.json 파일을 저장

## 2. JSON 파일 로드 (임시)
```java
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public List<Store> loadStoresFromJSON(Context context) {
    List<Store> stores = new ArrayList<>();
    try {
        InputStream is = context.getAssets().open("stores.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder jsonString = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonString.append(line);
        }
        JSONArray jsonArray = new JSONArray(jsonString.toString());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String name = jsonObject.getString("name");
            String address = jsonObject.getString("address");
            String roadAddress = jsonObject.getString("roadAddress");
            double latitude = jsonObject.getDouble("latitude");
            double longitude = jsonObject.getDouble("longitude");
            JSONArray categoryArray = jsonObject.getJSONArray("category");
            List<String> categories = new ArrayList<>();
            for (int j = 0; j < categoryArray.length(); j++) {
                categories.add(categoryArray.getString(j));
            }
            stores.add(new Store(name, address, roadAddress, latitude, longitude, categories));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return stores;
}

```

## 3. 카테고리를 기반 매장 필터링 메서드

```java
  public List<Store> filterStores(List<Store> stores, List<String> selectedCategories) {
    List<Store> filteredStores = new ArrayList<>();
    for (Store store : stores) {
        for (String category : selectedCategories) {
            if (store.getCategories().contains(category)) {
                filteredStores.add(store);
                break; // 중복 방지
            }
        }
    }
    return filteredStores;
}

```

## 4. Store 클래스

```java
    public class Store {
    private String name;
    private String address;
    private String roadAddress;
    private double latitude;
    private double longitude;
    private List<String> categories;

    public Store(String name, String address, String roadAddress, double latitude, double longitude, List<String> categories) {
        this.name = name;
        this.address = address;
        this.roadAddress = roadAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.categories = categories;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getRoadAddress() {
        return roadAddress;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public List<String> getCategories() {
        return categories;
    }
}
  
```


