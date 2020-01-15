/*Copyright ©2015 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package com.ecs.sign.base.common.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.ecs.sign.model.room.info.SliderInfo;
import com.ecs.sign.model.room.info.TemplateInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**数据存储工具类
 * @must 
 * <br> 1.将fileRootPath中的包名 改为你的应用包名
 * <br> 2.在Application中调用init方法
 */
public class DataKeeper {
	private static final String TAG = "DataKeeper";

	public static final String SAVE_SUCCEED = "保存成功";
	public static final String SAVE_FAILED = "保存失败";
	public static final String DELETE_SUCCEED = "删除成功";
	public static final String DELETE_FAILED = "删除失败";

	//文件缓存<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	public static final String fileRootPath = getSDPath() != null ? (getSDPath() + "/signage/") : null;

	public static final String accountPath = fileRootPath + "account/";
	public static final String videoPath = fileRootPath + "video/";
	public static final String imagePath = fileRootPath + "image/";
	public static final String musicPath = fileRootPath + "music/";

	//文件缓存>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	//存储文件的类型<<<<<<<<<<<<<<<<<<<<<<<<<
	public static final int TYPE_FILE_IMAGE = 1;							//保存图片
	public static final int TYPE_FILE_VIDEO = 2;							//保存视频
	public static final int TYPE_FILE_AUDIO = 3;							//保存语音

	//存储文件的类型>>>>>>>>>>>>>>>>>>>>>>>>>

	//不能实例化
	private DataKeeper() {}

	private static Context context;
	//获取context，获取存档数据库引用
	public static void init(Context context_) {
		context = context_;
		LogUtils.i(TAG, "init fileRootPath = " + fileRootPath);
		//判断SD卡存在
		if (hasSDCard()) {
			if(fileRootPath != null) {
				File file = new File(imagePath);
				if(!file.exists()) {
					file.mkdirs();
				}
			}
		}
	}





	/**
	 * 复制单个文件
	 * @param oldFilePath
	 * @param newFilePath
	 * @param fileName 需要带后缀
	 * @return
	 * @throws IOException
	 */
	public static String fileCopy(String oldFilePath,String newFilePath,String fileName) throws IOException {
		//如果原文件不存在
		File oldFile = new File(oldFilePath);
		if(!oldFile.exists()){
			return null;
		}
		File newFileDir = new File(newFilePath);
		if (!newFileDir.exists()) {
			newFileDir.mkdirs();
		}

		File newFile = new File( newFilePath,fileName);
		if (!newFile.exists()){
			newFile.createNewFile();
		}


		//获得原文件流
		FileInputStream inputStream = new FileInputStream(oldFile);
		byte[] data = new byte[1024];
		//输出流
		FileOutputStream outputStream =new FileOutputStream(newFile);
		//开始处理流
		while (inputStream.read(data) != -1) {
			outputStream.write(data);
		}
		inputStream.close();
		outputStream.close();
		return newFile.getAbsolutePath();
	}

	/**
	 *
	 * @param fileDirPath
	 * @param name 不用带后缀
	 * @param bmp
	 * @return
	 */
	public static String saveBitmap2JpgFile(String fileDirPath, String name, Bitmap bmp) {
		File appDir = new File(fileDirPath);
		if (!appDir.exists()) {
			appDir.mkdirs();
		}
		String fileName = name + ".jpg";
		File file = new File( fileDirPath,fileName);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String path = file.getAbsolutePath();
		Log.d("zwcc", "saveBitmap2JpgFile: " + path);
		return path;
	}




	//**********外部存储缓存***************
	/**
	 * 存储缓存文件 返回文件绝对路径
	 * @param file
	 * 		要存储的文件
	 * @param type
	 * 		文件的类型
	 *		IMAGE = "imgae";							//图片         
	 *		VIDEO = "video";							//视频        
	 *		VOICE = "voice";							//语音         
	 *		 = "voice";							//语音         
	 * @return	存储文件的绝对路径名
	 * 		若SDCard不存在返回null
	 */
	public static String storeFile(File file, String type) {

		if(!hasSDCard()) {
			return null;
		}
		String suffix = file.getName().substring(file.getName().lastIndexOf(".") + 1);
		byte[] data = null;
		try {
			FileInputStream in = new FileInputStream(file);
			data = new byte[in.available()];
			in.read(data, 0, data.length);
			in.close();
		} catch (IOException e) {
			LogUtils.e(TAG, "storeFile  try { FileInputStream in = new FileInputStream(file); ... >>" +
					" } catch (IOException e) {\n" + e.getMessage());
		}
		return storeFile(data, suffix, type);
	}

	/** @return	存储文件的绝对路径名
				若SDCard不存在返回null */
	@SuppressLint("DefaultLocale")
	public static String storeFile(byte[] data, String suffix, String type) {

		if(!hasSDCard()) {
			return null;
		}
		String path = null;
		if(type.equals(TYPE_FILE_IMAGE)) {
			path = imagePath + "IMG_" + Long.toHexString(System.currentTimeMillis()).toUpperCase()
					+ "." + suffix;
		} else if(type.equals(TYPE_FILE_VIDEO)) {
			path = videoPath + "VIDEO_" + Long.toHexString(System.currentTimeMillis()).toUpperCase()
					+ "." + suffix;
		} else if(type.equals(TYPE_FILE_AUDIO)) {
			path = musicPath + "VOICE_" + Long.toHexString(System.currentTimeMillis()).toUpperCase()
					+ "." + suffix;
		}
		try {
			FileOutputStream out = new FileOutputStream(path);
			out.write(data, 0, data.length);
			out.close();
		} catch (FileNotFoundException e) {
			LogUtils.e(TAG, "storeFile  try { FileInputStream in = new FileInputStream(file); ... >>" +
					" } catch (FileNotFoundException e) {\n" + e.getMessage() + "\n\n >> path = null;");
			path = null;
		} catch (IOException e) {
			LogUtils.e(TAG, "storeFile  try { FileInputStream in = new FileInputStream(file); ... >>" +
					" } catch (IOException e) {\n" + e.getMessage() + "\n\n >> path = null;");
			path = null;
		}
		return path;
	}


	/**jpg
	 * @param fileName
	 * @return
	 */
	public static String getImageFileCachePath(String fileName) {
		return getFileCachePath(TYPE_FILE_IMAGE, fileName, "jpg");
	}
	/**mp4
	 * @param fileName
	 * @return
	 */
	public static String getVideoFileCachePath(String fileName) {
		return getFileCachePath(TYPE_FILE_VIDEO, fileName, "mp4");
	}
	/**mp3
	 * @param fileName
	 * @return
	 */
	public static String getAudioFileCachePath(String fileName) {
		return getFileCachePath(TYPE_FILE_AUDIO, fileName, "mp3");
	}

	/** 获取一个文件缓存的路径  */
	public static String getFileCachePath(int fileType, String fileName, String formSuffix) {

		switch (fileType) {
		case TYPE_FILE_IMAGE:
			return imagePath + fileName + "." + formSuffix; 
		case TYPE_FILE_VIDEO:
			return videoPath + fileName + "." + formSuffix;
		default:
			return accountPath + fileName + "." + formSuffix;
		}
	}

	/**若存在SD 则获取SD卡的路径 不存在则返回null*/
	public static String getSDPath(){
		File sdDir = null;
		String path = null;
		//判断sd卡是否存在
		boolean sdCardExist = hasSDCard();
		if (sdCardExist) {
			//获取跟目录
			sdDir = Environment.getExternalStorageDirectory();
			path = sdDir.toString();
		}
		return path;
	}

	/**判断是否有SD卡*/
	public static boolean hasSDCard() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}


	public static String getTemplatePath(TemplateInfo templateInfo) {
		return DataKeeper.fileRootPath + "temp_" + templateInfo.getId() ;
	}

    public static String getSlidePath(SliderInfo sliderInfo) {
//		return DataKeeper.fileRootPath + "temp_" + sliderInfo.getTemplateId() + "/slider_" + sliderInfo.getId() + "/";
		return DataKeeper.fileRootPath + "temp_" + sliderInfo.getTemplateId() + "/slider_" + sliderInfo.getId();
	}



	//flie：要删除的文件夹的所在位置
	public static void deleteFile(File file) {
		if (file == null){
			return;
		}
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				File f = files[i];
				deleteFile(f);
			}
			file.delete();//如要保留文件夹，只删除文件，请注释这行
		} else if (file.exists()) {
			file.delete();
		}
	}


    private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {

		File direct = new File(Environment.getExternalStorageDirectory() + "/DirName");

		if (!direct.exists()) {
			File wallpaperDirectory = new File("/sdcard/DirName/");
			wallpaperDirectory.mkdirs();
		}

		File file = new File(new File("/sdcard/DirName/"), fileName);
		if (file.exists()) {
			file.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(file);
			imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}





}