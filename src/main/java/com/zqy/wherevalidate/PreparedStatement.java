package com.zqy.wherevalidate;

import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetEncoder;


public class PreparedStatement {
   private String sql;
   private CharsetEncoder charsetEncoder;
   private String[] parameterValues;
   private int parameterCount;
   public  PreparedStatement(String str){
	       sql=str;
	       initParameter();
   }
   private void initParameter(){
	   char[] c = this.sql.toCharArray();
	   int j=0;
	   for(int i = 0;i<c.length;i++){
		   if(c[i]=="?".charAt(0)){
			   j++;
		   }
	   }
	   this.parameterCount = j;
	   this.parameterValues=new String[j];
   }
   public boolean query() throws NumberFormatException, ParseException{
	 if(parameterCount>0){
		 for(int i=0;i<parameterCount;i++){
			 this.sql = this.sql.replaceFirst("\\?", this.parameterValues[i]);
		 }
	 }
	 StringReader reader = new StringReader(this.sql);
	 SqlParser parser = new SqlParser(reader);
	 return parser.start();
   }
   public void setString(int parameterIndex, String x) throws Exception{
	   StringBuffer buf = new StringBuffer((int)(x.length() * 1.1D));
	   int stringLength = x.length();
       buf.append('\'');

       for (int i = 0; i < stringLength; i++) {
         char c = x.charAt(i);

         switch (c) {
         case '\000':
           buf.append('\\');
           buf.append('0');

           break;
         case '\n':
           buf.append('\\');
           buf.append('n');

           break;
         case '\r':
           buf.append('\\');
           buf.append('r');

           break;
         case '\\':
           buf.append('\\');
           buf.append('\\');

           break;
         case '\'':
           buf.append('\\');
           buf.append('\'');

           break;
         case '"':
          

           buf.append('"');

           break;
         case '\032':
           buf.append('\\');
           buf.append('Z');

           break;
         case '¥':
         case '₩':
           if (this.charsetEncoder != null) {
             CharBuffer cbuf = CharBuffer.allocate(1);
             ByteBuffer bbuf = ByteBuffer.allocate(1);
             cbuf.put(c);
             cbuf.position(0);
             this.charsetEncoder.encode(cbuf, bbuf, true);
             if (bbuf.get(0) == 92) {
               buf.append('\\');
             }
           }
           break;
         }

         buf.append(c);
       }
       buf.append('\'');
       String parameterAsString = buf.toString();
       checkParameterIndex(parameterIndex);
	   this.parameterValues[parameterIndex-1]= String.valueOf(parameterAsString);
	    
   }
   public void setInt(int parameterIndex, int x) throws Exception{
	   checkParameterIndex(parameterIndex);
	   this.parameterValues[parameterIndex-1]= String.valueOf(x);
	  
   }
   public void setDouble(int parameterIndex, double x) throws Exception{
	   checkParameterIndex(parameterIndex);
	   this.parameterValues[parameterIndex-1]= String.valueOf(x);
   }
   private void checkParameterIndex(int parameterIndex){
	   if(this.parameterValues[parameterIndex-1]!=null){
		   throw new RuntimeException("下标不能相同！parameterIndex："+parameterIndex+" 已存在");
	   }else if(parameterIndex<=0){
		   throw new RuntimeException("下标不能小于或0！"); 
	   }else if(parameterIndex>this.parameterCount){
		   throw new RuntimeException("下标不能大于参数个数！"); 
	   } 
	     
   }
}
