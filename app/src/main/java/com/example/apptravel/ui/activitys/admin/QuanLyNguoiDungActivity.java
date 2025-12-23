package com.example.apptravel.ui.activitys.admin;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptravel.R;
import com.example.apptravel.data.adapters.QuanLyNguoiDungAdapter;
import com.example.apptravel.data.api.ApiClient;
import com.example.apptravel.data.api.ApiService;
import com.example.apptravel.data.models.NguoiDung;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuanLyNguoiDungActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private QuanLyNguoiDungAdapter adapter;
    private List<NguoiDung> fullUserList = new ArrayList<>();
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_tai_khoan);

        // 1. Ánh xạ View
        recyclerView = findViewById(R.id.recycler_view_users);
        SearchView searchView = findViewById(R.id.search_view);

        // 2. Cấu hình RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new QuanLyNguoiDungAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        // 3. Khởi tạo API
        apiService = ApiClient.getClient(this).create(ApiService.class);

        // 4. Tải dữ liệu
        loadUsersFromServer();

        // 5. Xử lý tìm kiếm
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterUsers(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterUsers(newText);
                return false;
            }
        });
    }

    private void loadUsersFromServer() {
        apiService.getAllUsers().enqueue(new Callback<List<NguoiDung>>() {
            @Override
            public void onResponse(Call<List<NguoiDung>> call, Response<List<NguoiDung>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fullUserList = response.body();
                    adapter.updateList(fullUserList); // Cập nhật dữ liệu vào adapter
                } else {
                    Toast.makeText(QuanLyNguoiDungActivity.this, "Không thể lấy danh sách", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<NguoiDung>> call, Throwable t) {
                Toast.makeText(QuanLyNguoiDungActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterUsers(String text) {
        List<NguoiDung> filteredList = new ArrayList<>();
        for (NguoiDung item : fullUserList) {
            if (item.getHoTen().toLowerCase().contains(text.toLowerCase()) ||
                    item.getEmail().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.updateList(filteredList);
    }
}