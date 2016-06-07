package au.com.hellopeople.hotch.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;

import java.io.File;

import au.com.hellopeople.hotch.R;
import au.com.hellopeople.hotch.util.StaticData;

public class VideoSelectionActivity extends AppCompatActivity {

    Button btAddVideos;
    private String upLoadServerUri = null;
    private String videoPath=null, videoName =null, fileName = null;
    private String[] videoPathArr = new String[1];
    private String[] videoNameArr = new String[1];
    VideoView vv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_selection);

        btAddVideos = (Button)findViewById(R.id.add_videos_button);
        vv1 = (VideoView) findViewById(R.id.vv1);

//        for(int i=0;i<5;i++){
//            imgPathArr[i] = null;
//        }

        btAddVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Video"),1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            //Bitmap photo = (Bitmap) data.getData().getPath();

            Uri selectedVideoUri = data.getData();
            videoPath = getPath(selectedVideoUri);

            if (videoPathArr[0] == null) {
                videoPathArr[0] = videoPath;
                vv1.setVideoPath(videoPath);
                File file = new File(videoPath);
                videoNameArr[0] = file.getName();
            }
            StaticData.arrActivityVideoNames = videoNameArr;
            StaticData.arrActivityVideoPaths = videoPathArr;
        }

//        if (resultCode == RESULT_OK) {
//            if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
//                Uri selectedImageUri = data.getData();
//
//                // OI FILE Manager
//                filemanagerstring = selectedImageUri.getPath();
//
//                // MEDIA GALLERY
//                selectedImagePath = getPath(selectedImageUri);
//                if (selectedImagePath != null) {
//
//                    Intent intent = new Intent(HomeActivity.this,
//                            VideoplayAvtivity.class);
//                    intent.putExtra("path", selectedImagePath);
//                    startActivity(intent);
//                }
//            }
//        }
    }

    //get the image location......................
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);


//        String[] projection = { MediaStore.Images.Media.DATA };
//        Cursor cursor = managedQuery(uri, projection, null, null, null);
//        if (cursor != null) {
//            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
//            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
//            int column_index = cursor
//                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            cursor.moveToFirst();
//            return cursor.getString(column_index);
//        } else
//            return null;
    }

    public void sellOrShareActivity(View view) {
        Intent intent = new Intent(this, SellActivity.class);
        startActivity(intent);
        finish();
    }
}
