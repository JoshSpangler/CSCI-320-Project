import java.io.*;
import java.util.ArrayList;

public class GetData {

    /**
     * Reads a file for the dealer's orfer
     * @param filename the file to read from
     * @return an array of strings from the file
     * @throws FileNotFoundException if the file was not found
     * @throws IOException if the file cannot be read
     */
    public static String[] readFile(String filename) {
        try {
            File file = new File(filename);
            BufferedReader bf = new BufferedReader(new FileReader(file));
            ArrayList<String> lines = new ArrayList();
            String line = "";
            while ((line = bf.readLine()) != null) {
                lines.add(line);
            }
            return lines.toArray(String[]::new);
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets a list of the unique individual attributes available to the dealer
     * @param contents the file contents
     * @param index the index of the content that we want
     * @return an array of string attributes from the file
     */
    public static String[] getAttribute(String[] contents, int index){
        ArrayList<String> attribute=new ArrayList<>();
        for(String line:contents){
            String[] parsedLine=line.split(",")[index].split("/");
            for(String parsed:parsedLine) {
                if (!attribute.contains(parsed)) {
                    attribute.add(parsed);
                }
            }
        }
        return attribute.toArray(String[]::new);
    }

    public static String[] getWheels(String[] contents){
        ArrayList<String> wheelList=new ArrayList<String>();
        String currentWheel="";
        for(int i=0; i<contents.length; i++){
            if(contents[i].contains("style")){
                wheelList.add(currentWheel+" -"+contents[i]);
            }
            else{
                currentWheel=contents[i];
            }
        }
        String[] wheels=wheelList.toArray(String[]::new);
        for(int i=0; i<wheels.length; i++){
            for(int j=wheels[i].length()-1; j>=0; j--){
                if(wheels[i].charAt(j)=='"'||wheels[i].charAt(j)=='”'){
                    if(j!=wheels[i].length()-1){
                        wheels[i]=wheels[i].substring(0,j)+wheels[i].substring(j+1);
                    }
                    else{
                        wheels[i]=wheels[i].substring(0,j);
                    }
                }
            }
            wheels[i]=wheels[i].substring(0,2)+"\""+wheels[i].substring(2);
        }
        return wheels;
    }



    public static int getPrice(String[] contents, String model){
        for(String c: contents){
            if(c.contains(model)){
                System.out.println(c.split(",")[10]);
                return(Integer.parseInt(c.split(",")[10]));
            }
        }
        return((int)(Math.random()*100000+30000));
    }
}
