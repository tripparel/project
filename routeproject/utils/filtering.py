import json

def filter_stores(user_category, store_file="store.json"):
    """
    성수동 데이터를 기반으로 사용자 선호 카테고리에 맞는 매장을 필터링.

    :param user_category: 사용자 선호 카테고리
    :param store_file: 매장 데이터 파일 경로
    :return: 필터링된 매장 리스트
    """
    try:
        with open(store_file, "r", encoding="utf-8") as file:
            stores = json.load(file)

        # 성수동 매장 데이터 필터링
        filtered_stores = [
            {
                "Store Name": store["Store Name"],
                "Category": store["Category"],
                "Latitude": store["Latitude"] / 1e7,
                "Longitude": store["Longitude"] / 1e7,
                "Address": store["Road Address"],
                "Link": store["Link"]
            }
            for store in stores
            if user_category in store["Category"]
        ]

        return filtered_stores

    except Exception as e:
        raise RuntimeError(f"필터링 중 오류 발생: {e}")
