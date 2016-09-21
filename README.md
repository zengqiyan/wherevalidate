# wherevalidate
sql where子句风格验证器
例子：
package com.zqy.wherevalidate.wherevalidate;

import com.zqy.wherevalidate.PreparedStatement;

public class Test {

	public static void main(String[] args) throws Exception {
		String sql = "?  like '%解析%' and (1=? or 6=?)";
	    PreparedStatement ps = new PreparedStatement(sql);
	    ps.setString(1, "解析器");
	    ps.setInt(2, 2);
	    ps.setInt(3, 6);
	    if(ps.query()){
	    	System.out.println("验证通过！");
	    }
	    
	}
   

}
输出：	 验证通过！   
	
