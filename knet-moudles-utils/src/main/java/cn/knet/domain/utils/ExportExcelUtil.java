package cn.knet.domain.utils;


import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExportExcelUtil<T> {

	public void exportExcel(Collection<T> dataset,String[] headers,String path,String excelName,String sheetName,String title) throws IOException
	{
		// 声明一个工作薄
		XSSFWorkbook workbook = new XSSFWorkbook();
		// 生成一个表格
		XSSFSheet sheet=workbook.createSheet();
		
		// 设置表格默认列宽度为15个字节
		//要是提供的sheetName就给第一个表格设置名字
		if(StringUtils.isNotBlank(sheetName))
		{
			workbook.setSheetName(0,sheetName);
		}
		//设置行的默认值
		int row_num=0;
		if(StringUtils.isNotBlank(title)){
			XSSFRow rowtitle = sheet.createRow(row_num);  
			rowtitle.setHeightInPoints(23);  
	        XSSFCell cellHead = rowtitle.createCell(0);
	        cellHead.setCellValue(title); 
	        //合并标题的单元格
	        sheet.addMergedRegion(new CellRangeAddress(row_num,row_num,0,headers.length-1));//startRow,endRow,startColumn,endColumn
	        //设置总标题样式==============================
	        //设置字体
	        XSSFFont font_title = workbook.createFont();
	        font_title.setColor(new XSSFColor(Color.black).getIndexed());
	        font_title.setFontHeightInPoints((short) 16);
	        font_title.setBold(true);
	        //设置title样式
	        XSSFCellStyle style_title = workbook.createCellStyle(); 
	        style_title.setAlignment(HorizontalAlignment.CENTER);
	        style_title.setBorderBottom(BorderStyle.THIN);
	        style_title.setBorderLeft(BorderStyle.THIN);
	        style_title.setBorderRight(BorderStyle.THIN);
	        style_title.setBorderTop(BorderStyle.THIN);
	        style_title.setFont(font_title);
	        //设置cell样式
	        cellHead.setCellStyle(style_title);
	        //解决合并单元格后加边框问题
	        for(int i=1;i<headers.length;i++){
	        	cellHead = rowtitle.createCell(i);
	        	cellHead.setCellValue("");
	        	cellHead.setCellStyle(style_title);
	        	}
			
			
	        //行数加1行
	        row_num=row_num+1;
		}
		
		//设置head样式============================================
		// 生成一个字体
        XSSFFont font_head = workbook.createFont();
        font_head.setColor(new XSSFColor(Color.BLACK).getIndexed());
        font_head.setFontHeightInPoints((short) 12);
        font_head.setBold(true);
       // 生成head样式
        XSSFCellStyle style_head = workbook.createCellStyle();
        // 把字体应用到当前的样式
        style_head.setFont(font_head);
       // 设置这些样式
       // style_head.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        //style_head.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style_head.setBorderBottom(BorderStyle.THIN);
        style_head.setBorderLeft(BorderStyle.THIN);
        style_head.setBorderRight(BorderStyle.THIN);
        style_head.setBorderTop(BorderStyle.THIN);
        style_head.setAlignment(HorizontalAlignment.CENTER);
      //设置head样式======End==================================
      //设置body样式==================================================
      // 生成另一个字体
        XSSFFont font_body = workbook.createFont();
        font_body.setBold(false);;
        //生成body样式
        XSSFCellStyle style_body = workbook.createCellStyle();
       // 把字体应用到当前的样式
        style_body.setFont(font_body);
       // style_body.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
        //style_body.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style_body.setBorderBottom(BorderStyle.THIN);
        style_body.setBorderLeft(BorderStyle.THIN);
        style_body.setBorderRight(BorderStyle.THIN);
        style_body.setBorderTop(BorderStyle.THIN);
        style_body.setAlignment(HorizontalAlignment.CENTER);
        style_body.setVerticalAlignment(VerticalAlignment.CENTER);
        
       // 添加表格标题行
        XSSFRow row_head = sheet.createRow(row_num);
        row_head.setHeightInPoints(25);
        for (int i = 0; i < headers.length; i++) {
            XSSFCell cell = row_head.createCell(i);
            cell.setCellStyle(style_head);
            XSSFRichTextString text = new XSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
        //添加标题行后行号加1
        row_num=row_num+1;
        //添加body
        // 遍历集合数据，产生数据行
        //Field[] fields = types.getDeclaredFields();
        Iterator<T> it = dataset.iterator();
        while (it.hasNext()) {
        	System.err.println(row_num);
        	XSSFRow row_body = sheet.createRow(row_num);
        	row_body.setHeightInPoints(20);
        	
        	T t = (T) it.next();
            // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
            Field[] fields = t.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
            	
            	//设置列宽度自适应
            	sheet.autoSizeColumn(i,true);
            	
            	XSSFCell cell = row_body.createCell(i);
                cell.setCellStyle(style_body);
                Field field = fields[i];
                String fieldName = field.getName();
                String getMethodName = "get"+ fieldName.substring(0, 1).toUpperCase()+ fieldName.substring(1);
                try {
                    Class<? extends Object> tCls = t.getClass();
                    Method getMethod = tCls.getMethod(getMethodName,new Class[] {});
                    Object value = getMethod.invoke(t, new Object[] {});
                 // 判断值的类型后进行强制类型转换
                    String textValue = null;
                    if (value instanceof Integer) {
                        int intValue = (Integer) value;
                        cell.setCellValue(intValue);
                      } 
                     else if (value instanceof Float) {
                        float fValue = (Float) value;
                        textValue = String.valueOf(fValue);
                        cell.setCellValue(textValue);
                      } 
                     else if (value instanceof Double) {
                        double dValue = (Double) value;
                        textValue = String.valueOf(dValue);
                        cell.setCellValue(textValue);
                      } 
                     else if (value instanceof Long) {
                        long longValue = (Long) value;
                        cell.setCellValue(longValue);
                      }
                     else if (value instanceof Date) {
                         Date date = (Date) value;
                         SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
                         textValue = sdf.format(date);
                     }else {
                        // 其它数据类型都当作字符串简单处理
                        textValue = value.toString();
                    }
                    
                    if (textValue != null) {
                        Pattern p = Pattern.compile("^//d+(//.//d+)?{1}");
                        Matcher matcher = p.matcher(textValue);
                        if (matcher.matches()) {
                            // 是数字当作double处理
                            cell.setCellValue(Double.parseDouble(textValue));
                        } else {
                            XSSFRichTextString richString = new XSSFRichTextString(textValue);
                            XSSFFont font3 = workbook.createFont();
                            font3.setColor(new XSSFColor(Color.BLACK).getIndexed());
                            richString.applyFont(font3);
                            cell.setCellValue(richString);
                        }
                    }
                   
                    
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } finally {
                    // 清理资源
                }
            }
            row_num=row_num+1;
        }
        OutputStream os = new FileOutputStream(new File(path+"/"+excelName+".xlsx"));  
        try{
          workbook.write(os); 
        }
        catch(Exception ex){
        	ex.printStackTrace();
        }
        finally{
        	os.flush();
            os.close(); 
        }
        
		
	}
	
	public void exportExcelFast(Collection<T> dataset,String[] headers,Class<T> t,String path,String excelName,String sheetName) throws IOException
	{

        File filePath = new File(path);
	    File file = new File(path+"/"+excelName+".xlsx");
	    if(filePath.exists()) {
            if (file.exists()) {

            } else {
                file.createNewFile();
            }
        }else {
            filePath.mkdir();
        }

		 FileOutputStream output = new FileOutputStream(file); //读取的文件路径
		// 声明一个工作薄
			XSSFWorkbook workbook = new XSSFWorkbook();
			// 生成一个表格
			XSSFSheet sheet=workbook.createSheet(sheetName);
			 XSSFRow row_head = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
	            XSSFCell cell = row_head.createCell(i);
	            XSSFRichTextString text = new XSSFRichTextString(headers[i]);
	            cell.setCellValue(text);
	        }
			
			Field[] fields = t.getDeclaredFields();
			int row_num=1;
            for (T tt : dataset) {
            	
            	XSSFRow row_body = sheet.createRow(row_num);
            	 for(int i=0;i<fields.length;i++){
            		 XSSFCell cell = row_body.createCell(i);   
            		Object v;
					try {
						v = BeanUtils.getProperty(tt, fields[i].getName());
						String value =v==null?"":v.toString();
	            		 cell.setCellType(XSSFCell.CELL_TYPE_STRING);//文本格式  
		                 cell.setCellValue(value);//写入内容 
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}//.getBeanProperty(tt, fields[i].getName());
            		 
            	 }
            	 row_num++;
			}

	        
            workbook.write(output);  
            output.flush();
	        output.close();    
	}
}
