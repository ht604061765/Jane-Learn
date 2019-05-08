package com.tenfine.napoleon.framework.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.tenfine.napoleon.framework.FrameworkConstant;


/**
 * 文件操作工具类
 */
public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    private static final int BUFFER_SIZE = 2 * 1024;
    
    public static void toZipReturn(OutputStream out, List<String> filePaths)throws RuntimeException {
    	long start = System.currentTimeMillis();
    	ZipOutputStream zos = null ;
    	try {
    		zos = new ZipOutputStream(out);
    		for(int i=0; i<filePaths.size(); i++) {
    			File file = new File(filePaths.get(i));
    			byte[] buf = new byte[BUFFER_SIZE];
    			zos.putNextEntry(new ZipEntry(file.getName()));
    			int len;
    			FileInputStream in = new FileInputStream(file);
    			while ((len = in.read(buf)) != -1){
    				zos.write(buf, 0, len);
    			}
    			zos.closeEntry();
    			in.close();
    		}
    		
    		long end = System.currentTimeMillis();
    		logger.info("压缩完成，耗时：" + (end - start) +" ms，文件人数=" + filePaths.size());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("压缩文件出错，msg=" + e.getMessage());

		}finally {
			if(zos != null){
				try {
					zos.close();
				}catch (IOException e){
					e.printStackTrace();
				}
			}
		}
    }  
    
    /**
     * 创建目录
     */
    public static File createDir(String dirPath) {
        File dir;
        try {
            dir = new File(dirPath);
            if (!dir.exists()) {
                FileUtils.forceMkdir(dir);
            }
        } catch (IOException e) {
            logger.error("创建目录出错！", e);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return dir;
    }

    /**
     * 创建文件
     */
    public static File createFile(String filePath) {
        File file;
        try {
            file = new File(filePath);
            File parentDir = file.getParentFile();
            if (!parentDir.exists()) {
                FileUtils.forceMkdir(parentDir);
            }else {
            	 if(file.isFile()) {
                 	file.delete();
                 }
            }
        } catch (Exception e) {
            logger.error("创建文件出错！", e);
            throw new RuntimeException(e);
        }
        return file;
    }

    /**
     * 复制目录（不会复制空目录）
     */
    public static void copyDir(String srcPath, String destPath) {
        try {
            File srcDir = new File(srcPath);
            File destDir = new File(destPath);
            if (srcDir.exists() && srcDir.isDirectory()) {
                FileUtils.copyDirectoryToDirectory(srcDir, destDir);
            }
        } catch (Exception e) {
            logger.error("复制目录出错！", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 复制文件
     */
    public static void copyFile(String srcPath, String destPath) {
        try {
            File srcFile = new File(srcPath);
            File destDir = new File(destPath);
            if (srcFile.exists() && srcFile.isFile()) {
                FileUtils.copyFileToDirectory(srcFile, destDir);
            }
        } catch (Exception e) {
            logger.error("复制文件出错！", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除目录
     */
    public static void deleteDir(String dirPath) {
        try {
            File dir = new File(dirPath);
            if (dir.exists() && dir.isDirectory()) {
                FileUtils.deleteDirectory(dir);
            }
        } catch (Exception e) {
            logger.error("删除目录出错！", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除文件
     */
    public static void deleteFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists() && file.isFile()) {
                FileUtils.forceDelete(file);
            }
        } catch (Exception e) {
            logger.error("删除文件出错！", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 重命名文件
     */
    public static void renameFile(String srcPath, String destPath) {
        File srcFile = new File(srcPath);
        if (srcFile.exists()) {
            File newFile = new File(destPath);
            boolean result = srcFile.renameTo(newFile);
            if (!result) {
                throw new RuntimeException("重命名文件出错！" + newFile);
            }
        }
    }

    
    /**
	 * 写入考勤图片
	 */
	public static void saveAttendanceFile(String timeStamp, String idNo, MultipartFile file){		
		String filePath = PathStatic.getAttendancePath(idNo) + idNo + "/" + timeStamp + "attend.jpg";
        try {
        	createFile(filePath);
        	File newFile = new File(filePath);
			file.transferTo(newFile);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


    /**
     * 保存修改分辨率后的采集图片
     * @param idNo
     * @param image
     */
	public static void  saveResizeFile(String idNo, File image) {
	    String filePath = PathStatic.getCollectPath(idNo) + "RGB.jpg";
	    File newFile = new File(filePath);
        try {
            FileUtils.copyFile(image, newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
	 * 写入采集图片
	 */
	public static void saveCollectFile(int type, String idNo, MultipartFile file){		
		String filePath = PathStatic.getCollectPath(idNo);
        if(type == 1) {//身份证正面
        	filePath = filePath + "cardFront.jpg";
        }else if(type == 2) {//身份证背面
        	filePath = filePath + "cardBack.jpg";
        }else if(type == 3) {//拍摄的人的照片
        	filePath = filePath + "RGB.jpg";
        }
        try {
        	createFile(filePath);
        	File newFile = new File(filePath);
			file.transferTo(newFile);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
//      writeFile(filePath, file.toString());
	}
	
	/**
	 * 保存Vip图片
	 */
	public static void saveVipFile(String personNo, MultipartFile file){		
		String filePath = PathStatic.getVipPath(personNo);
        try {
        	createFile(filePath);
        	File newFile = new File(filePath);
			file.transferTo(newFile);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		String date = DateUtil.getCurrentDatetime();
		String converts = date.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "");
		System.out.println(converts);
	}
    
	
	/**
	 * 保存apk包
	 */
	@Transactional
	public static boolean saveApkFile(MultipartFile file, String createTime){
		String fileName = file.getOriginalFilename();
		String timeFold = createTime.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "");
		String parentDir = PathStatic.getApkFilePath() + timeFold;
        try {
        	createDir(parentDir);
    		String filePath = parentDir + "/" + fileName;
        	File newFile = new File(filePath);
			file.transferTo(newFile);
			return true;
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return false;
	}
	
    /**
     * 将字符串写入文件
     */
    public static void writeFile(String filePath, String fileContent) {
        OutputStream os = null;
        Writer w = null;
        try {
            createFile(filePath);
            os = new BufferedOutputStream(new FileOutputStream(filePath));
            w = new OutputStreamWriter(os, FrameworkConstant.UTF_8);
            w.write(fileContent);
            w.flush();
        } catch (Exception e) {
            logger.error("写入文件出错！", e);
            throw new RuntimeException(e);
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (w != null) {
                    w.close();
                }
            } catch (Exception e) {
                logger.error("释放资源出错！", e);
            }
        }
    }
    
    
    
    /**
     * 获取真实文件名（去掉文件路径）
     */
    public static String getRealFileName(String fileName) {
        return FilenameUtils.getName(fileName);
    }

    /**
     * 判断文件是否存在
     */
    public static boolean checkFileExists(String filePath) {
        return new File(filePath).exists();
    }
}

