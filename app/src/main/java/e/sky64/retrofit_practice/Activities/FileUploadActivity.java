package e.sky64.retrofit_practice.Activities;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;

import e.sky64.retrofit_practice.R;
//이 액티비티는 강의자료를 업로드하는 액티비티입니다. ( 교수님 계정으로 이용 가능)
public class FileUploadActivity extends AppCompatActivity {
    private static final int SELECT_PHOTO = 100;
    Uri selectedImage;
    FirebaseStorage storage;
    StorageReference storageRef,imageRef;
    ProgressDialog progressDialog;
    UploadTask uploadTask;
    ImageView imageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_upload);
        //firebase저장소에 접근. 필수
        storage = FirebaseStorage.getInstance();
        //storage 레퍼런스 가져옴. 필수
        storageRef = storage.getReference();
    }
    //파일 선택 메소드
    public void selectFile(View view) {
        Intent photoPickerIntent = new Intent();
        photoPickerIntent.setType("*/*");  //파일 선택 시, 모든 종류의 type을 보여준다.
        photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }
    //파일 선택 완료후 실행되는 메소드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(FileUploadActivity.this,"File selected, click on upload button",Toast.LENGTH_SHORT).show();
                    selectedImage = imageReturnedIntent.getData();
                }
        }
    }
    //파일 업로드 버튼을 눌렀을 시에 실행되는 메소드. firebase storage에 올려지게 된다.
    public void uploadFile(View view) {
        //create reference to images folder and assing a name to the file that will be uploaded
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mm");
        Date now = new Date();
        String filename = formatter.format(now);
        //여기서 filename 은 현재날짜와 기존이름을 합쳐서 unique하게 생성해준다.(추후 강의번호까지 조합하여 구별될 수 있게끔 구현예정)
        imageRef = storageRef.child("images/"+filename+ selectedImage.getLastPathSegment());
        // progress dialog 생성 및 보여준다 .
        progressDialog = new ProgressDialog(this);
        progressDialog.setMax(100);
        progressDialog.setMessage("Uploading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
        progressDialog.setCancelable(false);
        //업로드 시작
        uploadTask = imageRef.putFile(selectedImage);


        // 업로드 상태 (퍼센트)를 실시간으로 보여준다
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                //sets and increments value of progressbar
                progressDialog.incrementProgressBy((int) progress);
            }
        });


        // 업로드 완료 혹은 실패 여부를 Listen해주는 메소드
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {  //만약 업로드 실패 시에
                Toast.makeText(FileUploadActivity.this,"Error in uploading!",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) { //만약 업로드 성공시에
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Toast.makeText(FileUploadActivity.this,"Upload successful",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}