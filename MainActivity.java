package com.example.tripparel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import com.skt.tmap.TMapData;
import com.skt.tmap.TMapPoint;
import com.skt.tmap.TMapTapi;
import com.skt.tmap.TMapView;
import com.skt.tmap.poi.TMapPOIItem;
import com.skt.tmap.overlay.TMapMarkerItem;
import com.skt.tmap.overlay.TMapPolyLine;
import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private TMapView tMapView;
    private TMapData tMapData;
    private TMapTapi tMapTapi;
    private ArrayList<TMapPOIItem> poiItems;
    private Bitmap markerIcon, pointIcon;

    // UI 요소 참조
    private EditText editTextStartPoint;
    private EditText editTextEndPoint;
    private Button buttonSearchPOI;
    private Button buttonFindRoute;
    private ListView listViewPOI;
    private TMapPoint startPoint, endPoint;  // 출발지, 목적지 좌표 저장
    private boolean isStartPointSelected = false;  // 출발지 설정 여부 확인용 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 아이콘 초기화
        markerIcon = getBitmapFromResource(R.drawable.marker_icon, 16);
        pointIcon = getBitmapFromResource(R.drawable.point_icon, 8);

        // TMap 초기화
        initializeTMapView();

        // UI 요소 초기화
        editTextStartPoint = findViewById(R.id.editTextStartPoint);
        editTextEndPoint = findViewById(R.id.editTextEndPoint);
        buttonSearchPOI = findViewById(R.id.buttonSearchPOI);
        buttonFindRoute = findViewById(R.id.buttonFindRoute);
        listViewPOI = findViewById(R.id.listViewPOI);

        // POI 검색 버튼 클릭 시
        buttonSearchPOI.setOnClickListener(v -> searchNearbyPOI(editTextStartPoint.getText().toString().trim(), tMapView.getCenterPoint()));

        // POI 리스트 아이템 클릭 시
        listViewPOI.setOnItemClickListener((parent, view, position, id) -> {
            TMapPOIItem selectedPOI = poiItems.get(position);
            String selectedPOIName = selectedPOI.getPOIName();
            TMapPoint selectedPoint = selectedPOI.getPOIPoint();

            // 첫 번째 클릭은 출발지, 두 번째 클릭은 목적지로 설정
            if (!isStartPointSelected) {
                startPoint = selectedPoint;
                editTextStartPoint.setText(selectedPOIName); // 출발지 설정
                isStartPointSelected = true;
                showToast("출발지가 설정되었습니다.");
            } else {
                endPoint = selectedPoint;
                editTextEndPoint.setText(selectedPOIName); // 목적지 설정
                showToast("목적지가 설정되었습니다.");
            }

            // 출발지와 목적지가 모두 설정되면 경로 찾기 버튼 활성화
            if (startPoint != null && endPoint != null) {
                buttonFindRoute.setEnabled(true); // 경로 찾기 버튼 활성화
            }
        });

        // 경로 찾기 버튼 클릭 시
        buttonFindRoute.setOnClickListener(v -> {
            if (startPoint != null && endPoint != null) {
                // 로그로 startPoint, endPoint 확인
                Log.d("Start Point", "Longitude: " + startPoint.getLongitude() + ", Latitude: " + startPoint.getLatitude());
                Log.d("End Point", "Longitude: " + endPoint.getLongitude() + ", Latitude: " + endPoint.getLatitude());

                // 경로 안내 함수 호출
                searchWalkingRouteToEnd(startPoint, endPoint);

                // 경로 찾기 버튼 클릭 후 나머지 UI 숨기기
                hideUIElements();

                // 출발지, 목적지 마커만 보이게 하기
                showRouteMarkers();
            } else {
                showToast("출발지와 목적지를 모두 선택해주세요.");
            }
        });
    }

    // TMapView 초기화
    private void initializeTMapView() {
        tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey("8FjquJWLby2TBpTZHjjYq3HSTW63ag3P75zZCFEw"); // API 키 설정
        FrameLayout container = findViewById(R.id.tmapViewContainer);
        container.addView(tMapView);

        tMapData = new TMapData();
        tMapTapi = new TMapTapi(this);

        // TMapView 초기화가 완료되었을 때 호출되는 리스너 추가
        tMapView.setOnMapReadyListener(() -> {
            // 성수역 좌표 설정
            TMapPoint defaultPoint = new TMapPoint(37.544575, 127.055961); // 성수역
            tMapView.setCenterPoint(defaultPoint.getLongitude(), defaultPoint.getLatitude());
            tMapView.setLocationPoint(defaultPoint.getLongitude(), defaultPoint.getLatitude()); // 현재 위치로 설정

            addMarker("현재 위치", "성수역", defaultPoint, pointIcon);
            tMapView.setZoomLevel(15); // Zoom 레벨 설정
        });
    }

    // POI 검색
    private void searchNearbyPOI(String keyword, TMapPoint location) {
        tMapData.findAroundNamePOI(location, keyword, 5000, 30, poiItems -> runOnUiThread(() -> {
            if (poiItems == null || poiItems.isEmpty()) {
                showToast("검색 결과가 없습니다.");
                return;
            }
            this.poiItems = new ArrayList<>(poiItems);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getPOIItemDescriptions(poiItems));
            listViewPOI.setAdapter(adapter);
            listViewPOI.setVisibility(View.VISIBLE);

            // 모든 POI 마커를 표시
            for (TMapPOIItem poi : poiItems) {
                addMarker(poi.getPOIName(), poi.getPOIAddress(), poi.getPOIPoint(), markerIcon);
            }
        }));
    }

    // POI에 대한 마커 추가
    private void addMarker(String title, String subtitle, TMapPoint point, Bitmap icon) {
        TMapMarkerItem marker = new TMapMarkerItem();
        marker.setId(UUID.randomUUID().toString());
        marker.setTMapPoint(point);
        marker.setCalloutTitle(title);
        marker.setCalloutSubTitle(subtitle);
        marker.setCanShowCallout(true);
        marker.setIcon(icon);

        tMapView.addTMapMarkerItem(marker);
    }

    // POI 목록의 간단한 설명 생성
    private ArrayList<String> getPOIItemDescriptions(ArrayList<TMapPOIItem> poiItems) {
        ArrayList<String> descriptions = new ArrayList<>();
        for (TMapPOIItem poi : poiItems) {
            descriptions.add(poi.getPOIName() + " - " + poi.getPOIAddress());
        }
        return descriptions;
    }

    // 보행자 경로 찾기
    private void searchWalkingRouteToEnd(TMapPoint startPoint, TMapPoint endPoint) {
        tMapData.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, startPoint, endPoint, polyLine -> {
            runOnUiThread(() -> {
                if (polyLine == null) {
                    showToast("경로를 찾을 수 없습니다.");
                    return;
                }

                // 경로 색상 및 선 너비 설정
                polyLine.setLineColor(0xFF00FF00); // 경로 색상 (녹색)
                polyLine.setLineWidth(10);          // 경로 선 너비

                // 경로 지도에 추가
                tMapView.addTMapPolyLine(polyLine);
                showToast("보행자 경로가 지도에 표시되었습니다.");
            });
        });
    }

    // 이미지 리소스를 비트맵으로 변환
    private Bitmap getBitmapFromResource(int resId, int scaleFactor) {
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), resId);
        int newWidth = originalBitmap.getWidth() / scaleFactor;
        int newHeight = originalBitmap.getHeight() / scaleFactor;
        return Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
    }

    // 출발지와 목적지 마커만 보이게 하기
    private void showRouteMarkers() {
        // 출발지와 목적지 마커만 보이게 하기
        tMapView.removeAllTMapMarkerItem();

        // 출발지 POI 이름 가져오기
        String startPointName = null;
        for (TMapPOIItem poi : poiItems) {
            if (poi.getPOIPoint().equals(startPoint)) {
                startPointName = poi.getPOIName(); // POI 이름 가져오기
                break;
            }
        }
        // 출발지 마커 추가 (POI 이름을 사용할 수 있으면 사용)
        if (startPointName != null) {
            addMarker(startPointName, "출발지", startPoint, pointIcon);
        } else {
            addMarker("출발지", "출발지", startPoint, pointIcon); // 이름을 찾을 수 없으면 기본값 사용
        }

        // 목적지 POI 이름 가져오기
        String destinationName = null;
        for (TMapPOIItem poi : poiItems) {
            if (poi.getPOIPoint().equals(endPoint)) {
                destinationName = poi.getPOIName(); // POI 이름 가져오기
                break;
            }
        }
        // 목적지 마커 추가 (POI 이름을 사용할 수 있으면 사용)
        if (destinationName != null) {
            addMarker(destinationName, "목적지", endPoint, pointIcon);
        } else {
            addMarker("목적지", "목적지", endPoint, pointIcon); // 이름을 찾을 수 없으면 기본값 사용
        }
    }




    // UI 요소 숨기기
    private void hideUIElements() {
        editTextStartPoint.setVisibility(View.GONE);
        editTextEndPoint.setVisibility(View.GONE);
        buttonSearchPOI.setVisibility(View.GONE);
        listViewPOI.setVisibility(View.GONE);
        buttonFindRoute.setVisibility(View.GONE);
    }

    // 토스트 메시지 출력
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
