package com.example.tripparel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.skt.tmap.TMapData;
import com.skt.tmap.TMapPoint;
import com.skt.tmap.overlay.TMapPolyLine;
import com.skt.tmap.TMapView;
import com.skt.tmap.overlay.TMapMarkerItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private TMapView tMapView;
    private TMapData tMapData;
    private ArrayList<Store> filteredStores = new ArrayList<>();
    private Bitmap markerIcon;
    private Bitmap pointIcon;
    private DrawerLayout drawerLayout;
    private LinearLayout sidebar;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            initializeDrawerLayout(toolbar);
            initializeIcons();
            initializeTMapView();
            initializeRouteButton();

            String gender = getIntent().getStringExtra("gender");
            ArrayList<String> categories = getIntent().getStringArrayListExtra("categories");
            if (categories == null) categories = new ArrayList<>();
            Log.d("Filters", "Gender: " + gender + ", Categories: " + categories);

            loadAndFilterStoresAsync(gender, categories);
        } catch (Exception e) {
            Log.e("MainActivity", "Error initializing activity.", e);
        }
    }

    private void initializeDrawerLayout(Toolbar toolbar) {
        drawerLayout = findViewById(R.id.drawer_layout);
        sidebar = findViewById(R.id.sidebar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initializeIcons() {
        markerIcon = getBitmapFromResource(R.drawable.marker_icon, 16);
        pointIcon = getBitmapFromResource(R.drawable.point_icon, 8);
    }

    private void initializeTMapView() {
        FrameLayout container = findViewById(R.id.tmap_container);
        tMapView = new TMapView(this);
        tMapData = new TMapData();

        tMapView.setSKTMapApiKey("8FjquJWLby2TBpTZHjjYq3HSTW63ag3P75zZCFEw");
        container.addView(tMapView);

        tMapView.setOnMapReadyListener(() -> {
            Log.d("TMapView", "TMapView is ready.");
            TMapPoint defaultPoint = new TMapPoint(37.544579, 127.055874); // 성수역
            tMapView.setCenterPoint(defaultPoint.getLongitude(), defaultPoint.getLatitude());
            tMapView.setZoomLevel(15);

            setCurrentLocationMarker();
            addStoreMarkers();
            initializeListView();
        });
    }

    private Bitmap getBitmapFromResource(int resId, int scaleFactor) {
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), resId);
        return Bitmap.createScaledBitmap(originalBitmap, originalBitmap.getWidth() / scaleFactor, originalBitmap.getHeight() / scaleFactor, true);
    }

    private void setCurrentLocationMarker() {
        try {
            TMapPoint currentLocation = new TMapPoint(37.544579, 127.055874); // 성수역
            addMarker("현재 위치", "성수역", currentLocation, pointIcon);
        } catch (Exception e) {
            Log.e("CurrentLocation", "Error adding current location marker.", e);
        }
    }

    private void addMarker(String title, String subtitle, TMapPoint point, Bitmap icon) {
        try {
            TMapMarkerItem markerItem = new TMapMarkerItem();
            markerItem.setId(UUID.randomUUID().toString());
            markerItem.setTMapPoint(point);
            markerItem.setIcon(icon);
            markerItem.setCalloutTitle(title);
            markerItem.setCalloutSubTitle(subtitle);
            markerItem.setCanShowCallout(true);

            tMapView.addTMapMarkerItem(markerItem);
            Log.d("AddMarker", "Marker added: " + title + " at " + point.toString());
        } catch (Exception e) {
            Log.e("AddMarker", "Error adding marker: " + title, e);
        }
    }

    private void addStoreMarkers() {
        if (filteredStores.isEmpty()) {
            Log.w("TMapMarkers", "No stores to add markers for.");
            return;
        }
        for (Store store : filteredStores) {
            double latitude = store.getLatitude() / 10_000_000.0;
            double longitude = store.getLongitude() / 10_000_000.0;

            if (isValidCoordinate(latitude, longitude)) {
                TMapPoint point = new TMapPoint(latitude, longitude);
                addMarker(store.getName(), store.getAddress(), point, markerIcon);
            } else {
                Log.e("InvalidCoordinate", "Invalid coordinates for store: " + store.getName());
            }
        }
    }

    private boolean isValidCoordinate(double latitude, double longitude) {
        return latitude >= -90 && latitude <= 90 && longitude >= -180 && longitude <= 180;
    }

    private void initializeListView() {
        ListView listView = findViewById(R.id.store_list);

        // 이름과 주소를 결합하여 표시
        ArrayList<String> displayItems = new ArrayList<>();
        for (Store store : filteredStores) {
            displayItems.add(store.getName() + " - " + store.getCategory());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayItems);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Store selectedStore = filteredStores.get(position);
            Toast.makeText(MainActivity.this, "선택된 매장: " + selectedStore.getName(), Toast.LENGTH_SHORT).show();
            tMapView.setCenterPoint(selectedStore.getLongitude() / 10_000_000.0, selectedStore.getLatitude() / 10_000_000.0);

            EditText waypointInput = findViewById(R.id.waypoint_input);
            EditText destinationInput = findViewById(R.id.destination_input);

            if (waypointInput.getText().toString().isEmpty()) {
                waypointInput.setText(selectedStore.getName());
            } else if (destinationInput.getText().toString().isEmpty()) {
                destinationInput.setText(selectedStore.getName());
            }
        });
    }


    private void initializeRouteButton() {
        Button routeButton = findViewById(R.id.route_button);
        EditText waypointInput = findViewById(R.id.waypoint_input);
        EditText destinationInput = findViewById(R.id.destination_input);

        routeButton.setOnClickListener(v -> {
            String waypointName = waypointInput.getText().toString().trim();
            String destinationName = destinationInput.getText().toString().trim();

            if (waypointName.isEmpty() && destinationName.isEmpty()) {
                Toast.makeText(this, "경유지와 목적지를 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            Store waypointStore = findStoreByName(waypointName);
            Store destinationStore = findStoreByName(destinationName);

            if (destinationStore == null) {
                Toast.makeText(this, "목적지가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            showRoute(waypointStore, destinationStore);
        });
    }

    private Store findStoreByName(String name) {
        for (Store store : filteredStores) {
            if (store.getName().equals(name)) {
                return store;
            }
        }
        return null;
    }

    private void showRoute(Store waypoint, Store destination) {
        TMapPoint startPoint = new TMapPoint(37.544579, 127.055874); // 성수역
        addMarker("출발지", "성수역", startPoint, pointIcon);

        ArrayList<TMapPoint> passList = new ArrayList<>();
        TMapPoint endPoint = null;

        if (waypoint != null) {
            TMapPoint waypointPoint = new TMapPoint(waypoint.getLatitude() / 10_000_000.0, waypoint.getLongitude() / 10_000_000.0);
            addMarker("경유지", waypoint.getAddress(), waypointPoint, pointIcon);
            passList.add(waypointPoint);
        }

        if (destination != null) {
            endPoint = new TMapPoint(destination.getLatitude() / 10_000_000.0, destination.getLongitude() / 10_000_000.0);
            addMarker("목적지", destination.getAddress(), endPoint, pointIcon);
        }

        if (endPoint == null) {
            Toast.makeText(this, "목적지를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        calculateMultiPointRoute(startPoint, endPoint, passList);
    }

    private void calculateMultiPointRoute(TMapPoint startPoint, TMapPoint endPoint, ArrayList<TMapPoint> passList) {
        tMapData.findMultiPointPathData(startPoint, endPoint, passList, 0, polyLine -> {
            if (polyLine != null) {
                runOnUiThread(() -> {
                    polyLine.setLineColor(Color.BLUE);
                    polyLine.setLineWidth(5);
                    tMapView.addTMapPolyLine( polyLine);
                    Log.d("Route", "경로 표시 완료");
                });
            } else {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "경로를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show());
                Log.e("Route", "경로 계산 실패");
            }
        });
    }

    private ArrayList<String> getStoreNames(ArrayList<Store> stores) {
        ArrayList<String> names = new ArrayList<>();
        for (Store store : stores) {
            names.add(store.getName());
        }
        return names;
    }

    private void loadAndFilterStoresAsync(String gender, ArrayList<String> categories) {
        executorService.execute(() -> {
            filteredStores = loadAndFilterStores(gender, categories);

            runOnUiThread(() -> {
                if (filteredStores.isEmpty()) {
                    Toast.makeText(this, "필터에 해당하는 매장이 없습니다.", Toast.LENGTH_SHORT).show();
                }
                initializeListView();
                addStoreMarkers();
            });
        });
    }

    private ArrayList<Store> loadAndFilterStores(String gender, ArrayList<String> categories) {
        ArrayList<Store> stores = new ArrayList<>();
        try (InputStream inputStream = getAssets().open("store.json");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            JSONArray storesJson = new JSONArray(jsonBuilder.toString());
            for (int i = 0; i < storesJson.length(); i++) {
                JSONObject store = storesJson.getJSONObject(i);

                String storeCategory = store.getString("Category");
                String storeName = store.getString("Store Name");
                String storeAddress = store.getString("Address");
                double latitude = store.getDouble("Latitude");
                double longitude = store.getDouble("Longitude");

                if ((gender.equals("상관없음") || storeCategory.contains(gender)) &&
                        categories.stream().anyMatch(storeCategory::contains)) {
                    stores.add(new Store(storeName, latitude, longitude, storeAddress, storeCategory));
                }
            }
        } catch (Exception e) {
            Log.e("StoreLoader", "Error loading and filtering stores.", e);
        }
        return stores;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_open_sidebar) {
            if (drawerLayout.isDrawerOpen(sidebar)) {
                drawerLayout.closeDrawer(sidebar);
            } else {
                drawerLayout.openDrawer(sidebar);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
