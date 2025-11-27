package com.example.apptravel;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class KetQuaThanhToanActivity extends AppCompatActivity {

    private ImageView btn_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ket_qua_thanh_toan);

        btn_home = findViewById(R.id.btn_home);

//        btn_home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(PaymentResultActivity.this, HomeActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
    }
}