
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

import java.io.File;

import au.com.hellopeople.hotch.R;
import au.com.hellopeople.hotch.util.StaticData;

public class PhotoSelectionActivity extends AppCompatActivity {

    Button btAddPhotos;
    private String upLoadServerUri = null;
    private String imgPath=null, imgName =null, fileName = null;
    private String[] imgPathArr = new String[5];
    private String[] imgNameArr = new String[5];
    ImageView iv1, iv2, iv3, iv4, iv5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_selection);

        btAddPhotos = (Button)findViewById(R.id.add_photos_button);
        iv1 = (ImageView)findViewById(R.id.iv1);
        iv2 = (ImageView)findViewById(R.id.iv2);
        iv3 = (ImageView)findViewById(R.id.iv3);
        iv4 = (ImageView)findViewById(R.id.iv4);
        iv5 = (ImageView)findViewById(R.id.iv5);

//        for(int i=0;i<5;i++){
//            imgPathArr[i] = null;
//        }

        btAddPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            //Bitmap photo = (Bitmap) data.getData().getPath();

            Uri selectedImageUri = data.getData();
            imgPath = getPath(selectedImageUri);

            if (imgPathArr[0] == null) {
                imgPathArr[0] = imgPath;
                Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
                iv1.setImageBitmap(bitmap);
                File file = new File(imgPath);
                imgNameArr[0] = file.getName();
            }
            else if (imgPathArr[1] == null) {
                imgPathArr[1] = imgPath;
                Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
                iv2.setImageBitmap(bitmap);
                File file = new File(imgPath);
                imgNameArr[1] = file.getName();
            }
            else if (imgPathArr[2] == null) {
                imgPathArr[2] = imgPath;
                Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
                iv3.setImageBitmap(bitmap);
                File file = new File(imgPath);
                imgNameArr[2] = file.getName();
            }
            else if (imgPathArr[3] == null) {
                imgPathArr[3] = imgPath;
                Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
                iv4.setImageBitmap(bitmap);
                File file = new File(imgPath);
                imgNameArr[3] = file.getName();
            }
            else if (imgPathArr[4] == null) {
                imgPathArr[4] = imgPath;
                Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
                iv5.setImageBitmap(bitmap);
                File file = new File(imgPath);
                imgNameArr[4] = file.getName();
            }

            StaticData.arrActivityPicsNames = imgNameArr;
            StaticData.arrActivityPicsPaths = imgPathArr;
        }
    }

    //get the image location......................
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void sellOrShareActivity(View view) {
        Intent intent = new Intent(this, SellActivity.class);
        startActivity(intent);
        finish();
    }
}
