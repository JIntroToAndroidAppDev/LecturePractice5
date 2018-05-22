package shankhadeepghoshal.textapp.org.lecturepractice5;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class ImageUploadActivity extends AppCompatActivity {

    private Button button;
    private RecyclerView recyclerView;
    private ImageListRVAdapter rvAdapter;
    private final int return_tag = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);

        this.button = findViewById(R.id.buttonImageSelect);
        this.recyclerView = findViewById(R.id.recyclerView);
        this.rvAdapter = new ImageListRVAdapter(new ArrayList<>());

        this.rvAdapter.addOnClickListener(new ImageListRVAdapter.ClickListener() {
            @Override
            public void onItemClicked(int position, View view) {
                String filePath = rvAdapter.getFilePath(position);
            }
        });

        this.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        this.recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));

        this.recyclerView.setAdapter(this.rvAdapter);

        button.setOnClickListener((view) -> {
            openGallery();
        });
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,return_tag);
    }

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == return_tag && resultCode == RESULT_OK && data!=null){
            this.rvAdapter.addEntry(data.getData());
        }
    }
}