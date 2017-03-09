/**
 * 
 */
package com.xzg.publicUtil;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.codec.Base64;
/**
 * @author xzg
 * @TIME 2017年3月8日
 * 注意类的隐藏和实例创建
 */
public class CookieUtil {
       //保存cookie时的cookieName
       private final static String cookieDomainName = "cn.xzg";
      
       //加密cookie时的网站自定码
       public  final static String webKey = "myinfo";
      
//设置cookie有效期是一小时，根据需要自定义
       private final static long cookieMaxAge = 60 * 5  ;//测试5分钟        
       public static void saveCookie(String username,String password, HttpServletResponse response) {
              //cookie的有效期
              long validTime = System.currentTimeMillis() + (cookieMaxAge * 1000);
              //MD5加密用户详细信息
              //首先将用户密码进行MD5加密得到加密后的用户密码
              String passwordMd5=getMD5(password);
              //形成MD5明文
              String cookieValueWithMd5 =getMD5(username +passwordMd5+webKey);
              //将要被保存的完整的Cookie值
              String cookieValue = username + ":" + validTime + ":" + cookieValueWithMd5;
              //再一次对Cookie的值进行BASE64编码 Base64.encodeBase64(src.getBytes());
              String cookieValueBase64 = new String(Base64.encode(cookieValue.getBytes()));
              //开始保存Cookie
              Cookie cookie = new Cookie(cookieDomainName, cookieValueBase64);
              cookie.setMaxAge(60 * 60 * 24 * 365 * 2);
              //cookie有效路径是网站根目录
              cookie.setPath("/");
              //向客户端写入
              response.addCookie(cookie);
       }

	/**
	 * @param string
	 * @return
	 */
	public static String getMD5(String string) {
		// TODO Auto-generated method stub
		 //确定计算方法
	    try { 
        MessageDigest md5=MessageDigest.getInstance("MD5");
        // 输入的字符串转换成字节数组  
        byte[] inputByteArray = string.getBytes();  
        // inputByteArray是输入字符串转换得到的字节数组  
        md5.update(inputByteArray);  
        // 转换并返回结果，也是字节数组，包含16个元素  
        byte[] resultByteArray = md5.digest();  
        // 字符数组转换成字符串返回  
        return byteArrayToHex(resultByteArray); 
        } catch (NoSuchAlgorithmException e) {  
            return null;  
         }  
	}
	 public static String byteArrayToHex(byte[] byteArray) {  
	        // 首先初始化一个字符数组，用来存放每个16进制字符  
	        char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9', 'A','B','C','D','E','F' };  
	        // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））  
	        char[] resultCharArray =new char[byteArray.length * 2];  
	        // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去  
	        int index = 0; 
	        for (byte b : byteArray) {  
	           resultCharArray[index++] = hexDigits[b>>> 4 & 0xf];  
	           resultCharArray[index++] = hexDigits[b& 0xf];  
	        }
	        // 字符数组组合成字符串返回  
	        return new String(resultCharArray);  
	    }
	
	 /**
	      * 删除cookie
	      * 
	      * @param response
	      * @param name
	     */
	  public static void removeCookie(HttpServletResponse response, String name) {
	         Cookie uid = new Cookie(name, null);
	         uid.setPath("/");
	         uid.setMaxAge(0);
	        response.addCookie(uid);
    }
	  /**
	    * 获取cookie值
	   * @param request
	  * @return
	*/
	    public static String getUid(HttpServletRequest request,String cookieName) {
	        Cookie cookies[] = request.getCookies();
	       for (Cookie cookie : cookies) {
	              if (cookie.getName().equals(cookieName)) {
	                return cookie.getValue();
	               }
	           }
	         return null;
	         }
}