package com.example.lightupthelove;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class DirectoryPathChooseActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private LinearLayout llRoot;
    private LinearLayout llLast;
    private RecyclerView rvDirectory;
    private DirectoryPathChooseAdapter directoryPathChooseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory_choose);

        toolbar = findViewById(R.id.toolbar);
        llRoot = (LinearLayout) findViewById(R.id.llRoot);
        llLast = (LinearLayout) findViewById(R.id.llLast);
        rvDirectory = (RecyclerView) findViewById(R.id.rvDirectory);

        toolbar.setTitle("Choose Folder");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);


        LinearLayoutManager manager = new LinearLayoutManager(DirectoryPathChooseActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvDirectory.setLayoutManager(manager);
        rvDirectory.setItemAnimator(new DefaultItemAnimator());

        directoryPathChooseAdapter = new DirectoryPathChooseAdapter(DirectoryPathChooseActivity.this, new DirectoryPathChooseAdapter.OperationListener() {
            @Override
            public void forbidJump() {
                Toast.makeText(DirectoryPathChooseActivity.this, "Deselect and then jump！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void forbidChoose() {
                Toast.makeText(DirectoryPathChooseActivity.this, "Folder selected, please cancel and then select again！", Toast.LENGTH_SHORT).show();
            }
        });
        llRoot.setOnClickListener(v -> directoryPathChooseAdapter.goToRootPath());
        llLast.setOnClickListener(v -> directoryPathChooseAdapter.gotoLast());
        rvDirectory.setAdapter(directoryPathChooseAdapter);
    }

    @Override
    public void onBackPressed() {
        if (directoryPathChooseAdapter != null && !directoryPathChooseAdapter.canChoose()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(DirectoryPathChooseActivity.this);
            builder.setTitle("Tip:");
            builder.setMessage("Please select the save folder and exit!！");
            builder.create();
            builder.show();
        }

    }

    //The knowledge of Menu is learnt from a CSDN tutorial.
    //Reference URL: https://blog.csdn.net/weixin_42536863/article/details/117502225
    //The tutorial introduces three ways to use the Android menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_directory_path_choose, menu);//Filling menu items
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //The folder choose is inspired from a tutorial. The tutorial is just a revelation, on which I have made very significant changes
        //Reference URL: https://blog.csdn.net/sinat_25689603/article/details/51887756
        if (item.getItemId() == R.id.chooseFinish) {
            //If this place is selected, exit;
            //If not, give a hint
            if (directoryPathChooseAdapter != null && directoryPathChooseAdapter.canChoose()) {
                Intent intent = new Intent();
                intent.putExtra("CHOOSE_PATH", directoryPathChooseAdapter.getChooseDirectory().getAbsolutePath());
                setResult(RESULT_OK, intent);
                finish();
            }
        }
        return true;
    }
}