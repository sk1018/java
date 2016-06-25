package jp.ac.fukushima_u.gp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


class streamTest4{

    public static void main(String args[]){
        try{
            File file = new File("C:\\Users\\GP\\Desktop\\seiya\\pro\\java\\streamTest4.txt");

            if (checkBeforeWritefile(file)){
                FileWriter fw = new FileWriter(file);

                fw.write("test\n");
                fw.write("ttt\r\n");

                fw.close();
            }else{
                System.out.println("   t   t");
            }
        }catch(IOException e){
            System.out.println(e);
        }
    }

    private static boolean checkBeforeWritefile(File file){
        if (file.exists()){
            if (file.isFile() && file.canWrite()){
                return true;
            }
        }

        return false;
    }
}
