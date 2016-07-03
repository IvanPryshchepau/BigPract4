import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class TestFramework {

    InputStream in = null;
    HSSFWorkbook wb = null;

    public ArrayList<String[]> readCommandFile() {
        ArrayList<String[]> commands = new ArrayList<String[]>();
        try {
            in = new FileInputStream(System.getProperty("user.dir") + File.separator + "command.xls");
            wb = new HSSFWorkbook(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Sheet sheet = wb.getSheetAt(0);
        Iterator<Row> it = sheet.iterator();
        while (it.hasNext()) {

            String[] command = new String[6];
            int i = 0;
            Row row = it.next();
            Iterator<Cell> cells = row.iterator();
            while (cells.hasNext()) {
                Cell cell = cells.next();
                if (cell.getCellType() == Cell.CELL_TYPE_STRING){
                    command[i] = cell.getStringCellValue();
                } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
                    command[i] = String.valueOf((int) cell.getNumericCellValue());
                }
                i++;
            }
            commands.add(command);
        }
        return commands;
    }

    public void commandRun(){
        Function function = new Function();
        ArrayList<String[]> commands = readCommandFile();
        int i = 0;
        for (String[] command : commands){
            String toRun;
            boolean typeTest;
            if(command[0].startsWith("!")){
                toRun = command[0].replace("!", "");
                typeTest = false;
            } else {
                toRun = command[0];
                typeTest = true;
            }
            switch (toRun){
                case "pageContainsText":{
                    if (function.pageContainsText(command[1], command[2], typeTest)){
                        wb.getSheetAt(0).getRow(i).createCell(7, Cell.CELL_TYPE_STRING).setCellValue("+");
                    } else {
                        wb.getSheetAt(0).getRow(i).createCell(7, Cell.CELL_TYPE_STRING).setCellValue("!" + function.error);
                    }
                    break;
                }
                case "pageContainsLinkText":{
                    if(function.pageContainsLinkText(command[1], command[2], typeTest)){
                        wb.getSheetAt(0).getRow(i).createCell(7, Cell.CELL_TYPE_STRING).setCellValue("+");
                    } else {
                        wb.getSheetAt(0).getRow(i).createCell(7, Cell.CELL_TYPE_STRING).setCellValue("!" + function.error);
                    }
                    break;
                }
                case "registration":{
                    function.registration(command[1], command[2], command[3]);
                    break;
                }
                case "autorize":{
                    if (function.autorize(command[1], command[2], typeTest)){
                        wb.getSheetAt(0).getRow(i).createCell(7, Cell.CELL_TYPE_STRING).setCellValue("+");
                    } else {
                        wb.getSheetAt(0).getRow(i).createCell(7, Cell.CELL_TYPE_STRING).setCellValue("!" + function.error);
                    }
                    break;
                }
                case "search":{
                    if (function.search(command[1], Integer.parseInt(command[2]), typeTest)){
                        wb.getSheetAt(0).getRow(i).createCell(7, Cell.CELL_TYPE_STRING).setCellValue("+");
                    } else {
                        wb.getSheetAt(0).getRow(i).createCell(7, Cell.CELL_TYPE_STRING).setCellValue("!" + function.error);
                    }
                    break;
                }
                case "addTopic":{
                    if (function.addTopic(command[1], command[2], typeTest)){
                        wb.getSheetAt(0).getRow(i).createCell(7, Cell.CELL_TYPE_STRING).setCellValue("+");
                    } else {
                        wb.getSheetAt(0).getRow(i).createCell(7, Cell.CELL_TYPE_STRING).setCellValue("!" + function.error);
                    }
                    break;
                }
                case "addComment":{
                    if (function.addComment(command[1], command[2], command[3], typeTest)){
                        wb.getSheetAt(0).getRow(i).createCell(7, Cell.CELL_TYPE_STRING).setCellValue("+");
                    } else {
                        wb.getSheetAt(0).getRow(i).createCell(7, Cell.CELL_TYPE_STRING).setCellValue("!" + function.error);
                    }
                    break;
                }
                case "editComment":{
                    if (function.editComment(command[1],typeTest)){
                        wb.getSheetAt(0).getRow(i).createCell(7, Cell.CELL_TYPE_STRING).setCellValue("+");
                    } else {
                        wb.getSheetAt(0).getRow(i).createCell(7, Cell.CELL_TYPE_STRING).setCellValue("!" + function.error);
                    }
                    break;
                }
                case "isOnline":{
                    if (function.isOnline(command[1], typeTest)){
                        wb.getSheetAt(0).getRow(i).createCell(7, Cell.CELL_TYPE_STRING).setCellValue("+");
                    } else {
                        wb.getSheetAt(0).getRow(i).createCell(7, Cell.CELL_TYPE_STRING).setCellValue("!" + function.error);
                    }
                    break;
                }
                case "statisticChange":{
                    int stat = 0;
                    String rand = "test" + Math.round(Math.random()*10);
                    switch (command[1]){
                        case "posts":{
                            stat = function.getTotalPosts();
                            function.addComment("Your first forum", "Welcome to phpBB3", rand, true);
                            stat = function.getTotalPosts() - stat;
                            break;
                        }
                        case "topics":{
                            stat = function.getTotalTopics();
                            function.addTopic("Your first forum", rand, true);
                            stat = function.getTotalTopics() - stat;
                            break;
                        }
                        case "members":{
                            stat = function.getTotalMembers();
                            function.registration(rand, rand + "@test.com", "123456");
                            stat = function.getTotalMembers() - stat;
                            break;
                        }
                    }
                    if (stat > 0){
                        wb.getSheetAt(0).getRow(i).createCell(7, Cell.CELL_TYPE_STRING).setCellValue("+");
                    } else {
                        wb.getSheetAt(0).getRow(i).createCell(7, Cell.CELL_TYPE_STRING).setCellValue("!");
                    }
                    break;
                }
                default:{
                    wb.getSheetAt(0).getRow(i).createCell(7, Cell.CELL_TYPE_STRING).setCellValue("?(undefined command)");
                }
            }
            i++;
        }

        try {
            wb.write(new FileOutputStream(System.getProperty("user.dir") + File.separator + "command.xls"));
            wb.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        function.driver.close();
        function.driver = null;
    }

}
