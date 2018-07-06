package com.evergrande.publics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/**
 * @author huangxiao
 * @version 创建时间：2018年6月21日 下午4:13:51 类说明
 */
public class ExcelUtil {
	private String mFilePath;
	private LinkedHashMap<Integer, String> firstRowMap = new LinkedHashMap<Integer, String>();
	// 创建Document对象
	private static Document document = DocumentHelper.createDocument();
	// 创建根节点
	private static Element testUnit = document.addElement("unit");
	private String lastCaseId=null;
	private Element testCase =null;
	static {
		testUnit.addAttribute("id", "HDAutoTest");
		testUnit.addAttribute("desc", "恒大金服测试场景点");
	}
	
	
	public void setFilePath(String filePath) {
		mFilePath = filePath;
	}

	private static final String SUFFIX_2003 = ".xls";
	private static final String SUFFIX_2007 = ".xlsx";

	public Workbook initWorkBook() throws IOException {
		File file = new File(mFilePath);
		InputStream is = new FileInputStream(file);

		Workbook workbook = null;
		if (mFilePath.endsWith(SUFFIX_2003)) {
			workbook = new HSSFWorkbook(is);
		} else if (mFilePath.endsWith(SUFFIX_2007)) {
			workbook = new XSSFWorkbook(is);
		}

		return workbook;
	}

	public void parseWorkbook(Workbook workbook,String xmlPath) throws Exception {
		int numOfSheet = workbook.getNumberOfSheets();

		for (int i = 0; i < numOfSheet; ++i) {
			Sheet sheet = workbook.getSheetAt(i);
			parseSheet(sheet);
		}
		CreateXMLByDOM4J(new File(xmlPath), document);
	}

	private void parseSheet(Sheet sheet) throws Exception {
		Row row;

		int count = 0;

		Iterator<Row> iterator = sheet.iterator();
		List<CellRangeAddress> cras = getCombineCell(sheet);
		while (iterator.hasNext()) {
			row = iterator.next();

			if (count == 0) {
				parseRow(row, sheet, cras);
			} else {
				parseRowAndFillData(row,sheet,cras);
			}

			++count;
		}
	}

	private void parseRowAndFillData(Row row,Sheet sheet,List<CellRangeAddress> cras) throws Exception {
		LinkedHashMap<Integer, String> rstMap = parseRow(row, sheet, cras);

		if (rstMap.size() == 0) {
			throw new Exception("行内容为空！");
		} else {
			if (firstRowMap.isEmpty()) {
				throw new Exception("首行信息未获取！");
			}
			
			Element step=null;
			if(testUnit.element("case")==null){
				// // 如果没有case节点则创建case节点
				testCase = testUnit.addElement("case");
			}
			
			
			for(Map.Entry<Integer, String> entry:rstMap.entrySet()){
				int colIndex=entry.getKey();
				String cellString=entry.getValue();
				String key = firstRowMap.get(colIndex);
				
				
				if (key.equals("id")) {
					if (testUnit.element("case").attributeValue("id") == null) {
						// // 如果case节点的id属性为空
						testCase.addAttribute(key, cellString);
					}
					else if(!lastCaseId.equals(cellString)){
						// // 如果case节点的id与上一个id不相等，增加一个case节点
						testCase = testUnit.addElement("case");
						testCase.addAttribute(key, cellString);
					}
					this.lastCaseId = cellString;
				}
				else if(key.equals("name")){
					if(testUnit.element("case").attributeValue("name")==null){
						// // 如果case节点的name属性为空
						testCase.addAttribute(key, cellString);
					}
					
				}
				else if(key.equals("step")){
					// 创建step子节点
					step = testCase.addElement("step");
				}
				else{
					step.addAttribute(key, cellString);
				}
				
				
			}
			

		
			

		}
	}

	private LinkedHashMap<Integer, String> parseRow(Row row,Sheet sheet,List<CellRangeAddress> cras) throws Exception {
		LinkedHashMap<Integer, String> rstMap = new LinkedHashMap<Integer, String>();
		Cell cell;
		Iterator<Cell> iterator = row.iterator();
		while (iterator.hasNext()) {
			cell = iterator.next();
			cell.setCellType(CellType.STRING);
			if (row.getRowNum() == 0) {
				parseCell(cell);
			} else {
				String str = this.isCombineCell(cras, cell, sheet);
				if (str==null||str.equals("")) {
					rstMap.put(cell.getColumnIndex(), cell.getStringCellValue());
				} else {
					rstMap.put(cell.getColumnIndex(), str);
				}
			}

		}

		return rstMap;
	}

	private void parseCell(Cell cell) throws Exception {
		final String regex = "\\((\\w*)\\)";
		final Pattern pattern = Pattern.compile(regex);
		final Matcher matcher = pattern.matcher(cell.getStringCellValue());
		String cellKey = null;

		if (matcher.find()) {
			if (matcher.groupCount() > 1) {
				throw new Exception("excel标题出现多组()！");
			} else if (matcher.groupCount() == 0) {
				throw new Exception("excel标题未出现()关键字标志！");
			}

			cellKey = matcher.group(1);
		}
		int cellColumnIndex = cell.getColumnIndex();
		firstRowMap.put(cellColumnIndex, cellKey);

	}
	public void addXMLNode(Document document){
		
	}
	public static void CreateXMLByDOM4J(File dest, Document document) {

		// 创建输出格式(OutputFormat对象)
		OutputFormat format = OutputFormat.createPrettyPrint();

		try {
			// 创建XMLWriter对象
			XMLWriter writer = new XMLWriter(new FileOutputStream(dest), format);

			// 设置不自动进行转义
			writer.setEscapeText(false);

			// 生成XML文件
			writer.write(document);

			// 关闭XMLWriter对象
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取单元格的值
	 * 
	 * @param cell
	 * @return
	 */
	public String getCellValue(Cell cell) {
		if (cell == null)
			return "";
		else {
			cell.setCellType(CellType.STRING);
			return cell.getStringCellValue();
		}
	}

	/**
	 * 合并单元格处理,获取合并行
	 * 
	 * @param sheet
	 * @return List<CellRangeAddress>
	 */
	public List<CellRangeAddress> getCombineCell(Sheet sheet) {
		List<CellRangeAddress> list = new ArrayList<CellRangeAddress>();
		// 获得一个 sheet 中合并单元格的数量
		int sheetmergerCount = sheet.getNumMergedRegions();
		// 遍历所有的合并单元格
		for (int i = 0; i < sheetmergerCount; i++) {
			// 获得合并单元格保存进list中
			CellRangeAddress ca = sheet.getMergedRegion(i);
			list.add(ca);
		}
		return list;
	}

	private int getRowNum(List<CellRangeAddress> listCombineCell, Cell cell, Sheet sheet) {
		int xr = 0;
		int firstC = 0;
		int lastC = 0;
		int firstR = 0;
		int lastR = 0;
		for (CellRangeAddress ca : listCombineCell) {
			// 获得合并单元格的起始行, 结束行, 起始列, 结束列
			firstC = ca.getFirstColumn();
			lastC = ca.getLastColumn();
			firstR = ca.getFirstRow();
			lastR = ca.getLastRow();
			if (cell.getRowIndex() >= firstR && cell.getRowIndex() <= lastR) {
				if (cell.getColumnIndex() >= firstC && cell.getColumnIndex() <= lastC) {
					xr = lastR;
				}
			}

		}
		return xr;

	}

	/**
	 * 判断单元格是否为合并单元格，是的话则将单元格的值返回
	 * 
	 * @param listCombineCell
	 *            存放合并单元格的list
	 * @param cell
	 *            需要判断的单元格
	 * @param sheet
	 *            sheet
	 * @return
	 */
	public String isCombineCell(List<CellRangeAddress> listCombineCell, Cell cell, Sheet sheet) throws Exception {
		int firstC = 0;
		int lastC = 0;
		int firstR = 0;
		int lastR = 0;
		String cellValue = null;
		for (CellRangeAddress ca : listCombineCell) {
			// 获得合并单元格的起始行, 结束行, 起始列, 结束列
			firstC = ca.getFirstColumn();
			lastC = ca.getLastColumn();
			firstR = ca.getFirstRow();
			lastR = ca.getLastRow();
			if (cell.getRowIndex() >= firstR && cell.getRowIndex() <= lastR) {
				if (cell.getColumnIndex() >= firstC && cell.getColumnIndex() <= lastC) {
					Row fRow = sheet.getRow(firstR);
					Cell fCell = fRow.getCell(firstC);
					cellValue = getCellValue(fCell);
					break;
				}
			} else {
				cellValue = "";
			}
		}
		return cellValue;
	}

	/**
	 * 获取合并单元格的值
	 * 
	 * @param sheet
	 * @param row
	 * @param column
	 * @return
	 */
	public String getMergedRegionValue(Sheet sheet, int row, int column) {
		int sheetMergeCount = sheet.getNumMergedRegions();

		for (int i = 0; i < sheetMergeCount; i++) {
			CellRangeAddress ca = sheet.getMergedRegion(i);
			int firstColumn = ca.getFirstColumn();
			int lastColumn = ca.getLastColumn();
			int firstRow = ca.getFirstRow();
			int lastRow = ca.getLastRow();

			if (row >= firstRow && row <= lastRow) {
				if (column >= firstColumn && column <= lastColumn) {
					Row fRow = sheet.getRow(firstRow);
					Cell fCell = fRow.getCell(firstColumn);
					return getCellValue(fCell);
				}
			}
		}

		return null;
	}

	/**
	 * 判断指定的单元格是否是合并单元格
	 * 
	 * @param sheet
	 * @param row
	 *            行下标
	 * @param column
	 *            列下标
	 * @return
	 */
	private boolean isMergedRegion(Sheet sheet, int row, int column) {
		int sheetMergeCount = sheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergeCount; i++) {
			CellRangeAddress range = sheet.getMergedRegion(i);
			int firstColumn = range.getFirstColumn();
			int lastColumn = range.getLastColumn();
			int firstRow = range.getFirstRow();
			int lastRow = range.getLastRow();
			if (row >= firstRow && row <= lastRow) {
				if (column >= firstColumn && column <= lastColumn) {
					return true;
				}
			}
		}
		return false;
	}
}
