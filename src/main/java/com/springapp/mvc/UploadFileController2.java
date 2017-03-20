package com.springapp.mvc;

import ShuaiZheng.Machine.ai;
import ShuaiZheng.Machine.d;
import com.springTeam55UCI.mvc.com.util.ConnectionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.SQLException;

import static com.springTeam55UCI.mvc.com.util.CheckTable.CheckTable;
import static com.springTeam55UCI.mvc.com.util.WriteBlob.writeBlob;


/**
 * Created by Shuai Zheng on 3/6/17.
 */
@Controller
public class UploadFileController2 {
    private static final Logger logger = LoggerFactory
            .getLogger(UploadFileController2.class);


    /**
     * Upload single file using Spring Controller
     */
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public
    @ResponseBody
    ModelAndView uploadFileHandler(
            @RequestParam("file") MultipartFile file, ModelMap model, @RequestParam("name") String name,HttpServletRequest request,
            HttpServletResponse response) {


        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();

                // Creating the directory to store file
                String rootPath = System.getProperty("catalina.home");
                File dir = new File(rootPath + File.separator + "tmpFiles");
                if (!dir.exists())
                    dir.mkdirs();

                // Create the file on server
                File serverFile = new File(dir.getAbsolutePath()
                        + File.separator + file.getOriginalFilename());
                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();


                logger.info("Server File Location="
                        + serverFile.getAbsolutePath());
                String[] input = new String[3];
                input[0] = dir.getAbsolutePath()
                        + File.separator + file.getOriginalFilename();
                input[1] = dir.getAbsolutePath()
                        + File.separator;
                ServletContext context = request.getServletContext();
                input[2] = context.getRealPath("/")+"resources"+File.separator+"ShuaiZhengTrained.zip";

                System.out.println(input[2]);
                System.out.println(input[1]);

                (new ai(input[0], input[1], input[2])).run(input);

//                (new ai()).a(input);

                model.addAttribute("username", name);
                model.addAttribute("download", dir.getAbsolutePath()
                        + File.separator + "output.txt");

//
                String outputaddr = dir.getAbsolutePath()
                        + File.separator + "output.txt";
                //System.out.println(outputaddr);
//                String outputaddr = request.getSession().getServletContext().getRealPath("") + File.separator + "output.txt";
                //System.out.println(outputaddr);

                Connection connection = null;
//                String username = (String)request.getSession().getAttribute("username");
                String username = name;
                try {
                    connection = ConnectionConfig.getConnection();
                    if (connection != null) {
                        System.out.println("Connection established.");
                        int last_id = CheckTable(connection, username);
                        writeBlob(connection, outputaddr, last_id, username);
                    } else {
                        System.out.println("Connection failed.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        try {
                            connection.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }


                return new ModelAndView("result");

            } catch (Exception e) {

                return new ModelAndView("index");

//                return "You failed to upload " + file.getOriginalFilename() + " => " + e.getMessage();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return new ModelAndView("index");

    }
//
//    @RequestMapping("/hello")
//    public String uploadStatus() {
//        return "hello";
//    }

}


