##### 简书文章地址：https://www.jianshu.com/p/0442195fe30e
 ##### github源码地址：https://github.com/mahuanh/algorithmpro
 ![123.gif](https://upload-images.jianshu.io/upload_images/4907924-717080a0c268d3c6.gif?imageMogr2/auto-orient/strip)


## 核心代码 
 ####  bitmap分割
```
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
```
 #### 洗牌算法
```
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
```
