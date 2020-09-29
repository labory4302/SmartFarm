package com.smartfarm.www.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.smartfarm.www.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class CropActivity extends Fragment {

    View view;

    private static final int REQUEST_IMAGE_CAPTURE = 672;   //사진파일을  Request코드
    private String imageFilePath;                           //사진이 저장되어있는 파일 경로
    private Uri photoUri;                                   //이미지 자원 식별자

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.crop_page, container, false);



        //권한체크
        //bulid.gradle(Module: app)부분에 dependencies에 implementation 'gun0912.ted:tedpermission:2.0.0'라고 선언되어있음
        //안드로이드에서 특정 권한을 물어볼 때 그 창을 쉽게 만들어 낼 수 있음
        TedPermission.with(getActivity().getApplicationContext())
                .setPermissionListener(permissionListener)        //권한이 허용되거나 거부되었을때의 행동을 지정함
                .setRationaleMessage("카메라 권한이 필요합니다.")  //권한을 확인할 때 사용자에게 어떤 메시지를 띄워줄것인지 정하는 것
                .setDeniedMessage("거부하셨습니다.")              //권한을 거부했을 시 나오는 메시지
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)     //어떤 권한을 체크할 지 가져오는 것
                .check();


        view.findViewById(R.id.btn_capture).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);    //카메라 애플리케이션으로 가도록 함

                //컴포넌트를 실행하지 못하면 앱 작동이 종료되므로 사전에 컴포넌트가 실행가능한지 여부를 판단
                //패키지매니저를 불러와 해당 인텐트가 갖는 컴포넌트가 사용가능한지 확인. 사용가능하다면 null이 아닌 값을 반환
                if(intent.resolveActivity(getActivity().getPackageManager()) != null) {

                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException e) {}
                    Toast.makeText(getContext(), "test" + photoFile, Toast.LENGTH_SHORT).show();
                    if(photoFile != null) {     //생성한 임시파일이 존재한다면
                        //Android 7.0 이상부터 파일공유 정책이 변경됨. 개인 파일의 보안을 강화하기 위해 개인 디렉토리의 액세스를 제한하여
                        //안드로이드에서는 FileProvider 사용을 권장하고 있다.

                        photoUri = FileProvider.getUriForFile(getActivity().getApplicationContext(), getActivity().getPackageName(), photoFile);    //임시파일의 Uri를 가져옴
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);     //요청 된 이미지 또는 비디오를 저장하는 데 사용할 콘텐츠 확인자 Uri를 나타내는 데 사용되는 Intent-extra의 이름
                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                    }
                }

                Toast.makeText(getContext(), "test", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());      //현재시간을 문자열로 반환
        String imageFileName = "TEST_" + timeStamp + "_";                                           //파일이름의 형식 지정. 끝에 언더바하는 이유는 사진 저장할 때 뒤에 숫자가 더 붙음
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);                      //외부저장소 고유영역중 사진디렉토리의 주소를 불러옴
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);                 //임시파일 생성
        imageFilePath = image.getAbsolutePath();                                                    //임시파일의 절대경로 불러옴
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);    //찍은 사진파일을 비트맵으로 변환하여 가져옴
            ExifInterface exif = null;      //사진에 대한 정보를 가질 수 있는 객체
            try {
                exif = new ExifInterface(imageFilePath);    //찍은 사진의 정보 가져오기
            } catch (IOException e) {
                e.printStackTrace();
            }

            int exifOrientation;
            int exifDegree;

            if(exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);    //사진에 대한 정보 가져오기
                exifDegree = exifOrientationToDegrees(exifOrientation);     //사진이 회전되어있는 각도를 가져온다
            } else {
                exifDegree = 0;
            }

            ((ImageView) view.findViewById(R.id.iv_result)).setImageBitmap(rotate(bitmap, exifDegree));  //사진을 회전하여 이미지뷰에 표시
        }
    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        } else {
            return 0;
        }
    }

    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {     //권한이 허용이 되었을 때 작업을 적어주는 것
            Toast.makeText(getContext(), "권한이 허용됨", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {       //권한이 거부되었을 때 작업을 적어주는 것
            Toast.makeText(getContext(), "권한이 거부됨", Toast.LENGTH_SHORT).show();
        }
    };
}
