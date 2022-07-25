package generic;

import java.io.FileInputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class Excel {
	public static int getRowCount(String path,String sheet) {
		int rc=0;
		try 
		{
			Workbook wb = WorkbookFactory.create(new FileInputStream(path));
			rc=wb.getSheet(sheet).getLastRowNum();
			wb.close();
		}
		catch (Exception e) 
		{
			
		}
		
		return rc;
	}
	
	public static int getColumnCount(String path,String sheet,int r) {
		int cc=0;
		try 
		{
			Workbook wb = WorkbookFactory.create(new FileInputStream(path));
			cc= wb.getSheet(sheet).getRow(r).getLastCellNum();
			wb.close();
		}
		catch (Exception e) 
		{
			
		}
		
		return cc;
	}
	
	
	public static String getCellData(String path,String sheet,int r,int c) {
		String  data="";
		try 
		{
			Workbook wb = WorkbookFactory.create(new FileInputStream(path));
			data=wb.getSheet(sheet).getRow(r).getCell(c).toString();
			wb.close();
		}
		catch (Exception e) 
		{
			
		}
		
		return data;
	}
	

}
