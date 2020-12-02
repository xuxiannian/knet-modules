package cn.knet.domain.utils;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


public class ExcelReadUtil {

public <T> List<T>  read(String excelPath,Class<T> t,boolean isString){
	  List<T> list = new ArrayList<T>();
	  try {
	    InputStream fs = new FileInputStream(excelPath);
		XSSFWorkbook  wb = new XSSFWorkbook(fs);  
		XSSFSheet sheet = wb.getSheetAt(0);
		Field[] fields = t.getDeclaredFields();
		for(int i=1;i<=sheet.getLastRowNum();i++){
			T hr = t.newInstance();
			XSSFRow row = sheet.getRow(i);
			if (row == null) {
                continue;
            }
			for(int j=0;j<fields.length;j++){
			    XSSFCell xh = row.getCell(j);
                if (xh == null) {
                    continue;
                }
                if(isString){
                	xh.setCellType(XSSFCell.CELL_TYPE_STRING);
                }
                BeanUtils.setProperty(hr, fields[j].getName(), getStringCellValue(xh));
			}
			list.add(hr);
		}
		
		
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} catch (IllegalAccessException e) {
		e.printStackTrace();
	} catch (InvocationTargetException e) {
		e.printStackTrace();
	} catch (InstantiationException e) {
		e.printStackTrace();
	} 
	  return list;
  }

    public <T> List<T>  read(InputStream fs,Class<T> t,boolean isString){
        List<T> list = new ArrayList<T>();
        try {
//            InputStream fs = new FileInputStream(excelPath);
            XSSFWorkbook  wb = new XSSFWorkbook(fs);
            XSSFSheet sheet = wb.getSheetAt(0);
            Field[] fields = t.getDeclaredFields();
            for(int i=1;i<=sheet.getLastRowNum();i++){
                T hr = t.newInstance();
                XSSFRow row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                for(int j=1;j<fields.length;j++){
                    XSSFCell xh = row.getCell(j);
                    if (xh == null) {
                        continue;
                    }
                    if(isString){
                    	xh.setCellType(XSSFCell.CELL_TYPE_STRING);
                    }
                    System.out.println(fields[j].getName()+"------------------------"+j+"-----");
                    BeanUtils.setProperty(hr, fields[j].getName(), getStringCellValue(xh));
                }
                list.add(hr);
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return list;
    }
  
  private Object getStringCellValue(XSSFCell cell) {  
	  Object obj=null;
	  try{
		  switch (cell.getCellType()) {
		  case XSSFCell.CELL_TYPE_NUMERIC: // 数字  
			  if (HSSFDateUtil.isCellDateFormatted(cell)) {
				  obj = cell.getDateCellValue();
			  }
			  else{
				  obj = cell.getNumericCellValue();
			  }
			  break;
          case XSSFCell.CELL_TYPE_STRING: // 字符串  
        	  obj = cell.getStringCellValue();  
              break;  
          case XSSFCell.CELL_TYPE_BOOLEAN: // Boolean  
        	  obj = cell.getBooleanCellValue();  
              break;  
          case XSSFCell.CELL_TYPE_FORMULA: // 公式 
        	  FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
        	    evaluator.evaluateFormulaCell(cell);
        	    CellValue cellValue = evaluator.evaluate(cell);
        	  try {  
        		  obj = cellValue.getStringValue();  
        	   } catch (IllegalStateException e) {  
        		obj = cellValue.getNumberValue();  
        	   }
              break;  
          case XSSFCell.CELL_TYPE_BLANK: // 空值  
        	  obj=""; 
              break;  
          case XSSFCell.CELL_TYPE_ERROR: // 故障  
        	  obj="";
              break;  
          default:  
              System.out.print("未知类型   ");  
              break;  
		}
	  }
	  catch(Exception ex){
		  ex.printStackTrace();
	  }
	  return obj;
  }
  
  public static void main(String[] args){
	  /*
	  //如果对象中有日期类型就要注册一个时间转换
		ConvertUtils.register(new Converter() {
			@Override
			public Object convert(Class type, Object value) {
				if (value == null) {
					return null;
				}
				// 自定义时间的格式为yyyy-MM-dd
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-mm-dd HH:mm:ss");
				// 创建日期类对象

				Date dt = null;
				// 使用自定义日期的格式转化value参数为yyyy-MM-dd格式
				try {
					dt = sdf.parse((String) value);
				} catch (java.text.ParseException e) {
					e.printStackTrace();
				}
				// 返回dt日期对象
				return dt;
			}

		}, Date.class);
		   
	  ExcelReadUtil read = new ExcelReadUtil();
	  System.out.println(ExcelReadUtil.class.getResource("/"));  
	  URL url = ExcelReadUtil.class.getResource("/cn/knet/resource/category_bl.xlsx");
	  String path = url.getFile().replace("file:/","");
	  //List<Cat> list = read.read("D:\\123\\two.xlsx", Cat.class);
	  //List<Cat> list = read.read(path, Cat.class,true);
	  //System.out.println(list.size());
  */
  }
}
