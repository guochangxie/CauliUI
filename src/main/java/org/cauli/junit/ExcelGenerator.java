package org.cauli.junit;

import com.google.common.collect.Lists;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.lang.reflect.Method;

import java.util.List;

/**
 * Created by celeskyking on 14-3-2
 */
public class ExcelGenerator implements FileGenerator{
    private Workbook workbook;
    private Method method;
    private Sheet sheet;
    public ExcelGenerator(File excel,Method method) {
        if(!excel.exists()){
            throw new RuntimeException("没有找到excel文件");
        }else {
            try {
                this.workbook = WorkbookFactory.create(excel);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("初始化excel文件的时候出现了错误");
            }
            this.sheet=this.workbook.getSheet(method.getName());
        }
    }

    public List<RowParameter> generator()    {
        int numberOfRows = this.sheet.getPhysicalNumberOfRows();
        List<RowParameter> rowParameters= Lists.newArrayList();
        for(int i=0;i<numberOfRows;i++){
            Row row = this.sheet.getRow(i);
            int cellNum = row.getPhysicalNumberOfCells();
            RowParameter rowParameter=new RowParameter();
            for(int j=0;j<cellNum;j++){
                rowParameter.addParam(row.getCell(j).getStringCellValue());
            }
            rowParameters.add(rowParameter);
        }
        return rowParameters;
    }

}
