package br.com.rtoolsws.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Paths {

    public String get()  {
        String path = this.getClass().getClassLoader().getResource("").getPath();
        String fullPath = null;
        try {
            fullPath = URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Paths.class.getName()).log(Level.SEVERE, null, ex);
        }
        String pathArr[] = fullPath.split("/WEB-INF/");
        System.out.println(fullPath);
        System.out.println(pathArr[0]);
        fullPath = pathArr[0];
        String reponsePath = "";
        // to read a file from webcontent
        reponsePath = new File(fullPath).getPath();
        return reponsePath;
    }

}
