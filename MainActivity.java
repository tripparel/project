package com.example.tripparel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.skt.tmap.TMapData;
import com.skt.tmap.TMapPoint;
import com.skt.tmap.TMapTapi;
import com.skt.tmap.TMapView;
import com.skt.tmap.poi.TMapPOIItem;
import com.skt.tmap.overlay.TMapMarkerItem;

import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private TMapView tMapView;
    private ArrayList<TMapPOIItem> poiItems;
    private Bitmap markerIcon, pointIcon;
    private TMapTapi tMapTapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 아이콘 초기화
        markerIcon = getBitmapFromResource(R.drawable.marker_icon, 16);
        pointIcon = getBitmapFromResource(R.drawable.point_icon, 8);

        initializeTMapView();
        setupSearch();
        setupShowMarkersButton();
        setupTMapAppButton();
    }

    private void initializeTMapView() {
        tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey("8FjquJWLby2TBpTZHjjYq3HSTW63ag3P75zZCFEw");

        FrameLayout container = findViewById(R.id.tmapViewContainer);
        container.addView(tMapView);

        tMapView.setOnMapReadyListener(() -> {
            TMapPoint centerPoint = new TMapPoint(37.544575, 127.055961); // 성수역
            tMapView.setCenterPoint(centerPoint.getLongitude(), centerPoint.getLatitude());
            tMapView.setLocationPoint(centerPoint.getLongitude(), centerPoint.getLatitude()); // 현재 위치로 설정
            tMapView.setZoomLevel(15);
            addMarker("현재 위치", "성수역", centerPoint, pointIcon);
            searchPOI("", centerPoint); // 주변 POI 로드
        });
    }

    private void setupSearch() {
        EditText editTextKeyword = findViewById(R.id.editTextKeyword);
        Button buttonSearch = findViewById(R.id.buttonSearch);

        buttonSearch.setOnClickListener(v -> {
            String keyword = editTextKeyword.getText().toString().trim();
            if (keyword.isEmpty()) {
                showToast("검색어를 입력하세요.");
            } else {
                searchPOI(keyword, tMapView.getCenterPoint());
            }
        });
    }

    private void setupShowMarkersButton() {
        Button buttonShowMarkers = findViewById(R.id.buttonShowMarkers);

        buttonShowMarkers.setOnClickListener(v -> {
            if (poiItems == null || poiItems.isEmpty()) {
                showToast("표시할 POI 정보가 없습니다.");
            } else {
                displayPOIInfo();
            }
        });
    }

    private void setupTMapAppButton() {
        Button buttonOpenTMapApp = findViewById(R.id.buttonOpenTMapApp);

        buttonOpenTMapApp.setOnClickListener(v -> {
            tMapTapi = new TMapTapi(this);

            if (tMapTapi.isTmapApplicationInstalled()) {
                TMapPoint centerPoint = tMapView.getCenterPoint();
                boolean success = tMapTapi.invokeRoute("성수역", (float) centerPoint.getLongitude(), (float) centerPoint.getLatitude());
                if (success) {
                    showToast("TMap에서 경로 안내를 시작합니다.");
                } else {
                    showToast("경로 안내를 시작하지 못했습니다.");
                }
            } else {
                showToast("TMap 어플이 설치되어 있지 않습니다. 설치가 필요합니다.");
            }
        });
    }

    private void searchPOI(String keyword, TMapPoint location) {
        new TMapData().findAroundNamePOI(location, keyword, 5000, 10, poiItems -> runOnUiThread(() -> {
            if (poiItems == null || poiItems.isEmpty()) {
                showToast(keyword.isEmpty() ? "주변에 POI가 없습니다." : "검색 결과가 없습니다.");
                return;
            }
            this.poiItems = new ArrayList<>(poiItems);
            tMapView.removeAllTMapMarkerItem();
            for (TMapPOIItem poi : poiItems) {
                addMarker(poi.getPOIName(), poi.getPOIAddress(), poi.getPOIPoint(), markerIcon);
            }
            tMapView.setCenterPoint(poiItems.get(0).getPOIPoint().getLongitude(), poiItems.get(0).getPOIPoint().getLatitude());
            showToast(poiItems.size() + "개의 결과를 찾았습니다.");
        }));
    }

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

    private void displayPOIInfo() {
        StringBuilder poiInfo = new StringBuilder();
        for (TMapPOIItem poi : poiItems) {
            poiInfo.append("이름: ").append(poi.getPOIName()).append("\n")
                    .append("주소: ").append(poi.getPOIAddress()).append("\n")
                    .append("위도: ").append(poi.getPOIPoint().getLatitude()).append("\n")
                    .append("경도: ").append(poi.getPOIPoint().getLongitude()).append("\n\n");
        }

        ScrollView scrollView = new ScrollView(this);
        TextView textView = new TextView(this);
        textView.setText(poiInfo.toString());
        scrollView.addView(textView);

        new AlertDialog.Builder(this)
                .setView(scrollView)
                .setTitle("POI 정보")
                .setPositiveButton("확인", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private Bitmap getBitmapFromResource(int resId, int scaleFactor) {
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), resId);
        int newWidth = originalBitmap.getWidth() / scaleFactor;
        int newHeight = originalBitmap.getHeight() / scaleFactor;
        return Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}








































