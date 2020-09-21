package com.xmdd.algorithmpro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.xmdd.algorithmpro.base.MyBeseActivity;
import com.xmdd.algorithmpro.myutils.MyLog;
import com.xmdd.algorithmpro.myutils.MyToast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 洗牌算法
 */
public class ShuffleAlgorithmActivity extends MyBeseActivity implements View.OnClickListener {
    private List<ImageBean> imgLists = new ArrayList<>();
    private MyGridViewAdapter myGridViewAdapter;
    private ImageView iv_realimg;
    Bitmap originalBitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_algorithm_shuffle);
        findViewById(R.id.tv_shuffle_camera).setOnClickListener(this);
        findViewById(R.id.tv_shuffle_album).setOnClickListener(this);
        iv_realimg = findViewById(R.id.iv_realimg);
        iv_realimg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        iv_realimg.setImageAlpha(255);
                        break;
                    case MotionEvent.ACTION_UP:
                        iv_realimg.setImageAlpha(10);
                        break;
                }
                return true;
            }
        });

        GridView gv_shuffle = findViewById(R.id.gv_shuffle);
        gv_shuffle.setAdapter(myGridViewAdapter = new MyGridViewAdapter());

        gv_shuffle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            int oldPosition = -1;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (oldPosition != -1) {//交换
                    ImageBean temp = imgLists.get(oldPosition);
                    temp.isSelected = false;
                    imgLists.set(oldPosition, imgLists.get(position));
                    imgLists.set(position, temp);

                    myGridViewAdapter.notifyDataSetChanged();

                    //判断拼图成功失败
                    if (position == imgLists.get(position).realPosition) {
                        //遍历
                        boolean isWin = true;
                        for (int i = 0; i < imgLists.size(); i++) {
                            if (imgLists.get(i).realPosition != i) {
                                isWin = false;
                                break;
                            }
                        }
                        if (isWin) {
                            MyToast.showToast("恭喜您完成拼图！！！！");
                            // TODO 显示图片

                        }
                    }

                    oldPosition = -1;
                } else {//选中
                    //加选中背景
                    imgLists.get(position).isSelected = true;

                    myGridViewAdapter.notifyDataSetChanged();

                    oldPosition = position;
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_shuffle_camera:
                clickCamera(v);
                break;
            case R.id.tv_shuffle_album:
                clickAlbum(v);
                break;
        }
    }

    /**
     * 点击相机 先判断权限
     *
     * @param v
     */
    public void clickCamera(View v) {
        XXPermissions.with(getActivity())
                .permission(Permission.CAMERA)
                .permission(Permission.READ_EXTERNAL_STORAGE)
                .permission(Permission.WRITE_EXTERNAL_STORAGE)
                .request(new OnPermission() {

                    @Override
                    public void hasPermission(List<String> granted, boolean all) {
                        if (all) {
//                            MyToast.showToast("获取存储和拍照权限成功");
                            openCamera();
                        } else {
                            MyToast.showToast("获取权限成功，部分权限未正常授予");
                        }

                    }

                    @Override
                    public void noPermission(List<String> denied, boolean never) {
                        if (never) {
                            MyToast.showToast("被永久拒绝授权，请手动授予存储和拍照权限");
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(getActivity(), denied);
                        } else {
                            MyToast.showToast("获取存储和拍照权限失败");
                        }
                    }
                });
    }

    /**
     * 点击相册 先判断权限
     *
     * @param v
     */
    public void clickAlbum(View v) {
        XXPermissions.with(getActivity())
                .permission(Permission.CAMERA)
                .permission(Permission.READ_EXTERNAL_STORAGE)
                .permission(Permission.WRITE_EXTERNAL_STORAGE)
                .request(new OnPermission() {

                    @Override
                    public void hasPermission(List<String> granted, boolean all) {
                        if (all) {
                            MyToast.showToast("获取存储和拍照权限成功");
                            openZooM();
                        } else {
                            MyToast.showToast("获取权限成功，部分权限未正常授予");
                        }

                    }

                    @Override
                    public void noPermission(List<String> denied, boolean never) {
                        if (never) {
                            MyToast.showToast("被永久拒绝授权，请手动授予存储和拍照权限");
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(getActivity(), denied);
                        } else {
                            MyToast.showToast("获取存储和拍照权限失败");
                        }
                    }
                });
    }


    public static File mCameraTmpFile;


    /**
     * 打开相册
     */
    public void openZooM() {
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT < 19) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        }
        getActivity().startActivityForResult(intent, 1002);
    }

    /**
     * 打开相机
     */
    public void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            mCameraTmpFile = createTmpFile(this);  // 设置系统相机拍照后的输出路径, 创建临时文件
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCameraTmpFile));
            MyLog.info("==openCamera,mCameraTmpFile-->" + mCameraTmpFile);
            startActivityForResult(cameraIntent, 1001);
        } else {
            MyToast.showToast("没有系统相机");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String absoluteImagePath;
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 1001://相机
                if (mCameraTmpFile != null && mCameraTmpFile.exists()) {
                    //开始裁剪
                    startPhotoZoom(Uri.fromFile(mCameraTmpFile));
                }
                break;
            case 1002://相册
                Uri uri = data.getData();
                MyLog.info(uri + "");
                //开始裁剪
                startPhotoZoom(uri);

                break;
            case 1003://裁剪
                if (mCropOutPutFile != null) {
                    if (data == null) {
                        return;
                    }
                    absoluteImagePath = mCropOutPutFile.getAbsolutePath();
                    MyLog.info(absoluteImagePath);
                    if (originalBitmap != null) {
                        originalBitmap.recycle();
                    }
                    originalBitmap = openImage(absoluteImagePath);
                    iv_realimg.setImageBitmap(originalBitmap);
                    iv_realimg.setImageAlpha(10);
                    bitmapCut(originalBitmap);
                    shuffleList(imgLists);
                    myGridViewAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    /**
     * bitmap分割
     */
    public void bitmapCut(Bitmap bitmap) {
        int bitmapW = bitmap.getWidth();
        int bitmapH = bitmap.getHeight();
        int pieceWidth = bitmapW / 4;
        int pieceHeight = bitmapH / 4;
        imgLists.clear();
        int count = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Bitmap imgBitmap = Bitmap.createBitmap(bitmap, j * pieceWidth, i * pieceHeight,
                        pieceWidth, pieceHeight);
                imgLists.add(new ImageBean(imgBitmap, false, count));
                count++;
            }
        }
    }

    /**
     * 洗牌  打乱图片list
     *
     * @param imgLists
     */
    public void shuffleList(List<ImageBean> imgLists) {
        for (int i = 0; i < imgLists.size(); i++) {
            //生成随机下标  //更改随机下标的取值范围 例：0 ~ n-i
            int randomIndex = (int) (Math.random() * (imgLists.size() - i));
            //方法1：生成的随机下标对应的对象和最后一个未确定的对象 交换
            ImageBean temp = imgLists.get(imgLists.size() - 1 - i);
            imgLists.set(imgLists.size() - 1 - i, imgLists.get(randomIndex));
            imgLists.set(randomIndex, temp);

            //方法2：把生成的随机下标对应的对象放到队尾
            //imgLists.add(imgLists.remove(randomIndex));
        }
    }

    /**
     * 将本地图片转成Bitmap
     *
     * @param path 已有图片的路径
     * @return
     */
    public static Bitmap openImage(String path) {
        Bitmap bitmap = null;
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path));
            bitmap = BitmapFactory.decodeStream(bis);
            bis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    //裁剪
    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);// 去黑边
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 400);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(createTmpFile(this)));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("return-data", false);
        intent.putExtra("noFaceDetection", true);

        startActivityForResult(intent, 1003);
    }

    private File mCropOutPutFile;

    public File createTmpFile(Context context) {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) { // 已挂载
//			File pic = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File pic = Environment.getExternalStorageDirectory();
            if (!pic.exists()) {
                pic.mkdirs();
            }
            MyLog.e("Lee", "已挂载: pic dir = " + pic.getAbsolutePath());
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
            String fileName = TextUtils.concat("multi_image_", timeStamp, ".jpg").toString();
            mCropOutPutFile = new File(pic, fileName);
            return mCropOutPutFile;
        } else {
            File cacheDir = context.getCacheDir();
            MyLog.e("Lee", "未挂载: pic dir = " + cacheDir.getAbsolutePath());
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
            String fileName = TextUtils.concat("multi_image_", timeStamp, ".jpg").toString();
            mCropOutPutFile = new File(cacheDir, fileName);
            return mCropOutPutFile;
        }
    }


    class ImageBean {
        public ImageBean(Bitmap bitmap, boolean isSelected, int realPosition) {
            this.bitmap = bitmap;
            this.isSelected = isSelected;
            this.realPosition = realPosition;
        }

        Bitmap bitmap;
        boolean isSelected = false;
        int realPosition = -1;
    }

    class MyGridViewAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return imgLists.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.activity_algorithm_shuffle_item, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_img = convertView.findViewById(R.id.iv_img);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.iv_img.setImageBitmap(imgLists.get(position).bitmap);
            if (imgLists.get(position).isSelected) {
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                lp.setMargins(20, 20, 20, 20);
                viewHolder.iv_img.setLayoutParams(lp);
            } else {
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                lp.setMargins(0, 0, 0, 0);
                viewHolder.iv_img.setLayoutParams(lp);
            }


            return convertView;
        }

        class ViewHolder {
            ImageView iv_img;
        }

    }


    @Override
    protected void onDestroy() {
        if (originalBitmap != null) {
            originalBitmap.recycle();
        }
        super.onDestroy();
    }
}
