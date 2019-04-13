import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class GetData {

    private static final String SELECT_ALL = "*";

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
    public static String[] getAttribute(String[] contents, String brand, String model, String series, String design,
                                        String attrib, int index, boolean sort){
        ArrayList<String> attribute=new ArrayList<>();
        for(String line:contents){
            if((!brand.equals(SELECT_ALL) && !line.contains(brand)) || (!model.equals(SELECT_ALL) &&
                    !line.contains(model)) || (!series.equals(SELECT_ALL) && !line.contains(series)) ||
                    (!design.equals(SELECT_ALL) && !line.contains(design))){
                continue;
            }
            String[] parsedLine=line.split(",")[index].split("/");
            for(String parsed:parsedLine) {
                if (!attribute.contains(parsed)) {
                    attribute.add(parsed);
                }
            }
        }
        if(sort) {
            Collections.sort(attribute);
        }
        if(!attrib.equals("Upgrade")){
            attribute.add(0, attrib);
        }
        //System.out.println(attribute);
        return attribute.toArray(String[]::new);
    }


    /**
     * Parses the wheels into a format that can be displayed on the dropdown menu
     * @param fileContents the file contents
     * @return the parsed contents of the file
     */
    public static String[] getWheels(String[] fileContents, String brand, String model, String series, String design){
        String[] contents=GetData.getAttribute(fileContents, brand, model, series, design,
                "Wheels", 7, false);
        ArrayList<String> wheelList=new ArrayList<String>();
        wheelList.add("Wheels");
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
        for(int i=1; i<wheels.length; i++){
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
}
