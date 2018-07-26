package com.example.yueli.flightcheck.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yueli.flightcheck.Bean.User;
import com.example.yueli.flightcheck.CallBackInterface.LogStateListener;
import com.example.yueli.flightcheck.LoginActivity;
import com.example.yueli.flightcheck.MainActivity;
import com.example.yueli.flightcheck.R;
import com.example.yueli.flightcheck.Util.ApplicationUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * Created by yueli on 2018/7/5.
 */

public class fragmentUser extends Fragment implements View.OnClickListener{
    private static final int REQUEST_IMAGE_GET = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PHOTO_REQUEST_CUT=2;
    private TextView picture,camera;
    private ImageView userImg;
    private Dialog mPickDialog;
    private Uri uritempFile,uriToken;
    private TextView logoff;
    LogStateListener logStateListener;
    String fileName;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_user,null);
        userImg=view.findViewById(R.id.user_img);
        userImg.setOnClickListener(this);
        logoff=view.findViewById(R.id.logoff);
        logoff.setOnClickListener(this);
        View view1 = getActivity().getLayoutInflater().inflate(R.layout.layout_dialog_pick, null);
        picture=view1.findViewById(R.id.picture_dialog_pick);
        camera=view1.findViewById(R.id.camera_dialog_pick);
        picture.setOnClickListener(this);
        camera.setOnClickListener(this);
        mPickDialog = new AlertDialog.Builder(getActivity()).setView(view1).create();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        ApplicationUtil applicationUtil=(ApplicationUtil)getActivity().getApplication();
        String uriStr=applicationUtil.userBox.get(applicationUtil.userId).uri;
        if(uriStr!=null&&uriStr.length()>0){
            Uri uri=Uri.parse(uriStr);
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            userImg.setImageBitmap(bitmap);
        }
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        logStateListener=((MainActivity)activity).getLogStateListener();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 回调成功
        if (resultCode == RESULT_OK) {
            String filePath = null;
            //判断是哪一个的回调
            if (requestCode == REQUEST_IMAGE_GET) {
                //返回的是content://的样式
                filePath = getFilePathFromContentUri(data.getData(), getActivity());
                Uri uri = data.getData();
                Log.v("arki",uri.toString());
                crop(uri);
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                crop(uriToken);
            }else if(requestCode==PHOTO_REQUEST_CUT){
               try {
                   Log.v("arki",uritempFile.toString());
                   ApplicationUtil applicationUtil=(ApplicationUtil)getActivity().getApplication();
                   User user=applicationUtil.userBox.get(applicationUtil.userId);
                   user.uri=uritempFile.toString();
                   applicationUtil.userBox.put(user);
                    Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uritempFile));
                    userImg.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }else
            Log.v("arki","无回调数据");
    }
    private void crop(Uri uri) {

        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + timeStamp+".jpg");
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());// 图片格式
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);

        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }
    private void takePhoto(){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        this.fileName = "easyasset"+format.format(date);
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File outputImage = new File(path,this.fileName+".jpg");
        if (outputImage.exists())
            outputImage.delete();
        try {
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.uriToken=Uri.fromFile(outputImage);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE"); //Android系统自带的照相intent
        intent.putExtra(MediaStore.EXTRA_OUTPUT, this.uriToken); //指定图片输出地址
        startActivityForResult(intent,this.REQUEST_IMAGE_CAPTURE);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_img:
                mPickDialog.show();
                break;
            case R.id.picture_dialog_pick: {
                selectImage();
                mPickDialog.dismiss();
            }
            break;
            case R.id.camera_dialog_pick: {
                Log.v("arki","camera click");
               // dispatchTakePictureIntent();
                takePhoto();
                mPickDialog.dismiss();
            }
            break;
            case R.id.logoff:
                Log.v("arki","logoff");
                logStateListener.getLogState(false);//send callback data
        }
    }

    /**
     * 从相册中获取
     */
    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,null);
        intent.setType("image/*");
        //判断系统中是否有处理该Intent的Activity
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        } else {
            Log.v("arki","未找到图片查看器");
        }
    }



    /**
     * @param uri     content:// 样式
     * @param context
     * @return real file path
     */
    public static String getFilePathFromContentUri(Uri uri, Context context) {
        String filePath;
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};
        Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
        if (cursor == null) return null;
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

}
