package vuvanhai568.project.appDocTruyen;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import vuvanhai568.project.appDocTruyen.Fragment.Fragment_Account;
import vuvanhai568.project.appDocTruyen.Fragment.Fragment_Home;
import vuvanhai568.project.appDocTruyen.Fragment.Fragment_Search;
import vuvanhai568.project.appDocTruyen.SignIn_SignUp.SignIn;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private boolean isAccountDialogShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        replaceFragment(new Fragment_Home());
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    replaceFragment(new Fragment_Home());
                } else if (itemId == R.id.nav_search) {
                    replaceFragment(new Fragment_Search());
                } else if (itemId == R.id.nav_profile) {
                    replaceFragment(new Fragment_Account());
                } else if (itemId == R.id.nav_logout) {
                    showAccountDialog();
                }

                return true;
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.Frame_layout, fragment);
        transaction.commit();
    }

    private void showAccountDialog() {
        if (isAccountDialogShowing) {
            return; // Nếu dialog đã được hiển thị thì không làm gì cả
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Xác nhận")
                .setMessage("Bạn có muốn đăng xuất?")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putString("username", " ");
                        editor.putString("name", " ");

                        Intent i = new Intent(getApplicationContext(), SignIn.class);
                        startActivity(i);
                        finish();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isAccountDialogShowing = false; // Đánh dấu dialog đã đóng
            }
        });

        dialog.show();
        isAccountDialogShowing = true; // Đánh dấu dialog đang được hiển thị
    }
}