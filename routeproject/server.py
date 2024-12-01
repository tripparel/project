from flask import Flask, request, jsonify
from utils.filtering import filter_stores

app = Flask(__name__)

# 매장 데이터 파일 경로
STORE_FILE = "store.json"

@app.route("/filter_stores", methods=["POST"])
def filter_stores_endpoint():
    """
    사용자 요청에 따라 매장을 필터링하고 결과를 JSON 형식으로 반환.
    """
    try:
        # 클라이언트로부터 받은 요청 데이터
        user_data = request.json
        user_category = user_data.get("category")  # 사용자 선호 카테고리

        # 필터링 수행
        filtered_stores = filter_stores(user_category, STORE_FILE)

        # 결과 반환
        return jsonify(filtered_stores), 200

    except Exception as e:
        # 오류 처리
        return jsonify({"error": str(e)}), 500

if __name__ == "__main__":
    app.run(debug=True, host="0.0.0.0", port=5000)
